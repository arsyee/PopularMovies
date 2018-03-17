package hu.fallen.popularmovies.adapters;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings({"WeakerAccess", "unused", "CanBeFinal"})
// public access required for Data Binding and GSON
public class TrailerInfo implements Parcelable {
    public String id;
    public String key;
    public String name;

    protected TrailerInfo(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
    }

    public static final Creator<TrailerInfo> CREATOR = new Creator<TrailerInfo>() {
        @Override
        public TrailerInfo createFromParcel(Parcel in) {
            return new TrailerInfo(in);
        }

        @Override
        public TrailerInfo[] newArray(int size) {
            return new TrailerInfo[size];
        }
    };

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(name);
    }
}
