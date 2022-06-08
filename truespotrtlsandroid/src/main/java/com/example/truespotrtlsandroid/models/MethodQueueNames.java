package com.example.truespotrtlsandroid.models;

import android.os.Parcel;
import android.os.Parcelable;

public enum  MethodQueueNames implements Parcelable {
    START_SCAN(1),
    STOP_SCAN(2),
    UPDATE_SCAN_TYPE_RULES(3),
    UPDATE_SCAN_RANGE_RULES(4),
    UPDATE_IBCN_UUID_RULES(5),
    UPDATE_RANGE_RULES(6),
    START_SCAN_WITH_PERIOD(7);

    private int mMethodName;
    public static final Creator<MethodQueueNames> CREATOR = new Creator<MethodQueueNames>() {
        public MethodQueueNames createFromParcel(Parcel source) {
            return MethodQueueNames.values()[source.readInt()];
        }

        public MethodQueueNames[] newArray(int size) {
            return new MethodQueueNames[size];
        }
    };

    private MethodQueueNames(int val) {
        this.mMethodName = val;
    }

    public int getInt() {
        return this.mMethodName;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ordinal());
    }
}
