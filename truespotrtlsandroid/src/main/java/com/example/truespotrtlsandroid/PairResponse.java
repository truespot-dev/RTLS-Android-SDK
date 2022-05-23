package com.example.truespotrtlsandroid;

import com.example.truespotrtlsandroid.models.BaseJSONModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PairResponse extends BaseJSONModel {

    @SerializedName("code")
    public PairCode code;
    @SerializedName("message")
    public String message;
    @SerializedName("overwritable")
    public boolean overwritable;
    @SerializedName("vehicleId")
    public String vehicleId;
    @SerializedName("success")
    public boolean success;
    @SerializedName("previousVehicleId")
    public String previousVehicleId;

}
