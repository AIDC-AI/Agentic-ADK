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

package com.alibaba.langengine.amap.sdk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

// If some string fields are empty, the API will return an empty list,
// so some fields require setter injection during deserialization
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceSearchResponse {

    /**
     * Result status: 0 for failure, 1 for success
     */
    @JsonProperty("status")
    private String status;

    /**
     * Status description
     */
    @JsonProperty("info")
    private String info;

    /**
     * Number of search results
     */
    @JsonProperty("count")
    private String count;

    /**
     * City suggestion list
     */
    @JsonProperty("suggestion")
    private Suggestion suggestion;

    /**
     * POI information list
     */
    @JsonProperty("pois")
    private List<Poi> pois;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Suggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion;
    }

    public List<Poi> getPois() {
        return pois;
    }

    public void setPois(List<Poi> pois) {
        this.pois = pois;
    }

    /**
     * Suggestion information
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Suggestion {

        /**
         * Keywords
         */
        @JsonProperty("keywords")
        private List<String> keywords;

        /**
         * City list
         */
        @JsonProperty("cities")
        private List<CitySuggestion> cities;

        public List<String> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<String> keywords) {
            this.keywords = keywords;
        }

        public List<CitySuggestion> getCities() {
            return cities;
        }

        public void setCities(List<CitySuggestion> cities) {
            this.cities = cities;
        }
    }

    /**
     * City suggestion
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CitySuggestion {

        /**
         * City name
         */
        @JsonProperty("name")
        private String name;

        /**
         * Number of results in this city
         */
        @JsonProperty("num")
        private String num;

        /**
         * City code
         */
        @JsonProperty("citycode")
        private String cityCode;

        /**
         * Administrative code
         */
        @JsonProperty("adcode")
        private String adCode;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getAdCode() {
            return adCode;
        }

        public void setAdCode(String adCode) {
            this.adCode = adCode;
        }
    }

    /**
     * POI information
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Poi {

        /**
         * Unique ID
         */
        @JsonProperty("id")
        private String id;

        /**
         * Parent POI ID
         */
        private String parent;

        /**
         * Name
         */
        @JsonProperty("name")
        private String name;

        /**
         * POI type
         */
        @JsonProperty("type")
        private String type;

        /**
         * POI type code
         */
        @JsonProperty("typecode")
        private String typeCode;

        /**
         * Business type
         */
        private String bizType;

        /**
         * Address
         */
        @JsonProperty("address")
        private String address;

        /**
         * Location coordinates
         */
        @JsonProperty("location")
        private String location;

        /**
         * Distance from center point
         */
        @JsonProperty("distance")
        private List<String> distance;

        /**
         * Telephone number
         */
        @JsonProperty("tel")
        private String tel;

        /**
         * Postal code
         */
        @JsonProperty("postcode")
        private String postcode;

        /**
         * Website URL
         */
        @JsonProperty("website")
        private String website;

        /**
         * Email address
         */
        @JsonProperty("email")
        private String email;

        /**
         * Province code
         */
        @JsonProperty("pcode")
        private String pcode;

        /**
         * Province name
         */
        @JsonProperty("pname")
        private String pname;

        /**
         * City code
         */
        @JsonProperty("citycode")
        private String cityCode;

        /**
         * City name
         */
        @JsonProperty("cityname")
        private String cityName;

        /**
         * Administrative code
         */
        @JsonProperty("adcode")
        private String adCode;

        /**
         * Administrative name
         */
        @JsonProperty("adname")
        private String adName;

        /**
         * Entrance location coordinates
         */
        @JsonProperty("entr_location")
        private String entranceLocation;

        /**
         * Exit location coordinates
         */
        @JsonProperty("exit_location")
        private String exitLocation;

        /**
         * Navigation POI ID
         */
        @JsonProperty("navi_poiid")
        private String naviPoiId;

        /**
         * Grid code
         */
        @JsonProperty("gridcode")
        private String gridCode;

        /**
         * Alias
         */
        @JsonProperty("alias")
        private String alias;

        /**
         * Parking type
         */
        @JsonProperty("parking_type")
        private String parkingType;

        /**
         * Special tags/features
         */
        @JsonProperty("tag")
        private String tag;

        /**
         * Indoor map flag
         */
        @JsonProperty("indoor_map")
        private String indoorMap;

        /**
         * Indoor data
         */
        @JsonProperty("indoor_data")
        private IndoorData indoorData;

        /**
         * Group buy count
         */
        @JsonProperty("groupbuy_num")
        private String groupBuyNum;

        /**
         * Business area
         */
        @JsonProperty("business_area")
        private String businessArea;

        /**
         * Discount count
         */
        @JsonProperty("discount_num")
        private String discountNum;

        /**
         * Business extension information
         */
        @JsonProperty("biz_ext")
        private BizExtension bizExt;

        /**
         * Photos information
         */
        @JsonProperty("photos")
        private List<Photo> photos;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getParent() {
            return parent;
        }

        @JsonProperty("parent")
        public void setParent(Object parent) {
            this.parent = parent.toString();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }

        public String getBizType() {
            return bizType;
        }

        @JsonProperty("biz_type")
        public void setBizType(Object bizType) {
            this.bizType = bizType.toString();
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public List<String> getDistance() {
            return distance;
        }

        public void setDistance(List<String> distance) {
            this.distance = distance;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(Object tel) {
            this.tel = tel.toString();
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPcode() {
            return pcode;
        }

        public void setPcode(String pcode) {
            this.pcode = pcode;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getAdCode() {
            return adCode;
        }

        public void setAdCode(String adCode) {
            this.adCode = adCode;
        }

        public String getAdName() {
            return adName;
        }

        public void setAdName(String adName) {
            this.adName = adName;
        }

        public String getEntranceLocation() {
            return entranceLocation;
        }

        public void setEntranceLocation(String entranceLocation) {
            this.entranceLocation = entranceLocation;
        }

        public String getExitLocation() {
            return exitLocation;
        }

        public void setExitLocation(String exitLocation) {
            this.exitLocation = exitLocation;
        }

        public String getNaviPoiId() {
            return naviPoiId;
        }

        public void setNaviPoiId(String naviPoiId) {
            this.naviPoiId = naviPoiId;
        }

        public String getGridCode() {
            return gridCode;
        }

        public void setGridCode(String gridCode) {
            this.gridCode = gridCode;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getParkingType() {
            return parkingType;
        }

        public void setParkingType(String parkingType) {
            this.parkingType = parkingType;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getIndoorMap() {
            return indoorMap;
        }

        public void setIndoorMap(String indoorMap) {
            this.indoorMap = indoorMap;
        }

        public IndoorData getIndoorData() {
            return indoorData;
        }

        public void setIndoorData(IndoorData indoorData) {
            this.indoorData = indoorData;
        }

        public String getGroupBuyNum() {
            return groupBuyNum;
        }

        public void setGroupBuyNum(String groupBuyNum) {
            this.groupBuyNum = groupBuyNum;
        }

        public String getBusinessArea() {
            return businessArea;
        }

        public void setBusinessArea(String businessArea) {
            this.businessArea = businessArea;
        }

        public String getDiscountNum() {
            return discountNum;
        }

        public void setDiscountNum(String discountNum) {
            this.discountNum = discountNum;
        }

        public BizExtension getBizExt() {
            return bizExt;
        }

        public void setBizExt(BizExtension bizExt) {
            this.bizExt = bizExt;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photo> photos) {
            this.photos = photos;
        }
    }

    /**
     * Indoor data information
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IndoorData {

        /**
         * Parent POI ID
         */
        @JsonProperty("cpid")
        private String cpId;

        /**
         * Floor index
         */
        @JsonProperty("floor")
        private String floor;

        /**
         * Actual floor name
         */
        @JsonProperty("truefloor")
        private String trueFloor;

        public String getCpId() {
            return cpId;
        }

        public void setCpId(String cpId) {
            this.cpId = cpId;
        }

        public String getFloor() {
            return floor;
        }

        public void setFloor(String floor) {
            this.floor = floor;
        }

        public String getTrueFloor() {
            return trueFloor;
        }

        public void setTrueFloor(String trueFloor) {
            this.trueFloor = trueFloor;
        }
    }

    /**
     * Business extension information
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BizExtension {

        /**
         * Rating score
         */
        private String rating;

        /**
         * Average cost
         */
        private String cost;

        /**
         * Meal ordering availability
         */
        @JsonProperty("meal_ordering")
        private String mealOrdering;

        /**
         * Seat ordering availability
         */
        @JsonProperty("seat_ordering")
        private String seatOrdering;

        /**
         * Ticket ordering availability
         */
        @JsonProperty("ticket_ordering")
        private String ticketOrdering;

        /**
         * Hotel ordering availability
         */
        @JsonProperty("hotel_ordering")
        private String hotelOrdering;

        public String getRating() {
            return rating;
        }

        @JsonProperty("rating")
        public void setRating(Object rating) {
            this.rating = rating.toString();
        }

        public String getCost() {
            return cost;
        }

        @JsonProperty("cost")
        public void setCost(Object cost) {
            this.cost = cost.toString();
        }

        public String getMealOrdering() {
            return mealOrdering;
        }

        public void setMealOrdering(String mealOrdering) {
            this.mealOrdering = mealOrdering;
        }

        public String getSeatOrdering() {
            return seatOrdering;
        }

        public void setSeatOrdering(String seatOrdering) {
            this.seatOrdering = seatOrdering;
        }

        public String getTicketOrdering() {
            return ticketOrdering;
        }

        public void setTicketOrdering(String ticketOrdering) {
            this.ticketOrdering = ticketOrdering;
        }

        public String getHotelOrdering() {
            return hotelOrdering;
        }

        public void setHotelOrdering(String hotelOrdering) {
            this.hotelOrdering = hotelOrdering;
        }
    }

    /**
     * Photo information
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Photo {

        /**
         * Photo title/description
         */
        private String title;

        /**
         * Photo URL
         */
        @JsonProperty("url")
        private String url;

        public String getTitle() {
            return title;
        }

        @JsonProperty("title")
        public void setTitle(Object title) {
            this.title = title.toString();
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
