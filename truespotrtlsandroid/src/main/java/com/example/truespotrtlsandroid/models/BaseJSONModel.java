package com.example.truespotrtlsandroid.models;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseJSONModel {
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
