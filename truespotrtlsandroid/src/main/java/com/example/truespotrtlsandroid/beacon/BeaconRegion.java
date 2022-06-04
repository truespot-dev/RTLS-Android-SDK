package com.example.truespotrtlsandroid.beacon;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;

import java.util.UUID;

public class BeaconRegion implements  Parcelable {

    private final String identifier;
    private final UUID proximityUUID;
    private final Integer major;
    private final Integer minor;

    public BeaconRegion(String identifier, UUID proximityUUID, Integer major, Integer minor) {
        this.identifier = Preconditions.checkNotNull(identifier);
       // Preconditions.checkArgument(!EstimoteBeacons.isSecureUUID(proximityUUID), "Invalid UUID (secure).");
        this.proximityUUID =  Preconditions.checkNotNull(proximityUUID);
        this.major = major;
        this.minor = minor;
    }


    public String getIdentifier() {
        return identifier;
    }

    public UUID getProximityUUID() {
        return proximityUUID;
    }


    public Integer getMajor() {
        return major;
    }


    public Integer getMinor() {
        return minor;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("identifier", identifier)
                .add("proximityUUID", proximityUUID)
                .add("major", major)
                .add("minor", minor)
                .add("secure", this instanceof SecureBeaconRegion)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ((Object) this).getClass() != o.getClass()) return false;

        BeaconRegion beaconRegion = (BeaconRegion) o;

        if (major != null ? !major.equals(beaconRegion.major) : beaconRegion.major != null) return false;
        if (minor != null ? !minor.equals(beaconRegion.minor) : beaconRegion.minor != null) return false;
        if (proximityUUID != null ? !proximityUUID.equals(beaconRegion.proximityUUID) : beaconRegion.proximityUUID != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = proximityUUID != null ? proximityUUID.hashCode() : 0;
        result = 31 * result + (major != null ? major.hashCode() : 0);
        result = 31 * result + (minor != null ? minor.hashCode() : 0);
        return result;
    }

    // Parcelable

    public static final Creator<BeaconRegion> CREATOR = new Creator<BeaconRegion>() {
        @Override
        public BeaconRegion createFromParcel(Parcel source) {
            return new BeaconRegion(source);
        }

        @Override
        public BeaconRegion[] newArray(int size) {
            return new BeaconRegion[size];
        }
    };

    private BeaconRegion(Parcel parcel) {
        this.identifier = parcel.readString();
        this.proximityUUID = (UUID) parcel.readValue(UUID.class.getClassLoader());
        Integer majorTemp = parcel.readInt();
        if (majorTemp == -1) {
            majorTemp = null;
        }
        this.major = majorTemp;
        Integer minorTemp = parcel.readInt();
        if (minorTemp == -1) {
            minorTemp = null;
        }
        this.minor = minorTemp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(identifier);
        dest.writeValue(proximityUUID);
        dest.writeInt(major == null ? -1 : major);
        dest.writeInt(minor == null ? -1 : minor);
    }
}
