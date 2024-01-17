// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.audit.logs.service;

import static org.talend.daikon.spring.audit.common.model.AuditLogFieldEnum.ACCOUNT_ID;
import static org.talend.daikon.spring.audit.common.model.AuditLogFieldEnum.APPLICATION_ID;
import static org.talend.daikon.spring.audit.common.model.AuditLogFieldEnum.EVENT_CATEGORY;
import static org.talend.daikon.spring.audit.common.model.AuditLogFieldEnum.EVENT_OPERATION;
import static org.talend.daikon.spring.audit.common.model.AuditLogFieldEnum.EVENT_TYPE;
import static org.talend.daikon.spring.audit.common.model.AuditLogFieldEnum.TIMESTAMP;

import java.lang.reflect.InvocationTargetException;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.daikon.spring.audit.common.exception.AuditLogException;
import org.talend.daikon.spring.audit.logs.api.AuditContextFilter;
import org.talend.daikon.spring.audit.logs.api.AuditUserProvider;
import org.talend.daikon.spring.audit.logs.api.GenerateAuditLog;
import org.talend.daikon.spring.audit.service.AppAuditLogger;
import org.talend.logging.audit.Context;

import io.micrometer.core.instrument.Counter;
import jakarta.servlet.http.HttpServletRequest;

public class AuditLogSenderImpl implements AuditLogSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLogSenderImpl.class);

    private final AuditUserProvider auditUserProvider;

    private final AppAuditLogger auditLogger;

    private final AuditLogIpExtractor auditLogIpExtractor;

    private final AuditLogUrlExtractor auditLogUrlExtractor;

    private final Counter auditLogsGeneratedCounter;

    public AuditLogSenderImpl(AuditUserProvider auditUserProvider, AppAuditLogger auditLogger,
            AuditLogIpExtractor auditLogIpExtractor, AuditLogUrlExtractor auditLogUrlExtractor,
            Counter auditLogsGeneratedCounter) {
        this.auditUserProvider = auditUserProvider;
        this.auditLogger = auditLogger;
        this.auditLogIpExtractor = auditLogIpExtractor;
        this.auditLogUrlExtractor = auditLogUrlExtractor;
        this.auditLogsGeneratedCounter = auditLogsGeneratedCounter;
    }

    /**
     * Build the context and send the audit log
     */
    @Override
    public void sendAuditLog(HttpServletRequest request, Object requestBody, int responseCode, Object responseObject,
            GenerateAuditLog auditLogAnnotation) {
        try {
            LOGGER.info("generating audit log with metadata {}", auditLogAnnotation);
            // Build context from request, response & annotation info
            AuditLogContextBuilder auditLogContextBuilder = AuditLogContextBuilder.create() //
                    .withTimestamp(OffsetDateTime.now().toString()) //
                    .withLogId(UUID.randomUUID()) //
                    .withRequestId(UUID.randomUUID()) //
                    .withApplicationId(auditLogAnnotation.application()) //
                    .withEventType(auditLogAnnotation.eventType()) //
                    .withEventCategory(auditLogAnnotation.eventCategory()) //
                    .withEventOperation(auditLogAnnotation.eventOperation()) //
                    .withUserId(auditUserProvider.getUserId()) //
                    .withUsername(auditUserProvider.getUsername()) //
                    .withEmail(auditUserProvider.getUserEmail()) //
                    .withAccountId(auditUserProvider.getAccountId()) //
                    .withRequest(request, requestBody) //
                    .withResponse(responseCode, auditLogAnnotation.includeBodyResponse() ? responseObject : null) //
                    .withIpExtractor(this.auditLogIpExtractor) //
                    .withUrlExtractor(this.auditLogUrlExtractor);

            // Filter the context if needed
            AuditContextFilter filter = auditLogAnnotation.filter().getDeclaredConstructor().newInstance();
            auditLogContextBuilder = filter.filter(auditLogContextBuilder, requestBody, responseObject);

            // Finally send the log
            this.sendAuditLog(auditLogContextBuilder.build());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOGGER.error("audit log with metadata {} has not been generated", auditLogAnnotation, e);
        } catch (AuditLogException e) {
            LOGGER.debug("audit log with metadata {} has not been generated", auditLogAnnotation, e);
        }
    }

    /**
     * Build a context from a context builder and send the generated context
     */
    public void sendAuditLog(AuditLogContextBuilder builder) {
        this.sendAuditLog(builder.withIpExtractor(this.auditLogIpExtractor).build());
    }

    /**
     * Send the audit log
     */
    @Override
    public void sendAuditLog(Context context) {
        try {
            auditLogger.sendAuditLog(context);
        } catch (Exception e) {
            // Clean audit log context from PIIs
            context.keySet().retainAll(Stream.of( //
                    TIMESTAMP.getId(), //
                    APPLICATION_ID.getId(), //
                    ACCOUNT_ID.getId(), //
                    EVENT_TYPE.getId(), //
                    EVENT_CATEGORY.getId(), //
                    EVENT_OPERATION //
            ).collect(Collectors.toSet()));
            LOGGER.warn("Error sending audit logs to Kafka : {}", context, e);
        } finally {
            auditLogsGeneratedCounter.increment();
        }
    }

    @Override
    public AuditUserProvider getAuditUserProvider() {
        return this.auditUserProvider;
    }
}
