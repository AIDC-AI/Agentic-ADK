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

import java.util.Objects;



/**
 * Client Configuration
 * Holds configuration parameters for the AI Search client
 */
public class ClientConfig {
    /** Service access address (public network or VPC, documentation provides example domain names) */
    private final String host;
    /** API-Key (format: Bearer OS-xxx, Authorization field in documentation) */
    private final String apiKey;
    /** Workspace name (default: default, workspace_name parameter in documentation) */
    private final String workspaceName;
    /** Service ID (system built-in: ops-web-search-001, service_id parameter in documentation) */
    private final String serviceId;
    /** Connection timeout (default: 30 seconds) */
    private final int connectTimeoutSeconds;
    /** Read timeout (default: 60 seconds) */
    private final int readTimeoutSeconds;

    // Constructor (Builder pattern for easy configuration extension)
    private ClientConfig(Builder builder) {
        this.host = Objects.requireNonNull(builder.host, "Host cannot be null (public/VPC address)");
        this.apiKey = Objects.requireNonNull(builder.apiKey, "API-Key cannot be null (format: Bearer OS-xxx)");
        this.workspaceName = builder.workspaceName == null ? "default" : builder.workspaceName;
        this.serviceId = builder.serviceId == null ? "ops-web-search-001" : builder.serviceId;
        this.connectTimeoutSeconds = builder.connectTimeoutSeconds <= 0 ? 30 : builder.connectTimeoutSeconds;
        this.readTimeoutSeconds = builder.readTimeoutSeconds <= 0 ? 60 : builder.readTimeoutSeconds;
    }

    // Builder static inner class
    public static class Builder {
        private String host;
        private String apiKey;
        private String workspaceName;
        private String serviceId;
        private int connectTimeoutSeconds;
        private int readTimeoutSeconds;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder workspaceName(String workspaceName) {
            this.workspaceName = workspaceName;
            return this;
        }

        public Builder serviceId(String serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        public Builder connectTimeoutSeconds(int connectTimeoutSeconds) {
            this.connectTimeoutSeconds = connectTimeoutSeconds;
            return this;
        }

        public Builder readTimeoutSeconds(int readTimeoutSeconds) {
            this.readTimeoutSeconds = readTimeoutSeconds;
            return this;
        }

        public ClientConfig build() {
            return new ClientConfig(this);
        }
    }

    // Getter methods (configuration is immutable for security)
    public String getHost() { return host; }
    public String getApiKey() { return apiKey; }
    public String getWorkspaceName() { return workspaceName; }
    public String getServiceId() { return serviceId; }
    public int getConnectTimeoutSeconds() { return connectTimeoutSeconds; }
    public int getReadTimeoutSeconds() { return readTimeoutSeconds; }
}