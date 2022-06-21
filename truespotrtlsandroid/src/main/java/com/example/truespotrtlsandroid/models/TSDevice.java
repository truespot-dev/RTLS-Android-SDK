package com.example.truespotrtlsandroid.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TSDevice extends BaseJSONModel {

    @SerializedName("tagIdentifier")
    public String tagIdentifier;
    @SerializedName("assetIdentifier")
    public String assetIdentifier;
    @SerializedName("assetIdentifierB")
    public String assetIdentifierB;
    @SerializedName("assetType")
    public String assetType;
    @SerializedName("batteryLevel")
    public int batteryLevel;
    @SerializedName("pairedOnTimestamp")
    public String pairedOnTimestamp;
    @SerializedName("pairedByUsername")
    public String pairedByUsername;
    @SerializedName("locationUpdateTimestamp")
    public String locationUpdateTimestamp;
    @SerializedName("latitude")
    public double latitude;
    @SerializedName("longitude")
    public double longitude;
    @SerializedName("level")
    public int level;
    @SerializedName("accuracy")
    public double accuracy;
    @SerializedName("subGeoZone")
    public String subGeoZone;
    @SerializedName("zoneId")
    public String zoneId;
    @SerializedName("zoneName")
    public String zoneName;
    @SerializedName("placeId")
    public String placeId;
    @SerializedName("placeName")
    public String placeName;
    @SerializedName("placeCategoryId")
    public String placeCategoryId;
    @SerializedName("placeCategoryName")
    public String placeCategoryName;
    @SerializedName("localeId")
    public String localeId;
    @SerializedName("localeName")
    public String localeName;
    @SerializedName("uuid")
    public String uuid;
    @SerializedName("major")
    public String major;
    @SerializedName("minor")
    public String minor;
    @SerializedName("sourceDeviceNames")
    public String[] sourceDeviceNames;
}
