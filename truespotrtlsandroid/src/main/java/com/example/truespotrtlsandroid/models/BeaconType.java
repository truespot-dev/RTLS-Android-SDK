package com.example.truespotrtlsandroid.models;

import android.os.Parcel;
import android.os.Parcelable;

public enum BeaconType implements Parcelable {
    I_BEACON("0201061aff4c000215"),
    EDDYSTONE("AAFE"),
    EDDYSTONE_TLM("0201060303AAFE1116AAFE20"),
    EDDYSTONE_UID("0201060303AAFE1716AAFE00");

    private String mType;
    public static final Creator<BeaconType> CREATOR = new Creator<BeaconType>() {
        public BeaconType createFromParcel(Parcel source) {
            return BeaconType.values()[source.readInt()];
        }

        public BeaconType[] newArray(int size) {
            return new BeaconType[size];
        }
    };

    private BeaconType(String str) {
        this.mType = str;
    }

    public String getStringType() {
        return this.mType;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ordinal());
    }
}
