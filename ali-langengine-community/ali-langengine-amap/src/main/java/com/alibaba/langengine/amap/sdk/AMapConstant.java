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

package com.alibaba.langengine.amap.sdk;

public final class AMapConstant {

    private AMapConstant() {

    }

    public static final String AMAP_BASE_URL = "https://restapi.amap.com/v3";

    /**
     * The default timeout in seconds for API requests.
     */
    public static int DEFAULT_TIMEOUT = 30;

    public static final String PLACE_SEARCH_API_ENDPOINT = "/place/text";

    public static final String WEATHER_API_ENDPOINT = "/weather/weatherInfo";

}
