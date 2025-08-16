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
import lombok.Data;


/**
 * Single search result (corresponds to result.search_result list element in documentation)
 */
public class SearchResult {
    @SerializedName("title")
    private String title;       // Web page title
    @SerializedName("link")
    private String link;        // Web page link
    @SerializedName("snippet")
    private String snippet;     // Web page abstract
    @SerializedName("content")
    private String content;     // Web page content
    @SerializedName("position")
    private Integer position;   // Result position

    // Getter
    public String getTitle() { return title; }
    public String getLink() { return link; }
    public String getSnippet() { return snippet; }
    public String getContent() { return content; }
    public Integer getPosition() { return position; }
}

/**
 * Query rewrite model token usage (corresponds to usage.rewrite_model in documentation)
 */
class RewriteModelUsage {
    @SerializedName("input_tokens")
    private Integer inputTokens;
    @SerializedName("output_tokens")
    private Integer outputTokens;
    @SerializedName("total_tokens")
    private Integer totalTokens;

    // Getter
    public Integer getInputTokens() { return inputTokens; }
    public Integer getOutputTokens() { return outputTokens; }
    public Integer getTotalTokens() { return totalTokens; }
}

/**
 * Result filtering model token usage (corresponds to usage.filter_model in documentation)
 */
class FilterModelUsage {
    @SerializedName("input_tokens")
    private Integer inputTokens;
    @SerializedName("output_tokens")
    private Integer outputTokens;
    @SerializedName("total_tokens")
    private Integer totalTokens;

    // Getter
    public Integer getInputTokens() { return inputTokens; }
    public Integer getOutputTokens() { return outputTokens; }
    public Integer getTotalTokens() { return totalTokens; }
}

/**
 * Overall usage statistics (corresponds to usage parameter in documentation)
 */
class Usage {
    @SerializedName("search_count")
    private Integer searchCount;          // Search count
    @SerializedName("rewrite_model")
    private RewriteModelUsage rewriteModel; // Rewrite model usage
    @SerializedName("filter_model")
    private FilterModelUsage filterModel;  // Filter model usage

    // Getter
    public Integer getSearchCount() { return searchCount; }
    public RewriteModelUsage getRewriteModel() { return rewriteModel; }
    public FilterModelUsage getFilterModel() { return filterModel; }
}