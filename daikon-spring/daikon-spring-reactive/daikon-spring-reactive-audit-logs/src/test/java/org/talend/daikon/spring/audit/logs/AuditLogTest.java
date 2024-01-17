// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.audit.logs;

import static io.restassured.module.webtestclient.RestAssuredWebTestClient.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.talend.daikon.spring.audit.common.model.AuditLogFieldEnum.*;
import static org.talend.daikon.spring.audit.logs.AuditLogTestApp.TEST_BODY_RESPONSE;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.talend.daikon.spring.audit.logs.api.AppTestConfig;
import org.talend.daikon.spring.audit.logs.api.TestBody;
import org.talend.logging.audit.impl.AuditLoggerBase;
import org.talend.logging.audit.impl.DefaultContextImpl;

import io.restassured.module.webtestclient.response.WebTestClientResponse;
import io.restassured.module.webtestclient.specification.WebTestClientRequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AppTestConfig.class)
@AutoConfigureWebTestClient(timeout = "36000")
public class AuditLogTest {

    public static final String TENANT_ID = "ba118361-cf64-47dc-8c72-0fb872a6dc62";

    @Captor
    ArgumentCaptor<DefaultContextImpl> contextArgumentCaptor;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AuditLoggerBase auditLoggerBase;

    private TestBody testBody;

    private WebTestClientRequestSpecification baseRequest() {
        return given().webTestClient(webTestClient).header(CLIENT_IP.name(), "0.0.0.0.0")
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @BeforeEach
    public void setup() {
        reset(auditLoggerBase);
        testBody = TestBody.builder().name("request").date(ZonedDateTime.now()).password("secret").build();
    }

    @Test
    @WithMockUser(username = TENANT_ID)
    public void testAuditLogMethodNotHandledOK() {
        WebTestClientResponse r = baseRequest().delete("/test");

        assertEquals(OK.value(), r.getStatusCode());
        assertEquals(TEST_BODY_RESPONSE, r.getBody().as(TestBody.class));

        Mockito.verifyNoInteractions(auditLoggerBase);
    }

    @Test
    @WithMockUser(username = TENANT_ID)
    public void testAuditLogMethodNotHandledKO() {
        WebTestClientResponse r = baseRequest().patch("/test");

        assertEquals(INTERNAL_SERVER_ERROR.value(), r.getStatusCode());

        Mockito.verifyNoInteractions(auditLoggerBase);
    }

    @Test
    @WithMockUser(username = TENANT_ID)
    public void testAuditLogMethodHandledOK() {
        WebTestClientResponse r = baseRequest().body(testBody).post("/test");

        assertEquals(OK.value(), r.getStatusCode());
        assertEquals(TEST_BODY_RESPONSE, r.getBody().as(TestBody.class));

        Mockito.verify(auditLoggerBase).log(any(), any(), contextArgumentCaptor.capture(), any(), any());
        DefaultContextImpl context = contextArgumentCaptor.getValue();
        assertNotNull(context.get(LOG_ID.getId()));
        assertNotNull(context.get(TIMESTAMP.getId()));
        assertNotNull(context.get(REQUEST_ID.getId()));
        assertNotNull(context.get(CLIENT_IP.getId()));
        assertEquals(AuditLogTestApp.APP_NAME, context.get(APPLICATION_ID.getId()));
        assertEquals(AuditLogTestApp.EVENT_TYPE, context.get(EVENT_TYPE.getId()));
        assertEquals(AuditLogTestApp.EVENT_CATEGORY, context.get(EVENT_CATEGORY.getId()));
        assertEquals("post", context.get(EVENT_OPERATION.getId()));
        assertEquals(TENANT_ID, context.get(ACCOUNT_ID.getId()));
        assertNotNull(context.get(REQUEST.getId()));
        assertNotNull(context.get(RESPONSE.getId()));

        assertFalse(context.get(REQUEST.getId()).contains("secret"));
        assertFalse(context.get(RESPONSE.getId()).contains("secret"));
    }

    @Test
    @WithMockUser(username = TENANT_ID)
    public void testAuditLogMethodHandledKO() {
        WebTestClientResponse r = baseRequest().body(testBody).put("/test");

        assertEquals(INTERNAL_SERVER_ERROR.value(), r.getStatusCode());

        Mockito.verify(auditLoggerBase).log(any(), any(), contextArgumentCaptor.capture(), any(), any());
        DefaultContextImpl context = contextArgumentCaptor.getValue();
        assertNotNull(context.get(LOG_ID.getId()));
        assertNotNull(context.get(TIMESTAMP.getId()));
        assertNotNull(context.get(REQUEST_ID.getId()));
        assertNotNull(context.get(CLIENT_IP.getId()));
        assertEquals(AuditLogTestApp.APP_NAME, context.get(APPLICATION_ID.getId()));
        assertEquals(AuditLogTestApp.EVENT_TYPE, context.get(EVENT_TYPE.getId()));
        assertEquals(AuditLogTestApp.EVENT_CATEGORY, context.get(EVENT_CATEGORY.getId()));
        assertEquals("put", context.get(EVENT_OPERATION.getId()));
        assertEquals(TENANT_ID, context.get(ACCOUNT_ID.getId()));
        assertNotNull(context.get(REQUEST.getId()));
        assertNotNull(context.get(RESPONSE.getId()));
    }
}
