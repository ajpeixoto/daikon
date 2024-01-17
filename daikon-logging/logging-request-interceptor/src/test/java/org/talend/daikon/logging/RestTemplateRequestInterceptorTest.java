// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.logging;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@ContextConfiguration
public class RestTemplateRequestInterceptorTest {

    private RestTemplate restTemplate;

    private ClientHttpRequestFactory requestFactory;

    private ClientHttpRequest request;

    private ClientHttpResponse response;

    private ResponseErrorHandler errorHandler;

    @BeforeEach
    public void setup() {
        requestFactory = mock(ClientHttpRequestFactory.class);
        request = mock(ClientHttpRequest.class);
        response = mock(ClientHttpResponse.class);
        errorHandler = mock(ResponseErrorHandler.class);
        @SuppressWarnings("rawtypes")
        HttpMessageConverter converter = mock(HttpMessageConverter.class);
        restTemplate = new RestTemplate(singletonList(converter));
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setErrorHandler(errorHandler);
        restTemplate.setInterceptors(singletonList(new TalendRestRequestInterceptor()));
    }

    @Test
    public void testRequestInterceptor() throws Exception {

        given(requestFactory.createRequest(new URI("http://localhost:8080"), HttpMethod.POST)).willReturn(request);
        HttpHeaders requestHeaders = new HttpHeaders();
        given(request.getHeaders()).willReturn(requestHeaders);
        given(request.execute()).willReturn(response);
        given(errorHandler.hasError(response)).willReturn(false);
        HttpStatus status = HttpStatus.OK;
        given(response.getStatusCode()).willReturn(status);
        given(response.getStatusText()).willReturn(status.getReasonPhrase());

        HttpHeaders entityHeaders = new HttpHeaders();
        entityHeaders.add("MyHeader", "MyEntityValue");
        HttpEntity<Void> entity = new HttpEntity<>(null, entityHeaders);
        restTemplate.exchange("http://localhost:8080", HttpMethod.POST, entity, Void.class);
        assertThat(requestHeaders.get("MyHeader"), contains("MyEntityValue"));
        verify(response).close();
    }

}
