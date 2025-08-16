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
package com.alibaba.langengine.aliyunaisearch;

import com.alibaba.langengine.core.util.WorkPropertiesUtils;

/**
 * Aliyun AI Search Configuration
 * Retrieves configuration from work properties
 */
public class AliyunAiSearchConfiguration {
    /**
     * API Key for Aliyun AI Search
     * Retrieved from work properties with key "aliyun_ai_search_api_key"
     */
    public static String ALIYUN_AI_SEARCH_API_KEY = WorkPropertiesUtils.get("aliyun_ai_search_api_key");

    public static String ALIYUN_AI_SEARCH_API_HOST = WorkPropertiesUtils.get("aliyun_ai_search_api_host");

    public static String ALIYUN_AI_SEARCH_API_WORKSPACE = WorkPropertiesUtils.get("aliyun_ai_search_api_workspace");

    public static String ALIYUN_AI_SEARCH_API_SERVICE_ID = WorkPropertiesUtils.get("aliyun_ai_search_api_service_id");

    public static String ALIYUN_AI_SEARCH_API_TIME_OUT = WorkPropertiesUtils.get("aliyun_ai_search_api_time_out",30);

    public static String ALIYUN_AI_SEARCH_API_READ_TIME_OUT = WorkPropertiesUtils.get("aliyun_ai_search_api_read_time_out",60);


}