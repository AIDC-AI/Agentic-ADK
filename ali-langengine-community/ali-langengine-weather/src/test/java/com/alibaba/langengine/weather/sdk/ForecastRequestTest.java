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

class ForecastRequestTest {

    @Test
    void testGettersAndSetters() {
        ForecastRequest request = new ForecastRequest();
        
        // Test key
        request.setKey("test-key");
        assertEquals("test-key", request.getKey());
        
        // Test q
        request.setQ("Beijing");
        assertEquals("Beijing", request.getQ());
        
        // Test days
        request.setDays(3);
        assertEquals(3, request.getDays());
        
        // Test dt
        request.setDt("2023-01-01");
        assertEquals("2023-01-01", request.getDt());
        
        // Test aqi
        request.setAqi("yes");
        assertEquals("yes", request.getAqi());
        
        // Test alerts
        request.setAlerts("no");
        assertEquals("no", request.getAlerts());
        
        // Test lang
        request.setLang("en");
        assertEquals("en", request.getLang());
    }
}