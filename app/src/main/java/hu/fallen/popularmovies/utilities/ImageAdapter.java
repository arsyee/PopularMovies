package hu.fallen.popularmovies.utilities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import hu.fallen.popularmovies.DetailActivity;
import hu.fallen.popularmovies.R;

public class ImageAdapter extends BaseAdapter {

    private static final String TAG = ImageAdapter.class.getSimpleName();

    private final Context mContext;
    private final ImageLoader mImageLoader;

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
        NetworkImageView networkImageView;
        if (view == null) { // create a new NetworkImageView and setup properties
            networkImageView = new NetworkImageView(mContext);
            networkImageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            networkImageView.setAdjustViewBounds(true);
            networkImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else { // recycle view, we don't need to set common properties
            networkImageView = (NetworkImageView) view;
        }
        // setup unique properties
        if (movieList != null && movieList.size() > i) {
            final MovieDetails movieDetails = (MovieDetails) getItem(i);
            Log.d(TAG, String.format("Drawing %s", movieDetails));
            networkImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, String.format("Movie %d has been selected: %s", i, movieDetails));
                    Intent intent = new Intent();
                    intent.putExtra(SharedConstants.MOVIE_DETAILS_EXTRA, movieDetails);
                    intent.setClass(mContext, DetailActivity.class);
                    mContext.startActivity(intent);
                }
            });
            networkImageView.setContentDescription(mContext.getString(R.string.poster_description, movieDetails.original_title));
            networkImageView.setImageUrl(SharedConstants.BASE_POSTER_URL + "/w185" + movieDetails.poster_path, mImageLoader);
        } else {
            networkImageView.setOnClickListener(null);
            networkImageView.setImageUrl(null, null);
            Log.d(TAG, String.format("movieList is empty for %d", i));
        }
        return networkImageView;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return movieList == null || movieList.isEmpty();
    }

    @Override
    public void notifyDataSetChanged() {
        Log.d(TAG, String.format("notifyDataSetChanged() called, list size: %d", movieList == null ? -1 : movieList.size()));
        super.notifyDataSetChanged();
    }
}
