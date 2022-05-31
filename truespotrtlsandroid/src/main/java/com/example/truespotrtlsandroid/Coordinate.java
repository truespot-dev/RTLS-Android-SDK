package com.example.truespotrtlsandroid;

import com.example.truespotrtlsandroid.models.BaseJSONModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coordinate extends BaseJSONModel {
    @JsonProperty("latitude")
    public String  latitude;
    @JsonProperty("longitude")
    public String longitude;
}
