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

package com.alibaba.langengine.amap.sdk.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceSearchRequest {

    /**
     * Service permission key
     */
    @JsonProperty("key")
    private String key;

    /**
     * Query keywords
     */
    @JsonProperty("keywords")
    private String keywords;

    /**
     * POI types to search
     */
    @JsonProperty("types")
    private String types;

    /**
     * City for search
     */
    @JsonProperty("city")
    private String city;

    /**
     * Whether to limit results to specified city only
     */
    @JsonProperty("citylimit")
    private Boolean cityLimit;

    /**
     * Whether to group child POIs under parent POIs
     */
    @JsonProperty("children")
    private Integer children;

    /**
     * Number of records per page
     */
    @JsonProperty("offset")
    private Integer offset;

    /**
     * Current page number
     */
    @JsonProperty("page")
    private Integer page;

    /**
     * Result control extension
     */
    @JsonProperty("extensions")
    private String extensions;

    /**
     * Digital signature
     */
    @JsonProperty("sig")
    private String sig;

    /**
     * Callback function name
     */
    @JsonProperty("callback")
    private String callback;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getCityLimit() {
        return cityLimit;
    }

    public void setCityLimit(Boolean cityLimit) {
        this.cityLimit = cityLimit;
    }

    public Integer getChildren() {
        return children;
    }

    public void setChildren(Integer children) {
        this.children = children;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getExtensions() {
        return extensions;
    }

    public void setExtensions(String extensions) {
        this.extensions = extensions;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }
}
