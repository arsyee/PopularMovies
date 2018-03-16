package hu.fallen.popularmovies.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMoviesContract {

    private FavoriteMoviesContract() {}

    static final String AUTHORITY = "hu.fallen.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_FAVORITE = PATH_MOVIE + "/favorite";

    public static class MovieEntry implements BaseColumns {
        static final String TABLE_NAME = "movie";
        public static final String COLUMN_NAME_POSTER_PATH = "poster_path";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_RELEASE_DATE = "release_date";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "vote_average";
    }

    static final String SQL_CREATE_MOVIE_ENTRIES =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                    MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                    MovieEntry.COLUMN_NAME_POSTER_PATH + " TEXT, " +
                    MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT, " +
                    MovieEntry.COLUMN_NAME_RELEASE_DATE + " TEXT, " +
                    MovieEntry.COLUMN_NAME_ID + " INTEGER, " +
                    MovieEntry.COLUMN_NAME_ORIGINAL_TITLE + " TEXT, "+
                    MovieEntry.COLUMN_NAME_VOTE_AVERAGE + " REAL" +
                    ")";

    static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;
}
