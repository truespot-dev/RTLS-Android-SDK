package com.example.truespotrtlsandroid;


import com.example.truespotrtlsandroid.models.BaseJSONModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/*@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)*/
public class CLLocation  {

    public Coordinate coordinate;
    public String horizontalAccuracy;

    public CLLocation(Coordinate mCoordinate,String mHorizontalAccuracy)
    {
        coordinate = mCoordinate;
        horizontalAccuracy = mHorizontalAccuracy;
    }
   /* @JsonProperty("coordinate")
    public Coordinate coordinate;

    @JsonProperty("horizontalAccuracy")
    public String horizontalAccuracy;*/

}
