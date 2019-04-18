/*
 * Copyright 2019 Shanghai Qiyin Information Technology Co., Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yuanben.common;

import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description: 发送http的get、post请求
 */
public class HttpUtil {

    public static String ContentTypeFormData = "multipart/form-data";
    public static String ContentTypeJson = "application/json";

    protected HttpClientConnectionManager connectionManager = createHttpClientConnectionManager();
    protected CloseableHttpClient httpClient;


    public HttpUtil() throws HttpException {
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url      发送请求的 URL
     * @param jsonData 请求参数
     * @return 所代表远程资源的响应结果
     */
    public String sendPost(String url, String jsonData, Map<String, String> headers) throws Exception {
        httpClient = createHttpClient(connectionManager);

        HttpPost httpPost = new HttpPost(url);
        if (headers != null && headers.size() > 0) {
            Iterator<String> iterator = headers.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                httpPost.addHeader(key, headers.get(key));
            }
        }
        StringEntity entity = new StringEntity(jsonData, "UTF-8");

        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            String line = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).readLine();
            throw new HttpException(Strings.unicodeToCn(line));
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line;
        String respJson = "";
        while ((line = reader.readLine()) != null) {
            respJson += line;
        }
        return respJson;
    }

    protected HttpClientConnectionManager createHttpClientConnectionManager() throws HttpException {
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }

            }).build();

        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        }

        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext,
                NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory).build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry);
        connectionManager.setDefaultMaxPerRoute(1024);
        connectionManager.setMaxTotal(1024);
        connectionManager.setValidateAfterInactivity(20000);
        connectionManager.setDefaultSocketConfig(
                SocketConfig.custom().setSoTimeout(50 * 1000).setTcpNoDelay(true).build());
        return connectionManager;
    }

    protected CloseableHttpClient createHttpClient(HttpClientConnectionManager connectionManager) {
        return HttpClients.custom().setConnectionManager(connectionManager)
                .disableContentCompression().disableAutomaticRetries().build();
    }
}




