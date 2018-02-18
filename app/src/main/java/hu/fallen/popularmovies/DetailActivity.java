package hu.fallen.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;

import hu.fallen.popularmovies.databinding.ActivityDetailBinding;
import hu.fallen.popularmovies.utilities.BitmapLruCache;
import hu.fallen.popularmovies.utilities.MovieDetails;
import hu.fallen.popularmovies.utilities.SharedConstants;
import hu.fallen.popularmovies.utilities.TrailerAdapter;
import hu.fallen.popularmovies.utilities.TrailerInfo;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private String api_key = null;

    private TrailerAdapter trailerAdapter;

    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        ImageLoader mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(BitmapLruCache.getCacheSize(this)));

        Intent incomingIntent = getIntent();
        if (incomingIntent == null || !incomingIntent.hasExtra("MovieDetails")) {
            Toast.makeText(this, getString(R.string.movie_not_found), Toast.LENGTH_LONG).show();
            finish();
            return; // if intent has no extra, I have no information at all on what movie this is
        }
        MovieDetails movieDetails = incomingIntent.getParcelableExtra("MovieDetails");
        binding.setMovieDetails(movieDetails);
        binding.ivPoster.setImageUrl(SharedConstants.BASE_POSTER_URL + "/w185" + movieDetails.poster_path, mImageLoader);
        binding.ivPoster.setContentDescription(getString(R.string.poster_description, movieDetails.original_title));

        if (api_key == null) {
            api_key = "api_key=" + getString(R.string.tmdb_api_key);
        }
        String movieUrl = SharedConstants.BASE_URL + "movie/" + Integer.toString(movieDetails.id) + "?" + api_key;
        Log.d(TAG, movieUrl);
        StringRequest movieRequest = new StringRequest(Request.Method.GET, movieUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        JsonElement runtimeElement = gson.fromJson(response, JsonObject.class).get("runtime");
                        Integer runtime = gson.fromJson(runtimeElement, Integer.class);
                        binding.tvRuntime.setText(getString(R.string.min_duration, runtime));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }
        );
        // overriding RetryPolicy to fix Bad Gateway (HTTP 502) errors; runtime loads very slow, though
        movieRequest.setRetryPolicy(new DefaultRetryPolicy(1000, 3, 2));
        mRequestQueue.add(movieRequest);

        trailerAdapter = new TrailerAdapter(DetailActivity.this);
        final RecyclerView trailers = binding.rvTrailers;
        trailers.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
        trailers.setAdapter(trailerAdapter);

        String trailerUrl = SharedConstants.BASE_URL + "movie/" + Integer.toString(movieDetails.id) + "/videos" + "?" + api_key;
        Log.d(TAG, trailerUrl);
        StringRequest trailerRequest = new StringRequest(Request.Method.GET, trailerUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        JsonElement resultArray = gson.fromJson(response, JsonObject.class).get("results");
                        List<TrailerInfo> trailerInfos = Arrays.asList(gson.fromJson(resultArray, TrailerInfo[].class));
                        trailerAdapter.setTrailerUrls(trailerInfos);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }
        );
        trailerRequest.setRetryPolicy(new DefaultRetryPolicy(1000, 3, 2));
        mRequestQueue.add(trailerRequest);
    }

}
