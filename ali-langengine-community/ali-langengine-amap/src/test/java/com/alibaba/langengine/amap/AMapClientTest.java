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

package com.alibaba.langengine.amap;

import com.alibaba.langengine.amap.sdk.AMapClient;
import com.alibaba.langengine.amap.sdk.request.WeatherRequest;
import com.alibaba.langengine.amap.sdk.response.PlaceSearchResponse;
import com.alibaba.langengine.amap.sdk.response.WeatherResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "AMAP_API_KEY", matches = ".*")
public class AMapClientTest {

    private static final String API_KEY = System.getenv("AMAP_API_KEY");

    @Test
    public void testPlaceSearch() {
        AMapClient aMapClient = new AMapClient(API_KEY);
        PlaceSearchResponse response = aMapClient.placeSearch("中街", "沈阳市沈河区");
        assert response != null;
    }

    @Test
    public void testWeather() {
        AMapClient aMapClient = new AMapClient(API_KEY);
        WeatherRequest request = new WeatherRequest();
        request.setCity("130681");
        request.setExtensions("all");
        WeatherResponse response = aMapClient.getWeather(request);
        assert response != null && response.getStatus().equals("1");
    }

}
