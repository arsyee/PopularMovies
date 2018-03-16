package hu.fallen.popularmovies.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMoviesContract {

    private FavoriteMoviesContract() {}

    public static final String AUTHORITY = "hu.fallen.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_FAVORITE = PATH_MOVIE + "/favorite";

    /*
    Example result object from TMDB:
        {
			"vote_count": 5727,
			"id": 211672,
			"video": false,
			"vote_average": 6.4,
			"title": "Minions",
			"popularity": 526.313015,
			"poster_path": "\/q0R4crx2SehcEEQEkYObktdeFy.jpg",
			"original_language": "en",
			"original_title": "Minions",
			"genre_ids": [10751, 16, 12, 35],
			"backdrop_path": "\/qLmdjn2fv0FV2Mh4NBzMArdA0Uu.jpg",
			"adult": false,
			"overview": "Minions Stuart, Kevin and Bob are recruited by Scarlet Overkill, a super-villain who, alongside her inventor husband Herb, hatches a plot to take over the world.",
			"release_date": "2015-06-17"
		}
	Values stored in class MovieDetails
        public final String poster_path;
        public final String overview;
        public final String release_date;
        public final int id;
        public final String original_title;
        public final double vote_average;
     */
    public static class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_NAME_POSTER_PATH = "poster_path";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_RELEASE_DATE = "release_date";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "vote_average";
    }

    public static final String[] movieEntryProjectionAll = {
            MovieEntry._ID,
            MovieEntry.COLUMN_NAME_POSTER_PATH,
            MovieEntry.COLUMN_NAME_OVERVIEW,
            MovieEntry.COLUMN_NAME_RELEASE_DATE,
            MovieEntry.COLUMN_NAME_ID,
            MovieEntry.COLUMN_NAME_ORIGINAL_TITLE,
            MovieEntry.COLUMN_NAME_VOTE_AVERAGE
    };

    public static final String SQL_CREATE_MOVIE_ENTRIES =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                    MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                    MovieEntry.COLUMN_NAME_POSTER_PATH + " TEXT, " +
                    MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT, " +
                    MovieEntry.COLUMN_NAME_RELEASE_DATE + " TEXT, " +
                    MovieEntry.COLUMN_NAME_ID + " INTEGER, " +
                    MovieEntry.COLUMN_NAME_ORIGINAL_TITLE + " TEXT, "+
                    MovieEntry.COLUMN_NAME_VOTE_AVERAGE + " REAL" +
                    ")";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;
}
