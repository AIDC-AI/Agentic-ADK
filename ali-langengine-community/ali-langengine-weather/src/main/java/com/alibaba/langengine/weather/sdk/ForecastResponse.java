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
package com.alibaba.langengine.weather.sdk;

import java.util.List;

/**
 * Forecast Response
 * Represents the response from the Weather forecast API
 */
public class ForecastResponse {
    
    private Location location;
    private Current current;
    private Forecast forecast;
    
    // Getters and Setters
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public Current getCurrent() {
        return current;
    }
    
    public void setCurrent(Current current) {
        this.current = current;
    }
    
    public Forecast getForecast() {
        return forecast;
    }
    
    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }
    
    public static class Location {
        private String name;
        private String region;
        private String country;
        private double lat;
        private double lon;
        private String tz_id;
        private long localtime_epoch;
        private String localtime;
        
        // Getters and Setters
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getRegion() {
            return region;
        }
        
        public void setRegion(String region) {
            this.region = region;
        }
        
        public String getCountry() {
            return country;
        }
        
        public void setCountry(String country) {
            this.country = country;
        }
        
        public double getLat() {
            return lat;
        }
        
        public void setLat(double lat) {
            this.lat = lat;
        }
        
        public double getLon() {
            return lon;
        }
        
        public void setLon(double lon) {
            this.lon = lon;
        }
        
        public String getTz_id() {
            return tz_id;
        }
        
        public void setTz_id(String tz_id) {
            this.tz_id = tz_id;
        }
        
        public long getLocaltime_epoch() {
            return localtime_epoch;
        }
        
        public void setLocaltime_epoch(long localtime_epoch) {
            this.localtime_epoch = localtime_epoch;
        }
        
        public String getLocaltime() {
            return localtime;
        }
        
        public void setLocaltime(String localtime) {
            this.localtime = localtime;
        }
    }
    
    public static class Current {
        private long last_updated_epoch;
        private String last_updated;
        private double temp_c;
        private double temp_f;
        private int is_day;
        private Condition condition;
        private double wind_mph;
        private double wind_kph;
        private int wind_degree;
        private String wind_dir;
        private double pressure_mb;
        private double pressure_in;
        private double precip_mm;
        private double precip_in;
        private int humidity;
        private int cloud;
        private double feelslike_c;
        private double feelslike_f;
        private double vis_km;
        private double vis_miles;
        private double uv;
        private double gust_mph;
        private double gust_kph;
        
        // Getters and Setters
        
        public long getLast_updated_epoch() {
            return last_updated_epoch;
        }
        
        public void setLast_updated_epoch(long last_updated_epoch) {
            this.last_updated_epoch = last_updated_epoch;
        }
        
        public String getLast_updated() {
            return last_updated;
        }
        
        public void setLast_updated(String last_updated) {
            this.last_updated = last_updated;
        }
        
        public double getTemp_c() {
            return temp_c;
        }
        
        public void setTemp_c(double temp_c) {
            this.temp_c = temp_c;
        }
        
        public double getTemp_f() {
            return temp_f;
        }
        
        public void setTemp_f(double temp_f) {
            this.temp_f = temp_f;
        }
        
        public int getIs_day() {
            return is_day;
        }
        
        public void setIs_day(int is_day) {
            this.is_day = is_day;
        }
        
        public Condition getCondition() {
            return condition;
        }
        
        public void setCondition(Condition condition) {
            this.condition = condition;
        }
        
        public double getWind_mph() {
            return wind_mph;
        }
        
        public void setWind_mph(double wind_mph) {
            this.wind_mph = wind_mph;
        }
        
        public double getWind_kph() {
            return wind_kph;
        }
        
        public void setWind_kph(double wind_kph) {
            this.wind_kph = wind_kph;
        }
        
        public int getWind_degree() {
            return wind_degree;
        }
        
        public void setWind_degree(int wind_degree) {
            this.wind_degree = wind_degree;
        }
        
        public String getWind_dir() {
            return wind_dir;
        }
        
        public void setWind_dir(String wind_dir) {
            this.wind_dir = wind_dir;
        }
        
        public double getPressure_mb() {
            return pressure_mb;
        }
        
        public void setPressure_mb(double pressure_mb) {
            this.pressure_mb = pressure_mb;
        }
        
        public double getPressure_in() {
            return pressure_in;
        }
        
        public void setPressure_in(double pressure_in) {
            this.pressure_in = pressure_in;
        }
        
        public double getPrecip_mm() {
            return precip_mm;
        }
        
        public void setPrecip_mm(double precip_mm) {
            this.precip_mm = precip_mm;
        }
        
        public double getPrecip_in() {
            return precip_in;
        }
        
        public void setPrecip_in(double precip_in) {
            this.precip_in = precip_in;
        }
        
        public int getHumidity() {
            return humidity;
        }
        
        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }
        
        public int getCloud() {
            return cloud;
        }
        
        public void setCloud(int cloud) {
            this.cloud = cloud;
        }
        
        public double getFeelslike_c() {
            return feelslike_c;
        }
        
        public void setFeelslike_c(double feelslike_c) {
            this.feelslike_c = feelslike_c;
        }
        
        public double getFeelslike_f() {
            return feelslike_f;
        }
        
        public void setFeelslike_f(double feelslike_f) {
            this.feelslike_f = feelslike_f;
        }
        
        public double getVis_km() {
            return vis_km;
        }
        
        public void setVis_km(double vis_km) {
            this.vis_km = vis_km;
        }
        
        public double getVis_miles() {
            return vis_miles;
        }
        
        public void setVis_miles(double vis_miles) {
            this.vis_miles = vis_miles;
        }
        
        public double getUv() {
            return uv;
        }
        
        public void setUv(double uv) {
            this.uv = uv;
        }
        
        public double getGust_mph() {
            return gust_mph;
        }
        
        public void setGust_mph(double gust_mph) {
            this.gust_mph = gust_mph;
        }
        
        public double getGust_kph() {
            return gust_kph;
        }
        
        public void setGust_kph(double gust_kph) {
            this.gust_kph = gust_kph;
        }
    }
    
    public static class Condition {
        private String text;
        private String icon;
        private int code;
        
        // Getters and Setters
        
        public String getText() {
            return text;
        }
        
        public void setText(String text) {
            this.text = text;
        }
        
        public String getIcon() {
            return icon;
        }
        
        public void setIcon(String icon) {
            this.icon = icon;
        }
        
        public int getCode() {
            return code;
        }
        
        public void setCode(int code) {
            this.code = code;
        }
    }
    
    public static class Forecast {
        private List<Forecastday> forecastday;
        
        // Getters and Setters
        
        public List<Forecastday> getForecastday() {
            return forecastday;
        }
        
        public void setForecastday(List<Forecastday> forecastday) {
            this.forecastday = forecastday;
        }
    }
    
    public static class Forecastday {
        private String date;
        private long date_epoch;
        private Day day;
        private Astro astro;
        private List<Hour> hour;
        
        // Getters and Setters
        
        public String getDate() {
            return date;
        }
        
        public void setDate(String date) {
            this.date = date;
        }
        
        public long getDate_epoch() {
            return date_epoch;
        }
        
        public void setDate_epoch(long date_epoch) {
            this.date_epoch = date_epoch;
        }
        
        public Day getDay() {
            return day;
        }
        
        public void setDay(Day day) {
            this.day = day;
        }
        
        public Astro getAstro() {
            return astro;
        }
        
        public void setAstro(Astro astro) {
            this.astro = astro;
        }
        
        public List<Hour> getHour() {
            return hour;
        }
        
        public void setHour(List<Hour> hour) {
            this.hour = hour;
        }
    }
    
    public static class Day {
        private double maxtemp_c;
        private double maxtemp_f;
        private double mintemp_c;
        private double mintemp_f;
        private double avgtemp_c;
        private double avgtemp_f;
        private double maxwind_mph;
        private double maxwind_kph;
        private double totalprecip_mm;
        private double totalprecip_in;
        private double totalsnow_cm;
        private double avgvis_km;
        private double avgvis_miles;
        private int avghumidity;
        private int daily_will_it_rain;
        private int daily_chance_of_rain;
        private int daily_will_it_snow;
        private int daily_chance_of_snow;
        private Condition condition;
        private double uv;
        
        // Getters and Setters
        
        public double getMaxtemp_c() {
            return maxtemp_c;
        }
        
        public void setMaxtemp_c(double maxtemp_c) {
            this.maxtemp_c = maxtemp_c;
        }
        
        public double getMaxtemp_f() {
            return maxtemp_f;
        }
        
        public void setMaxtemp_f(double maxtemp_f) {
            this.maxtemp_f = maxtemp_f;
        }
        
        public double getMintemp_c() {
            return mintemp_c;
        }
        
        public void setMintemp_c(double mintemp_c) {
            this.mintemp_c = mintemp_c;
        }
        
        public double getMintemp_f() {
            return mintemp_f;
        }
        
        public void setMintemp_f(double mintemp_f) {
            this.mintemp_f = mintemp_f;
        }
        
        public double getAvgtemp_c() {
            return avgtemp_c;
        }
        
        public void setAvgtemp_c(double avgtemp_c) {
            this.avgtemp_c = avgtemp_c;
        }
        
        public double getAvgtemp_f() {
            return avgtemp_f;
        }
        
        public void setAvgtemp_f(double avgtemp_f) {
            this.avgtemp_f = avgtemp_f;
        }
        
        public double getMaxwind_mph() {
            return maxwind_mph;
        }
        
        public void setMaxwind_mph(double maxwind_mph) {
            this.maxwind_mph = maxwind_mph;
        }
        
        public double getMaxwind_kph() {
            return maxwind_kph;
        }
        
        public void setMaxwind_kph(double maxwind_kph) {
            this.maxwind_kph = maxwind_kph;
        }
        
        public double getTotalprecip_mm() {
            return totalprecip_mm;
        }
        
        public void setTotalprecip_mm(double totalprecip_mm) {
            this.totalprecip_mm = totalprecip_mm;
        }
        
        public double getTotalprecip_in() {
            return totalprecip_in;
        }
        
        public void setTotalprecip_in(double totalprecip_in) {
            this.totalprecip_in = totalprecip_in;
        }
        
        public double getTotalsnow_cm() {
            return totalsnow_cm;
        }
        
        public void setTotalsnow_cm(double totalsnow_cm) {
            this.totalsnow_cm = totalsnow_cm;
        }
        
        public double getAvgvis_km() {
            return avgvis_km;
        }
        
        public void setAvgvis_km(double avgvis_km) {
            this.avgvis_km = avgvis_km;
        }
        
        public double getAvgvis_miles() {
            return avgvis_miles;
        }
        
        public void setAvgvis_miles(double avgvis_miles) {
            this.avgvis_miles = avgvis_miles;
        }
        
        public int getAvghumidity() {
            return avghumidity;
        }
        
        public void setAvghumidity(int avghumidity) {
            this.avghumidity = avghumidity;
        }
        
        public int getDaily_will_it_rain() {
            return daily_will_it_rain;
        }
        
        public void setDaily_will_it_rain(int daily_will_it_rain) {
            this.daily_will_it_rain = daily_will_it_rain;
        }
        
        public int getDaily_chance_of_rain() {
            return daily_chance_of_rain;
        }
        
        public void setDaily_chance_of_rain(int daily_chance_of_rain) {
            this.daily_chance_of_rain = daily_chance_of_rain;
        }
        
        public int getDaily_will_it_snow() {
            return daily_will_it_snow;
        }
        
        public void setDaily_will_it_snow(int daily_will_it_snow) {
            this.daily_will_it_snow = daily_will_it_snow;
        }
        
        public int getDaily_chance_of_snow() {
            return daily_chance_of_snow;
        }
        
        public void setDaily_chance_of_snow(int daily_chance_of_snow) {
            this.daily_chance_of_snow = daily_chance_of_snow;
        }
        
        public Condition getCondition() {
            return condition;
        }
        
        public void setCondition(Condition condition) {
            this.condition = condition;
        }
        
        public double getUv() {
            return uv;
        }
        
        public void setUv(double uv) {
            this.uv = uv;
        }
    }
    
    public static class Astro {
        private String sunrise;
        private String sunset;
        private String moonrise;
        private String moonset;
        private String moon_phase;
        private int moon_illumination;
        private int is_moon_up;
        private int is_sun_up;
        
        // Getters and Setters
        
        public String getSunrise() {
            return sunrise;
        }
        
        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }
        
        public String getSunset() {
            return sunset;
        }
        
        public void setSunset(String sunset) {
            this.sunset = sunset;
        }
        
        public String getMoonrise() {
            return moonrise;
        }
        
        public void setMoonrise(String moonrise) {
            this.moonrise = moonrise;
        }
        
        public String getMoonset() {
            return moonset;
        }
        
        public void setMoonset(String moonset) {
            this.moonset = moonset;
        }
        
        public String getMoon_phase() {
            return moon_phase;
        }
        
        public void setMoon_phase(String moon_phase) {
            this.moon_phase = moon_phase;
        }
        
        public int getMoon_illumination() {
            return moon_illumination;
        }
        
        public void setMoon_illumination(int moon_illumination) {
            this.moon_illumination = moon_illumination;
        }
        
        public int getIs_moon_up() {
            return is_moon_up;
        }
        
        public void setIs_moon_up(int is_moon_up) {
            this.is_moon_up = is_moon_up;
        }
        
        public int getIs_sun_up() {
            return is_sun_up;
        }
        
        public void setIs_sun_up(int is_sun_up) {
            this.is_sun_up = is_sun_up;
        }
    }
    
    public static class Hour {
        private long time_epoch;
        private String time;
        private double temp_c;
        private double temp_f;
        private int is_day;
        private Condition condition;
        private double wind_mph;
        private double wind_kph;
        private int wind_degree;
        private String wind_dir;
        private double pressure_mb;
        private double pressure_in;
        private double precip_mm;
        private double precip_in;
        private int humidity;
        private int cloud;
        private double feelslike_c;
        private double feelslike_f;
        private double windchill_c;
        private double windchill_f;
        private double heatindex_c;
        private double heatindex_f;
        private double dewpoint_c;
        private double dewpoint_f;
        private int will_it_rain;
        private int chance_of_rain;
        private int will_it_snow;
        private int chance_of_snow;
        private double vis_km;
        private double vis_miles;
        private double gust_mph;
        private double gust_kph;
        private double uv;
        
        // Getters and Setters
        
        public long getTime_epoch() {
            return time_epoch;
        }
        
        public void setTime_epoch(long time_epoch) {
            this.time_epoch = time_epoch;
        }
        
        public String getTime() {
            return time;
        }
        
        public void setTime(String time) {
            this.time = time;
        }
        
        public double getTemp_c() {
            return temp_c;
        }
        
        public void setTemp_c(double temp_c) {
            this.temp_c = temp_c;
        }
        
        public double getTemp_f() {
            return temp_f;
        }
        
        public void setTemp_f(double temp_f) {
            this.temp_f = temp_f;
        }
        
        public int getIs_day() {
            return is_day;
        }
        
        public void setIs_day(int is_day) {
            this.is_day = is_day;
        }
        
        public Condition getCondition() {
            return condition;
        }
        
        public void setCondition(Condition condition) {
            this.condition = condition;
        }
        
        public double getWind_mph() {
            return wind_mph;
        }
        
        public void setWind_mph(double wind_mph) {
            this.wind_mph = wind_mph;
        }
        
        public double getWind_kph() {
            return wind_kph;
        }
        
        public void setWind_kph(double wind_kph) {
            this.wind_kph = wind_kph;
        }
        
        public int getWind_degree() {
            return wind_degree;
        }
        
        public void setWind_degree(int wind_degree) {
            this.wind_degree = wind_degree;
        }
        
        public String getWind_dir() {
            return wind_dir;
        }
        
        public void setWind_dir(String wind_dir) {
            this.wind_dir = wind_dir;
        }
        
        public double getPressure_mb() {
            return pressure_mb;
        }
        
        public void setPressure_mb(double pressure_mb) {
            this.pressure_mb = pressure_mb;
        }
        
        public double getPressure_in() {
            return pressure_in;
        }
        
        public void setPressure_in(double pressure_in) {
            this.pressure_in = pressure_in;
        }
        
        public double getPrecip_mm() {
            return precip_mm;
        }
        
        public void setPrecip_mm(double precip_mm) {
            this.precip_mm = precip_mm;
        }
        
        public double getPrecip_in() {
            return precip_in;
        }
        
        public void setPrecip_in(double precip_in) {
            this.precip_in = precip_in;
        }
        
        public int getHumidity() {
            return humidity;
        }
        
        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }
        
        public int getCloud() {
            return cloud;
        }
        
        public void setCloud(int cloud) {
            this.cloud = cloud;
        }
        
        public double getFeelslike_c() {
            return feelslike_c;
        }
        
        public void setFeelslike_c(double feelslike_c) {
            this.feelslike_c = feelslike_c;
        }
        
        public double getFeelslike_f() {
            return feelslike_f;
        }
        
        public void setFeelslike_f(double feelslike_f) {
            this.feelslike_f = feelslike_f;
        }
        
        public double getWindchill_c() {
            return windchill_c;
        }
        
        public void setWindchill_c(double windchill_c) {
            this.windchill_c = windchill_c;
        }
        
        public double getWindchill_f() {
            return windchill_f;
        }
        
        public void setWindchill_f(double windchill_f) {
            this.windchill_f = windchill_f;
        }
        
        public double getHeatindex_c() {
            return heatindex_c;
        }
        
        public void setHeatindex_c(double heatindex_c) {
            this.heatindex_c = heatindex_c;
        }
        
        public double getHeatindex_f() {
            return heatindex_f;
        }
        
        public void setHeatindex_f(double heatindex_f) {
            this.heatindex_f = heatindex_f;
        }
        
        public double getDewpoint_c() {
            return dewpoint_c;
        }
        
        public void setDewpoint_c(double dewpoint_c) {
            this.dewpoint_c = dewpoint_c;
        }
        
        public double getDewpoint_f() {
            return dewpoint_f;
        }
        
        public void setDewpoint_f(double dewpoint_f) {
            this.dewpoint_f = dewpoint_f;
        }
        
        public int getWill_it_rain() {
            return will_it_rain;
        }
        
        public void setWill_it_rain(int will_it_rain) {
            this.will_it_rain = will_it_rain;
        }
        
        public int getChance_of_rain() {
            return chance_of_rain;
        }
        
        public void setChance_of_rain(int chance_of_rain) {
            this.chance_of_rain = chance_of_rain;
        }
        
        public int getWill_it_snow() {
            return will_it_snow;
        }
        
        public void setWill_it_snow(int will_it_snow) {
            this.will_it_snow = will_it_snow;
        }
        
        public int getChance_of_snow() {
            return chance_of_snow;
        }
        
        public void setChance_of_snow(int chance_of_snow) {
            this.chance_of_snow = chance_of_snow;
        }
        
        public double getVis_km() {
            return vis_km;
        }
        
        public void setVis_km(double vis_km) {
            this.vis_km = vis_km;
        }
        
        public double getVis_miles() {
            return vis_miles;
        }
        
        public void setVis_miles(double vis_miles) {
            this.vis_miles = vis_miles;
        }
        
        public double getGust_mph() {
            return gust_mph;
        }
        
        public void setGust_mph(double gust_mph) {
            this.gust_mph = gust_mph;
        }
        
        public double getGust_kph() {
            return gust_kph;
        }
        
        public void setGust_kph(double gust_kph) {
            this.gust_kph = gust_kph;
        }
        
        public double getUv() {
            return uv;
        }
        
        public void setUv(double uv) {
            this.uv = uv;
        }
    }
}