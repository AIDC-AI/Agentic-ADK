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

package com.alibaba.langengine.tencentmap;

import com.alibaba.langengine.tencentmap.sdk.request.WeatherRequest;
import com.alibaba.langengine.tencentmap.sdk.response.PlaceSearchResponse;
import com.alibaba.langengine.tencentmap.sdk.TencentMapClient;
import com.alibaba.langengine.tencentmap.sdk.response.WeatherResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "TENCENT_MAP_API_KEY", matches = ".*")
public class TencentMapClientTest {

    private static final String API_KEY = System.getenv("TENCENT_MAP_API_KEY");

    @Test
    public void testPlaceSearch() {
        TencentMapClient tencentMapClient = new TencentMapClient(API_KEY);
        PlaceSearchResponse response = tencentMapClient.placeSearch("中街", "沈阳");
        assert response != null;
    }

    @Test
    public void testWeatherNow() {
        TencentMapClient tencentMapClient = new TencentMapClient(API_KEY);
        WeatherRequest request = new WeatherRequest();
        request.setAdcode("130681");
        request.setType("now");
        WeatherResponse response = tencentMapClient.getWeather(request);
        assert response != null && response.getStatus() == 0;
    }

    @Test
    public void testWeatherFuture() {
        TencentMapClient tencentMapClient = new TencentMapClient(API_KEY);
        WeatherRequest request = new WeatherRequest();
        request.setAdcode("130681");
        request.setType("future");
        WeatherResponse response = tencentMapClient.getWeather(request);
        assert response != null && response.getStatus() == 0;
    }

}
