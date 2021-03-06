package hu.fallen.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.fallen.popularmovies.adapters.ReviewInfo;
import hu.fallen.popularmovies.adapters.TrailerAdapter;
import hu.fallen.popularmovies.database.FavoriteMoviesContract;
import hu.fallen.popularmovies.databinding.ActivityDetailBinding;
import hu.fallen.popularmovies.utilities.BitmapLruCache;
import hu.fallen.popularmovies.utilities.MovieDetails;
import hu.fallen.popularmovies.adapters.ReviewAdapter;
import hu.fallen.popularmovies.utilities.SharedConstants;
import hu.fallen.popularmovies.adapters.TrailerInfo;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private static final String ENDPOINT_VIDEOS = "videos";
    private static final String ENDPOINT_REVIEWS = "reviews";
    private String api_key = null;

    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    private final Gson gson = new Gson();
    private RequestQueue mRequestQueue;
    private ActivityDetailBinding mBinding;
    private MovieDetails mMovieDetails;

    private Toast detailToast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        mRequestQueue = Volley.newRequestQueue(this);
        ImageLoader mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(BitmapLruCache.getCacheSize(this)));

        Intent incomingIntent = getIntent();
        if (incomingIntent == null || !incomingIntent.hasExtra(SharedConstants.MOVIE_DETAILS_EXTRA)) {
            showToast(getString(R.string.movie_not_found));
            finish();
            return; // if intent has no extra, I have no information at all on what movie this is
        }
        mMovieDetails = incomingIntent.getParcelableExtra(SharedConstants.MOVIE_DETAILS_EXTRA);
        Log.d(TAG, String.format("setMovieDetails will be called on mBinding: %s", mMovieDetails));
        if (mMovieDetails == null) {
            Log.d(TAG, "mMovieDetails is null, will it finish?");
            showToast(getString(R.string.movie_not_found));
            finish();
            return; // if intent has no extra, I have no information at all on what movie this is
        }
        mBinding.setMovieDetails(mMovieDetails);

        mBinding.ivPoster.setImageUrl(SharedConstants.BASE_POSTER_URL + "/w185" + mMovieDetails.poster_path, mImageLoader);
        mBinding.ivPoster.setContentDescription(getString(R.string.poster_description, mMovieDetails.original_title));
        api_key = "api_key=" + getString(R.string.tmdb_api_key);
        updateRuntime();
        updateFavorite();

        trailerAdapter = new TrailerAdapter(DetailActivity.this);
        final RecyclerView trailers = mBinding.rvTrailers;
        trailers.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
        trailers.setAdapter(trailerAdapter);
        if (savedInstanceState != null && savedInstanceState.containsKey(ENDPOINT_VIDEOS)) {
            ArrayList<TrailerInfo> info = savedInstanceState.getParcelableArrayList(ENDPOINT_VIDEOS);
            trailerAdapter.setTrailerInfo(info);
        } else {
            updateList(ENDPOINT_VIDEOS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            JsonElement resultArray = gson.fromJson(response, JsonObject.class).get("results");
                            List<TrailerInfo> infoGSONIsTrickyAgain = Arrays.asList(gson.fromJson(resultArray, TrailerInfo[].class));
                            ArrayList<TrailerInfo> info = new ArrayList<>();
                            info.addAll(infoGSONIsTrickyAgain);
                            trailerAdapter.setTrailerInfo(info);
                            trailerAdapter.notifyDataSetChanged();
                        }
                    }
            );
        }

        reviewAdapter = new ReviewAdapter();
        final RecyclerView reviews = mBinding.rvReviews;
        reviews.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
        reviews.setAdapter(reviewAdapter);
        if (savedInstanceState != null && savedInstanceState.containsKey(ENDPOINT_REVIEWS)) {
            ArrayList<ReviewInfo> info = savedInstanceState.getParcelableArrayList(ENDPOINT_REVIEWS);
            reviewAdapter.setReviewInfo(info);
        } else {
            updateList(ENDPOINT_REVIEWS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            JsonElement resultArray = gson.fromJson(response, JsonObject.class).get("results");
                            List<ReviewInfo> infoGsonList = Arrays.asList(gson.fromJson(resultArray, ReviewInfo[].class));
                            ArrayList<ReviewInfo> info = new ArrayList<>();
                            info.addAll(infoGsonList);
                            reviewAdapter.setReviewInfo(info);
                            reviewAdapter.notifyDataSetChanged();
                        }
                    }
            );
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ENDPOINT_VIDEOS, trailerAdapter.getTrailerInfo());
        outState.putParcelableArrayList(ENDPOINT_REVIEWS, reviewAdapter.getReviewInfo());
    }

    private void updateList(final String endpoint, Response.Listener<String> responseListener) {
        MovieDetails movieDetails = mBinding.getMovieDetails();
        String url = SharedConstants.BASE_URL + "movie/" + Integer.toString(movieDetails.id) + "/" + endpoint + "?" + api_key;
        Log.d(TAG, url);
        StringRequest request = new StringRequest(Request.Method.GET, url, responseListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }
        );
        request.setRetryPolicy(new DefaultRetryPolicy(1000, 3, 2));
        mRequestQueue.add(request);
    }

    private void updateRuntime() {
        MovieDetails movieDetails = mBinding.getMovieDetails();
        String movieUrl = SharedConstants.BASE_URL_MOVIE + Integer.toString(movieDetails.id) + "?" + api_key;
        Log.d(TAG, movieUrl);
        StringRequest movieRequest = new StringRequest(Request.Method.GET, movieUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        JsonElement runtimeElement = gson.fromJson(response, JsonObject.class).get("runtime");
                        Integer runtime = gson.fromJson(runtimeElement, Integer.class);
                        mBinding.tvRuntime.setText(getString(R.string.min_duration, runtime));
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
    }

    private void updateFavorite() {
        MovieDetails movieDetails = mBinding.getMovieDetails();
        ContentResolver resolver = getContentResolver();
        Uri detailsUri = FavoriteMoviesContract.BASE_CONTENT_URI.buildUpon().appendPath(FavoriteMoviesContract.PATH_MOVIE).appendPath(Integer.toString(movieDetails.id)).build();
        Cursor details = resolver.query(
                detailsUri,
                null, null, null, null);
        if (details != null && details.getCount() > 0) {
            mBinding.btFavorite.setText(R.string.remove_from_favorites);
        } else {
            mBinding.btFavorite.setText(R.string.mark_as_favorite);
        }
        if (details != null) details.close();
    }

    public void flipFavourite(@SuppressWarnings("unused") View view) { // parameter is passed by the framework (function used in layout xml)
        MovieDetails movieDetails = mBinding.getMovieDetails();
        ContentResolver resolver = getContentResolver();
        Uri detailsUri = FavoriteMoviesContract.BASE_CONTENT_URI.buildUpon().appendPath(FavoriteMoviesContract.PATH_MOVIE).appendPath(Integer.toString(movieDetails.id)).build();
        Cursor details = resolver.query(
                detailsUri,
                null, null, null, null);
        boolean isFavorite = false;
        if (details != null) {
            if (details.getCount() > 0) isFavorite = true;
            details.close();
        }
        if (isFavorite) {
            resolver.delete(detailsUri, null, null);
            showToast(getResources().getString(R.string.favorite_removed, mMovieDetails.original_title));
        } else {
            ContentValues values = new ContentValues();
            values.put(FavoriteMoviesContract.MovieEntry.COLUMN_NAME_POSTER_PATH, movieDetails.poster_path);
            values.put(FavoriteMoviesContract.MovieEntry.COLUMN_NAME_OVERVIEW, movieDetails.overview);
            values.put(FavoriteMoviesContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, movieDetails.release_date);
            values.put(FavoriteMoviesContract.MovieEntry.COLUMN_NAME_ID, movieDetails.id);
            values.put(FavoriteMoviesContract.MovieEntry.COLUMN_NAME_ORIGINAL_TITLE, movieDetails.original_title);
            values.put(FavoriteMoviesContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE, movieDetails.vote_average);
            resolver.insert(detailsUri, values);
            showToast(getResources().getString(R.string.favorite_added, mMovieDetails.original_title));
        }
        updateFavorite();
    }

    private void showToast(String message) {
        if (detailToast != null) detailToast.cancel();
        detailToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        detailToast.show();
    }
}
