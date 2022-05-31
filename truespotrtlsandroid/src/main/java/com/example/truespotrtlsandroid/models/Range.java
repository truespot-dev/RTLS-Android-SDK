package com.example.truespotrtlsandroid.models;

import android.os.Parcel;
import android.os.Parcelable;

public enum Range implements Parcelable {

    UNKNOWN(-1),
    IMMEDIATE(1),
    NEAR(2),
    FAR(3);

    private int mRange;
    public static final Creator<Range> CREATOR = new Creator<Range>() {
        public Range createFromParcel(Parcel source) {
            return Range.values()[source.readInt()];
        }

        public Range[] newArray(int size) {
            return new Range[size];
        }
    };

    private Range(int val) {
        this.mRange = val;
    }

    public int getInt() {
        return this.mRange;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ordinal());
    }
}
