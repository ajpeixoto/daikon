// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.ccf.context;

import java.util.List;
import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.talend.daikon.spring.ccf.context.exception.CcfContextTenantIdNotFoundException;
import org.talend.daikon.spring.ccf.context.utils.ScimRequestUtilities;
import org.talend.daikon.spring.ccf.context.utils.UUIDValidator;
import org.talend.iam.scim.model.User;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
public class M2MFunctionalContextAspect {

    private final M2MContextManager m2MContextManager;

    private final TenantParameterExtractor tenantParameterExtractor;

    private final ScimRequestUtilities scimRequestUtilities;

    public M2MFunctionalContextAspect(M2MContextManager m2MContextManager, TenantParameterExtractor tenantParameterExtractor,
            ScimRequestUtilities scimRequestUtilities) {
        this.m2MContextManager = m2MContextManager;
        this.tenantParameterExtractor = tenantParameterExtractor;
        this.scimRequestUtilities = scimRequestUtilities;
    }

    @Around("@annotation(org.talend.daikon.spring.ccf.context.M2MFunctionalContext)")
    public Object buildTenantContext(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        // Get tenant Id and user Id
        log.debug("Building tenant context for M2M flow.");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String requestURI = request.getRequestURI();

        String tenantId = Optional.ofNullable(tenantParameterExtractor.extractAccountId(requestURI)).filter(UUIDValidator::isUUID)
                .orElseThrow(CcfContextTenantIdNotFoundException::new);

        String userId = Optional.ofNullable(tenantParameterExtractor.extractUserId(requestURI)).filter(UUIDValidator::isUUID)
                .orElse(null);
        if (userId == null && requestURI.contains("/user/")) {
            log.warn("Invalid use of @M2MFunctionalContext: /user/ is in the path but no userId found.");
        }
        List<String> listAttributes = AttributeProvider.getAttributes(proceedingJoinPoint);

        // User context
        User user = null;
        if (userId != null && !listAttributes.contains(UserContextConstant.NONE.getValue())) {
            if (listAttributes.contains(UserContextConstant.ALL.getValue())) {
                listAttributes = UserContextConstant.allConstantsList();
            }
            log.debug("Searching user attribute for tenantId {} and userId {} : {}", tenantId, userId, listAttributes);
            user = scimRequestUtilities.getUserWithAttributes(userId, listAttributes);
        }

        log.debug("Injecting context for tenant  {} ", tenantId);
        m2MContextManager.injectContext(tenantId, userId, Optional.ofNullable(user));

        Object returnObj = proceedingJoinPoint.proceed();

        m2MContextManager.clearContext();

        return returnObj;

    }
}
