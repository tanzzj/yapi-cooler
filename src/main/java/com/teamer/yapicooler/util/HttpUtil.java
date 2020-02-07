package com.teamer.yapicooler.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * http工具类
 *
 * @author tanzj
 */
@Slf4j
@Component
public class HttpUtil {

    @Value("${yapi.host}")
    private String host;
    @Value("${http.connectTimeout}")
    private Integer connectTimeout;

    public HttpResponse doPost(String uri, Object requestParams, Header... header) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpContext httpContext = HttpClientContext.create();

        HttpPost httpPost = new HttpPost(host + uri);

        StringEntity postEntity = new StringEntity(JSON.toJSONString(requestParams), StandardCharsets.UTF_8);
        postEntity.setContentType("application/json");
        httpPost.setEntity(postEntity);

        if (!CollectionUtils.isEmpty(Arrays.asList(header))) {
            httpPost.setHeaders(header);
        }

        try {
            return httpClient.execute(httpPost, httpContext);
        } catch (IOException e) {
            log.debug("httpPost exception ", e);
            return null;
        }
    }


    public HttpResponse doGet(String uri, Header... header) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(host + uri);
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectTimeout(connectTimeout)
                .build();
        httpGet.setHeader(new BasicHeader("Content-Type", StandardCharsets.UTF_8.toString()));
        httpGet.setConfig(requestConfig);
        if (!CollectionUtils.isEmpty(Arrays.asList(header))) {
            httpGet.setHeaders(header);
        }
        return httpClient.execute(httpGet);
    }

}
