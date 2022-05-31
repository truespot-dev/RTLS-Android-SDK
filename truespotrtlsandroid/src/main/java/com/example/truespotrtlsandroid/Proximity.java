package com.example.truespotrtlsandroid;

import android.os.Parcel;
import android.os.Parcelable;

public enum Proximity implements Parcelable {

    UNKNOWN(-1),
    IMMEDIATE(1),
    NEAR(2),
    FAR(3);

    private int mProximity;
    public static final Creator<Proximity> CREATOR = new Creator<Proximity>() {
        public Proximity createFromParcel(Parcel source) {
            return Proximity.values()[source.readInt()];
        }

        public Proximity[] newArray(int size) {
            return new Proximity[size];
        }
    };

    private Proximity(int val) {
        this.mProximity = val;
    }

    public int getInt() {
        return this.mProximity;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ordinal());
    }
}
