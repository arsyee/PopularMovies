package hu.fallen.popularmovies.adapters;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings({"WeakerAccess", "unused"}) // public access required for Data Binding and GSON
public class ReviewInfo implements Parcelable {
    public String id;
    public String author;
    public String content;

    protected ReviewInfo(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<ReviewInfo> CREATOR = new Creator<ReviewInfo>() {
        @Override
        public ReviewInfo createFromParcel(Parcel in) {
            return new ReviewInfo(in);
        }

        @Override
        public ReviewInfo[] newArray(int size) {
            return new ReviewInfo[size];
        }
    };

    @Override
    public String toString() {
        return author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
    }
}
