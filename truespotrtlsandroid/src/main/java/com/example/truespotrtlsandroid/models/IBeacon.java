package com.example.truespotrtlsandroid.models;

import android.os.Parcel;
import android.os.Parcelable;

public class IBeacon extends Beacon implements Parcelable {
    public static final String IDENTIFIER;
    private int mMajor;
    private int mMinor;
    private String mUuid;
    public static final Creator<IBeacon> CREATOR;

    public IBeacon() {
        this.setBeaconType(BeaconType.I_BEACON);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
    }

    public IBeacon(Parcel in) {
        super(in);
        this.parseData();
    }

    public IBeacon(Beacon beacon) {
        this.setBaseData(beacon);
        this.parseData();
    }

    private void parseData() {
        byte[] scanRecord = this.getScanRecord();
        int startIndex = 0;

        for (int i = 0; i < scanRecord.length; ++i) {
            if ((scanRecord[i] & 255) == 2 && (scanRecord[i + 1] & 255) == 1) {
                startIndex = i;
                break;
            }
        }

        this.mMajor = (scanRecord[startIndex + 25] & 255) << 8 | scanRecord[startIndex + 26] & 255;
        this.mMinor = (scanRecord[startIndex + 27] & 255) << 8 | scanRecord[startIndex + 28] & 255;
        this.mTxPower = scanRecord[startIndex + 29] & 255;
        byte[] uuidBytes = new byte[16];
        System.arraycopy(scanRecord, startIndex + 9, uuidBytes, 0, 16);
        this.mUuid = bytesToHex(uuidBytes);
    }

    public int getMajor() {
        return this.mMajor;
    }

    public int getMinor() {
        return this.mMinor;
    }

    public String getUuid() {
        return this.mUuid;
    }

    public Range calculateRange() {
        Range range = this.calculateAccuracy(this.mTxPower, (double) this.getRssi());
        if (this.mRange != null && !this.mRange.equals(range)) {
            this.notifyOnRangeChanged(this, range);
        }

        this.mRange = range;
        return range;
    }

    private Range calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0.0D) {
            return Range.UNKNOWN;
        } else {
            double ratio = rssi * 1.0D / (double) txPower;
            return ratio < 1.0D ? Range.NEAR : Range.FAR;
        }
    }

    static {
        IDENTIFIER = BeaconType.I_BEACON.getStringType();
        CREATOR = new Creator<IBeacon>() {
            public IBeacon createFromParcel(Parcel in) {
                return new IBeacon(in);
            }

            public IBeacon[] newArray(int size) {
                return new IBeacon[size];
            }
        };
    }
}
