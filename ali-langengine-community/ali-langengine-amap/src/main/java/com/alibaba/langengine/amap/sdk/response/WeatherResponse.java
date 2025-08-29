package com.alibaba.langengine.amap.sdk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Gaode Map Weather API Response POJO Class
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    /**
     * Return status
     * 1: success; 0: failure
     */
    @JsonProperty("status")
    private String status;

    /**
     * Total number of returned results
     */
    @JsonProperty("count")
    private String count;

    /**
     * Returned status information
     */
    @JsonProperty("info")
    private String info;

    /**
     * Return status description
     * 10000 represents correct
     */
    @JsonProperty("infocode")
    private String infocode;

    /**
     * Current weather data information
     */
    @JsonProperty("lives")
    private List<Live> lives;

    /**
     * Forecast weather information data
     */
    @JsonProperty("forecasts")
    private List<Forecast> forecasts;

    // Getters and Setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public List<Live> getLives() {
        return lives;
    }

    public void setLives(List<Live> lives) {
        this.lives = lives;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    /**
     * Current weather data information
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Live {

        /**
         * Province name
         */
        @JsonProperty("province")
        private String province;

        /**
         * City name
         */
        @JsonProperty("city")
        private String city;

        /**
         * Area code
         */
        @JsonProperty("adcode")
        private String adcode;

        /**
         * Weather phenomenon (Chinese description)
         */
        @JsonProperty("weather")
        private String weather;

        /**
         * Real-time temperature, unit: Celsius
         */
        @JsonProperty("temperature")
        private String temperature;

        /**
         * Wind direction description
         */
        @JsonProperty("winddirection")
        private String winddirection;

        /**
         * Wind force level, unit: level
         */
        @JsonProperty("windpower")
        private String windpower;

        /**
         * Air humidity
         */
        @JsonProperty("humidity")
        private String humidity;

        /**
         * Data release time
         */
        @JsonProperty("reporttime")
        private String reporttime;

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

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getWinddirection() {
            return winddirection;
        }

        public void setWinddirection(String winddirection) {
            this.winddirection = winddirection;
        }

        public String getWindpower() {
            return windpower;
        }

        public void setWindpower(String windpower) {
            this.windpower = windpower;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getReporttime() {
            return reporttime;
        }

        public void setReporttime(String reporttime) {
            this.reporttime = reporttime;
        }
    }

    /**
     * Forecast weather information data
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Forecast {

        /**
         * City name
         */
        @JsonProperty("city")
        private String city;

        /**
         * City code
         */
        @JsonProperty("adcode")
        private String adcode;

        /**
         * Province name
         */
        @JsonProperty("province")
        private String province;

        /**
         * Forecast release time
         */
        @JsonProperty("reporttime")
        private String reporttime;

        /**
         * Forecast data list structure
         */
        @JsonProperty("casts")
        private List<Cast> casts;

        // Getters and Setters

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getReporttime() {
            return reporttime;
        }

        public void setReporttime(String reporttime) {
            this.reporttime = reporttime;
        }

        public List<Cast> getCasts() {
            return casts;
        }

        public void setCasts(List<Cast> casts) {
            this.casts = casts;
        }
    }

    /**
     * Forecast data
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cast {

        /**
         * Date
         */
        @JsonProperty("date")
        private String date;

        /**
         * Day of the week
         */
        @JsonProperty("week")
        private String week;

        /**
         * Day weather phenomenon
         */
        @JsonProperty("dayweather")
        private String dayweather;

        /**
         * Night weather phenomenon
         */
        @JsonProperty("nightweather")
        private String nightweather;

        /**
         * Day temperature
         */
        @JsonProperty("daytemp")
        private String daytemp;

        /**
         * Night temperature
         */
        @JsonProperty("nighttemp")
        private String nighttemp;

        /**
         * Day wind direction
         */
        @JsonProperty("daywind")
        private String daywind;

        /**
         * Night wind direction
         */
        @JsonProperty("nightwind")
        private String nightwind;

        /**
         * Day wind force
         */
        @JsonProperty("daypower")
        private String daypower;

        /**
         * Night wind force
         */
        @JsonProperty("nightpower")
        private String nightpower;

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

        public String getDayweather() {
            return dayweather;
        }

        public void setDayweather(String dayweather) {
            this.dayweather = dayweather;
        }

        public String getNightweather() {
            return nightweather;
        }

        public void setNightweather(String nightweather) {
            this.nightweather = nightweather;
        }

        public String getDaytemp() {
            return daytemp;
        }

        public void setDaytemp(String daytemp) {
            this.daytemp = daytemp;
        }

        public String getNighttemp() {
            return nighttemp;
        }

        public void setNighttemp(String nighttemp) {
            this.nighttemp = nighttemp;
        }

        public String getDaywind() {
            return daywind;
        }

        public void setDaywind(String daywind) {
            this.daywind = daywind;
        }

        public String getNightwind() {
            return nightwind;
        }

        public void setNightwind(String nightwind) {
            this.nightwind = nightwind;
        }

        public String getDaypower() {
            return daypower;
        }

        public void setDaypower(String daypower) {
            this.daypower = daypower;
        }

        public String getNightpower() {
            return nightpower;
        }

        public void setNightpower(String nightpower) {
            this.nightpower = nightpower;
        }
    }
}
