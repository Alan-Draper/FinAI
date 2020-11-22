package com.example.finai.objects;


public class HousePrice {

    //object for House Prices, used to read and write from the database

    private String houseEstimationId, userId, sqFtLiving, latitude, sqFtAbove, sqFtBasement, longitude, bedrooms, bathrooms, yearBuilt, floors, zipcode, sqFtLiving15, waterfront, grade, view, condition, price;

    //empty constructor needed for reading from database
    public HousePrice(){}

    public HousePrice(String houseEstimationId, String userId, String sqFtLiving, String latitude, String sqFtAbove, String sqFtBasement, String waterfront, String grade, String longitude, String view, String condition, String bedrooms, String bathrooms, String yearBuilt, String floors, String zipcode, String sqFtLiving15, String price) {
        this.houseEstimationId = houseEstimationId;
        this.userId = userId;
        this.sqFtLiving = sqFtLiving;
        this.latitude = latitude;
        this.sqFtAbove = sqFtAbove;
        this.sqFtBasement = sqFtBasement;
        this.waterfront = waterfront;
        this.grade = grade;
        this.longitude = longitude;
        this.view = view;
        this.condition = condition;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.yearBuilt = yearBuilt;
        this.floors = floors;
        this.zipcode = zipcode;
        this.sqFtLiving15 = sqFtLiving15;
        this.price = price;
    }

    public String getHouseEstimationId() {
        return houseEstimationId;
    }

    public void setHouseEstimationId(String houseEstimationId) {
        this.houseEstimationId = houseEstimationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSqFtLiving() {
        return sqFtLiving;
    }

    public void setSqFtLiving(String sqFtLiving) {
        this.sqFtLiving = sqFtLiving;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getSqFtAbove() {
        return sqFtAbove;
    }

    public void setSqFtAbove(String sqFtAbove) {
        this.sqFtAbove = sqFtAbove;
    }

    public String getSqFtBasement() {
        return sqFtBasement;
    }

    public void setSqFtBasement(String sqFtBasement) {
        this.sqFtBasement = sqFtBasement;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(String bedrooms) {
        this.bedrooms = bedrooms;
    }

    public String getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(String bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getYearBuilt() {
        return yearBuilt;
    }

    public void setYearBuilt(String yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

    public String getFloors() {
        return floors;
    }

    public void setFloors(String floors) {
        this.floors = floors;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getSqFtLiving15() {
        return sqFtLiving15;
    }

    public void setSqFtLiving15(String sqFtLiving15) {
        this.sqFtLiving15 = sqFtLiving15;
    }

    public String getWaterfront() {
        return waterfront;
    }

    public void setWaterfront(String waterfront) {
        this.waterfront = waterfront;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
