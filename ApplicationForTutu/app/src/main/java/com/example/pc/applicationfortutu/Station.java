package com.example.pc.applicationfortutu;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Station {

    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String countryTitle;

    @DatabaseField
    private String longitude;

    @DatabaseField
    private String latitude;

    @DatabaseField
    private String districtTitle;

    @DatabaseField
    private String cityId;

    @DatabaseField
    private String cityTitle;

    @DatabaseField
    private String regionTitle;

    @DatabaseField
    private String stationId;

    @DatabaseField
    private String stationTitle;

    @DatabaseField
    private String stationTitleLow;

    @DatabaseField
    private String type;                                                                            //Нужно отличать From и To

    public long getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCountryTitle() {
        return countryTitle;
    }

    public void setCountryTitle(String countryTitle) {
        this.countryTitle = countryTitle;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDistrictTitle() {
        return districtTitle;
    }

    public void setDistrictTitle(String districtTitle) {
        this.districtTitle = districtTitle;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityTitle() {
        return cityTitle;
    }

    public void setCityTitle(String cityTitle) {
        this.cityTitle = cityTitle;
    }

    public String getRegionTitle() {
        return regionTitle;
    }

    public void setRegionTitle(String regionTitle) {
        this.regionTitle = regionTitle;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationTitle() {
        return stationTitle;
    }

    public void setStationTitle(String stationTitle) {
        this.stationTitle = stationTitle;
    }

    public String getStationTitleLow() {
        return stationTitleLow;
    }

    public void setStationTitleLow(String stationTitleLow) {
        this.stationTitleLow = stationTitleLow;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Station() {
    }

    public Station(int id, String countryTitle, String longitude, String latitude, String districtTitle, String cityId, String cityTitle, String regionTitle, String stationId, String stationTitle, String stationTitleLow, String type) {
        Id = id;
        this.countryTitle = countryTitle;
        this.longitude = longitude;
        this.latitude = latitude;
        this.districtTitle = districtTitle;
        this.cityId = cityId;
        this.cityTitle = cityTitle;
        this.regionTitle = regionTitle;
        this.stationId = stationId;
        this.stationTitle = stationTitle;
        this.stationTitleLow = stationTitleLow;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Station{" +
                "Id=" + Id +
                ", countryTitle='" + countryTitle + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", districtTitle='" + districtTitle + '\'' +
                ", cityId='" + cityId + '\'' +
                ", cityTitle='" + cityTitle + '\'' +
                ", regionTitle='" + regionTitle + '\'' +
                ", stationId='" + stationId + '\'' +
                ", stationTitle='" + stationTitle + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
