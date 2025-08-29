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

package com.alibaba.langengine.tencentmap.sdk.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceSearchRequest {

    /**
     * Search keyword, maximum length 96 bytes.
     * Note: keyword only supports searching for one term.
     * (API uses UTF-8 character encoding, 1 English character occupies 1 byte,
     * 1 Chinese character occupies 3 bytes)
     */
    @JsonProperty("keyword")
    private String keyword;

    /**
     * Format: boundary=nearby(lat,lng,radius[, auto_extend])
     * Format: boundary=region(city_name [,auto_extend][,lat,lng])
     * Format: boundary=rectangle(lat,lng,lat,lng)
     */
    @JsonProperty("boundary")
    private String boundary;

    /**
     * Whether to return sub-locations, such as building parking lots, entrances/exits, etc.
     * Values:
     * 0: [Default] Do not return
     * 1: Return
     */
    @JsonProperty("get_subpois")
    private Integer getSubpois;

    /**
     * Filter conditions:
     * 1. Specify category filter, format: category=categoryName1,categoryName2
     *    Recommended no more than 5 category terms, supports category codes
     * 2. Exclude specified categories, format: category&lt;&gt;categoryName1,categoryName2
     *    Recommended no more than 5 category terms, supports category codes
     * 3. Filter places with phone: tel&lt;&gt;null
     */
    @JsonProperty("filter")
    private String filter;

    /**
     * Return specified standard additional fields, supported values:
     * category_code - POI category code
     */
    @JsonProperty("added_fields")
    private String addedFields;

    /**
     * Sorting, supports sorting by distance from near to far, value: _distance
     * Note:
     * 1. Default sorting for nearby search considers multiple factors including distance and weight
     * 2. After setting distance sorting, only distance is considered, some low-weight places
     *    may rank first due to proximity, which may degrade user experience
     */
    @JsonProperty("orderby")
    private String orderby;

    /**
     * Number of items per page, maximum limit is 20, default is 10
     */
    @JsonProperty("page_size")
    private Integer pageSize;

    /**
     * Page number, default is page 1
     */
    @JsonProperty("page_index")
    private Integer pageIndex;

    /**
     * Return format:
     * Supports JSON/JSONP, default is JSON
     */
    @JsonProperty("output")
    private String output;

    /**
     * JSONP callback function
     */
    @JsonProperty("callback")
    private String callback;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getBoundary() {
        return boundary;
    }

    public void setBoundary(String boundary) {
        this.boundary = boundary;
    }

    public Integer getGetSubpois() {
        return getSubpois;
    }

    public void setGetSubpois(Integer getSubpois) {
        this.getSubpois = getSubpois;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getAddedFields() {
        return addedFields;
    }

    public void setAddedFields(String addedFields) {
        this.addedFields = addedFields;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }
}
