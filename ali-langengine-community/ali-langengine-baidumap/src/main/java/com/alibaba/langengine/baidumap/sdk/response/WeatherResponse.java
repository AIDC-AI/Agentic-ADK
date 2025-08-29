package com.alibaba.langengine.baidumap.sdk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Weather API Response POJO Class
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    /**
     * Status code
     */
    @JsonProperty("status")
    private Integer status;

    /**
     * Result data
     */
    @JsonProperty("result")
    private Result result;

    /**
     * Message
     */
    @JsonProperty("message")
    private String message;

    // Getters and Setters

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Result data
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {

        /**
         * Location information
         */
        @JsonProperty("location")
        private Location location;

        /**
         * Current weather information
         */
        @JsonProperty("now")
        private Now now;

        /**
         * Weather indexes
         */
        @JsonProperty("indexes")
        private List<Index> indexes;

        /**
         * Weather alerts
         */
        @JsonProperty("alerts")
        private List<Object> alerts;

        /**
         * Weather forecasts
         */
        @JsonProperty("forecasts")
        private List<Forecast> forecasts;

        /**
         * Hourly weather forecasts
         */
        @JsonProperty("forecast_hours")
        private List<ForecastHour> forecastHours;

        // Getters and Setters

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public Now getNow() {
            return now;
        }

        public void setNow(Now now) {
            this.now = now;
        }

        public List<Index> getIndexes() {
            return indexes;
        }

        public void setIndexes(List<Index> indexes) {
            this.indexes = indexes;
        }

        public List<Object> getAlerts() {
            return alerts;
        }

        public void setAlerts(List<Object> alerts) {
            this.alerts = alerts;
        }

        public List<Forecast> getForecasts() {
            return forecasts;
        }

        public void setForecasts(List<Forecast> forecasts) {
            this.forecasts = forecasts;
        }

        public List<ForecastHour> getForecastHours() {
            return forecastHours;
        }

        public void setForecastHours(List<ForecastHour> forecastHours) {
            this.forecastHours = forecastHours;
        }
    }

    /**
     * Location information
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {

        /**
         * Country
         */
        @JsonProperty("country")
        private String country;

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
         * Location name
         */
        @JsonProperty("name")
        private String name;

        /**
         * Location ID
         */
        @JsonProperty("id")
        private String id;

        // Getters and Setters

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    /**
     * Current weather information
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Now {

        /**
         * Weather description
         */
        @JsonProperty("text")
        private String text;

        /**
         * Temperature
         */
        @JsonProperty("temp")
        private Integer temp;

        /**
         * Feels like temperature
         */
        @JsonProperty("feels_like")
        private Integer feelsLike;

        /**
         * Relative humidity
         */
        @JsonProperty("rh")
        private Integer rh;

        /**
         * Wind class
         */
        @JsonProperty("wind_class")
        private String windClass;

        /**
         * Wind direction
         */
        @JsonProperty("wind_dir")
        private String windDir;

        /**
         * Precipitation in last 1 hour
         */
        @JsonProperty("prec_1h")
        private Integer prec1h;

        /**
         * Cloud cover
         */
        @JsonProperty("clouds")
        private Integer clouds;

        /**
         * Visibility
         */
        @JsonProperty("vis")
        private Integer vis;

        /**
         * Air quality index
         */
        @JsonProperty("aqi")
        private Integer aqi;

        /**
         * PM2.5 concentration
         */
        @JsonProperty("pm25")
        private Integer pm25;

        /**
         * PM10 concentration
         */
        @JsonProperty("pm10")
        private Integer pm10;

        /**
         * Nitrogen dioxide concentration
         */
        @JsonProperty("no2")
        private Integer no2;

        /**
         * Sulfur dioxide concentration
         */
        @JsonProperty("so2")
        private Integer so2;

        /**
         * Ozone concentration
         */
        @JsonProperty("o3")
        private Integer o3;

        /**
         * Carbon monoxide concentration
         */
        @JsonProperty("co")
        private Double co;

        /**
         * Wind angle
         */
        @JsonProperty("wind_angle")
        private Integer windAngle;

        /**
         * UV index
         */
        @JsonProperty("uvi")
        private Integer uvi;

        /**
         * Atmospheric pressure
         */
        @JsonProperty("pressure")
        private Integer pressure;

        /**
         * Dew point temperature
         */
        @JsonProperty("dpt")
        private Integer dpt;

        /**
         * Update time
         */
        @JsonProperty("uptime")
        private String uptime;

        // Getters and Setters

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Integer getTemp() {
            return temp;
        }

        public void setTemp(Integer temp) {
            this.temp = temp;
        }

        public Integer getFeelsLike() {
            return feelsLike;
        }

        public void setFeelsLike(Integer feelsLike) {
            this.feelsLike = feelsLike;
        }

        public Integer getRh() {
            return rh;
        }

        public void setRh(Integer rh) {
            this.rh = rh;
        }

        public String getWindClass() {
            return windClass;
        }

        public void setWindClass(String windClass) {
            this.windClass = windClass;
        }

        public String getWindDir() {
            return windDir;
        }

        public void setWindDir(String windDir) {
            this.windDir = windDir;
        }

        public Integer getPrec1h() {
            return prec1h;
        }

        public void setPrec1h(Integer prec1h) {
            this.prec1h = prec1h;
        }

        public Integer getClouds() {
            return clouds;
        }

        public void setClouds(Integer clouds) {
            this.clouds = clouds;
        }

        public Integer getVis() {
            return vis;
        }

        public void setVis(Integer vis) {
            this.vis = vis;
        }

        public Integer getAqi() {
            return aqi;
        }

        public void setAqi(Integer aqi) {
            this.aqi = aqi;
        }

        public Integer getPm25() {
            return pm25;
        }

        public void setPm25(Integer pm25) {
            this.pm25 = pm25;
        }

        public Integer getPm10() {
            return pm10;
        }

        public void setPm10(Integer pm10) {
            this.pm10 = pm10;
        }

        public Integer getNo2() {
            return no2;
        }

        public void setNo2(Integer no2) {
            this.no2 = no2;
        }

        public Integer getSo2() {
            return so2;
        }

        public void setSo2(Integer so2) {
            this.so2 = so2;
        }

        public Integer getO3() {
            return o3;
        }

        public void setO3(Integer o3) {
            this.o3 = o3;
        }

        public Double getCo() {
            return co;
        }

        public void setCo(Double co) {
            this.co = co;
        }

        public Integer getWindAngle() {
            return windAngle;
        }

        public void setWindAngle(Integer windAngle) {
            this.windAngle = windAngle;
        }

        public Integer getUvi() {
            return uvi;
        }

        public void setUvi(Integer uvi) {
            this.uvi = uvi;
        }

        public Integer getPressure() {
            return pressure;
        }

        public void setPressure(Integer pressure) {
            this.pressure = pressure;
        }

        public Integer getDpt() {
            return dpt;
        }

        public void setDpt(Integer dpt) {
            this.dpt = dpt;
        }

        public String getUptime() {
            return uptime;
        }

        public void setUptime(String uptime) {
            this.uptime = uptime;
        }
    }

    /**
     * Weather index
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Index {

        /**
         * Index name
         */
        @JsonProperty("name")
        private String name;

        /**
         * Brief description
         */
        @JsonProperty("brief")
        private String brief;

        /**
         * Detailed description
         */
        @JsonProperty("detail")
        private String detail;

        // Getters and Setters

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBrief() {
            return brief;
        }

        public void setBrief(String brief) {
            this.brief = brief;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }

    /**
     * Weather forecast
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Forecast {

        /**
         * Day weather description
         */
        @JsonProperty("text_day")
        private String textDay;

        /**
         * Night weather description
         */
        @JsonProperty("text_night")
        private String textNight;

        /**
         * High temperature
         */
        @JsonProperty("high")
        private Integer high;

        /**
         * Low temperature
         */
        @JsonProperty("low")
        private Integer low;

        /**
         * Day wind class
         */
        @JsonProperty("wc_day")
        private String wcDay;

        /**
         * Day wind direction
         */
        @JsonProperty("wd_day")
        private String wdDay;

        /**
         * Night wind class
         */
        @JsonProperty("wc_night")
        private String wcNight;

        /**
         * Night wind direction
         */
        @JsonProperty("wd_night")
        private String wdNight;

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

        // Getters and Setters

        public String getTextDay() {
            return textDay;
        }

        public void setTextDay(String textDay) {
            this.textDay = textDay;
        }

        public String getTextNight() {
            return textNight;
        }

        public void setTextNight(String textNight) {
            this.textNight = textNight;
        }

        public Integer getHigh() {
            return high;
        }

        public void setHigh(Integer high) {
            this.high = high;
        }

        public Integer getLow() {
            return low;
        }

        public void setLow(Integer low) {
            this.low = low;
        }

        public String getWcDay() {
            return wcDay;
        }

        public void setWcDay(String wcDay) {
            this.wcDay = wcDay;
        }

        public String getWdDay() {
            return wdDay;
        }

        public void setWdDay(String wdDay) {
            this.wdDay = wdDay;
        }

        public String getWcNight() {
            return wcNight;
        }

        public void setWcNight(String wcNight) {
            this.wcNight = wcNight;
        }

        public String getWdNight() {
            return wdNight;
        }

        public void setWdNight(String wdNight) {
            this.wdNight = wdNight;
        }

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
    }

    /**
     * Hourly weather forecast
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForecastHour {

        /**
         * Weather description
         */
        @JsonProperty("text")
        private String text;

        /**
         * Forecast temperature
         */
        @JsonProperty("temp_fc")
        private Integer tempFc;

        /**
         * Wind class
         */
        @JsonProperty("wind_class")
        private String windClass;

        /**
         * Wind direction
         */
        @JsonProperty("wind_dir")
        private String windDir;

        /**
         * Relative humidity
         */
        @JsonProperty("rh")
        private Integer rh;

        /**
         * Precipitation in last 1 hour
         */
        @JsonProperty("prec_1h")
        private Integer prec1h;

        /**
         * Cloud cover
         */
        @JsonProperty("clouds")
        private Integer clouds;

        /**
         * Wind angle
         */
        @JsonProperty("wind_angle")
        private Integer windAngle;

        /**
         * Probability of precipitation
         */
        @JsonProperty("pop")
        private Integer pop;

        /**
         * UV index
         */
        @JsonProperty("uvi")
        private Integer uvi;

        /**
         * Atmospheric pressure
         */
        @JsonProperty("pressure")
        private Integer pressure;

        /**
         * Dew point temperature
         */
        @JsonProperty("dpt")
        private Integer dpt;

        /**
         * Data time
         */
        @JsonProperty("data_time")
        private String dataTime;

        // Getters and Setters

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Integer getTempFc() {
            return tempFc;
        }

        public void setTempFc(Integer tempFc) {
            this.tempFc = tempFc;
        }

        public String getWindClass() {
            return windClass;
        }

        public void setWindClass(String windClass) {
            this.windClass = windClass;
        }

        public String getWindDir() {
            return windDir;
        }

        public void setWindDir(String windDir) {
            this.windDir = windDir;
        }

        public Integer getRh() {
            return rh;
        }

        public void setRh(Integer rh) {
            this.rh = rh;
        }

        public Integer getPrec1h() {
            return prec1h;
        }

        public void setPrec1h(Integer prec1h) {
            this.prec1h = prec1h;
        }

        public Integer getClouds() {
            return clouds;
        }

        public void setClouds(Integer clouds) {
            this.clouds = clouds;
        }

        public Integer getWindAngle() {
            return windAngle;
        }

        public void setWindAngle(Integer windAngle) {
            this.windAngle = windAngle;
        }

        public Integer getPop() {
            return pop;
        }

        public void setPop(Integer pop) {
            this.pop = pop;
        }

        public Integer getUvi() {
            return uvi;
        }

        public void setUvi(Integer uvi) {
            this.uvi = uvi;
        }

        public Integer getPressure() {
            return pressure;
        }

        public void setPressure(Integer pressure) {
            this.pressure = pressure;
        }

        public Integer getDpt() {
            return dpt;
        }

        public void setDpt(Integer dpt) {
            this.dpt = dpt;
        }

        public String getDataTime() {
            return dataTime;
        }

        public void setDataTime(String dataTime) {
            this.dataTime = dataTime;
        }
    }
}
