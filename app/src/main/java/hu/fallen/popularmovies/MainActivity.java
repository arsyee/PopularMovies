package hu.fallen.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

import hu.fallen.popularmovies.utilities.BitmapLruCache;
import hu.fallen.popularmovies.utilities.ImageAdapter;
import hu.fallen.popularmovies.utilities.MovieDetails;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String POPULAR = "movie/popular";
    @SuppressWarnings("FieldCanBeLocal")
    private String api_key = null;

    RequestQueue mRequestQueue = null;
    ImageLoader mImageLoader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRequestQueue = Volley.newRequestQueue(this);
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(BitmapLruCache.getCacheSize(this)));

        final GridView movies = (GridView) findViewById(R.id.gr_movies);
        api_key = "api_key=" + getString(R.string.tmdb_api_key);
        String url = BASE_URL + POPULAR + "?" + api_key;
        Log.d(TAG, url);

        final ImageAdapter adapter = new ImageAdapter(this, mImageLoader);
        movies.setAdapter(adapter);

        mRequestQueue.add(new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<MovieDetails> movieList = MovieDetails.buildList(response);
                        Log.d(TAG, response.toString());
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

}
