package com.abhishek.user.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StopWatch;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.UUID;

public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {

    private Logger logger = LoggerFactory.getLogger(RequestResponseLoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        StopWatch watch = new StopWatch();
        watch.start();
        String trackingId = Optional.ofNullable(MDC.get("TRACKING_ID"))
                .orElse(UUID.randomUUID().toString());
        request.getHeaders().add("TRACKING_ID", trackingId);
        ClientHttpResponse response = execution.execute(request, body);
        watch.stop();
        log(response, watch.getTotalTimeMillis(), request, body);
        return response;
    }

    /**
     * log request details
     *
     * @param response
     *            response
     * @param totalTime
     *            total time taken by downstream layer
     * @param request
     *            request
     * @param body
     *            request body
     * @throws IOException
     *             exception if any
     */
    private void log(ClientHttpResponse response, long totalTime, HttpRequest request, byte[] body) throws IOException {
        logger.info("{} {} {} {} {}", request.getURI(),response.getStatusCode(), totalTime,
                StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()),
                new String(body, "UTF-8"));
    }

}
