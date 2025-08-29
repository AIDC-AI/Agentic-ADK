package com.alibaba.langengine.tencentmap.sdk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Tencent Map Weather API Response POJO Class
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    /**
     * Status code, 0 for success, others for error
     */
    @JsonProperty("status")
    private Integer status;

    /**
     * Description of status
     */
    @JsonProperty("message")
    private String message;

    /**
     * Weather query result
     */
    @JsonProperty("result")
    private Result result;

    // Getters and Setters

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    /**
     * Weather query result
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {

        /**
         * Real-time weather information, returned when type=now
         */
        @JsonProperty("realtime")
        private List<Realtime> realtime;

        /**
         * Forecast weather information, returned when type=future
         */
        @JsonProperty("forecast")
        private List<Forecast> forecast;

        // Getters and Setters

        public List<Realtime> getRealtime() {
            return realtime;
        }

        public void setRealtime(List<Realtime> realtime) {
            this.realtime = realtime;
        }

        public List<Forecast> getForecast() {
            return forecast;
        }

        public void setForecast(List<Forecast> forecast) {
            this.forecast = forecast;
        }
    }

    /**
     * Real-time weather information
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Realtime {

        /**
         * Province
         */
        @JsonProperty("province")
        private String province;

        /**
         * City
         */
        @JsonProperty("city")
        private String city;

        /**
         * District
         */
        @JsonProperty("district")
        private String district;

        /**
         * Administrative division code
         */
        @JsonProperty("adcode")
        private Integer adcode;

        /**
         * Update time
         */
        @JsonProperty("update_time")
        private String updateTime;

        /**
         * Weather information
         */
        @JsonProperty("infos")
        private RealtimeInfo infos;

        // Getters and Setters

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public Integer getAdcode() {
            return adcode;
        }

        public void setAdcode(Integer adcode) {
            this.adcode = adcode;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public RealtimeInfo getInfos() {
            return infos;
        }

        public void setInfos(RealtimeInfo infos) {
            this.infos = infos;
        }
    }

    /**
     * Real-time weather information details
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RealtimeInfo {

        /**
         * Weather description
         */
        @JsonProperty("weather")
        private String weather;

        /**
         * Temperature, unit: Celsius
         */
        @JsonProperty("temperature")
        private Integer temperature;

        /**
         * Wind direction description
         */
        @JsonProperty("wind_direction")
        private String windDirection;

        /**
         * Wind power description
         */
        @JsonProperty("wind_power")
        private String windPower;

        /**
         * Humidity, unit: percentage
         */
        @JsonProperty("humidity")
        private Integer humidity;

        // Getters and Setters

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public Integer getTemperature() {
            return temperature;
        }

        public void setTemperature(Integer temperature) {
            this.temperature = temperature;
        }

        public String getWindDirection() {
            return windDirection;
        }

        public void setWindDirection(String windDirection) {
            this.windDirection = windDirection;
        }

        public String getWindPower() {
            return windPower;
        }

        public void setWindPower(String windPower) {
            this.windPower = windPower;
        }

        public Integer getHumidity() {
            return humidity;
        }

        public void setHumidity(Integer humidity) {
            this.humidity = humidity;
        }
    }

    /**
     * Forecast weather information
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Forecast {

        /**
         * Province
         */
        @JsonProperty("province")
        private String province;

        /**
         * City
         */
        @JsonProperty("city")
        private String city;

        /**
         * District
         */
        @JsonProperty("district")
        private String district;

        /**
         * Administrative division code
         */
        @JsonProperty("adcode")
        private Integer adcode;

        /**
         * Update time
         */
        @JsonProperty("update_time")
        private String updateTime;

        /**
         * Weather information
         */
        @JsonProperty("infos")
        private List<ForecastInfo> infos;

        // Getters and Setters

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public Integer getAdcode() {
            return adcode;
        }

        public void setAdcode(Integer adcode) {
            this.adcode = adcode;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public List<ForecastInfo> getInfos() {
            return infos;
        }

        public void setInfos(List<ForecastInfo> infos) {
            this.infos = infos;
        }
    }

    /**
     * Forecast weather information details
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForecastInfo {

        /**
         * Date
         */
        @JsonProperty("date")
        private String date;

        /**
         * Week
         */
        @JsonProperty("week")
        private String week;

        /**
         * Day weather
         */
        @JsonProperty("day")
        private DayWeather day;

        /**
         * Night weather
         */
        @JsonProperty("night")
        private NightWeather night;

        // Getters and Setters

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public DayWeather getDay() {
            return day;
        }

        public void setDay(DayWeather day) {
            this.day = day;
        }

        public NightWeather getNight() {
            return night;
        }

        public void setNight(NightWeather night) {
            this.night = night;
        }
    }

    /**
     * Day weather information
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DayWeather {

        /**
         * Weather description
         */
        @JsonProperty("weather")
        private String weather;

        /**
         * Temperature, unit: Celsius
         */
        @JsonProperty("temperature")
        private Integer temperature;

        /**
         * Wind direction description
         */
        @JsonProperty("wind_direction")
        private String windDirection;

        /**
         * Wind power description
         */
        @JsonProperty("wind_power")
        private String windPower;

        /**
         * Humidity, unit: percentage
         */
        @JsonProperty("humidity")
        private Integer humidity;

        // Getters and Setters

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public Integer getTemperature() {
            return temperature;
        }

        public void setTemperature(Integer temperature) {
            this.temperature = temperature;
        }

        public String getWindDirection() {
            return windDirection;
        }

        public void setWindDirection(String windDirection) {
            this.windDirection = windDirection;
        }

        public String getWindPower() {
            return windPower;
        }

        public void setWindPower(String windPower) {
            this.windPower = windPower;
        }

        public Integer getHumidity() {
            return humidity;
        }

        public void setHumidity(Integer humidity) {
            this.humidity = humidity;
        }
    }

    /**
     * Night weather information
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NightWeather {

        /**
         * Weather description
         */
        @JsonProperty("weather")
        private String weather;

        /**
         * Temperature, unit: Celsius
         */
        @JsonProperty("temperature")
        private Integer temperature;

        /**
         * Wind direction description
         */
        @JsonProperty("wind_direction")
        private String windDirection;

        /**
         * Wind power description
         */
        @JsonProperty("wind_power")
        private String windPower;

        /**
         * Humidity, unit: percentage
         */
        @JsonProperty("humidity")
        private Integer humidity;

        // Getters and Setters

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public Integer getTemperature() {
            return temperature;
        }

        public void setTemperature(Integer temperature) {
            this.temperature = temperature;
        }

        public String getWindDirection() {
            return windDirection;
        }

        public void setWindDirection(String windDirection) {
            this.windDirection = windDirection;
        }

        public String getWindPower() {
            return windPower;
        }

        public void setWindPower(String windPower) {
            this.windPower = windPower;
        }

        public Integer getHumidity() {
            return humidity;
        }

        public void setHumidity(Integer humidity) {
            this.humidity = humidity;
        }
    }
}
