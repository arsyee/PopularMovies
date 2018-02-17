package hu.fallen.popularmovies;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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


import java.util.Locale;

import hu.fallen.popularmovies.databinding.ActivityDetailBinding;
import hu.fallen.popularmovies.utilities.BitmapLruCache;
import hu.fallen.popularmovies.utilities.MovieDetails;
import hu.fallen.popularmovies.utilities.SharedConstants;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private String api_key = null;

    RequestQueue mRequestQueue = null;
    ImageLoader mImageLoader = null;

    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        mRequestQueue = Volley.newRequestQueue(this);
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(BitmapLruCache.getCacheSize(this)));

        MovieDetails movieDetails = getIntent().getParcelableExtra("MovieDetails");
        binding.setMovieDetails(movieDetails);
        binding.ivPoster.setImageUrl(SharedConstants.BASE_POSTER_URL + "/w185" + movieDetails.poster_path, mImageLoader);

        if (api_key == null) {
            api_key = "api_key=" + getString(R.string.tmdb_api_key);
        }
        String url = SharedConstants.BASE_URL + "movie/" + Integer.toString(movieDetails.id) + "?" + api_key;
        Log.d(TAG, url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
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
        request.setRetryPolicy(new DefaultRetryPolicy(1000, 3, 2));
        mRequestQueue.add(request);
    }
}
