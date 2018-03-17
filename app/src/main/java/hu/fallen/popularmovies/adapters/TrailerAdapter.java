package hu.fallen.popularmovies.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.fallen.popularmovies.R;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_TRAILER = 0;
    private static final int VIEW_TYPE_SEPARATOR = 1;

    private final Context mContext;

    private ArrayList<TrailerInfo> mTrailerInfo;

    public TrailerAdapter(Context context) {
        mContext = context.getApplicationContext();
    }

    public void setTrailerInfo(ArrayList<TrailerInfo> trailerInfo) {
        mTrailerInfo = trailerInfo;
    }

    @Override
    public long getItemId(int position) {
        return getItemViewType(position) == 0 ? position / 2 : -1;
    }

    @Override
    public int getItemCount() {
        return mTrailerInfo == null || mTrailerInfo.isEmpty() ? 0 : mTrailerInfo.size() * 2 - 1;
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_trailer, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SEPARATOR) {
            Log.d(TAG, String.format("onBindViewHolder called: %d (separator)", position));
            return; // no change needed
        }
        final TrailerInfo trailerInfo = mTrailerInfo.get(position / 2);
        String trailerTitle = trailerInfo.toString();
        Log.d(TAG, String.format("onBindViewHolder called: %d (%s)", position, trailerTitle));
        ((TextView) holder.itemView.findViewById(R.id.tv_title)).setText(trailerTitle);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // based on watchYoutubeVideo() at https://stackoverflow.com/a/12439378/1409960
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerInfo.key));
                try {
                    mContext.startActivity(youtubeIntent);
                } catch (ActivityNotFoundException e) {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailerInfo.key));
                    mContext.startActivity(webIntent);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int i) {
        return i % 2 == 0 ? VIEW_TYPE_TRAILER : VIEW_TYPE_SEPARATOR;
    }

    public ArrayList<TrailerInfo> getTrailerInfo() {
        return mTrailerInfo;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
