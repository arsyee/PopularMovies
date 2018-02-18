package hu.fallen.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hu.fallen.popularmovies.R;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private static final int VIEWTYPE_REVIEW = 0;
    private static final int VIEWTYPE_SEPARATOR = 1;

    private final Context mContext;

    private List<ReviewInfo> mReviewInfos;

    public ReviewAdapter(Context context) {
        mContext = context.getApplicationContext();
    }

    public void setReviewInfos(List<ReviewInfo> trailerInfos) {
        mReviewInfos = trailerInfos;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return getItemViewType(position) == 0 ? position / 2 : -1;
    }

    @Override
    public int getItemCount() {
        return mReviewInfos == null || mReviewInfos.isEmpty() ? 0 : mReviewInfos.size() * 2 - 1;
    }

    private int count = 0;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, String.format("onCreateViewHolder called: %d", count++));
        View view;
        if (viewType == VIEWTYPE_SEPARATOR) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.separator, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_review, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEWTYPE_SEPARATOR) {
            Log.d(TAG, String.format("onBindViewHolder called: %d (separator)", position));
            return; // no change needed
        }
        final ReviewInfo reviewInfo = mReviewInfos.get(position / 2);
        String trailerTitle = reviewInfo.toString();
        Log.d(TAG, String.format("onBindViewHolder called: %d (%s)", position, trailerTitle));
        ((TextView) holder.itemView.findViewById(R.id.tv_author)).setText(reviewInfo.author);
        ((TextView) holder.itemView.findViewById(R.id.tv_content)).setText(reviewInfo.content);
    }

    @Override
    public int getItemViewType(int i) {
        return i % 2 == 0 ? VIEWTYPE_REVIEW : VIEWTYPE_SEPARATOR;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
