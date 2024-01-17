// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.audit.logs.service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.daikon.exception.error.ErrorCode;
import org.talend.daikon.multitenant.context.TenancyContext;
import org.talend.daikon.multitenant.core.Tenant;
import org.talend.daikon.security.tenant.ReactiveTenancyContextHolder;
import org.talend.daikon.spring.audit.logs.api.GenerateAuditLog;
import org.talend.daikon.spring.audit.logs.error.AuditLogsErrorCode;
import org.talend.daikon.spring.audit.logs.model.BodyCaptureExchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
public class AuditLogCustomWebFilter implements WebFilter {

    private final AuditLogSender auditLogSender;

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public Mono<Void> filter(final ServerWebExchange serverWebExchange, final WebFilterChain chain) {
        return getEndpointMethod(serverWebExchange) //
                .doOnSubscribe(e -> log.debug("Starting filter: '{}'", this.getClass().getName())) //
                .mapNotNull(method -> method.getAnnotation(GenerateAuditLog.class)) //
                .switchIfEmpty(Mono.error(new AuditLogsWebFilterException(AuditLogsErrorCode.METHOD_NOT_HANDLED)))
                .zipWith(getTenantIdentity()) //
                .flatMap(annotationAndTenant -> {
                    BodyCaptureExchange bodyCaptureExchange = new BodyCaptureExchange(serverWebExchange);
                    GenerateAuditLog annotation = annotationAndTenant.getT1();
                    String tenant = annotationAndTenant.getT2();
                    return chain.filter(serverWebExchange)
                            .doFinally(unused -> sendAuditLog(bodyCaptureExchange, annotation, tenant));
                }) //
                .onErrorResume(throwable -> {
                    if (throwable instanceof AuditLogsWebFilterException) {
                        return chain.filter(serverWebExchange);
                    } else {
                        return Mono.error(throwable);
                    }
                });
    }

    private void sendAuditLog(final BodyCaptureExchange bodyCaptureExchange, final GenerateAuditLog annotation, String tenant) {
        log.debug("request: {}", bodyCaptureExchange.getRequest().getFullBody());
        log.debug("response: {}", bodyCaptureExchange.getResponse().getFullBody());
        auditLogSender.sendAuditLog(tenant, bodyCaptureExchange.getRequest(), bodyCaptureExchange.getRequest().getFullBody(),
                Objects.requireNonNull(bodyCaptureExchange.getResponse().getStatusCode()).value(),
                bodyCaptureExchange.getResponse().getFullBody(), annotation);
    }

    private Mono<Method> getEndpointMethod(ServerWebExchange serverWebExchange) {
        return requestMappingHandlerMapping.getHandler(serverWebExchange) //
                .cast(HandlerMethod.class) //
                .map(HandlerMethod::getMethod) //
                .switchIfEmpty(Mono.error(new AuditLogsWebFilterException(AuditLogsErrorCode.METHOD_NOT_HANDLED)))
                .doOnError(e -> log.debug("Skipping audit-log filter because endpoint is not handled. {}", e.getMessage()))
                .doOnSuccess(
                        o -> log.debug("Annotations found on method {}: {}", o.getName(), Arrays.toString(o.getAnnotations())));
    }

    private Mono<String> getTenantIdentity() {
        return ReactiveTenancyContextHolder.getContext() //
                .mapNotNull(TenancyContext::getTenant) //
                .mapNotNull(Tenant::getIdentity) //
                .switchIfEmpty(Mono.error(new AuditLogsWebFilterException(AuditLogsErrorCode.TENANT_UNAVAILABLE)))
                .doOnError(e -> log.debug("Skipping audit-log filter because tenant is not available. {}", e.getMessage()))
                .cast(String.class);
    }

    static class AuditLogsWebFilterException extends TalendRuntimeException {

        AuditLogsWebFilterException(ErrorCode code) {
            super(code);
        }
    }
}
