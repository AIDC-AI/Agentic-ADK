package com.alibaba.langengine.tencentmap.sdk.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Tencent Map Weather API Request Parameters POJO Class
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherRequest {

    /**
     * Administrative division code, mutually exclusive with location
     */
    @JsonProperty("adcode")
    private String adcode;

    /**
     * Location longitude and latitude, mutually exclusive with adcode
     */
    @JsonProperty("location")
    private String location;

    /**
     * Query weather type, values: now[default], future
     */
    @JsonProperty("type")
    private String type;

    /**
     * Future forecast days, only valid when type=future, values: 0[default], 1
     */
    @JsonProperty("get_md")
    private Integer getMd;

    /**
     * Return format, supports JSON/JSONP, default JSON
     */
    @JsonProperty("output")
    private String output;

    /**
     * JSONP callback function
     */
    @JsonProperty("callback")
    private String callback;

    // Getters and Setters

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getGetMd() {
        return getMd;
    }

    public void setGetMd(Integer getMd) {
        this.getMd = getMd;
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
