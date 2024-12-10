package com.example.esdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @Description
 * @Author GoryLee
 * @Date 2024/12/5
 */
@Configuration
@Slf4j
public class ElasticsearchConfig implements DisposableBean {

    private RestHighLevelClient client;

    @Value("${es.uris}")
    private List<String> uris;

    @Value("${es.username}")
    private String username;

    @Value("${es.password}")
    private String password;

    private HttpHost getHost(String uri) {
        URL url;
        try {
            url = new URL(uri);
        } catch (MalformedURLException e) {
            log.error("根据uri创建URL对象出现异常，uri={}", uri, e);
            return null;
        }

        return new HttpHost(url.getHost(), url.getPort() == -1 ? 80 : url.getPort(), url.getProtocol());
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        if (uris == null || uris.size() == 0) {
            throw new IllegalArgumentException("没有获取到spring.elasticsearch.rest.uris的配置");
        }
        HttpHost[] httpHosts = new HttpHost[uris.size()];
        for (int i = 0; i < httpHosts.length; i++) {
            HttpHost httpHost = getHost(uris.get(i));
            if (httpHost != null) {
                httpHosts[i] = new HttpHost(httpHost);
            }
        }
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));

        RestClientBuilder builder = RestClient.builder(httpHosts).setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(-1);
            requestConfigBuilder.setSocketTimeout(-1);
            requestConfigBuilder.setConnectionRequestTimeout(-1);
            return requestConfigBuilder;
        }).setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.disableAuthCaching();
            return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        });
        return new RestHighLevelClient(builder);
    }

    @Override
    public void destroy() throws Exception {
        if (client != null) {
            client.close();
        }
    }
}
