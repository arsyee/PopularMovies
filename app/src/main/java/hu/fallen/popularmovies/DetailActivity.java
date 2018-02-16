package hu.fallen.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import hu.fallen.popularmovies.utilities.MovieDetails;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        MovieDetails movieDetails = (MovieDetails) intent.getParcelableExtra("MovieDetails");
        Log.d(TAG, String.format("intent received %s", movieDetails.toString()));
    }
}
