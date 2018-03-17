package hu.fallen.popularmovies.adapters;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hu.fallen.popularmovies.R;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_REVIEW = 0;
    private static final int VIEW_TYPE_SEPARATOR = 1;

    private ArrayList<ReviewInfo> mReviewInfo;

    public ReviewAdapter() { }

    public void setReviewInfo(ArrayList<ReviewInfo> trailerInfo) {
        mReviewInfo = trailerInfo;
    }

    @Override
    public long getItemId(int position) {
        return getItemViewType(position) == 0 ? position / 2 : -1;
    }

    @Override
    public int getItemCount() {
        return mReviewInfo == null || mReviewInfo.isEmpty() ? 0 : mReviewInfo.size() * 2 - 1;
    }

    private int count = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, String.format("onCreateViewHolder called: %d", count++));
        View view;
        if (viewType == VIEW_TYPE_SEPARATOR) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.separator, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_review, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SEPARATOR) {
            Log.d(TAG, String.format("onBindViewHolder called: %d (separator)", position));
            return; // no change needed
        }
        final ReviewInfo reviewInfo = mReviewInfo.get(position / 2);
        String trailerTitle = reviewInfo.toString();
        Log.d(TAG, String.format("onBindViewHolder called: %d (%s)", position, trailerTitle));
        ((TextView) holder.itemView.findViewById(R.id.tv_author)).setText(reviewInfo.author);
        ((TextView) holder.itemView.findViewById(R.id.tv_content)).setText(reviewInfo.content);
    }

    @Override
    public int getItemViewType(int i) {
        return i % 2 == 0 ? VIEW_TYPE_REVIEW : VIEW_TYPE_SEPARATOR;
    }

    public ArrayList<ReviewInfo> getReviewInfo() {
        return mReviewInfo;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
