// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.audit.logs;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import org.talend.daikon.spring.audit.logs.api.GenerateAuditLog;
import org.talend.daikon.spring.audit.logs.api.TestBody;
import org.talend.daikon.spring.audit.logs.api.TestFilter;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/test")
@EnableAutoConfiguration
@SpringBootConfiguration
public class AuditLogTestApp {

    public static final String APP_NAME = "test-audit";

    public static final String EVENT_TYPE = "audit";

    public static final String EVENT_CATEGORY = "test";

    public static final TestBody TEST_BODY_RESPONSE = TestBody.builder().name("response").password("secret").build();

    public static void main(String[] args) {
        SpringApplication.run(AuditLogTestApp.class, args);
    }

    @ResponseStatus(OK)
    @DeleteMapping
    public Mono<TestBody> delete() {
        return Mono.just(TEST_BODY_RESPONSE);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @PatchMapping
    public Mono<Void> patch() {
        return Mono.error(new RuntimeException("error"));
    }

    @ResponseStatus(OK)
    @PostMapping
    @GenerateAuditLog(application = APP_NAME, eventType = EVENT_TYPE, eventCategory = EVENT_CATEGORY, eventOperation = "post", filter = TestFilter.class)
    public Mono<TestBody> post(@RequestBody final TestBody request) {
        return Mono.just(TEST_BODY_RESPONSE);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @PutMapping
    @GenerateAuditLog(application = APP_NAME, eventType = EVENT_TYPE, eventCategory = EVENT_CATEGORY, eventOperation = "put", filter = TestFilter.class)
    public Mono<TestBody> put(@RequestBody final TestBody request) {
        return Mono.error(new RuntimeException("error"));
    }
}
