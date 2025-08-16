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
import java.util.List;

/**
 * Normal response result (corresponds to "normal response example" in documentation)
 */
public class WebSearchResponse {
    @SerializedName("result")
    private Result result;
    @SerializedName("usage")
    private Usage usage;

    // Inner class: Result (corresponds to result parameter in documentation)
    public static class Result {
        @SerializedName("search_result")
        private List<SearchResult> searchResult;

        // Getter
        public List<SearchResult> getSearchResult() { return searchResult; }
    }

    // Getter
    public Result getResult() { return result; }
    public Usage getUsage() { return usage; }
}