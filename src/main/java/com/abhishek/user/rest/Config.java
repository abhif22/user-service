package com.abhishek.user.rest;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class Config {

    @Value("${rest.template.maxConnection}")
    private Integer maxConnection;
    @Value("${rest.template.maxConnectionPerHost}")
    private Integer maxConnectionPerHost;
    @Value("${rest.template.socketTimeOut}")
    private Integer socketTimeOut;
    @Value("${rest.template.connectionTimeOut}")
    private Integer connectionTimeOut;

    /**
     * @return rest template for communication with underlying layers
     */
    @Bean(name = "restTemplate")
    @LoadBalanced
    public RestTemplate restTemplate() {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(maxConnection);
        connManager.setDefaultMaxPerRoute(maxConnectionPerHost);
        RequestConfig config = RequestConfig.custom().setConnectTimeout(connectionTimeOut)
                .setSocketTimeout(socketTimeOut).build();
        CloseableHttpClient client = HttpClientBuilder.create().useSystemProperties().setConnectionManager(connManager)
                .setDefaultRequestConfig(config).useSystemProperties().build();
        RestTemplate restTemplate = new RestTemplate(
                new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory(client)));
        restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
        return restTemplate;
    }
}
