package hu.fallen.popularmovies.utilities;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hu.fallen.popularmovies.DetailActivity;
import hu.fallen.popularmovies.R;

public class ImageAdapter extends BaseAdapter {

    private static final String TAG = ImageView.class.getSimpleName();


    private Context mContext;
    @SuppressWarnings("FieldCanBeLocal")
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private List<MovieDetails> movieList = null;

    public ImageAdapter(Context context, ImageLoader imageLoader) {
        mContext = context.getApplicationContext();
        mImageLoader = imageLoader;
    }

    public void setMovieList(List<MovieDetails> movieList) {
        this.movieList = movieList;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public int getCount() {
        int count = movieList == null ? 0 : movieList.size();
        Log.d(TAG, "count: " + Integer.toString(count));
        return count;
    }

    @Override
    public Object getItem(int i) {
        if (movieList != null && movieList.size() > i) return movieList.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        NetworkImageView imageView;
        if (view == null) {
            imageView = new NetworkImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, String.format("Movie %d has been selected.", i));
                    Intent intent = new Intent();
                    intent.putExtra("MovieDetails", (MovieDetails) getItem(i));
                    intent.setClass(mContext, DetailActivity.class);
                    mContext.startActivity(intent);
                }
            });
        } else {
            imageView = (NetworkImageView) view;
        }
        if (movieList != null) {
            Log.d(TAG, String.format("Movie %d is %s", i, ((MovieDetails) getItem(i)).original_title));
            imageView.setImageUrl("https://image.tmdb.org/t/p/w185" + ((MovieDetails) getItem(i)).poster_path, mImageLoader);
        } else {
            Log.d(TAG, String.format("movieList is empty for %d", i));
        }
        return imageView;
    }

    @Override
    public int getItemViewType(int i) {
        Log.d(TAG, String.format("getItemViewType(%d) called", i));
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        Log.d(TAG, "isEmpty called");
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        Log.d(TAG, String.format("notifyDataSetChanged() called, list size: %d", movieList == null ? -1 : movieList.size()));
        super.notifyDataSetChanged();
    }
}
