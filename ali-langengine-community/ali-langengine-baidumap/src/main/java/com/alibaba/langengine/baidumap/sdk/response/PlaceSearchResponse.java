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

package com.alibaba.langengine.baidumap.sdk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceSearchResponse {

    /**
     * API access status, returns 0 if successful, other numbers if failed
     */
    @JsonProperty("status")
    private Integer status;

    /**
     * English description of API access status value, returns "ok" and result fields if successful,
     * returns error description if failed
     */
    @JsonProperty("message")
    private String message;

    /**
     * Number of recalled POIs, total field appears only when page_num field is set in developer's request.
     * For data protection purposes, maximum total is 150 per request
     */
    @JsonProperty("total")
    private Integer total;

    /**
     * Recall result type: region_type (administrative division type), address_type (structured address type),
     * poi_type (POI type), city_type (city type)
     */
    @JsonProperty("result_type")
    private String resultType;

    /**
     * Search type: precise (precise search) / general (general search)
     */
    @JsonProperty("query_type")
    private String queryType;

    /**
     * Returned results
     */
    @JsonProperty("results")
    private List<PlaceResult> results;

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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public List<PlaceResult> getResults() {
        return results;
    }

    public void setResults(List<PlaceResult> results) {
        this.results = results;
    }

    public static class PlaceResult {
        /**
         * POI unique identifier, ID
         */
        @JsonProperty("uid")
        private String uid;

        /**
         * POI name, maximum 10 results returned per request
         */
        @JsonProperty("name")
        private String name;

        /**
         * POI latitude and longitude coordinates
         */
        @JsonProperty("location")
        private Location location;

        /**
         * POI province
         */
        @JsonProperty("province")
        private String province;

        /**
         * POI city
         */
        @JsonProperty("city")
        private String city;

        /**
         * POI district/county
         */
        @JsonProperty("area")
        private String area;

        /**
         * POI township/street
         */
        @JsonProperty("town")
        private String town;

        /**
         * POI township/street code
         */
        @JsonProperty("town_code")
        private Integer townCode;

        /**
         * POI area code
         */
        @JsonProperty("adcode")
        private Integer adcode;

        /**
         * POI address
         */
        @JsonProperty("address")
        private String address;

        /**
         * POI business status:
         * Empty (represents normal operation)
         * Estimated location (operation status may have changed)
         * Temporarily closed
         * Possibly closed
         * Closed
         * Note: Commercial license required to access detailed status
         */
        @JsonProperty("status")
        private String status;

        /**
         * POI telephone
         */
        @JsonProperty("telephone")
        private String telephone;

        /**
         * POI street view ID
         */
        @JsonProperty("street_id")
        private String streetId;

        /**
         * Whether has detail page: 1 (has), 0 (does not have)
         */
        @JsonProperty("detail")
        private String detail;

        /**
         * Detailed information
         */
        @JsonProperty("detail_info")
        private DetailInfo detailInfo;

        /**
         * POI child points
         */
        @JsonProperty("children")
        private List<ChildPoi> children;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
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

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public Integer getTownCode() {
            return townCode;
        }

        public void setTownCode(Integer townCode) {
            this.townCode = townCode;
        }

        public Integer getAdcode() {
            return adcode;
        }

        public void setAdcode(Integer adcode) {
            this.adcode = adcode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getStreetId() {
            return streetId;
        }

        public void setStreetId(String streetId) {
            this.streetId = streetId;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public DetailInfo getDetailInfo() {
            return detailInfo;
        }

        public void setDetailInfo(DetailInfo detailInfo) {
            this.detailInfo = detailInfo;
        }

        public List<ChildPoi> getChildren() {
            return children;
        }

        public void setChildren(List<ChildPoi> children) {
            this.children = children;
        }

        public static class Location {
            /**
             * Latitude value
             */
            @JsonProperty("lat")
            private Float lat;

            /**
             * Longitude value
             */
            @JsonProperty("lng")
            private Float lng;

            public Float getLat() {
                return lat;
            }

            public void setLat(Float lat) {
                this.lat = lat;
            }

            public Float getLng() {
                return lng;
            }

            public void setLng(Float lng) {
                this.lng = lng;
            }
        }

        public static class DetailInfo {
            /**
             * POI display classification (detailed classification)
             */
            @JsonProperty("classified_poi_tag")
            private String classifiedPoiTag;

            /**
             * POI alias
             */
            @JsonProperty("new_alias")
            private String newAlias;

            /**
             * Type (e.g., hotel, cater, life), used with filter sorting
             */
            @JsonProperty("type")
            private String type;

            /**
             * POI detail page URL
             */
            @JsonProperty("detail_url")
            private String detailUrl;

            /**
             * POI business hours, e.g., 10:30-14:00,16:30-22:00
             */
            @JsonProperty("shop_hours")
            private String shopHours;

            /**
             * POI merchant price
             */
            @JsonProperty("price")
            private String price;

            /**
             * POI authoritative labels, detailed label explanations, e.g., parking lot labels (aboveground/underground),
             * famous scenic spot labels (A-level ratings), hotel labels (hotel types), etc.
             */
            @JsonProperty("label")
            private String label;

            /**
             * POI comprehensive rating
             */
            @JsonProperty("overall_rating")
            private String overallRating;

            /**
             * POI image count
             */
            @JsonProperty("image_num")
            private String imageNum;

            /**
             * POI comment count
             */
            @JsonProperty("comment_num")
            private String commentNum;

            /**
             * POI navigation guide point coordinates (pickup point). Navigation guide points for large area POIs,
             * usually various entrances/exits, convenient for use with navigation and route planning services
             */
            @JsonProperty("navi_location")
            private Location naviLocation;

            /**
             * POI brand
             */
            @JsonProperty("brand")
            private String brand;

            /**
             * Indoor POI floor level
             */
            @JsonProperty("indoor_floor")
            private String indoorFloor;

            /**
             * POI related ranking
             */
            @JsonProperty("ranking")
            private String ranking;

            /**
             * POI parent point ID
             */
            @JsonProperty("parent_id")
            private String parentId;

            /**
             * POI image download links
             * Note: Commercial license required to access
             */
            @JsonProperty("photos")
            private List<String> photos;

            /**
             * Best visiting time (refer to place detail search for this field)
             */
            @JsonProperty("best_time")
            private String bestTime;

            /**
             * Suggested duration (refer to place detail search for this field)
             */
            @JsonProperty("sug_time")
            private String sugTime;

            /**
             * Description (refer to place detail search for this field)
             */
            @JsonProperty("description")
            private String description;

            public String getClassifiedPoiTag() {
                return classifiedPoiTag;
            }

            public void setClassifiedPoiTag(String classifiedPoiTag) {
                this.classifiedPoiTag = classifiedPoiTag;
            }

            public String getNewAlias() {
                return newAlias;
            }

            public void setNewAlias(String newAlias) {
                this.newAlias = newAlias;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDetailUrl() {
                return detailUrl;
            }

            public void setDetailUrl(String detailUrl) {
                this.detailUrl = detailUrl;
            }

            public String getShopHours() {
                return shopHours;
            }

            public void setShopHours(String shopHours) {
                this.shopHours = shopHours;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getOverallRating() {
                return overallRating;
            }

            public void setOverallRating(String overallRating) {
                this.overallRating = overallRating;
            }

            public String getImageNum() {
                return imageNum;
            }

            public void setImageNum(String imageNum) {
                this.imageNum = imageNum;
            }

            public String getCommentNum() {
                return commentNum;
            }

            public void setCommentNum(String commentNum) {
                this.commentNum = commentNum;
            }

            public Location getNaviLocation() {
                return naviLocation;
            }

            public void setNaviLocation(Location naviLocation) {
                this.naviLocation = naviLocation;
            }

            public String getBrand() {
                return brand;
            }

            public void setBrand(String brand) {
                this.brand = brand;
            }

            public String getIndoorFloor() {
                return indoorFloor;
            }

            public void setIndoorFloor(String indoorFloor) {
                this.indoorFloor = indoorFloor;
            }

            public String getRanking() {
                return ranking;
            }

            public void setRanking(String ranking) {
                this.ranking = ranking;
            }

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            public List<String> getPhotos() {
                return photos;
            }

            public void setPhotos(List<String> photos) {
                this.photos = photos;
            }

            public String getBestTime() {
                return bestTime;
            }

            public void setBestTime(String bestTime) {
                this.bestTime = bestTime;
            }

            public String getSugTime() {
                return sugTime;
            }

            public void setSugTime(String sugTime) {
                this.sugTime = sugTime;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }

        public static class ChildPoi {
            /**
             * POI child point ID, can be used for POI detail search
             */
            @JsonProperty("uid")
            private String uid;

            /**
             * POI child point short name
             */
            @JsonProperty("show_name")
            private String showName;

            /**
             * POI child point name
             */
            @JsonProperty("name")
            private String name;

            /**
             * POI child point detailed classification label
             */
            @JsonProperty("classified_poi_tag")
            private String classifiedPoiTag;

            /**
             * POI child point coordinates
             */
            @JsonProperty("location")
            private Location location;

            /**
             * POI child point address
             */
            @JsonProperty("address")
            private String address;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getShowName() {
                return showName;
            }

            public void setShowName(String showName) {
                this.showName = showName;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getClassifiedPoiTag() {
                return classifiedPoiTag;
            }

            public void setClassifiedPoiTag(String classifiedPoiTag) {
                this.classifiedPoiTag = classifiedPoiTag;
            }

            public Location getLocation() {
                return location;
            }

            public void setLocation(Location location) {
                this.location = location;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }
        }
    }
}
