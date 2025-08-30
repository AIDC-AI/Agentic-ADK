package com.alibaba.langengine.feishu.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.langengine.feishu.FeishuConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;


@Slf4j
public class FeishuClient {

    private final FeishuConfiguration configuration;
    private final CloseableHttpClient httpClient;
    private final Map<String, String> tokenCache = new ConcurrentHashMap<>();
    private final Map<String, Long> tokenExpireTime = new ConcurrentHashMap<>();
    private final ReentrantLock tokenLock = new ReentrantLock();

    /**
     * 构造函数
     * 
     * @param configuration 飞书配置
     */
    public FeishuClient(FeishuConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("FeishuConfiguration cannot be null");
        }
        if (!configuration.isValid()) {
            throw new IllegalArgumentException("Invalid FeishuConfiguration: " + configuration);
        }
        
        this.configuration = configuration;
        
        // 创建HTTP客户端
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(configuration.getConnectTimeout()))
                .setResponseTimeout(Timeout.ofMilliseconds(configuration.getReadTimeout()))
                .build();
                
        this.httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
                
        log.info("FeishuClient initialized with configuration: {}", configuration);
    }

    /**
     * 获取tenant_access_token
     * 
     * @return tenant_access_token
     * @throws FeishuException 获取失败时抛出异常
     */
    public String getTenantAccessToken() throws FeishuException {
        String cacheKey = "tenant_access_token";
        
        // 检查缓存中的令牌是否有效
        if (isTokenValid(cacheKey)) {
            return tokenCache.get(cacheKey);
        }
        
        tokenLock.lock();
        try {
            // 双重检查
            if (isTokenValid(cacheKey)) {
                return tokenCache.get(cacheKey);
            }
            
            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("app_id", configuration.getAppId());
            requestBody.put("app_secret", configuration.getAppSecret());
            
            // 发送请求
            String response = doPost(FeishuConstant.API_TENANT_ACCESS_TOKEN, requestBody.toJSONString(), null);
            JSONObject responseJson = JSON.parseObject(response);
            
            // 检查响应
            int code = responseJson.getIntValue("code");
            if (code != FeishuConstant.CODE_SUCCESS) {
                String msg = responseJson.getString("msg");
                throw new FeishuException(code, msg);
            }
            
            // 提取令牌
            String token = responseJson.getString("tenant_access_token");
            int expire = responseJson.getIntValue("expire");
            
            // 缓存令牌
            tokenCache.put(cacheKey, token);
            tokenExpireTime.put(cacheKey, System.currentTimeMillis() + (expire - FeishuConstant.TOKEN_REFRESH_ADVANCE_TIME) * 1000L);
            
            log.debug("Successfully obtained tenant_access_token, expires in {} seconds", expire);
            return token;
            
        } finally {
            tokenLock.unlock();
        }
    }

    /**
     * 获取app_access_token
     * 
     * @return app_access_token
     * @throws FeishuException 获取失败时抛出异常
     */
    public String getAppAccessToken() throws FeishuException {
        String cacheKey = "app_access_token";
        
        // 检查缓存中的令牌是否有效
        if (isTokenValid(cacheKey)) {
            return tokenCache.get(cacheKey);
        }
        
        tokenLock.lock();
        try {
            // 双重检查
            if (isTokenValid(cacheKey)) {
                return tokenCache.get(cacheKey);
            }
            
            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("app_id", configuration.getAppId());
            requestBody.put("app_secret", configuration.getAppSecret());
            
            // 发送请求
            String response = doPost(FeishuConstant.API_APP_ACCESS_TOKEN, requestBody.toJSONString(), null);
            JSONObject responseJson = JSON.parseObject(response);
            
            // 检查响应
            int code = responseJson.getIntValue("code");
            if (code != FeishuConstant.CODE_SUCCESS) {
                String msg = responseJson.getString("msg");
                throw new FeishuException(code, msg);
            }
            
            // 提取令牌
            String token = responseJson.getString("app_access_token");
            int expire = responseJson.getIntValue("expire");
            
            // 缓存令牌
            tokenCache.put(cacheKey, token);
            tokenExpireTime.put(cacheKey, System.currentTimeMillis() + (expire - FeishuConstant.TOKEN_REFRESH_ADVANCE_TIME) * 1000L);
            
            log.debug("Successfully obtained app_access_token, expires in {} seconds", expire);
            return token;
            
        } finally {
            tokenLock.unlock();
        }
    }

    /**
     * 执行GET请求
     * 
     * @param path API路径
     * @param accessToken 访问令牌
     * @return 响应内容
     * @throws FeishuException 请求失败时抛出异常
     */
    public String doGet(String path, String accessToken) throws FeishuException {
        return doGet(path, accessToken, 0);
    }

    /**
     * 执行POST请求
     * 
     * @param path API路径
     * @param requestBody 请求体
     * @param accessToken 访问令牌
     * @return 响应内容
     * @throws FeishuException 请求失败时抛出异常
     */
    public String doPost(String path, String requestBody, String accessToken) throws FeishuException {
        return doPost(path, requestBody, accessToken, 0);
    }

    /**
     * 执行PUT请求
     * 
     * @param path API路径
     * @param requestBody 请求体
     * @param accessToken 访问令牌
     * @return 响应内容
     * @throws FeishuException 请求失败时抛出异常
     */
    public String doPut(String path, String requestBody, String accessToken) throws FeishuException {
        return doPut(path, requestBody, accessToken, 0);
    }

    /**
     * 执行GET请求（带重试）
     *
     * @param path API路径
     * @param accessToken 访问令牌
     * @param retryCount 重试次数
     * @return 响应内容
     * @throws FeishuException 请求失败时抛出异常
     */
    private String doGet(String path, String accessToken, int retryCount) throws FeishuException {
        try {
            String url = configuration.getApiUrl(path);
            HttpGet httpGet = new HttpGet(url);

            // 设置请求头
            httpGet.setHeader(FeishuConstant.HEADER_CONTENT_TYPE, FeishuConstant.CONTENT_TYPE_JSON);
            if (accessToken != null && !accessToken.isEmpty()) {
                httpGet.setHeader(FeishuConstant.HEADER_AUTHORIZATION, FeishuConstant.BEARER_PREFIX + accessToken);
            }

            if (configuration.isDebug()) {
                log.debug("GET request to: {}", url);
            }

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                String responseBody = EntityUtils.toString(entity, FeishuConstant.CHARSET_UTF8);

                if (configuration.isDebug()) {
                    log.debug("Response status: {}, body: {}", response.getCode(), responseBody);
                }

                // 检查HTTP状态码
                if (response.getCode() >= 400) {
                    throw new FeishuException("HTTP error: " + response.getCode() + ", response: " + responseBody);
                }

                return responseBody;
            }

        } catch (IOException | org.apache.hc.core5.http.ParseException e) {
            if (retryCount < configuration.getMaxRetries()) {
                log.warn("GET request failed, retrying... (attempt {}/{})", retryCount + 1, configuration.getMaxRetries(), e);
                try {
                    Thread.sleep(configuration.getRetryInterval());
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new FeishuException("Request interrupted", ie);
                }
                return doGet(path, accessToken, retryCount + 1);
            }
            throw new FeishuException("GET request failed after " + configuration.getMaxRetries() + " retries", e);
        }
    }

    /**
     * 执行POST请求（带重试）
     *
     * @param path API路径
     * @param requestBody 请求体
     * @param accessToken 访问令牌
     * @param retryCount 重试次数
     * @return 响应内容
     * @throws FeishuException 请求失败时抛出异常
     */
    private String doPost(String path, String requestBody, String accessToken, int retryCount) throws FeishuException {
        try {
            String url = configuration.getApiUrl(path);
            HttpPost httpPost = new HttpPost(url);

            // 设置请求头
            httpPost.setHeader(FeishuConstant.HEADER_CONTENT_TYPE, FeishuConstant.CONTENT_TYPE_JSON);
            if (accessToken != null && !accessToken.isEmpty()) {
                httpPost.setHeader(FeishuConstant.HEADER_AUTHORIZATION, FeishuConstant.BEARER_PREFIX + accessToken);
            }

            // 设置请求体
            if (requestBody != null && !requestBody.isEmpty()) {
                httpPost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));
            }

            if (configuration.isDebug()) {
                log.debug("POST request to: {}, body: {}", url, requestBody);
            }

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity entity = response.getEntity();
                String responseBody = EntityUtils.toString(entity, FeishuConstant.CHARSET_UTF8);

                if (configuration.isDebug()) {
                    log.debug("Response status: {}, body: {}", response.getCode(), responseBody);
                }

                // 检查HTTP状态码
                if (response.getCode() >= 400) {
                    throw new FeishuException("HTTP error: " + response.getCode() + ", response: " + responseBody);
                }

                return responseBody;
            }

        } catch (IOException | org.apache.hc.core5.http.ParseException e) {
            if (retryCount < configuration.getMaxRetries()) {
                log.warn("POST request failed, retrying... (attempt {}/{})", retryCount + 1, configuration.getMaxRetries(), e);
                try {
                    Thread.sleep(configuration.getRetryInterval());
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new FeishuException("Request interrupted", ie);
                }
                return doPost(path, requestBody, accessToken, retryCount + 1);
            }
            throw new FeishuException("POST request failed after " + configuration.getMaxRetries() + " retries", e);
        }
    }

    /**
     * 执行PUT请求（带重试）
     *
     * @param path API路径
     * @param requestBody 请求体
     * @param accessToken 访问令牌
     * @param retryCount 重试次数
     * @return 响应内容
     * @throws FeishuException 请求失败时抛出异常
     */
    private String doPut(String path, String requestBody, String accessToken, int retryCount) throws FeishuException {
        try {
            String url = configuration.getApiUrl(path);
            HttpPut httpPut = new HttpPut(url);

            // 设置请求头
            httpPut.setHeader(FeishuConstant.HEADER_CONTENT_TYPE, FeishuConstant.CONTENT_TYPE_JSON);
            if (accessToken != null && !accessToken.isEmpty()) {
                httpPut.setHeader(FeishuConstant.HEADER_AUTHORIZATION, FeishuConstant.BEARER_PREFIX + accessToken);
            }

            // 设置请求体
            if (requestBody != null && !requestBody.isEmpty()) {
                httpPut.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));
            }

            if (configuration.isDebug()) {
                log.debug("PUT request to: {}, body: {}", url, requestBody);
            }

            try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
                HttpEntity entity = response.getEntity();
                String responseBody = EntityUtils.toString(entity, FeishuConstant.CHARSET_UTF8);

                if (configuration.isDebug()) {
                    log.debug("Response status: {}, body: {}", response.getCode(), responseBody);
                }

                // 检查HTTP状态码
                if (response.getCode() >= 400) {
                    throw new FeishuException("HTTP error: " + response.getCode() + ", response: " + responseBody);
                }

                return responseBody;
            }

        } catch (IOException | org.apache.hc.core5.http.ParseException e) {
            if (retryCount < configuration.getMaxRetries()) {
                log.warn("PUT request failed, retrying... (attempt {}/{})", retryCount + 1, configuration.getMaxRetries(), e);
                try {
                    Thread.sleep(configuration.getRetryInterval());
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new FeishuException("Request interrupted", ie);
                }
                return doPut(path, requestBody, accessToken, retryCount + 1);
            }
            throw new FeishuException("PUT request failed after " + configuration.getMaxRetries() + " retries", e);
        }
    }

    /**
     * 检查令牌是否有效
     *
     * @param cacheKey 缓存键
     * @return 令牌是否有效
     */
    private boolean isTokenValid(String cacheKey) {
        String token = tokenCache.get(cacheKey);
        Long expireTime = tokenExpireTime.get(cacheKey);

        return token != null && expireTime != null && System.currentTimeMillis() < expireTime;
    }



    /**
     * 关闭客户端，释放资源
     */
    public void close() {
        try {
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException e) {
            log.warn("Failed to close HTTP client", e);
        }
    }
}
