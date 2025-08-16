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

package com.alibaba.langengine.aliyunaisearch.sdk;

import com.google.gson.annotations.SerializedName;

/**
 * Web Search Error Response
 * Represents an error response from the web search API
 */
public class WebSearchErrorResponse {
    @SerializedName("request_id")
    private String requestId;   // Request ID (for troubleshooting)
    @SerializedName("latency")
    private Double latency;     // Response latency (seconds)
    @SerializedName("code")
    private String code;        // Error code (e.g. InvalidParameter)
    @SerializedName("http_code")
    private Integer httpCode;   // HTTP status code
    @SerializedName("message")
    private String message;     // Error message

    // Getter
    public String getRequestId() { return requestId; }
    public Double getLatency() { return latency; }
    public String getCode() { return code; }
    public Integer getHttpCode() { return httpCode; }
    public String getMessage() { return message; }
}