package com.example.truespotrtlsandroid.data;

import com.example.truespotrtlsandroid.models.BaseJSONModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarsNearMeRequestBody extends BaseJSONModel {
    @JsonProperty("beaconid")
    public String beaconId;
    @JsonProperty("rssi")
    private int rssi;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("minor")
    private int minor;

    public CarsNearMeRequestBody(String beaconId, int rssi, String uuid, int minor) {
        this.beaconId = beaconId;
        this.rssi = rssi;
        this.uuid = uuid;
        this.minor = minor;
    }

    public CarsNearMeRequestBody(int rssi, String uuid, int minor) {
        this.rssi = rssi;
        this.uuid = uuid;
        this.minor = minor;
    }
}
