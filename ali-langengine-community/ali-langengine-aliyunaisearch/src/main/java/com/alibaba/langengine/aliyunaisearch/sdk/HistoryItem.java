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
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;



/**
 * History Item
 * Represents a single item in the search history
 */
public class HistoryItem {
    // Role enumeration (document limitation: system, user, assistant)
    public enum Role {
        @SerializedName("system") SYSTEM,
        @SerializedName("user") USER,
        @SerializedName("assistant") ASSISTANT
    }

    @SerializedName("role")
    private final Role role;
    @SerializedName("content")
    private final String content;

    public HistoryItem(Role role, String content) {
        this.role = Objects.requireNonNull(role, "Conversation role cannot be null");
        this.content = Objects.requireNonNull(content, "Conversation content cannot be null");
    }

    // Getter
    public Role getRole() { return role; }
    public String getContent() { return content; }
}

/**
 * Search result filtering mode (corresponds to optional values of way parameter in documentation)
 */
enum SearchWayEnum {
    @SerializedName("normal") NORMAL,
    @SerializedName("fast") FAST,
    @SerializedName("full") FULL
}