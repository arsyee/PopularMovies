package hu.fallen.popularmovies.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import hu.fallen.popularmovies.utilities.MovieDetails;

public class MoviesContentProvider extends ContentProvider {
    public static final int FAVORITE = 100;
    public static final int MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITE, FAVORITE);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_MOVIE + "/#", MOVIE_WITH_ID);
        return uriMatcher;
    }

    private FavoriteMoviesDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new FavoriteMoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor = null;
        switch (match) {
            case FAVORITE:
                cursor = db.query(
                        FavoriteMoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                return cursor;
            case MOVIE_WITH_ID:
                String id = uri.getLastPathSegment();
                String mSelection = FavoriteMoviesContract.MovieEntry.COLUMN_NAME_ID + "=?";
                String[] mSelectionArgs = new String[]{id};
                cursor = db.query(
                        FavoriteMoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder
                );
                return cursor;
            default:
                break;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE_WITH_ID:
                long id = db.insert(FavoriteMoviesContract.MovieEntry.TABLE_NAME, null, values);
                if (id <= 0) throw new SQLiteException(String.format("Failed to insert row"));
                return uri;
            default:
                throw new UnsupportedOperationException(String.format("Unsupported uri: %s", uri));
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE_WITH_ID:
                String id = uri.getLastPathSegment();
                return db.delete(
                        FavoriteMoviesContract.MovieEntry.TABLE_NAME,
                        FavoriteMoviesContract.MovieEntry.COLUMN_NAME_ID + " LIKE ?",
                        new String[] { id });
            default:
                throw new UnsupportedOperationException(String.format("Unsupported uri: %s", uri));
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
