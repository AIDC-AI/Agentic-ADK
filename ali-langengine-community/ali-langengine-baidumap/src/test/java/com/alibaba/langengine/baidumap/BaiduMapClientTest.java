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

package com.alibaba.langengine.baidumap;

import com.alibaba.langengine.baidumap.sdk.BaiduMapClient;
import com.alibaba.langengine.baidumap.sdk.request.WeatherRequest;
import com.alibaba.langengine.baidumap.sdk.response.PlaceSearchResponse;
import com.alibaba.langengine.baidumap.sdk.response.WeatherResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "BAIDU_MAP_API_KEY", matches = ".*")
public class BaiduMapClientTest {

    private static final String API_KEY = System.getenv("BAIDU_MAP_API_KEY");

    @Test
    public void testPlaceSearch() {
        BaiduMapClient baiduMapClient = new BaiduMapClient(API_KEY);
        PlaceSearchResponse response = baiduMapClient.placeSearch("中街", "沈阳市沈河区");
        assert response != null;
    }

    @Test
    public void testWeather() {
        BaiduMapClient baiduMapClient = new BaiduMapClient(API_KEY);
        WeatherRequest request = new WeatherRequest();
        request.setDistrictId("222405");
        request.setDataType("all");
        WeatherResponse response = baiduMapClient.getWeather(request);
        assert response != null && response.getStatus() == 0;
    }

}
