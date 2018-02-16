package hu.fallen.popularmovies.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieDetails {
    private static final String TAG = MovieDetails.class.getSimpleName();

    String poster_path;
    String original_title;

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
}
