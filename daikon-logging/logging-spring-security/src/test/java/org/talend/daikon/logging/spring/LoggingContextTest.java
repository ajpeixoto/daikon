// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.logging.spring;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.talend.daikon.logging.event.field.MdcKeys;

import io.restassured.RestAssured;

@SpringBootTest(classes = { LoggingApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(LoggingContextTest.SampleRequestHandlerConfiguration.class)
@ExtendWith(SpringExtension.class)
public class LoggingContextTest {

    @Value("${local.server.port}")
    public int port;

    @Autowired
    private SampleRequestHandlerConfiguration sampleRequestHandler;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        sampleRequestHandler.verifier = () -> {
        };
    }

    @Test
    public void testSyncAuthenticated() {
        final String userId = LoggingApplication.USER_ID;

        sampleRequestHandler.verifier = () -> {
            assertEquals(userId, MDC.get(MdcKeys.USER_ID));
        };
        String result = given().auth() //
                .basic(userId, LoggingApplication.PASSWORD) //
                .get("/") //
                .then().statusCode(200) //
                .extract().asString();
        assertEquals(LoggingApplication.MESSAGE, result);
    }

    @Test
    public void testSyncPublic() {
        sampleRequestHandler.verifier = () -> {
            assertNull(MDC.get(MdcKeys.USER_ID));
        };
        String result = given().get("/public").then().statusCode(200).extract().asString();
        assertEquals(LoggingApplication.MESSAGE, result);
    }

    @Test
    public void testAsyncAuthenticated() {
        final String userId = LoggingApplication.USER_ID;

        sampleRequestHandler.verifier = () -> {
            assertEquals(userId, MDC.get(MdcKeys.USER_ID));
        };
        String result = given().auth().basic(userId, LoggingApplication.PASSWORD).get("/async").then().statusCode(200).extract()
                .asString();
        assertEquals(LoggingApplication.MESSAGE, result);
    }

    @Test
    public void testAsyncPublic() {
        final String userId = LoggingApplication.USER_ID;

        sampleRequestHandler.verifier = () -> {
            assertNull(MDC.get(MdcKeys.USER_ID));
        };
        String result = given().auth().basic(userId, LoggingApplication.PASSWORD).get("/public/async").then().statusCode(200)
                .extract().asString();
        assertEquals(LoggingApplication.MESSAGE, result);
    }

    @AutoConfiguration
    public static class SampleRequestHandlerConfiguration {

        public Runnable verifier = () -> {
        };

        @Bean
        public LoggingApplication.SampleRequestHandler sampleRequestHandler() {

            return () -> verifier.run();

        }
    }

}
