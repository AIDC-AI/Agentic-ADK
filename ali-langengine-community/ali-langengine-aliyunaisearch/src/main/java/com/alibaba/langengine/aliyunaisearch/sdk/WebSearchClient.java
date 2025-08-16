/**
 * Copyright (C) 2024 AIDC-AI
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
package com.alibaba.langengine.aliyunaisearch.sdk;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.alibaba.langengine.aliyunaisearch.AliyunAiSearchConfiguration.*;

/**
 * Aliyun Web Search Client (Jackson serialization implementation, core business logic)
 */
public class WebSearchClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSearchClient.class);
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");
    // Jackson core utility: Configure to ignore unknown fields (avoid parsing failures due to new API fields), support enum serialization
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

    private final OkHttpClient okHttpClient; // HTTP client
    private final ClientConfig clientConfig; // Client configuration
    private final String requestUrl;         // Full request URL (format: {host}/v3/openapi/workspaces/{workspaceName}/web-search/{serviceId})

    /**
     * Construct client (with configuration)
     * @param clientConfig Client configuration (including service address, API-Key, etc.)
     */
    public WebSearchClient(ClientConfig clientConfig) {
        this.clientConfig = Objects.requireNonNull(clientConfig, "Client configuration (ClientConfig) cannot be null");
        // Initialize OkHttpClient (set timeouts consistent with configuration)
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(clientConfig.getConnectTimeoutSeconds(), TimeUnit.SECONDS)
                .readTimeout(clientConfig.getReadTimeoutSeconds(), TimeUnit.SECONDS)
                .build();
        // Build request URL (remove trailing "/" from host to avoid URL format errors)
        this.requestUrl = String.format("%s/v3/openapi/workspaces/%s/web-search/%s",
                clientConfig.getHost().trim().replaceAll("/$", ""),
                clientConfig.getWorkspaceName(),
                clientConfig.getServiceId());
        LOGGER.info("WebSearchClient initialized, request URL: {}", requestUrl);
    }


    public WebSearchClient() {
        this.clientConfig = new ClientConfig.Builder()
                .apiKey(ALIYUN_AI_SEARCH_API_KEY)
                .serviceId(ALIYUN_AI_SEARCH_API_SERVICE_ID)
                .connectTimeoutSeconds(Integer.valueOf(ALIYUN_AI_SEARCH_API_TIME_OUT))
                .readTimeoutSeconds(Integer.valueOf(ALIYUN_AI_SEARCH_API_READ_TIME_OUT))
                .workspaceName(ALIYUN_AI_SEARCH_API_WORKSPACE)
                .build();
        // Initialize OkHttpClient (set timeouts consistent with configuration)
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(clientConfig.getConnectTimeoutSeconds(), TimeUnit.SECONDS)
                .readTimeout(clientConfig.getReadTimeoutSeconds(), TimeUnit.SECONDS)
                .build();
        // Build request URL (remove trailing "/" from host to avoid URL format errors)
        this.requestUrl = String.format("%s/v3/openapi/workspaces/%s/web-search/%s",
                clientConfig.getHost().trim().replaceAll("/$", ""),
                clientConfig.getWorkspaceName(),
                clientConfig.getServiceId());
        LOGGER.info("WebSearchClient initialized, request URL: {}", requestUrl);
    }

    /**
     * Execute search request
     * @param searchRequest Search request parameters (including search terms, conversation history, etc.)
     * @return Normal response result (WebSearchResponse)
     * @throws AISearchException Exception occurred during search (parameter error, service exception, serialization failure, etc.)
     */
    public WebSearchResponse doSearch(WebSearchRequest searchRequest) throws AISearchException {
        Objects.requireNonNull(searchRequest, "Search request (WebSearchRequest) cannot be null");

        try {
            // 1. Serialize request parameters to JSON (Jackson implementation)
            String requestBodyJson;
            try {
                requestBodyJson = OBJECT_MAPPER.writeValueAsString(searchRequest);
                LOGGER.debug("Executing search request: URL={}, Request body={}", requestUrl, requestBodyJson);
            } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                throw new AISearchException("Request parameter serialization failed (Jackson parsing error): " + e.getMessage(), e);
            }

            // 2. Build HTTP request (strictly aligned with documentation: POST method, Header configuration)
            Request request = new Request.Builder()
                    .url(requestUrl)
                    .addHeader("Content-Type", "application/json") // Documentation requirement: Content-Type is application/json
                    .addHeader("Authorization", clientConfig.getApiKey()) // Documentation requirement: Authorization is API-Key (Bearer OS-xxx)
                    .post(RequestBody.create(requestBodyJson, JSON_MEDIA_TYPE))
                    .build();

            // 3. Send request and get response
            try (Response response = okHttpClient.newCall(request).execute()) {
                String responseBody = Objects.requireNonNull(response.body()).string();
                LOGGER.debug("Search response: HTTP status code={}, Response body={}", response.code(), responseBody);

                // 4. Process response (distinguish normal/abnormal)
                if (response.isSuccessful()) {
                    // 4.1 Normal response: First check if it contains error fields (some scenarios have HTTP 200 but return errors)
                    if (responseBody.contains("\"code\"")) {
                        WebSearchErrorResponse errorResponse = parseErrorResponse(responseBody);
                        throw new AISearchException(errorResponse);
                    }
                    // Parse as normal response
                    try {
                        return OBJECT_MAPPER.readValue(responseBody, WebSearchResponse.class);
                    } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                        throw new AISearchException("Normal response deserialization failed (Jackson parsing error): " + e.getMessage(), e);
                    }
                } else {
                    // 4.2 Abnormal response (HTTP status code non-2xx): Parse error information
                    WebSearchErrorResponse errorResponse = parseErrorResponse(responseBody);
                    throw new AISearchException(errorResponse);
                }
            }
        } catch (IOException e) {
            // 5. Handle IO exceptions (such as network timeout, connection failure)
            LOGGER.error("Search request IO exception: ", e);
            throw new AISearchException("Search request failed (network exception): " + e.getMessage(), e);
        } catch (Exception e) {
            // 6. Handle other exceptions (such as parameter validation failure)
            LOGGER.error("Search request exception: ", e);
            if (e instanceof AISearchException) {
                throw (AISearchException) e;
            } else {
                throw new AISearchException("Search request failed: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Private utility method: Parse error response (reusable logic)
     */
    private WebSearchErrorResponse parseErrorResponse(String responseBody) throws AISearchException {
        try {
            return OBJECT_MAPPER.readValue(responseBody, WebSearchErrorResponse.class);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new AISearchException("Error response deserialization failed (Jackson parsing error): " + e.getMessage(), e);
        }
    }
}