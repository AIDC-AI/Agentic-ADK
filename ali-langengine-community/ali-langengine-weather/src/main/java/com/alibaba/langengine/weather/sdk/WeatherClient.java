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
package com.alibaba.langengine.weather.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.alibaba.langengine.weather.WeatherConfiguration.WEATHER_API_KEY;
import static com.alibaba.langengine.weather.WeatherConfiguration.WEATHER_API_URL;
import static com.alibaba.langengine.weather.sdk.WeatherConstant.*;

/**
 * Weather API Client for Java
 * This client provides methods to interact with the Weather forecast API.
 */
public class WeatherClient {

    private final String apiKey;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a WeatherClient with a specified API key.
     * 
     * @param apiKey the API key for authentication with the Weather service
     */
    public WeatherClient(String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Constructs a WeatherClient using the default API key from configuration.
     */
    public WeatherClient() {
        this.apiKey = WEATHER_API_KEY;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Constructs a WeatherClient with a specified API key and custom OkHttpClient.
     * 
     * @param apiKey the API key for authentication with the Weather service
     * @param okHttpClient the custom OkHttpClient instance to use for HTTP requests
     */
    public WeatherClient(String apiKey, OkHttpClient okHttpClient) {
        this.apiKey = apiKey;
        this.client = okHttpClient;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Executes a forecast request to the Weather API.
     * 
     * @param request the forecast request parameters
     * @return the forecast response result
     * @throws WeatherException thrown when the API call fails
     */
    public ForecastResponse getForecast(ForecastRequest request) throws WeatherException {
        try {
            // Set the API key in the request
            request.setKey(apiKey);

            // Convert the request object to JSON
            String jsonBody = objectMapper.writeValueAsString(request);

            // Create the HTTP request
            Request httpRequest = new Request.Builder()
                    .url(WEATHER_API_URL + FORECAST_ENDPOINT + "?" + buildQueryString(request))
                    .get()
                    .build();

            // Execute the request
            try (Response response = client.newCall(httpRequest).execute()) {
                if (!response.isSuccessful()) {
                    throw new WeatherException("API request failed: " + response.code() + " " + response.message());
                }

                ResponseBody body = response.body();
                if (body == null) {
                    throw new WeatherException("API returned empty response");
                }

                // Parse the response
                return objectMapper.readValue(body.string(), ForecastResponse.class);
            }
        } catch (IOException e) {
            throw new WeatherException("Error occurred during API call", e);
        }
    }

    /**
     * Simplified forecast method using default parameters.
     * 
     * @param query the location query string
     * @param days the number of days to forecast (1-14)
     * @return the forecast response result
     * @throws WeatherException thrown when the API call fails
     */
    public ForecastResponse getForecast(String query, int days) throws WeatherException {
        ForecastRequest request = new ForecastRequest();
        request.setQ(query);
        request.setDays(days);
        request.setAqi("no");
        request.setAlerts("no");
        return getForecast(request);
    }
    
    /**
     * Build query string from request parameters
     * 
     * @param request the forecast request
     * @return query string
     */
    private String buildQueryString(ForecastRequest request) {
        StringBuilder query = new StringBuilder();
        
        if (request.getKey() != null) {
            query.append("key=").append(request.getKey());
        }
        
        if (request.getQ() != null) {
            if (query.length() > 0) query.append("&");
            query.append("q=").append(request.getQ());
        }
        
        if (request.getDays() != null) {
            if (query.length() > 0) query.append("&");
            query.append("days=").append(request.getDays());
        }
        
        if (request.getDt() != null) {
            if (query.length() > 0) query.append("&");
            query.append("dt=").append(request.getDt());
        }
        
        if (request.getAqi() != null) {
            if (query.length() > 0) query.append("&");
            query.append("aqi=").append(request.getAqi());
        }
        
        if (request.getAlerts() != null) {
            if (query.length() > 0) query.append("&");
            query.append("alerts=").append(request.getAlerts());
        }
        
        if (request.getLang() != null) {
            if (query.length() > 0) query.append("&");
            query.append("lang=").append(request.getLang());
        }
        
        return query.toString();
    }
}