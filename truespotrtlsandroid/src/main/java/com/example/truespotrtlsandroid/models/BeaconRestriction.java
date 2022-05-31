package com.example.truespotrtlsandroid.models;

public  enum BeaconRestriction {
    EXCLUSIVE(0),
    INCLUSIVE(1),
    LESS_THAN(2),
    GREATER_THAN(3),
    EQUAL_TO(4),
    RANGE_IGNORE_RESTRICTION(-1000);

    private int mType;

    private BeaconRestriction(int value) {
        this.mType = value;
    }

    public int getIntType() {
        return this.mType;
    }
}