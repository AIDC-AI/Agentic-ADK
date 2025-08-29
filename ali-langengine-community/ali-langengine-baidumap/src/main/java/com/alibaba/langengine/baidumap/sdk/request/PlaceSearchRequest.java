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

package com.alibaba.langengine.baidumap.sdk.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceSearchRequest {

    /**
     * Search keywords
     */
    @JsonProperty("query")
    private String query;

    /**
     * Search administrative region (supports district/county level)
     * (Increases weight of data recall within the region. To strictly limit recall data to the region,
     * use with region_limit parameter). Can input administrative region name or corresponding citycode
     */
    @JsonProperty("region")
    private String region;

    /**
     * Region data recall restriction. When true, only recalls data within the region corresponding to the region parameter
     */
    @JsonProperty("region_limit")
    private Boolean regionLimit;

    /**
     * true: Prioritizes search speed, sorting is simpler and more direct;
     * false, default: Optimizes the sorting of search results, making returned POIs closer to
     * Baidu Map App's recommended order, improving result relevance and experience;
     * may slightly increase response time, recommended for scenarios sensitive to result order
     * but with lower requirements on response time.
     */
    @JsonProperty("is_light_version")
    private Boolean lightVersion;

    /**
     * Secondary filtering of query recall results. Type content should refer to POI classification.
     * It is recommended that query and type belong to the same major category.
     * Example: query=美食 type=火锅 (for general search scenarios)
     * Note: query and type can be filled separately
     */
    @JsonProperty("type")
    private String type;

    /**
     * POI coordinates input to assist in sorting search results by distance and return
     * Note: Must be used together with sorting fields and combined with coord_type field
     * to specify the coordinate type of this field
     */
    @JsonProperty("center")
    private String center;

    /**
     * Search result detail level. Value of 1 or empty returns basic information;
     * Value of 2 returns detailed POI information
     */
    @JsonProperty("scope")
    private Integer scope;

    /**
     * Input coordinate type: 1 (wgs84ll, GPS coordinates), 2 (gcj02ll, National Bureau of Survey coordinates),
     * 3 (bd09ll, Baidu coordinates, default), 4 (bd09mc, Baidu meter coordinates)
     */
    @JsonProperty("coord_type")
    private Integer coordType;

    /**
     * Search sorting conditions, includes 3 parts:
     * industry_type: Industry types supported for sorting - hotel (hotels), cater (catering), life (life services)
     * sort_name: Sorting method - default (default), price (price), overall_rating (rating),
     * distance (distance sorting, requires center field)
     * sort_rule: Sorting rule - 0 (high to low), 1 (low to high)
     */
    @JsonProperty("filter")
    private String filter;

    /**
     * Whether to recall national standard administrative division codes: true (recall), false (no recall)
     */
    @JsonProperty("extensions_adcode")
    private Boolean extensionsAdcode;

    /**
     * When query is a structured address (e.g.: 上地十街10号), this parameter determines the type of data returned.
     * If not provided, defaults to recalling address data. Only when address_result=false,
     * corresponding POI data is recalled
     */
    @JsonProperty("address_result")
    private Boolean addressResult;

    /**
     * Whether to output image information: true (output), false (no output)
     * Note: Commercial license required. Contact business representative and submit work order to enable
     */
    @JsonProperty("photo_show")
    private Boolean photoShow;

    /**
     * Language type of query. Supports search terms in different languages as input.
     * Default is Chinese if not specified. Can be set to auto for automatic language detection by AI model
     */
    @JsonProperty("from_language")
    private String fromLanguage;

    /**
     * Multi-language search, supports returning results in multiple languages
     * Note: This is a premium paid feature. Submit a work order for consultation
     */
    @JsonProperty("language")
    private String language;

    /**
     * Pagination page number, defaults to 0. 0 represents first page, 1 represents second page, etc.
     * Usually used with page_size. Pagination only available when return results are POIs
     */
    @JsonProperty("page_num")
    private Integer pageNum;

    /**
     * Number of POIs recalled per request. Defaults to 10 records, maximum 20. Value range: 10-20
     */
    @JsonProperty("page_size")
    private Integer pageSize;

    /**
     * Return coordinate type. Optional parameter. If added, POIs return National Bureau of Survey coordinates
     */
    @JsonProperty("ret_coordtype")
    private String retCoordtype;

    /**
     * Output data format, only supports json
     */
    @JsonProperty("output")
    private String output;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Boolean getRegionLimit() {
        return regionLimit;
    }

    public void setRegionLimit(Boolean regionLimit) {
        this.regionLimit = regionLimit;
    }

    public Boolean getLightVersion() {
        return lightVersion;
    }

    public void setLightVersion(Boolean lightVersion) {
        this.lightVersion = lightVersion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public Integer getScope() {
        return scope;
    }

    public void setScope(Integer scope) {
        this.scope = scope;
    }

    public Integer getCoordType() {
        return coordType;
    }

    public void setCoordType(Integer coordType) {
        this.coordType = coordType;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Boolean getExtensionsAdcode() {
        return extensionsAdcode;
    }

    public void setExtensionsAdcode(Boolean extensionsAdcode) {
        this.extensionsAdcode = extensionsAdcode;
    }

    public Boolean getAddressResult() {
        return addressResult;
    }

    public void setAddressResult(Boolean addressResult) {
        this.addressResult = addressResult;
    }

    public Boolean getPhotoShow() {
        return photoShow;
    }

    public void setPhotoShow(Boolean photoShow) {
        this.photoShow = photoShow;
    }

    public String getFromLanguage() {
        return fromLanguage;
    }

    public void setFromLanguage(String fromLanguage) {
        this.fromLanguage = fromLanguage;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getRetCoordtype() {
        return retCoordtype;
    }

    public void setRetCoordtype(String retCoordtype) {
        this.retCoordtype = retCoordtype;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
