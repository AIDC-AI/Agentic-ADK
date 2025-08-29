package com.alibaba.langengine.amap.sdk.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Gaode Map Weather API Request Parameters POJO Class
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherRequest {

    /**
     * City code (adcode)
     */
    @JsonProperty("city")
    private String city;

    /**
     * Weather type, values: base/all
     * base: return current weather
     * all: return forecast weather
     */
    @JsonProperty("extensions")
    private String extensions;

    /**
     * Return format, values: JSON,XML
     */
    @JsonProperty("output")
    private String output;

    // Getters and Setters

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getExtensions() {
        return extensions;
    }

    public void setExtensions(String extensions) {
        this.extensions = extensions;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
