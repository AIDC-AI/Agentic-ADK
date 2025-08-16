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

class ForecastResponseTest {

    @Test
    void testForecastResponseGettersAndSetters() {
        ForecastResponse response = new ForecastResponse();
        
        // Test location
        ForecastResponse.Location location = new ForecastResponse.Location();
        location.setName("Beijing");
        response.setLocation(location);
        assertEquals("Beijing", response.getLocation().getName());
        
        // Test current
        ForecastResponse.Current current = new ForecastResponse.Current();
        current.setTemp_c(20.5);
        response.setCurrent(current);
        assertEquals(20.5, response.getCurrent().getTemp_c());
        
        // Test forecast
        ForecastResponse.Forecast forecast = new ForecastResponse.Forecast();
        response.setForecast(forecast);
        assertNotNull(response.getForecast());
    }
    
    @Test
    void testLocationGettersAndSetters() {
        ForecastResponse.Location location = new ForecastResponse.Location();
        
        location.setName("Beijing");
        assertEquals("Beijing", location.getName());
        
        location.setRegion("Beijing");
        assertEquals("Beijing", location.getRegion());
        
        location.setCountry("China");
        assertEquals("China", location.getCountry());
        
        location.setLat(39.9042);
        assertEquals(39.9042, location.getLat());
        
        location.setLon(116.4074);
        assertEquals(116.4074, location.getLon());
    }
    
    @Test
    void testCurrentGettersAndSetters() {
        ForecastResponse.Current current = new ForecastResponse.Current();
        
        current.setTemp_c(20.5);
        assertEquals(20.5, current.getTemp_c());
        
        current.setTemp_f(68.9);
        assertEquals(68.9, current.getTemp_f());
        
        current.setHumidity(65);
        assertEquals(65, current.getHumidity());
    }
    
    @Test
    void testConditionGettersAndSetters() {
        ForecastResponse.Condition condition = new ForecastResponse.Condition();
        
        condition.setText("Sunny");
        assertEquals("Sunny", condition.getText());
        
        condition.setIcon("icon-url");
        assertEquals("icon-url", condition.getIcon());
        
        condition.setCode(1000);
        assertEquals(1000, condition.getCode());
    }
}