package com.example.truespotrtlsandroid;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.truespotrtlsandroid.models.BaseJSONModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TSApplicationContext extends BaseJSONModel {
    @SuppressLint("StaticFieldLeak")
    @JsonProperty("TSContext")
    public static Context TSContext;
}
