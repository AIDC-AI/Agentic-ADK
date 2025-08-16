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
package com.alibaba.langengine.sinanews.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.alibaba.langengine.sinanews.SinanewsConfiguration.SINANEWS_API_URL;
import static com.alibaba.langengine.sinanews.sdk.SinanewsConstant.*;

/**
 * Sinanews API Client for Java
 * This client provides methods to interact with the Sinanews hotlist API.
 */
public class SinanewsClient {

    private final String baseUrl;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a SinanewsClient using the default base URL from configuration.
     */
    public SinanewsClient() {
        this.baseUrl = SINANEWS_API_URL;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Constructs a SinanewsClient with a custom base URL.
     * 
     * @param baseUrl the base URL for the Sinanews service
     */
    public SinanewsClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Constructs a SinanewsClient with a custom base URL and custom OkHttpClient.
     * 
     * @param baseUrl the base URL for the Sinanews service
     * @param okHttpClient the custom OkHttpClient instance to use for HTTP requests
     */
    public SinanewsClient(String baseUrl, OkHttpClient okHttpClient) {
        this.baseUrl = baseUrl;
        this.client = okHttpClient;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Executes a hotlist request to the Sinanews API.
     * 
     * @param request the hotlist request
     * @return the hotlist response
     * @throws SinanewsException if the request fails
     */
    public HotlistResponse getHotlist(HotlistRequest request) throws SinanewsException {
        try {
            // Build the URL
            String url = buildUrl(request);
            
            // Build the HTTP request
            Request httpRequest = new Request.Builder()
                    .url(url)
                    .addHeader("Accept", "application/json")
                    .get()
                    .build();
            
            // Execute the request
            try (Response response = client.newCall(httpRequest).execute()) {
                if (!response.isSuccessful() || response.body() == null) {
                    throw new SinanewsException("API request failed with HTTP status: " + response.code());
                }
                
                // Parse successful response
                String responseBody = response.body().string();
                return objectMapper.readValue(responseBody, HotlistResponse.class);
            }
        } catch (IOException e) {
            throw new SinanewsException("Failed to get hotlist", e);
        }
    }
    
    /**
     * Gets the default hotlist.
     * 
     * @return the hotlist response
     * @throws SinanewsException if the request fails
     */
    public HotlistResponse getHotlist() throws SinanewsException {
        HotlistRequest request = new HotlistRequest(DEFAULT_NEWS_ID);
        return getHotlist(request);
    }
    
    /**
     * Build the full URL for the hotlist API.
     *
     * @param request the hotlist request
     * @return Full URL string
     */
    private String buildUrl(HotlistRequest request) {
        String url = baseUrl;
        if (!url.contains("?")) {
            url += "?";
        } else if (!url.endsWith("&")) {
            url += "&";
        }
        
        url += "newsId=" + request.getNewsId();
        return url;
    }
}