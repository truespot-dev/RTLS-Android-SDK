package com.example.truespotrtlsandroid.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Credentials extends BaseJSONModel{

    @JsonProperty("jwt")
    public static String jwt;

    @JsonProperty("clientSecret")
    public static String clientSecret;

    @JsonProperty("appInfo")
    public static TSApplication appInfo;

    @JsonProperty("tenantId")
    public static String tenantId;

}
