package com.example.truespotrtlsandroid.models;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.truespotrtlsandroid.listeners.OnBeaconChangeListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Beacon implements Parcelable {

    protected static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private final long ON_EXIT_LAST_SEEN_TOLERANCE = 5L;
    private final long ON_EXIT_INTERVAL = 5000L;
    protected int mRssi;
    protected byte[] mScanRecord;
    protected int mTxPower;
    protected BeaconType mBeaconType;
    protected BluetoothDevice mDevice;
    protected long mFirstDiscovered;
    protected long mLastSeen;
    protected float mTemperature;
    protected float mBattery;
    protected Range mRange;
    private boolean notifiedOfOnEnter = false;
    private boolean notifiedOfOnExit = false;
    protected static final int FSPL_FREQ = 189;
    protected static final int FSPL_LIGHT = -148;
    protected static final int FREE_SPACE_PATH_LOSS_CONSTANT_FOR_BLE = 41;
    protected static final double NEAR_TO_MID_METERS = 0.5D;
    protected static final double MID_TO_FAR_METERS = 2.0D;
    private Runnable exitRunnable;
    private Handler exitHandler;
    protected ArrayList<OnBeaconChangeListener> mBeaconChangeListeners;
    public static final Parcelable.Creator<Beacon> CREATOR = new Parcelable.Creator<Beacon>() {
        public Beacon createFromParcel(Parcel in) {
            return new Beacon(in);
        }

        public Beacon[] newArray(int size) {
            return new Beacon[size];
        }
    };

    public Beacon() {

        class NamelessClass_1 implements Runnable {
            NamelessClass_1() {
            }

            public void run() {
                notifyOnExit();
                exitHandler.postDelayed(exitRunnable, 5000L);
            }
        }

        this.exitRunnable = new NamelessClass_1();
        this.mBeaconChangeListeners = new ArrayList();
        this.mFirstDiscovered = System.currentTimeMillis();
        this.mLastSeen = System.currentTimeMillis();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(this.mDevice, flags);
        parcel.writeInt(this.mRssi);
        parcel.writeInt(this.mScanRecord.length);
        parcel.writeByteArray(this.mScanRecord);
        parcel.writeInt(this.mTxPower);
        parcel.writeParcelable(this.mBeaconType, flags);
        parcel.writeFloat(this.mBattery);
        parcel.writeFloat(this.mTemperature);
        this.mLastSeen = System.currentTimeMillis();
    }

    public Beacon(Parcel in) {
        class NamelessClass_1 implements Runnable {
            NamelessClass_1() {
            }

            public void run() {
                notifyOnExit();
                exitHandler.postDelayed(exitRunnable, 5000L);
            }
        }

        this.exitRunnable = new NamelessClass_1();
        this.mBeaconChangeListeners = new ArrayList();
        this.mDevice = (BluetoothDevice) in.readParcelable((ClassLoader) null);
        this.mRssi = in.readInt();
        this.mScanRecord = new byte[in.readInt()];
        in.readByteArray(this.mScanRecord);
        this.mTxPower = in.readInt();
        this.mBeaconType = (BeaconType) in.readParcelable(BeaconType.class.getClassLoader());
        this.mBattery = in.readFloat();
        this.mTemperature = in.readFloat();
        this.mLastSeen = System.currentTimeMillis();
    }

    protected void setBaseData(Beacon beacon) {
        this.setRssi(beacon.getRssi());
        this.setScanRecord(beacon.getScanRecord());
        this.setDevice(beacon.getDevice());
        this.setBeaconType(beacon.getBeaconType());
    }

    public BluetoothDevice getDevice() {
        return this.mDevice;
    }

    public void setDevice(BluetoothDevice device) {
        this.mDevice = device;
    }

    public int getRssi() {
        return this.mRssi;
    }

    public void setRssi(int rssi) {
        this.mRssi = rssi;
        this.mLastSeen = System.currentTimeMillis();
        this.notifyChangeListener();
        this.notifyOnEnter();
    }

    private void notifyChangeListener() {
        Iterator var1 = this.mBeaconChangeListeners.iterator();

        while (var1.hasNext()) {
            OnBeaconChangeListener listener = (OnBeaconChangeListener) var1.next();
            listener.onRssiChanged(this, this.getRssi());
        }

    }

    public byte[] getScanRecord() {
        return this.mScanRecord;
    }

    public void setScanRecord(byte[] scanRecord) {
        this.mScanRecord = scanRecord;
    }

    public void updateSeen() {
        this.mLastSeen = System.currentTimeMillis();
    }

    public long getDwellTime() {
        return this.mLastSeen - this.mFirstDiscovered;
    }

    public long getFirstDiscovered() {
        return this.mFirstDiscovered;
    }

    public long getLastSeen() {
        return this.mLastSeen;
    }

    public BeaconType getBeaconType() {
        return this.mBeaconType;
    }

    public void setBeaconType(BeaconType beaconType) {
        this.mBeaconType = beaconType;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; ++j) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 15];
        }

        return new String(hexChars);
    }

    public void addOnBeaconChangeListener(OnBeaconChangeListener beaconChangeListener) {
        this.mBeaconChangeListeners.add(beaconChangeListener);
        if (this.exitHandler == null) {
            this.exitHandler = new Handler(Looper.getMainLooper());
            this.exitHandler.postDelayed(this.exitRunnable, 5000L);
        }

    }

    private void notifyOnExit() {
        if (this.mBeaconChangeListeners.size() > 0 && !this.hasBeenNotifiedOfOnExit()) {
            Date lastSeen = new Date(this.getLastSeen());
            Date now = new Date();
            long difference = (now.getTime() - lastSeen.getTime()) / 1000L;
            if (difference > 5L) {
                this.setNotifiedOfOnEnter(false);
                this.setNotifiedOfOnExit(true);
                Iterator var5 = this.mBeaconChangeListeners.iterator();

                while (var5.hasNext()) {
                    OnBeaconChangeListener beaconExitListener = (OnBeaconChangeListener) var5.next();
                    beaconExitListener.onBeaconExit(this);
                }
            }
        }

    }

    private void notifyOnEnter() {
        if (!this.hasBeenNotifiedOfOnEnter() && this.mBeaconChangeListeners.size() > 0) {
            this.setNotifiedOfOnEnter(true);
            this.setNotifiedOfOnExit(false);
            Iterator var1 = this.mBeaconChangeListeners.iterator();

            while (var1.hasNext()) {
                OnBeaconChangeListener listener = (OnBeaconChangeListener) var1.next();
                listener.onBeaconEnter(this);
            }
        }

    }

    public boolean hasBeenNotifiedOfOnEnter() {
        return this.notifiedOfOnEnter;
    }

    public void setNotifiedOfOnEnter(boolean notifiedOfOnEnter) {
        this.notifiedOfOnEnter = notifiedOfOnEnter;
    }

    public boolean hasBeenNotifiedOfOnExit() {
        return this.notifiedOfOnExit;
    }

    public void setNotifiedOfOnExit(boolean notifiedOfOnExit) {
        this.notifiedOfOnExit = notifiedOfOnExit;
    }

    public boolean hasExitListeners() {
        return this.mBeaconChangeListeners.size() > 0;
    }

    public Range getRange() {
        return this.mRange == null ? Range.UNKNOWN : this.mRange;
    }

    public Range calculateRange() {
        int pathLoss = this.mTxPower - this.getRssi();
        double distance = Math.pow(10.0D, (double) (pathLoss - 41) / 20.0D);
        Range range = Range.FAR;
        if (!(distance < 0.0D) && this.getRssi() != 127 && this.mTxPower != 127) {
            if (distance <= 0.5D) {
                range = Range.IMMEDIATE;
            } else if (distance <= 2.0D) {
                range = Range.NEAR;
            }
        } else {
            range = Range.UNKNOWN;
        }

        if (this.mRange != null && !this.mRange.equals(range)) {
            this.notifyOnRangeChanged(this, range);
        }

        this.mRange = range;
        return range;
    }

    protected void notifyOnRangeChanged(Beacon beacon, Range range) {
        Iterator var3 = this.mBeaconChangeListeners.iterator();

        while (var3.hasNext()) {
            OnBeaconChangeListener listener = (OnBeaconChangeListener) var3.next();
            listener.onRangeChanged(beacon, range);
        }

    }

    public float getTemperature() {
        return this.mTemperature;
    }

    public int getTxPower() {
        return this.mTxPower;
    }

}
