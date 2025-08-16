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
package com.alibaba.langengine.sinanews.sdk;

/**
 * Hotlist Item
 * Represents a single item in the hotlist
 */
public class HotlistItem {
    private String id;
    private String title;
    private String url;
    private String desc;
    private String logo;
    private String type;
    private Integer hotnum;
    private String lbs;
    private String lbsCode;
    private String lbsType;
    private String lbsTypeName;
    private String lbsName;
    private String lbsProvince;
    private String lbsCity;
    private String lbsDistrict;
    private String lbsPoi;
    private String lbsLat;
    private String lbsLon;
    private String lbsAdcode;
    private String lbsBusinessArea;
    private String lbsDistance;
    private String lbsAddress;
    private String lbsTel;
    private String lbsCategory;
    private String lbsTag;
    private String lbsCheckinNum;
    private String lbsCommentNum;
    private String lbsScore;
    private String lbsPrice;
    private String lbsOpenTime;
    private String lbsClosedTime;
    private String lbsAlias;
    private String lbsIntroduction;
    private String lbsTips;
    private String lbsTraffic;
    private String lbsWebsite;
    private String lbsEmail;
    private String lbsWeibo;
    private String lbsWechat;
    private String lbsQq;
    private String lbsPhone;
    private String lbsFax;
    private String lbsPostcode;
    private String lbsCountry;
    private String lbsContinent;
    private String lbsTimezone;
    private String lbsCurrency;
    private String lbsLanguage;
    private String lbsEmergency;
    private String lbsMedical;
    private String lbsPolice;
    private String lbsFire;
    private String lbsCustoms;
    private String lbsImmigration;
    private String lbsVisa;
    private String lbsInsurance;
    private String lbsPower;
    private String lbsWater;
    private String lbsSewage;
    private String lbsGarbage;
    private String lbsRecycling;
    private String lbsWifi;
    private String lbsParking;
    private String lbsGas;
    private String lbsElectricity;
    private String lbsHeating;
    private String lbsCooling;
    private String lbsElevator;
    private String lbsStairs;
    private String lbsRamp;
    private String lbsToilet;
    private String lbsShower;
    private String lbsBath;
    private String lbsSauna;
    private String lbsPool;
    private String lbsGym;
    private String lbsSpa;
    private String lbsMassage;
    private String lbsRestaurant;
    private String lbsBar;
    private String lbsCafe;
    private String lbsShop;
    private String lbsMarket;
    private String lbsBank;
    private String lbsAtm;
    private String lbsHospital;
    private String lbsClinic;
    private String lbsPharmacy;
    private String lbsSchool;
    private String lbsUniversity;
    private String lbsLibrary;
    private String lbsMuseum;
    private String lbsTheater;
    private String lbsCinema;
    private String lbsPark;
    private String lbsBeach;
    private String lbsMountain;
    private String lbsLake;
    private String lbsRiver;
    private String lbsSea;
    private String lbsIsland;
    private String lbsDesert;
    private String lbsForest;
    private String lbsCave;
    private String lbsWaterfall;
    private String lbsVolcano;
    private String lbsGlacier;
    private String lbsCanyon;
    private String lbsValley;
    private String lbsPlateau;
    private String lbsPlain;
    private String lbsHill;
    private String lbsDune;
    private String lbsReef;
    private String lbsWetland;
    private String lbsReserve;
    private String lbsGarden;
    private String lbsZoo;
    private String lbsAquarium;
    private String lbsStadium;
    private String lbsArena;
    private String lbsCasino;
    private String lbsHotel;
    private String lbsHostel;
    private String lbsResort;
    private String lbsMotel;
    private String lbsCamp;
    private String lbsCottage;
    private String lbsVilla;
    private String lbsApartment;
    private String lbsHouse;
    private String lbsFlat;
    private String lbsRoom;
    private String lbsSuite;
    private String lbsStudio;
    private String lbsLoft;
    private String lbsPenthouse;
    private String lbsDuplex;
    private String lbsTriplex;
    private String lbsQuadplex;
    private String lbsTownhouse;
    private String lbsBungalow;
    private String lbsChalet;
    private String lbsCabin;
    private String lbsLodge;
    private String lbsManor;
    private String lbsMansion;
    private String lbsPalace;
    private String lbsCastle;
    private String lbsFort;
    private String lbsBarracks;
    private String lbsPrison;
    private String lbsAsylum;
    private String lbsOrphanage;
    private String lbsNursingHome;
    private String lbsRetirementHome;
    private String lbsHospice;
    private String lbsMorgue;
    private String lbsCemetery;
    private String lbsMausoleum;
    private String lbsTomb;
    private String lbsGrave;
    private String lbsHeadstone;
    private String lbsMonument;
    private String lbsStatue;
    private String lbsSculpture;
    private String lbsFountain;
    private String lbsBridge;
    private String lbsTunnel;
    private String lbsDam;
    private String lbsCanal;
    private String lbsAqueduct;
    private String lbsLock;
    private String lbsWeir;
    private String lbsReservoir;
    private String lbsTank;
    private String lbsSilo;
    private String lbsBunker;
    private String lbsShelter;
    private String lbsTent;
    private String lbsYurt;
    private String lbsIgloo;
    private String lbsHut;
    private String lbsShack;
    private String lbsShed;
    private String lbsBarn;
    private String lbsStable;
    private String lbsGarage;
    private String lbsHangar;
    private String lbsWarehouse;
    private String lbsFactory;
    private String lbsMill;
    private String lbsMine;
    private String lbsQuarry;
    private String lbsOilField;
    private String lbsGasField;
    private String lbsCoalMine;
    private String lbsGoldMine;
    private String lbsSilverMine;
    private String lbsCopperMine;
    private String lbsIronMine;
    private String lbsAluminumMine;
    private String lbsUraniumMine;
    private String lbsDiamondMine;
    private String lbsEmeraldMine;
    private String lbsRubyMine;
    private String lbsSapphireMine;
    private String lbsOpalMine;
    private String lbsPearlFarm;
    private String lbsFishFarm;
    private String lbsShrimpFarm;
    private String lbsCrabFarm;
    private String lbsOysterFarm;
    private String lbsSeaweedFarm;
    private String lbsAlgaeFarm;
    private String lbsMushroomFarm;
    private String lbsVineyard;
    private String lbsOrchard;
    private String lbsFarm;
    private String lbsRanch;
    private String lbsPlantation;
    private String lbsEstate;
    private String lbsGolfCourse;
    private String lbsSkiResort;
    private String lbsAmusementPark;
    private String lbsThemePark;
    private String lbsWaterPark;
    private String lbsZooPark;
    private String lbsAquariumPark;
    private String lbsSafariPark;
    private String lbsBotanicalGarden;
    private String lbsHerbGarden;
    private String lbsVegetableGarden;
    private String lbsFlowerGarden;
    private String lbsRoseGarden;
    private String lbsTulipGarden;
    private String lbsSunflowerField;
    private String lbsLavenderField;
    private String lbsWheatField;
    private String lbsCornField;
    private String lbsRicePaddy;
    private String lbsSoybeanField;
    private String lbsCottonField;
    private String lbsSugarCaneField;
    private String lbsCoffeePlantation;
    private String lbsTeaPlantation;
    private String lbsCocoaPlantation;
    private String lbsBananaPlantation;
    private String lbsOrangePlantation;
    private String lbsAppleOrchard;
    private String lbsVineyardField;
    private String lbsOliveGrove;
    private String lbsDatePalmGrove;
    private String lbsCoconutPalmGrove;
    private String lbsPineForest;
    private String lbsOakForest;
    private String lbsBambooForest;
    private String lbsPalmForest;
    private String lbsRainforest;
    private String lbsCloudForest;
    private String lbsMangroveForest;
    private String lbsConiferousForest;
    private String lbsDeciduousForest;
    private String lbsMixedForest;
    private String lbsScrubland;
    private String lbsGrassland;
    private String lbsSavanna;
    private String lbsSteppe;
    private String lbsTundra;
    private String lbsTaiga;
    private String lbsBorealForest;
    private String lbsTemperateForest;
    private String lbsTropicalForest;
    private String lbsSubtropicalForest;
    private String lbsMediterraneanForest;
    private String lbsMontaneForest;
    private String lbsAlpineForest;
    private String lbsSubalpineForest;
    private String lbsTreelineForest;
    private String lbsTimberlineForest;
    private String lbsSnowlineForest;
    private String lbsIceCap;
    private String lbsIceSheet;
    private String lbsIceShelf;
    private String lbsIceberg;
    private String lbsGlacierField;
    private String lbsPermafrost;
    private String lbsTundraField;
    private String lbsArcticTundra;
    private String lbsAntarcticTundra;
    private String lbsAlpineTundra;
    private String lbsPolarDesert;
    private String lbsColdDesert;
    private String lbsHotDesert;
    private String lbsTemperateDesert;
    private String lbsCoastalDesert;
    private String lbsSemiaridDesert;
    private String lbsAridDesert;
    private String lbsHyperaridDesert;

