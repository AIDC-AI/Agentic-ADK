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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeatherClientTest {

    @Test
    void getForecast() {
        // Test forecast functionality using the default API key
        WeatherClient client = new WeatherClient();
        assertDoesNotThrow(() -> {
            ForecastResponse response = client.getForecast("Beijing", 3);
            assertNotNull(response);
            // Additional assertions can be added to validate the response content
        });
    }

    @Test
    void testGetForecast() {
        // Test forecast functionality using a custom ForecastRequest
        WeatherClient client = new WeatherClient();
        ForecastRequest request = new ForecastRequest();
        request.setQ("Shanghai");
        request.setDays(1);
        request.setAqi("no");
        request.setAlerts("no");
        
        assertDoesNotThrow(() -> {
            ForecastResponse response = client.getForecast(request);
            assertNotNull(response);
            // Additional assertions can be added to validate the response content
        });
    }
}