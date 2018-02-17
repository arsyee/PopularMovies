package hu.fallen.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

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

import hu.fallen.popularmovies.utilities.BitmapLruCache;
import hu.fallen.popularmovies.utilities.ImageAdapter;
import hu.fallen.popularmovies.utilities.MovieDetails;
import hu.fallen.popularmovies.utilities.SharedConstants;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String POPULAR = "movie/popular";
    private static final String TOP_RATED = "movie/top_rated";
    private String api_key = null;

    private RequestQueue mRequestQueue;
    private ImageAdapter adapter;

    private final Gson gson = new Gson();
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRequestQueue = Volley.newRequestQueue(this);
        ImageLoader mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(BitmapLruCache.getCacheSize(this)));

        api_key = "api_key=" + getString(R.string.tmdb_api_key);

        final GridView movies = findViewById(R.id.gr_movies);
        adapter = new ImageAdapter(this, mImageLoader);
        movies.setAdapter(adapter);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);

        refreshMovies();
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        refreshMovies();
    }

    private void refreshMovies() {
        String sort_by = POPULAR;
        switch (preferences.getString("sort_by", "popular")) {
            case "top_rated":
                sort_by = TOP_RATED;
                break;
        }
        String url = SharedConstants.BASE_URL + sort_by + "?" + api_key;
        Log.d(TAG, url);

        mRequestQueue.add(new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        JsonElement resultArray = gson.fromJson(response, JsonObject.class).get("results");
                        List<MovieDetails> movieList = Arrays.asList(gson.fromJson(resultArray, MovieDetails[].class));
                        adapter.setMovieList(movieList);
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }
        ));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
