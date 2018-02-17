package hu.fallen.popularmovies.utilities;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieDetails implements Parcelable {
    public final String poster_path;
    public final String overview;
    private final String release_date;
    public final int id;
    public final String original_title;
    private final double vote_average;

    public String getReleaseYear() {
        return release_date.substring(0, 4);
    }

    public String getRating() {
        return Double.toString(vote_average) + "/10";
    }

    // Parcelable implementation

    private MovieDetails(Parcel in) {
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        id = in.readInt();
        original_title = in.readString();
        vote_average = in.readDouble();
    }

    @Override
    public String toString() {
        return original_title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(poster_path);
        parcel.writeString(overview);
        parcel.writeString(release_date);
        parcel.writeInt(id);
        parcel.writeString(original_title);
        parcel.writeDouble(vote_average);
    }

    @SuppressWarnings("unused") // required by Parcelable interface
    public static final Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel in) {
            return new MovieDetails(in);
        }

        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };

}
