package com.example.truespotrtlsandroid;

import com.example.truespotrtlsandroid.models.BaseJSONModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Coordinate {

    public String latitude;
    public String longitude;

    public Coordinate(String mLatitude, String mLongitude) {
        latitude = mLatitude;
        longitude = mLongitude;
    }
}