    public HotlistItem() {
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getHotnum() {
        return hotnum;
    }

    public void setHotnum(Integer hotnum) {
        this.hotnum = hotnum;
    }

    public String getLbs() {
        return lbs;
    }

    public void setLbs(String lbs) {
        this.lbs = lbs;
    }

    public String getLbsCode() {
        return lbsCode;
    }

    public void setLbsCode(String lbsCode) {
        this.lbsCode = lbsCode;
    }

    public String getLbsType() {
        return lbsType;
    }

    public void setLbsType(String lbsType) {
        this.lbsType = lbsType;
    }

    public String getLbsTypeName() {
        return lbsTypeName;
    }

    public void setLbsTypeName(String lbsTypeName) {
        this.lbsTypeName = lbsTypeName;
    }

    public String getLbsName() {
        return lbsName;
    }

    public void setLbsName(String lbsName) {
        this.lbsName = lbsName;
    }

    public String getLbsProvince() {
        return lbsProvince;
    }

    public void setLbsProvince(String lbsProvince) {
        this.lbsProvince = lbsProvince;
    }

    public String getLbsCity() {
        return lbsCity;
    }

    public void setLbsCity(String lbsCity) {
        this.lbsCity = lbsCity;
    }

    public String getLbsDistrict() {
        return lbsDistrict;
    }

    public void setLbsDistrict(String lbsDistrict) {
        this.lbsDistrict = lbsDistrict;
    }

    public String getLbsPoi() {
        return lbsPoi;
    }

    public void setLbsPoi(String lbsPoi) {
        this.lbsPoi = lbsPoi;
    }

    public String getLbsLat() {
        return lbsLat;
    }

    public void setLbsLat(String lbsLat) {
        this.lbsLat = lbsLat;
    }

    public String getLbsLon() {
        return lbsLon;
    }

    public void setLbsLon(String lbsLon) {
        this.lbsLon = lbsLon;
    }

    public String getLbsAdcode() {
        return lbsAdcode;
    }

    public void setLbsAdcode(String lbsAdcode) {
        this.lbsAdcode = lbsAdcode;
    }

    public String getLbsBusinessArea() {
        return lbsBusinessArea;
    }

    public void setLbsBusinessArea(String lbsBusinessArea) {
        this.lbsBusinessArea = lbsBusinessArea;
    }

    public String getLbsDistance() {
        return lbsDistance;
    }

    public void setLbsDistance(String lbsDistance) {
        this.lbsDistance = lbsDistance;
    }

    public String getLbsAddress() {
        return lbsAddress;
    }

    public void setLbsAddress(String lbsAddress) {
        this.lbsAddress = lbsAddress;
    }

    public String getLbsTel() {
        return lbsTel;
    }

    public void setLbsTel(String lbsTel) {
        this.lbsTel = lbsTel;
    }

    public String getLbsCategory() {
        return lbsCategory;
    }

    public void setLbsCategory(String lbsCategory) {
        this.lbsCategory = lbsCategory;
    }

    public String getLbsTag() {
        return lbsTag;
    }

    public void setLbsTag(String lbsTag) {
        this.lbsTag = lbsTag;
    }

    public String getLbsCheckinNum() {
        return lbsCheckinNum;
    }

    public void setLbsCheckinNum(String lbsCheckinNum) {
        this.lbsCheckinNum = lbsCheckinNum;
    }

    public String getLbsCommentNum() {
        return lbsCommentNum;
    }

    public void setLbsCommentNum(String lbsCommentNum) {
        this.lbsCommentNum = lbsCommentNum;
    }

    public String getLbsScore() {
        return lbsScore;
    }

    public void setLbsScore(String lbsScore) {
        this.lbsScore = lbsScore;
    }

    public String getLbsPrice() {
        return lbsPrice;
    }

    public void setLbsPrice(String lbsPrice) {
        this.lbsPrice = lbsPrice;
    }

    public String getLbsOpenTime() {
        return lbsOpenTime;
    }

    public void setLbsOpenTime(String lbsOpenTime) {
        this.lbsOpenTime = lbsOpenTime;
    }

    public String getLbsClosedTime() {
        return lbsClosedTime;
    }

    public void setLbsClosedTime(String lbsClosedTime) {
        this.lbsClosedTime = lbsClosedTime;
    }

    public String getLbsAlias() {
        return lbsAlias;
    }

    public void setLbsAlias(String lbsAlias) {
        this.lbsAlias = lbsAlias;
    }

    public String getLbsIntroduction() {
        return lbsIntroduction;
    }

    public void setLbsIntroduction(String lbsIntroduction) {
        this.lbsIntroduction = lbsIntroduction;
    }

    public String getLbsTips() {
        return lbsTips;
    }

    public void setLbsTips(String lbsTips) {
        this.lbsTips = lbsTips;
    }

    public String getLbsTraffic() {
        return lbsTraffic;
    }

    public void setLbsTraffic(String lbsTraffic) {
        this.lbsTraffic = lbsTraffic;
    }

    public String getLbsWebsite() {
        return lbsWebsite;
    }

    public void setLbsWebsite(String lbsWebsite) {
        this.lbsWebsite = lbsWebsite;
    }

    public String getLbsEmail() {
        return lbsEmail;
    }

    public void setLbsEmail(String lbsEmail) {
        this.lbsEmail = lbsEmail;
    }

    public String getLbsWeibo() {
        return lbsWeibo;
    }

    public void setLbsWeibo(String lbsWeibo) {
        this.lbsWeibo = lbsWeibo;
    }

    public String getLbsWechat() {
        return lbsWechat;
    }

    public void setLbsWechat(String lbsWechat) {
        this.lbsWechat = lbsWechat;
    }

    public String getLbsQq() {
        return lbsQq;
    }

    public void setLbsQq(String lbsQq) {
        this.lbsQq = lbsQq;
    }

    public String getLbsPhone() {
        return lbsPhone;
    }

    public void setLbsPhone(String lbsPhone) {
        this.lbsPhone = lbsPhone;
    }

    public String getLbsFax() {
        return lbsFax;
    }

    public void setLbsFax(String lbsFax) {
        this.lbsFax = lbsFax;
    }

    public String getLbsPostcode() {
        return lbsPostcode;
    }

    public void setLbsPostcode(String lbsPostcode) {
        this.lbsPostcode = lbsPostcode;
    }

    public String getLbsCountry() {
        return lbsCountry;
    }

    public void setLbsCountry(String lbsCountry) {
        this.lbsCountry = lbsCountry;
    }

    public String getLbsContinent() {
        return lbsContinent;
    }

    public void setLbsContinent(String lbsContinent) {
        this.lbsContinent = lbsContinent;
    }

    public String getLbsTimezone() {
        return lbsTimezone;
    }

    public void setLbsTimezone(String lbsTimezone) {
        this.lbsTimezone = lbsTimezone;
    }

    public String getLbsCurrency() {
        return lbsCurrency;
    }

    public void setLbsCurrency(String lbsCurrency) {
        this.lbsCurrency = lbsCurrency;
    }

    public String getLbsLanguage() {
        return lbsLanguage;
    }

    public void setLbsLanguage(String lbsLanguage) {
        this.lbsLanguage = lbsLanguage;
    }

    public String getLbsEmergency() {
        return lbsEmergency;
    }

    public void setLbsEmergency(String lbsEmergency) {
        this.lbsEmergency = lbsEmergency;
    }

    public String getLbsMedical() {
        return lbsMedical;
    }

    public void setLbsMedical(String lbsMedical) {
        this.lbsMedical = lbsMedical;
    }

    public String getLbsPolice() {
        return lbsPolice;
    }

    public void setLbsPolice(String lbsPolice) {
        this.lbsPolice = lbsPolice;
    }

    public String getLbsFire() {
        return lbsFire;
    }

    public void setLbsFire(String lbsFire) {
        this.lbsFire = lbsFire;
    }

    public String getLbsCustoms() {
        return lbsCustoms;
    }

    public void setLbsCustoms(String lbsCustoms) {
        this.lbsCustoms = lbsCustoms;
    }

    public String getLbsImmigration() {
        return lbsImmigration;
    }

    public void setLbsImmigration(String lbsImmigration) {
        this.lbsImmigration = lbsImmigration;
    }

    public String getLbsVisa() {
        return lbsVisa;
    }

    public void setLbsVisa(String lbsVisa) {
        this.lbsVisa = lbsVisa;
    }

    public String getLbsInsurance() {
        return lbsInsurance;
    }

    public void setLbsInsurance(String lbsInsurance) {
        this.lbsInsurance = lbsInsurance;
    }

    public String getLbsPower() {
        return lbsPower;
    }

    public void setLbsPower(String lbsPower) {
        this.lbsPower = lbsPower;
    }

    public String getLbsWater() {
        return lbsWater;
    }

    public void setLbsWater(String lbsWater) {
        this.lbsWater = lbsWater;
    }

    public String getLbsSewage() {
        return lbsSewage;
    }

    public void setLbsSewage(String lbsSewage) {
        this.lbsSewage = lbsSewage;
    }

    public String getLbsGarbage() {
        return lbsGarbage;
    }

    public void setLbsGarbage(String lbsGarbage) {
        this.lbsGarbage = lbsGarbage;
    }

    public String getLbsRecycling() {
        return lbsRecycling;
    }

    public void setLbsRecycling(String lbsRecycling) {
        this.lbsRecycling = lbsRecycling;
    }

    public String getLbsWifi() {
        return lbsWifi;
    }

    public void setLbsWifi(String lbsWifi) {
        this.lbsWifi = lbsWifi;
    }

    public String getLbsParking() {
        return lbsParking;
    }

    public void setLbsParking(String lbsParking) {
        this.lbsParking = lbsParking;
    }

    public String getLbsGas() {
        return lbsGas;
    }

    public void setLbsGas(String lbsGas) {
        this.lbsGas = lbsGas;
    }

    public String getLbsElectricity() {
        return lbsElectricity;
    }

    public void setLbsElectricity(String lbsElectricity) {
        this.lbsElectricity = lbsElectricity;
    }

    public String getLbsHeating() {
        return lbsHeating;
    }

    public void setLbsHeating(String lbsHeating) {
        this.lbsHeating = lbsHeating;
    }

    public String getLbsCooling() {
        return lbsCooling;
    }

    public void setLbsCooling(String lbsCooling) {
        this.lbsCooling = lbsCooling;
    }

    public String getLbsElevator() {
        return lbsElevator;
    }

    public void setLbsElevator(String lbsElevator) {
        this.lbsElevator = lbsElevator;
    }

    public String getLbsStairs() {
        return lbsStairs;
    }

    public void setLbsStairs(String lbsStairs) {
        this.lbsStairs = lbsStairs;
    }

    public String getLbsRamp() {
        return lbsRamp;
    }

    public void setLbsRamp(String lbsRamp) {
        this.lbsRamp = lbsRamp;
    }

    public String getLbsToilet() {
        return lbsToilet;
    }

    public void setLbsToilet(String lbsToilet) {
        this.lbsToilet = lbsToilet;
    }

    public String getLbsShower() {
        return lbsShower;
    }

    public void setLbsShower(String lbsShower) {
        this.lbsShower = lbsShower;
    }

    public String getLbsBath() {
        return lbsBath;
    }

    public void setLbsBath(String lbsBath) {
        this.lbsBath = lbsBath;
    }

    public String getLbsSauna() {
        return lbsSauna;
    }

    public void setLbsSauna(String lbsSauna) {
        this.lbsSauna = lbsSauna;
    }

    public String getLbsPool() {
        return lbsPool;
    }

    public void setLbsPool(String lbsPool) {
        this.lbsPool = lbsPool;
    }

    public String getLbsGym() {
        return lbsGym;
    }

    public void setLbsGym(String lbsGym) {
        this.lbsGym = lbsGym;
    }

    public String getLbsSpa() {
        return lbsSpa;
    }

    public void setLbsSpa(String lbsSpa) {
        this.lbsSpa = lbsSpa;
    }

    public String getLbsMassage() {
        return lbsMassage;
    }

    public void setLbsMassage(String lbsMassage) {
        this.lbsMassage = lbsMassage;
    }

    public String getLbsRestaurant() {
        return lbsRestaurant;
    }

    public void setLbsRestaurant(String lbsRestaurant) {
        this.lbsRestaurant = lbsRestaurant;
    }

    public String getLbsBar() {
        return lbsBar;
    }

    public void setLbsBar(String lbsBar) {
        this.lbsBar = lbsBar;
    }

    public String getLbsCafe() {
        return lbsCafe;
    }

    public void setLbsCafe(String lbsCafe) {
        this.lbsCafe = lbsCafe;
    }

    public String getLbsShop() {
        return lbsShop;
    }

    public void setLbsShop(String lbsShop) {
        this.lbsShop = lbsShop;
    }

    public String getLbsMarket() {
        return lbsMarket;
    }

    public void setLbsMarket(String lbsMarket) {
        this.lbsMarket = lbsMarket;
    }

    public String getLbsBank() {
        return lbsBank;
    }

    public void setLbsBank(String lbsBank) {
        this.lbsBank = lbsBank;
    }

    public String getLbsAtm() {
        return lbsAtm;
    }

    public void setLbsAtm(String lbsAtm) {
        this.lbsAtm = lbsAtm;
    }

    public String getLbsHospital() {
        return lbsHospital;
    }

    public void setLbsHospital(String lbsHospital) {
        this.lbsHospital = lbsHospital;
    }

    public String getLbsClinic() {
        return lbsClinic;
    }

    public void setLbsClinic(String lbsClinic) {
        this.lbsClinic = lbsClinic;
    }

    public String getLbsPharmacy() {
        return lbsPharmacy;
    }

    public void setLbsPharmacy(String lbsPharmacy) {
        this.lbsPharmacy = lbsPharmacy;
    }

    public String getLbsSchool() {
        return lbsSchool;
    }

    public void setLbsSchool(String lbsSchool) {
        this.lbsSchool = lbsSchool;
    }

    public String getLbsUniversity() {
        return lbsUniversity;
    }

    public void setLbsUniversity(String lbsUniversity) {
        this.lbsUniversity = lbsUniversity;
    }

    public String getLbsLibrary() {
        return lbsLibrary;
    }

    public void setLbsLibrary(String lbsLibrary) {
        this.lbsLibrary = lbsLibrary;
    }

    public String getLbsMuseum() {
        return lbsMuseum;
    }

    public void setLbsMuseum(String lbsMuseum) {
        this.lbsMuseum = lbsMuseum;
    }

    public String getLbsTheater() {
        return lbsTheater;
    }

    public void setLbsTheater(String lbsTheater) {
        this.lbsTheater = lbsTheater;
    }

    public String getLbsCinema() {
        return lbsCinema;
    }

    public void setLbsCinema(String lbsCinema) {
        this.lbsCinema = lbsCinema;
    }

    public String getLbsPark() {
        return lbsPark;
    }

    public void setLbsPark(String lbsPark) {
        this.lbsPark = lbsPark;
    }

    public String getLbsBeach() {
        return lbsBeach;
    }

    public void setLbsBeach(String lbsBeach) {
        this.lbsBeach = lbsBeach;
    }

    public String getLbsMountain() {
        return lbsMountain;
    }

    public void setLbsMountain(String lbsMountain) {
        this.lbsMountain = lbsMountain;
    }

    public String getLbsLake() {
        return lbsLake;
    }

    public void setLbsLake(String lbsLake) {
        this.lbsLake = lbsLake;
    }

    public String getLbsRiver() {
        return lbsRiver;
    }

    public void setLbsRiver(String lbsRiver) {
        this.lbsRiver = lbsRiver;
    }

    public String getLbsSea() {
        return lbsSea;
    }

    public void setLbsSea(String lbsSea) {
        this.lbsSea = lbsSea;
    }

    public String getLbsIsland() {
        return lbsIsland;
    }

    public void setLbsIsland(String lbsIsland) {
        this.lbsIsland = lbsIsland;
    }

    public String getLbsDesert() {
        return lbsDesert;
    }

    public void setLbsDesert(String lbsDesert) {
        this.lbsDesert = lbsDesert;
    }

    public String getLbsForest() {
        return lbsForest;
    }

    public void setLbsForest(String lbsForest) {
        this.lbsForest = lbsForest;
    }

    public String getLbsCave() {
        return lbsCave;
    }

    public void setLbsCave(String lbsCave) {
        this.lbsCave = lbsCave;
    }

    public String getLbsWaterfall() {
        return lbsWaterfall;
    }

    public void setLbsWaterfall(String lbsWaterfall) {
        this.lbsWaterfall = lbsWaterfall;
    }

    public String getLbsVolcano() {
        return lbsVolcano;
    }

    public void setLbsVolcano(String lbsVolcano) {
        this.lbsVolcano = lbsVolcano;
    }

    public String getLbsGlacier() {
        return lbsGlacier;
    }

    public void setLbsGlacier(String lbsGlacier) {
        this.lbsGlacier = lbsGlacier;
    }

    public String getLbsCanyon() {
        return lbsCanyon;
    }

    public void setLbsCanyon(String lbsCanyon) {
        this.lbsCanyon = lbsCanyon;
    }

    public String getLbsValley() {
        return lbsValley;
    }

    public void setLbsValley(String lbsValley) {
        this.lbsValley = lbsValley;
    }

    public String getLbsPlateau() {
        return lbsPlateau;
    }

    public void setLbsPlateau(String lbsPlateau) {
        this.lbsPlateau = lbsPlateau;
    }

    public String getLbsPlain() {
        return lbsPlain;
    }

    public void setLbsPlain(String lbsPlain) {
        this.lbsPlain = lbsPlain;
    }

    public String getLbsHill() {
        return lbsHill;
    }

    public void setLbsHill(String lbsHill) {
        this.lbsHill = lbsHill;
    }

    public String getLbsDune() {
        return lbsDune;
    }

    public void setLbsDune(String lbsDune) {
        this.lbsDune = lbsDune;
    }

    public String getLbsReef() {
        return lbsReef;
    }

    public void setLbsReef(String lbsReef) {
        this.lbsReef = lbsReef;
    }

    public String getLbsWetland() {
        return lbsWetland;
    }

    public void setLbsWetland(String lbsWetland) {
        this.lbsWetland = lbsWetland;
    }

    public String getLbsReserve() {
        return lbsReserve;
    }

    public void setLbsReserve(String lbsReserve) {
        this.lbsReserve = lbsReserve;
    }

    public String getLbsGarden() {
        return lbsGarden;
    }

    public void setLbsGarden(String lbsGarden) {
        this.lbsGarden = lbsGarden;
    }

    public String getLbsZoo() {
        return lbsZoo;
    }

    public void setLbsZoo(String lbsZoo) {
        this.lbsZoo = lbsZoo;
    }

    public String getLbsAquarium() {
        return lbsAquarium;
    }

    public void setLbsAquarium(String lbsAquarium) {
        this.lbsAquarium = lbsAquarium;
    }

    public String getLbsStadium() {
        return lbsStadium;
    }

    public void setLbsStadium(String lbsStadium) {
        this.lbsStadium = lbsStadium;
    }

    public String getLbsArena() {
        return lbsArena;
    }

    public void setLbsArena(String lbsArena) {
        this.lbsArena = lbsArena;
    }

    public String getLbsCasino() {
        return lbsCasino;
    }

    public void setLbsCasino(String lbsCasino) {
        this.lbsCasino = lbsCasino;
    }

    public String getLbsHotel() {
        return lbsHotel;
    }

    public void setLbsHotel(String lbsHotel) {
        this.lbsHotel = lbsHotel;
    }

    public String getLbsHostel() {
        return lbsHostel;
    }

    public void setLbsHostel(String lbsHostel) {
        this.lbsHostel = lbsHostel;
    }

    public String getLbsResort() {
        return lbsResort;
    }

    public void setLbsResort(String lbsResort) {
        this.lbsResort = lbsResort;
    }

    public String getLbsMotel() {
        return lbsMotel;
    }

    public void setLbsMotel(String lbsMotel) {
        this.lbsMotel = lbsMotel;
    }

    public String getLbsCamp() {
        return lbsCamp;
    }

    public void setLbsCamp(String lbsCamp) {
        this.lbsCamp = lbsCamp;
    }

    public String getLbsCottage() {
        return lbsCottage;
    }

    public void setLbsCottage(String lbsCottage) {
        this.lbsCottage = lbsCottage;
    }

    public String getLbsVilla() {
        return lbsVilla;
    }

    public void setLbsVilla(String lbsVilla) {
        this.lbsVilla = lbsVilla;
    }

    public String getLbsApartment() {
        return lbsApartment;
    }

    public void setLbsApartment(String lbsApartment) {
        this.lbsApartment = lbsApartment;
    }

    public String getLbsHouse() {
        return lbsHouse;
    }

    public void setLbsHouse(String lbsHouse) {
        this.lbsHouse = lbsHouse;
    }

    public String getLbsFlat() {
        return lbsFlat;
    }

    public void setLbsFlat(String lbsFlat) {
        this.lbsFlat = lbsFlat;
    }

    public String getLbsRoom() {
        return lbsRoom;
    }

    public void setLbsRoom(String lbsRoom) {
        this.lbsRoom = lbsRoom;
    }

    public String getLbsSuite() {
        return lbsSuite;
    }

    public void setLbsSuite(String lbsSuite) {
        this.lbsSuite = lbsSuite;
    }

    public String getLbsStudio() {
        return lbsStudio;
    }

    public void setLbsStudio(String lbsStudio) {
        this.lbsStudio = lbsStudio;
    }

    public String getLbsLoft() {
        return lbsLoft;
    }

    public void setLbsLoft(String lbsLoft) {
        this.lbsLoft = lbsLoft;
    }

    public String getLbsPenthouse() {
        return lbsPenthouse;
    }

    public void setLbsPenthouse(String lbsPenthouse) {
        this.lbsPenthouse = lbsPenthouse;
    }

    public String getLbsDuplex() {
        return lbsDuplex;
    }

    public void setLbsDuplex(String lbsDuplex) {
        this.lbsDuplex = lbsDuplex;
    }

    public String getLbsTriplex() {
        return lbsTriplex;
    }

    public void setLbsTriplex(String lbsTriplex) {
        this.lbsTriplex = lbsTriplex;
    }

    public String getLbsQuadplex() {
        return lbsQuadplex;
    }

    public void setLbsQuadplex(String lbsQuadplex) {
        this.lbsQuadplex = lbsQuadplex;
    }

    public String getLbsTownhouse() {
        return lbsTownhouse;
    }

    public void setLbsTownhouse(String lbsTownhouse) {
        this.lbsTownhouse = lbsTownhouse;
    }

    public String getLbsBungalow() {
        return lbsBungalow;
    }

    public void setLbsBungalow(String lbsBungalow) {
        this.lbsBungalow = lbsBungalow;
    }

    public String getLbsChalet() {
        return lbsChalet;
    }

    public void setLbsChalet(String lbsChalet) {
        this.lbsChalet = lbsChalet;
    }

    public String getLbsCabin() {
        return lbsCabin;
    }

    public void setLbsCabin(String lbsCabin) {
        this.lbsCabin = lbsCabin;
    }

    public String getLbsLodge() {
        return lbsLodge;
    }

    public void setLbsLodge(String lbsLodge) {
        this.lbsLodge = lbsLodge;
    }

    public String getLbsManor() {
        return lbsManor;
    }

    public void setLbsManor(String lbsManor) {
        this.lbsManor = lbsManor;
    }

    public String getLbsMansion() {
        return lbsMansion;
    }

    public void setLbsMansion(String lbsMansion) {
        this.lbsMansion = lbsMansion;
    }

    public String getLbsPalace() {
        return lbsPalace;
    }

    public void setLbsPalace(String lbsPalace) {
        this.lbsPalace = lbsPalace;
    }

    public String getLbsCastle() {
        return lbsCastle;
    }

    public void setLbsCastle(String lbsCastle) {
        this.lbsCastle = lbsCastle;
    }

    public String getLbsFort() {
        return lbsFort;
    }

    public void setLbsFort(String lbsFort) {
        this.lbsFort = lbsFort;
    }

    public String getLbsBarracks() {
        return lbsBarracks;
    }

    public void setLbsBarracks(String lbsBarracks) {
        this.lbsBarracks = lbsBarracks;
    }

    public String getLbsPrison() {
        return lbsPrison;
    }

    public void setLbsPrison(String lbsPrison) {
        this.lbsPrison = lbsPrison;
    }

    public String getLbsAsylum() {
        return lbsAsylum;
    }

    public void setLbsAsylum(String lbsAsylum) {
        this.lbsAsylum = lbsAsylum;
    }

    public String getLbsOrphanage() {
        return lbsOrphanage;
    }

    public void setLbsOrphanage(String lbsOrphanage) {
        this.lbsOrphanage = lbsOrphanage;
    }

    public String getLbsNursingHome() {
        return lbsNursingHome;
    }

    public void setLbsNursingHome(String lbsNursingHome) {
        this.lbsNursingHome = lbsNursingHome;
    }

    public String getLbsRetirementHome() {
        return lbsRetirementHome;
    }

    public void setLbsRetirementHome(String lbsRetirementHome) {
        this.lbsRetirementHome = lbsRetirementHome;
    }

    public String getLbsHospice() {
        return lbsHospice;
    }

    public void setLbsHospice(String lbsHospice) {
        this.lbsHospice = lbsHospice;
    }

    public String getLbsMorgue() {
        return lbsMorgue;
    }

    public void setLbsMorgue(String lbsMorgue) {
        this.lbsMorgue = lbsMorgue;
    }

    public String getLbsCemetery() {
        return lbsCemetery;
    }

    public void setLbsCemetery(String lbsCemetery) {
        this.lbsCemetery = lbsCemetery;
    }

    public String getLbsMausoleum() {
        return lbsMausoleum;
    }

    public void setLbsMausoleum(String lbsMausoleum) {
        this.lbsMausoleum = lbsMausoleum;
    }

    public String getLbsTomb() {
        return lbsTomb;
    }

    public void setLbsTomb(String lbsTomb) {
        this.lbsTomb = lbsTomb;
    }

    public String getLbsGrave() {
        return lbsGrave;
    }

    public void setLbsGrave(String lbsGrave) {
        this.lbsGrave = lbsGrave;
    }

    public String getLbsHeadstone() {
        return lbsHeadstone;
    }

    public void setLbsHeadstone(String lbsHeadstone) {
        this.lbsHeadstone = lbsHeadstone;
    }

    public String getLbsMonument() {
        return lbsMonument;
    }

    public void setLbsMonument(String lbsMonument) {
        this.lbsMonument = lbsMonument;
    }

    public String getLbsStatue() {
        return lbsStatue;
    }

    public void setLbsStatue(String lbsStatue) {
        this.lbsStatue = lbsStatue;
    }

    public String getLbsSculpture() {
        return lbsSculpture;
    }

    public void setLbsSculpture(String lbsSculpture) {
        this.lbsSculpture = lbsSculpture;
    }

    public String getLbsFountain() {
        return lbsFountain;
    }

    public void setLbsFountain(String lbsFountain) {
        this.lbsFountain = lbsFountain;
    }

    public String getLbsBridge() {
        return lbsBridge;
    }

    public void setLbsBridge(String lbsBridge) {
        this.lbsBridge = lbsBridge;
    }

    public String getLbsTunnel() {
        return lbsTunnel;
    }

    public void setLbsTunnel(String lbsTunnel) {
        this.lbsTunnel = lbsTunnel;
    }

    public String getLbsDam() {
        return lbsDam;
    }

    public void setLbsDam(String lbsDam) {
        this.lbsDam = lbsDam;
    }

    public String getLbsCanal() {
        return lbsCanal;
    }

    public void setLbsCanal(String lbsCanal) {
        this.lbsCanal = lbsCanal;
    }

    public String getLbsAqueduct() {
        return lbsAqueduct;
    }

    public void setLbsAqueduct(String lbsAqueduct) {
        this.lbsAqueduct = lbsAqueduct;
    }

    public String getLbsLock() {
        return lbsLock;
    }

    public void setLbsLock(String lbsLock) {
        this.lbsLock = lbsLock;
    }

    public String getLbsWeir() {
        return lbsWeir;
    }

    public void setLbsWeir(String lbsWeir) {
        this.lbsWeir = lbsWeir;
    }

    public String getLbsReservoir() {
        return lbsReservoir;
    }

    public void setLbsReservoir(String lbsReservoir) {
        this.lbsReservoir = lbsReservoir;
    }

    public String getLbsTank() {
        return lbsTank;
    }

    public void setLbsTank(String lbsTank) {
        this.lbsTank = lbsTank;
    }

    public String getLbsSilo() {
        return lbsSilo;
    }

    public void setLbsSilo(String lbsSilo) {
        this.lbsSilo = lbsSilo;
    }

    public String getLbsBunker() {
        return lbsBunker;
    }

    public void setLbsBunker(String lbsBunker) {
        this.lbsBunker = lbsBunker;
    }

    public String getLbsShelter() {
        return lbsShelter;
    }

    public void setLbsShelter(String lbsShelter) {
        this.lbsShelter = lbsShelter;
    }

    public String getLbsTent() {
        return lbsTent;
    }

    public void setLbsTent(String lbsTent) {
        this.lbsTent = lbsTent;
    }

    public String getLbsYurt() {
        return lbsYurt;
    }

    public void setLbsYurt(String lbsYurt) {
        this.lbsYurt = lbsYurt;
    }

    public String getLbsIgloo() {
        return lbsIgloo;
    }

    public void setLbsIgloo(String lbsIgloo) {
        this.lbsIgloo = lbsIgloo;
    }

    public String getLbsHut() {
        return lbsHut;
    }

    public void setLbsHut(String lbsHut) {
        this.lbsHut = lbsHut;
    }

    public String getLbsShack() {
        return lbsShack;
    }

    public void setLbsShack(String lbsShack) {
        this.lbsShack = lbsShack;
    }

    public String getLbsShed() {
        return lbsShed;
    }

    public void setLbsShed(String lbsShed) {
        this.lbsShed = lbsShed;
    }

    public String getLbsBarn() {
        return lbsBarn;
    }

    public void setLbsBarn(String lbsBarn) {
        this.lbsBarn = lbsBarn;
    }

    public String getLbsStable() {
        return lbsStable;
    }

    public void setLbsStable(String lbsStable) {
        this.lbsStable = lbsStable;
    }

    public String getLbsGarage() {
        return lbsGarage;
    }

    public void setLbsGarage(String lbsGarage) {
        this.lbsGarage = lbsGarage;
    }

    public String getLbsHangar() {
        return lbsHangar;
    }

    public void setLbsHangar(String lbsHangar) {
        this.lbsHangar = lbsHangar;
    }

    public String getLbsWarehouse() {
        return lbsWarehouse;
    }

    public void setLbsWarehouse(String lbsWarehouse) {
        this.lbsWarehouse = lbsWarehouse;
    }

    public String getLbsFactory() {
        return lbsFactory;
    }

    public void setLbsFactory(String lbsFactory) {
        this.lbsFactory = lbsFactory;
    }

    public String getLbsMill() {
        return lbsMill;
    }

    public void setLbsMill(String lbsMill) {
        this.lbsMill = lbsMill;
    }

    public String getLbsMine() {
        return lbsMine;
    }

    public void setLbsMine(String lbsMine) {
        this.lbsMine = lbsMine;
    }

    public String getLbsQuarry() {
        return lbsQuarry;
    }

    public void setLbsQuarry(String lbsQuarry) {
        this.lbsQuarry = lbsQuarry;
    }

    public String getLbsOilField() {
        return lbsOilField;
    }

    public void setLbsOilField(String lbsOilField) {
        this.lbsOilField = lbsOilField;
    }

    public String getLbsGasField() {
        return lbsGasField;
    }

    public void setLbsGasField(String lbsGasField) {
        this.lbsGasField = lbsGasField;
    }

    public String getLbsCoalMine() {
        return lbsCoalMine;
    }

    public void setLbsCoalMine(String lbsCoalMine) {
        this.lbsCoalMine = lbsCoalMine;
    }

    public String getLbsGoldMine() {
        return lbsGoldMine;
    }

    public void setLbsGoldMine(String lbsGoldMine) {
        this.lbsGoldMine = lbsGoldMine;
    }

    public String getLbsSilverMine() {
        return lbsSilverMine;
    }

    public void setLbsSilverMine(String lbsSilverMine) {
        this.lbsSilverMine = lbsSilverMine;
    }

    public String getLbsCopperMine() {
        return lbsCopperMine;
    }

    public void setLbsCopperMine(String lbsCopperMine) {
        this.lbsCopperMine = lbsCopperMine;
    }

    public String getLbsIronMine() {
        return lbsIronMine;
    }

    public void setLbsIronMine(String lbsIronMine) {
        this.lbsIronMine = lbsIronMine;
    }

    public String getLbsAluminumMine() {
        return lbsAluminumMine;
    }

    public void setLbsAluminumMine(String lbsAluminumMine) {
        this.lbsAluminumMine = lbsAluminumMine;
    }

    public String getLbsUraniumMine() {
        return lbsUraniumMine;
    }

    public void setLbsUraniumMine(String lbsUraniumMine) {
        this.lbsUraniumMine = lbsUraniumMine;
    }

    public String getLbsDiamondMine() {
        return lbsDiamondMine;
    }

    public void setLbsDiamondMine(String lbsDiamondMine) {
        this.lbsDiamondMine = lbsDiamondMine;
    }

    public String getLbsEmeraldMine() {
        return lbsEmeraldMine;
    }

    public void setLbsEmeraldMine(String lbsEmeraldMine) {
        this.lbsEmeraldMine = lbsEmeraldMine;
    }

    public String getLbsRubyMine() {
        return lbsRubyMine;
    }

    public void setLbsRubyMine(String lbsRubyMine) {
        this.lbsRubyMine = lbsRubyMine;
    }

    public String getLbsSapphireMine() {
        return lbsSapphireMine;
    }

    public void setLbsSapphireMine(String lbsSapphireMine) {
        this.lbsSapphireMine = lbsSapphireMine;
    }

    public String getLbsOpalMine() {
        return lbsOpalMine;
    }

    public void setLbsOpalMine(String lbsOpalMine) {
        this.lbsOpalMine = lbsOpalMine;
    }

    public String getLbsPearlFarm() {
        return lbsPearlFarm;
    }

    public void setLbsPearlFarm(String lbsPearlFarm) {
        this.lbsPearlFarm = lbsPearlFarm;
    }

    public String getLbsFishFarm() {
        return lbsFishFarm;
    }

    public void setLbsFishFarm(String lbsFishFarm) {
        this.lbsFishFarm = lbsFishFarm;
    }

    public String getLbsShrimpFarm() {
        return lbsShrimpFarm;
    }

    public void setLbsShrimpFarm(String lbsShrimpFarm) {
        this.lbsShrimpFarm = lbsShrimpFarm;
    }

    public String getLbsCrabFarm() {
        return lbsCrabFarm;
    }

    public void setLbsCrabFarm(String lbsCrabFarm) {
        this.lbsCrabFarm = lbsCrabFarm;
    }

    public String getLbsOysterFarm() {
        return lbsOysterFarm;
    }

    public void setLbsOysterFarm(String lbsOysterFarm) {
        this.lbsOysterFarm = lbsOysterFarm;
    }

    public String getLbsSeaweedFarm() {
        return lbsSeaweedFarm;
    }

    public void setLbsSeaweedFarm(String lbsSeaweedFarm) {
        this.lbsSeaweedFarm = lbsSeaweedFarm;
    }

    public String getLbsAlgaeFarm() {
        return lbsAlgaeFarm;
    }

    public void setLbsAlgaeFarm(String lbsAlgaeFarm) {
        this.lbsAlgaeFarm = lbsAlgaeFarm;
    }

    public String getLbsMushroomFarm() {
        return lbsMushroomFarm;
    }

    public void setLbsMushroomFarm(String lbsMushroomFarm) {
        this.lbsMushroomFarm = lbsMushroomFarm;
    }

    public String getLbsVineyard() {
        return lbsVineyard;
    }

    public void setLbsVineyard(String lbsVineyard) {
        this.lbsVineyard = lbsVineyard;
    }

    public String getLbsOrchard() {
        return lbsOrchard;
    }

    public void setLbsOrchard(String lbsOrchard) {
        this.lbsOrchard = lbsOrchard;
    }

    public String getLbsFarm() {
        return lbsFarm;
    }

    public void setLbsFarm(String lbsFarm) {
        this.lbsFarm = lbsFarm;
    }

    public String getLbsRanch() {
        return lbsRanch;
    }

    public void setLbsRanch(String lbsRanch) {
        this.lbsRanch = lbsRanch;
    }

    public String getLbsPlantation() {
        return lbsPlantation;
    }

    public void setLbsPlantation(String lbsPlantation) {
        this.lbsPlantation = lbsPlantation;
    }

    public String getLbsEstate() {
        return lbsEstate;
    }

    public void setLbsEstate(String lbsEstate) {
        this.lbsEstate = lbsEstate;
    }

    public String getLbsGolfCourse() {
        return lbsGolfCourse;
    }

    public void setLbsGolfCourse(String lbsGolfCourse) {
        this.lbsGolfCourse = lbsGolfCourse;
    }

    public String getLbsSkiResort() {
        return lbsSkiResort;
    }

    public void setLbsSkiResort(String lbsSkiResort) {
        this.lbsSkiResort = lbsSkiResort;
    }

    public String getLbsAmusementPark() {
        return lbsAmusementPark;
    }

    public void setLbsAmusementPark(String lbsAmusementPark) {
        this.lbsAmusementPark = lbsAmusementPark;
    }

    public String getLbsThemePark() {
        return lbsThemePark;
    }

    public void setLbsThemePark(String lbsThemePark) {
        this.lbsThemePark = lbsThemePark;
    }

    public String getLbsWaterPark() {
        return lbsWaterPark;
    }

    public void setLbsWaterPark(String lbsWaterPark) {
        this.lbsWaterPark = lbsWaterPark;
    }

    public String getLbsZooPark() {
        return lbsZooPark;
    }

    public void setLbsZooPark(String lbsZooPark) {
        this.lbsZooPark = lbsZooPark;
    }

    public String getLbsAquariumPark() {
        return lbsAquariumPark;
    }

    public void setLbsAquariumPark(String lbsAquariumPark) {
        this.lbsAquariumPark = lbsAquariumPark;
    }

    public String getLbsSafariPark() {
        return lbsSafariPark;
    }

    public void setLbsSafariPark(String lbsSafariPark) {
        this.lbsSafariPark = lbsSafariPark;
    }

    public String getLbsBotanicalGarden() {
        return lbsBotanicalGarden;
    }

    public void setLbsBotanicalGarden(String lbsBotanicalGarden) {
        this.lbsBotanicalGarden = lbsBotanicalGarden;
    }

    public String getLbsHerbGarden() {
        return lbsHerbGarden;
    }

    public void setLbsHerbGarden(String lbsHerbGarden) {
        this.lbsHerbGarden = lbsHerbGarden;
    }

    public String getLbsVegetableGarden() {
        return lbsVegetableGarden;
    }

    public void setLbsVegetableGarden(String lbsVegetableGarden) {
        this.lbsVegetableGarden = lbsVegetableGarden;
    }

    public String getLbsFlowerGarden() {
        return lbsFlowerGarden;
    }

    public void setLbsFlowerGarden(String lbsFlowerGarden) {
        this.lbsFlowerGarden = lbsFlowerGarden;
    }

    public String getLbsRoseGarden() {
        return lbsRoseGarden;
    }

    public void setLbsRoseGarden(String lbsRoseGarden) {
        this.lbsRoseGarden = lbsRoseGarden;
    }

    public String getLbsTulipGarden() {
        return lbsTulipGarden;
    }

    public void setLbsTulipGarden(String lbsTulipGarden) {
        this.lbsTulipGarden = lbsTulipGarden;
    }

    public String getLbsSunflowerField() {
        return lbsSunflowerField;
    }

    public void setLbsSunflowerField(String lbsSunflowerField) {
        this.lbsSunflowerField = lbsSunflowerField;
    }

    public String getLbsLavenderField() {
        return lbsLavenderField;
    }

    public void setLbsLavenderField(String lbsLavenderField) {
        this.lbsLavenderField = lbsLavenderField;
    }

    public String getLbsWheatField() {
        return lbsWheatField;
    }

    public void setLbsWheatField(String lbsWheatField) {
        this.lbsWheatField = lbsWheatField;
    }

    public String getLbsCornField() {
        return lbsCornField;
    }

    public void setLbsCornField(String lbsCornField) {
        this.lbsCornField = lbsCornField;
    }

    public String getLbsRicePaddy() {
        return lbsRicePaddy;
    }

    public void setLbsRicePaddy(String lbsRicePaddy) {
        this.lbsRicePaddy = lbsRicePaddy;
    }

    public String getLbsSoybeanField() {
        return lbsSoybeanField;
    }

    public void setLbsSoybeanField(String lbsSoybeanField) {
        this.lbsSoybeanField = lbsSoybeanField;
    }

    public String getLbsCottonField() {
        return lbsCottonField;
    }

    public void setLbsCottonField(String lbsCottonField) {
        this.lbsCottonField = lbsCottonField;
    }

    public String getLbsSugarCaneField() {
        return lbsSugarCaneField;
    }

    public void setLbsSugarCaneField(String lbsSugarCaneField) {
        this.lbsSugarCaneField = lbsSugarCaneField;
    }

    public String getLbsCoffeePlantation() {
        return lbsCoffeePlantation;
    }

    public void setLbsCoffeePlantation(String lbsCoffeePlantation) {
        this.lbsCoffeePlantation = lbsCoffeePlantation;
    }

    public String getLbsTeaPlantation() {
        return lbsTeaPlantation;
    }

    public void setLbsTeaPlantation(String lbsTeaPlantation) {
        this.lbsTeaPlantation = lbsTeaPlantation;
    }

    public String getLbsCocoaPlantation() {
        return lbsCocoaPlantation;
    }

    public void setLbsCocoaPlantation(String lbsCocoaPlantation) {
        this.lbsCocoaPlantation = lbsCocoaPlantation;
    }

    public String getLbsBananaPlantation() {
        return lbsBananaPlantation;
    }

    public void setLbsBananaPlantation(String lbsBananaPlantation) {
        this.lbsBananaPlantation = lbsBananaPlantation;
    }

    public String getLbsOrangePlantation() {
        return lbsOrangePlantation;
    }

    public void setLbsOrangePlantation(String lbsOrangePlantation) {
        this.lbsOrangePlantation = lbsOrangePlantation;
    }

    public String getLbsAppleOrchard() {
        return lbsAppleOrchard;
    }

    public void setLbsAppleOrchard(String lbsAppleOrchard) {
        this.lbsAppleOrchard = lbsAppleOrchard;
    }

    public String getLbsVineyardField() {
        return lbsVineyardField;
    }

    public void setLbsVineyardField(String lbsVineyardField) {
        this.lbsVineyardField = lbsVineyardField;
    }

    public String getLbsOliveGrove() {
        return lbsOliveGrove;
    }

    public void setLbsOliveGrove(String lbsOliveGrove) {
        this.lbsOliveGrove = lbsOliveGrove;
    }

    public String getLbsDatePalmGrove() {
        return lbsDatePalmGrove;
    }

    public void setLbsDatePalmGrove(String lbsDatePalmGrove) {
        this.lbsDatePalmGrove = lbsDatePalmGrove;
    }

    public String getLbsCoconutPalmGrove() {
        return lbsCoconutPalmGrove;
    }

    public void setLbsCoconutPalmGrove(String lbsCoconutPalmGrove) {
        this.lbsCoconutPalmGrove = lbsCoconutPalmGrove;
    }

    public String getLbsPineForest() {
        return lbsPineForest;
    }

    public void setLbsPineForest(String lbsPineForest) {
        this.lbsPineForest = lbsPineForest;
    }

    public String getLbsOakForest() {
        return lbsOakForest;
    }

    public void setLbsOakForest(String lbsOakForest) {
        this.lbsOakForest = lbsOakForest;
    }

    public String getLbsBambooForest() {
        return lbsBambooForest;
    }

    public void setLbsBambooForest(String lbsBambooForest) {
        this.lbsBambooForest = lbsBambooForest;
    }

    public String getLbsPalmForest() {
        return lbsPalmForest;
    }

    public void setLbsPalmForest(String lbsPalmForest) {
        this.lbsPalmForest = lbsPalmForest;
    }

    public String getLbsRainforest() {
        return lbsRainforest;
    }

    public void setLbsRainforest(String lbsRainforest) {
        this.lbsRainforest = lbsRainforest;
    }

    public String getLbsCloudForest() {
        return lbsCloudForest;
    }

    public void setLbsCloudForest(String lbsCloudForest) {
        this.lbsCloudForest = lbsCloudForest;
    }

    public String getLbsMangroveForest() {
        return lbsMangroveForest;
    }

    public void setLbsMangroveForest(String lbsMangroveForest) {
        this.lbsMangroveForest = lbsMangroveForest;
    }

    public String getLbsConiferousForest() {
        return lbsConiferousForest;
    }

    public void setLbsConiferousForest(String lbsConiferousForest) {
        this.lbsConiferousForest = lbsConiferousForest;
    }

    public String getLbsDeciduousForest() {
        return lbsDeciduousForest;
    }

    public void setLbsDeciduousForest(String lbsDeciduousForest) {
        this.lbsDeciduousForest = lbsDeciduousForest;
    }

    public String getLbsMixedForest() {
        return lbsMixedForest;
    }

    public void setLbsMixedForest(String lbsMixedForest) {
        this.lbsMixedForest = lbsMixedForest;
    }

    public String getLbsScrubland() {
        return lbsScrubland;
    }

    public void setLbsScrubland(String lbsScrubland) {
        this.lbsScrubland = lbsScrubland;
    }

    public String getLbsGrassland() {
        return lbsGrassland;
    }

    public void setLbsGrassland(String lbsGrassland) {
        this.lbsGrassland = lbsGrassland;
    }

    public String getLbsSavanna() {
        return lbsSavanna;
    }

    public void setLbsSavanna(String lbsSavanna) {
        this.lbsSavanna = lbsSavanna;
    }

    public String getLbsSteppe() {
        return lbsSteppe;
    }

    public void setLbsSteppe(String lbsSteppe) {
        this.lbsSteppe = lbsSteppe;
    }

    public String getLbsTundra() {
        return lbsTundra;
    }

    public void setLbsTundra(String lbsTundra) {
        this.lbsTundra = lbsTundra;
    }

    public String getLbsTaiga() {
        return lbsTaiga;
    }

    public void setLbsTaiga(String lbsTaiga) {
        this.lbsTaiga = lbsTaiga;
    }

    public String getLbsBorealForest() {
        return lbsBorealForest;
    }

    public void setLbsBorealForest(String lbsBorealForest) {
        this.lbsBorealForest = lbsBorealForest;
    }

    public String getLbsTemperateForest() {
        return lbsTemperateForest;
    }

    public void setLbsTemperateForest(String lbsTemperateForest) {
        this.lbsTemperateForest = lbsTemperateForest;
    }

    public String getLbsTropicalForest() {
        return lbsTropicalForest;
    }

    public void setLbsTropicalForest(String lbsTropicalForest) {
        this.lbsTropicalForest = lbsTropicalForest;
    }

    public String getLbsSubtropicalForest() {
        return lbsSubtropicalForest;
    }

    public void setLbsSubtropicalForest(String lbsSubtropicalForest) {
        this.lbsSubtropicalForest = lbsSubtropicalForest;
    }

    public String getLbsMediterraneanForest() {
        return lbsMediterraneanForest;
    }

    public void setLbsMediterraneanForest(String lbsMediterraneanForest) {
        this.lbsMediterraneanForest = lbsMediterraneanForest;
    }

    public String getLbsMontaneForest() {
        return lbsMontaneForest;
    }

    public void setLbsMontaneForest(String lbsMontaneForest) {
        this.lbsMontaneForest = lbsMontaneForest;
    }

    public String getLbsAlpineForest() {
        return lbsAlpineForest;
    }

    public void setLbsAlpineForest(String lbsAlpineForest) {
        this.lbsAlpineForest = lbsAlpineForest;
    }

    public String getLbsSubalpineForest() {
        return lbsSubalpineForest;
    }

    public void setLbsSubalpineForest(String lbsSubalpineForest) {
        this.lbsSubalpineForest = lbsSubalpineForest;
    }

    public String getLbsTreelineForest() {
        return lbsTreelineForest;
    }

    public void setLbsTreelineForest(String lbsTreelineForest) {
        this.lbsTreelineForest = lbsTreelineForest;
    }

    public String getLbsTimberlineForest() {
        return lbsTimberlineForest;
    }

    public void setLbsTimberlineForest(String lbsTimberlineForest) {
        this.lbsTimberlineForest = lbsTimberlineForest;
    }

    public String getLbsSnowlineForest() {
        return lbsSnowlineForest;
    }

    public void setLbsSnowlineForest(String lbsSnowlineForest) {
        this.lbsSnowlineForest = lbsSnowlineForest;
    }

    public String getLbsIceCap() {
        return lbsIceCap;
    }

    public void setLbsIceCap(String lbsIceCap) {
        this.lbsIceCap = lbsIceCap;
    }

    public String getLbsIceSheet() {
        return lbsIceSheet;
    }

    public void setLbsIceSheet(String lbsIceSheet) {
        this.lbsIceSheet = lbsIceSheet;
    }

    public String getLbsIceShelf() {
        return lbsIceShelf;
    }

    public void setLbsIceShelf(String lbsIceShelf) {
        this.lbsIceShelf = lbsIceShelf;
    }

    public String getLbsIceberg() {
        return lbsIceberg;
    }

    public void setLbsIceberg(String lbsIceberg) {
        this.lbsIceberg = lbsIceberg;
    }

    public String getLbsGlacierField() {
        return lbsGlacierField;
    }

    public void setLbsGlacierField(String lbsGlacierField) {
        this.lbsGlacierField = lbsGlacierField;
    }

    public String getLbsPermafrost() {
        return lbsPermafrost;
    }

    public void setLbsPermafrost(String lbsPermafrost) {
        this.lbsPermafrost = lbsPermafrost;
    }

    public String getLbsTundraField() {
        return lbsTundraField;
    }

    public void setLbsTundraField(String lbsTundraField) {
        this.lbsTundraField = lbsTundraField;
    }

    public String getLbsArcticTundra() {
        return lbsArcticTundra;
    }

    public void setLbsArcticTundra(String lbsArcticTundra) {
        this.lbsArcticTundra = lbsArcticTundra;
    }

    public String getLbsAntarcticTundra() {
        return lbsAntarcticTundra;
    }

    public void setLbsAntarcticTundra(String lbsAntarcticTundra) {
        this.lbsAntarcticTundra = lbsAntarcticTundra;
    }

    public String getLbsAlpineTundra() {
        return lbsAlpineTundra;
    }

    public void setLbsAlpineTundra(String lbsAlpineTundra) {
        this.lbsAlpineTundra = lbsAlpineTundra;
    }

    public String getLbsPolarDesert() {
        return lbsPolarDesert;
    }

    public void setLbsPolarDesert(String lbsPolarDesert) {
        this.lbsPolarDesert = lbsPolarDesert;
    }

    public String getLbsColdDesert() {
        return lbsColdDesert;
    }

    public void setLbsColdDesert(String lbsColdDesert) {
        this.lbsColdDesert = lbsColdDesert;
    }

    public String getLbsHotDesert() {
        return lbsHotDesert;
    }

    public void setLbsHotDesert(String lbsHotDesert) {
        this.lbsHotDesert = lbsHotDesert;
    }

    public String getLbsTemperateDesert() {
        return lbsTemperateDesert;
    }

    public void setLbsTemperateDesert(String lbsTemperateDesert) {
        this.lbsTemperateDesert = lbsTemperateDesert;
    }

    public String getLbsCoastalDesert() {
        return lbsCoastalDesert;
    }

    public void setLbsCoastalDesert(String lbsCoastalDesert) {
        this.lbsCoastalDesert = lbsCoastalDesert;
    }

    public String getLbsSemiaridDesert() {
        return lbsSemiaridDesert;
    }

    public void setLbsSemiaridDesert(String lbsSemiaridDesert) {
        this.lbsSemiaridDesert = lbsSemiaridDesert;
    }

    public String getLbsAridDesert() {
        return lbsAridDesert;
    }

    public void setLbsAridDesert(String lbsAridDesert) {
        this.lbsAridDesert = lbsAridDesert;
    }

    public String getLbsHyperaridDesert() {
        return lbsHyperaridDesert;
    }

    public void setLbsHyperaridDesert(String lbsHyperaridDesert) {
        this.lbsHyperaridDesert = lbsHyperaridDesert;
    }
}