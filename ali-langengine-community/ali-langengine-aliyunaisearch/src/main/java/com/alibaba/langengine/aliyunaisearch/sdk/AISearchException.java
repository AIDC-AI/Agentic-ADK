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

/**
 * AI Search Exception
 * Custom exception class for AI Search related errors
 */
public class AISearchException extends RuntimeException {
    private final String requestId;  // Exception request ID (for Aliyun technical support)
    private final String errorCode;  // Error code (e.g. InvalidParameter)
    private final Integer httpCode;  // HTTP status code (e.g. 400, 500)

    // Build exception from error response
    public AISearchException(WebSearchErrorResponse errorResponse) {
        super(String.format("Request failed (RequestId: %s): %s (Error code: %s, HTTP status code: %d)",
                errorResponse.getRequestId(),
                errorResponse.getMessage(),
                errorResponse.getCode(),
                errorResponse.getHttpCode()));
        this.requestId = errorResponse.getRequestId();
        this.errorCode = errorResponse.getCode();
        this.httpCode = errorResponse.getHttpCode();
    }

    // Default constructor with message
    public AISearchException(String message) {
        super(message);
        this.requestId = null;
        this.errorCode = null;
        this.httpCode = null;
    }

    // Custom exception message
    public AISearchException(String message, Throwable cause) {
        super(message, cause);
        this.requestId = null;
        this.errorCode = null;
        this.httpCode = null;
    }

    // Getter
    public String getRequestId() { return requestId; }
    public String getErrorCode() { return errorCode; }
    public Integer getHttpCode() { return httpCode; }
}