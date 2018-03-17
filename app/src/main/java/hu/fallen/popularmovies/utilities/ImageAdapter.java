package hu.fallen.popularmovies.utilities;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import hu.fallen.popularmovies.DetailActivity;
import hu.fallen.popularmovies.MainActivity;
import hu.fallen.popularmovies.R;

public class ImageAdapter extends BaseAdapter {

    private static final String TAG = ImageAdapter.class.getSimpleName();

    private final Context mContext;
    private final ImageLoader mImageLoader;
    private final LayoutInflater mInflater;

    private ArrayList<MovieDetails> movieList;

    public ImageAdapter(MainActivity context, ImageLoader imageLoader, ArrayList<MovieDetails> movieList) {
        mContext = context.getApplicationContext();
        mInflater = context.getLayoutInflater();
        mImageLoader = imageLoader;
        setMovieList(movieList);
    }

    public void setMovieList(ArrayList<MovieDetails> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public ArrayList<MovieDetails> getMovieList() {
        return movieList;
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
        ConstraintLayout constraintLayout;
        if (view == null) {
            constraintLayout = (ConstraintLayout) mInflater.inflate(R.layout.poster_layout, viewGroup, false);
        } else {
            constraintLayout = (ConstraintLayout) view;
        }
        NetworkImageView networkImageView = (NetworkImageView) constraintLayout.getChildAt(0);
        // setup unique properties
        if (movieList != null && movieList.size() > i) {
            final MovieDetails movieDetails = (MovieDetails) getItem(i);
            // Log.d(TAG, String.format("Drawing %s", movieDetails));
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
        return constraintLayout;
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

}
