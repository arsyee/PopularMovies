package hu.fallen.popularmovies.utilities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

@SuppressWarnings("WeakerAccess") // public access required for Data Binding
public class MovieDetails implements Parcelable {
    private static final String TAG = MovieDetails.class.getSimpleName();
    public final String poster_path;
    public final String overview;
    public final String release_date;
    public final int id;
    public final String original_title;
    public final double vote_average;

    public String getReleaseYear() {
        return release_date.substring(0, 4);
    }

    public String getRating() {
        return Double.toString(vote_average) + "/10";
    }

    public MovieDetails(int id, String poster_path, String overview, String release_date, String original_title, double vote_average) {
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.id = id;
        this.original_title = original_title;
        this. vote_average = vote_average;
    }

    // Parcelable implementation

    private MovieDetails(Parcel in) {
        Log.d(TAG,String.format("MovieDetails will be created from parcel: %d %d", in.dataSize(), in.dataPosition()));
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        id = in.readInt();
        original_title = in.readString();
        vote_average = in.readDouble();
    }

    @Override
    public String toString() {
        return String.format("%d %f %s %s %s %s", id, vote_average, original_title, release_date, poster_path, overview);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Log.d(TAG, String.format("Writing to parcel: %d %d", parcel.dataSize(), parcel.dataPosition()));
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
