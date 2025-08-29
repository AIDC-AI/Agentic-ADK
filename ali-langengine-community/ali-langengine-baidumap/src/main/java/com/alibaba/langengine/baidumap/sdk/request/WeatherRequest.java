package com.alibaba.langengine.baidumap.sdk.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Weather API Request Parameters POJO Class
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherRequest {

    /**
     * District administrative division code, mutually exclusive with location
     */
    @JsonProperty("district_id")
    private String districtId;

    /**
     * Longitude and latitude, longitude first then latitude, separated by commas. 
     * Supported types: bd09mc/bd09ll/wgs84/gcj02
     */
    @JsonProperty("location")
    private String location;

    /**
     * Request data type. Available types: now/fc/index/alert/fc_hour/all, controls return content
     */
    @JsonProperty("data_type")
    private String dataType;

    /**
     * Return format, currently supports json/xml
     */
    @JsonProperty("output")
    private String output;

    /**
     * Coordinate type, supported types: wgs84/bd09ll/bd09mc/gcj02
     */
    @JsonProperty("coordtype")
    private String coordtype;

    // Getters and Setters

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getCoordtype() {
        return coordtype;
    }

    public void setCoordtype(String coordtype) {
        this.coordtype = coordtype;
    }
}
