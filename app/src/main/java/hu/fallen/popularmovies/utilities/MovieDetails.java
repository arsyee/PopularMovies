package hu.fallen.popularmovies.utilities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieDetails implements Parcelable {
    private static final String TAG = MovieDetails.class.getSimpleName();

    String poster_path;
    String original_title;

    private MovieDetails() {}

    private MovieDetails(Parcel in) {
        poster_path = in.readString();
        original_title = in.readString();
    }

    @Override
    public String toString() {
        return original_title;
    }

    public static List<MovieDetails> buildList(JSONObject response) {
        List<MovieDetails> result = new ArrayList<>();
        try {
            JSONArray resultArray = response.getJSONArray("results");
            for (int i = 0; i < resultArray.length(); ++i) {
                JSONObject object = resultArray.getJSONObject(i);
                MovieDetails movieDetails = new MovieDetails();
                movieDetails.original_title = object.getString("original_title");
                movieDetails.poster_path = object.getString("poster_path");
                Log.d(TAG, "Movie found: " + movieDetails);
                result.add(movieDetails);
            }
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(poster_path);
        parcel.writeString(original_title);
    }

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
