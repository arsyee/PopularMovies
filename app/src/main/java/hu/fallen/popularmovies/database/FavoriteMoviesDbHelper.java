package hu.fallen.popularmovies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import hu.fallen.popularmovies.utilities.MovieDetails;

public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Movies.db";
    private static final String TAG = FavoriteMoviesDbHelper.class.getSimpleName();

    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FavoriteMoviesContract.SQL_CREATE_MOVIE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FavoriteMoviesContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public List<MovieDetails> getMovieDetails() {
        List<MovieDetails> movieDetailsList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                FavoriteMoviesContract.MovieEntry.TABLE_NAME,
                FavoriteMoviesContract.movieEntryProjectionAll,
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            final Parcel parcel = Parcel.obtain();
            int dataPosition = parcel.dataPosition();
            parcel.writeString(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_NAME_POSTER_PATH)));
            parcel.writeString(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_NAME_OVERVIEW)));
            parcel.writeString(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_NAME_RELEASE_DATE)));
            parcel.writeInt(cursor.getInt(cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_NAME_ID)));
            parcel.writeString(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_NAME_ORIGINAL_TITLE)));
            parcel.writeDouble(cursor.getDouble(cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE)));
            parcel.setDataPosition(dataPosition);
            MovieDetails movieDetails = MovieDetails.CREATOR.createFromParcel(parcel);
            Log.d(TAG, String.format("Movie found in DB: %s", movieDetails.original_title));
            movieDetailsList.add(movieDetails);
        }
        cursor.close();
        db.close();
        Log.d(TAG, String.format("Returning %d favorite movies.", movieDetailsList.size()));
        return movieDetailsList;
    }

    public boolean flipMovieDetails(MovieDetails movieDetails) {
        if (movieDetailsIsInDB(movieDetails.id)) {
            removeMovieDetails(movieDetails.id);
            return false;
        } else {
            addMovieDetails(movieDetails);
            return true;
        }
    }

    private void removeMovieDetails(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(
                FavoriteMoviesContract.MovieEntry.TABLE_NAME,
                FavoriteMoviesContract.MovieEntry.COLUMN_NAME_ID + " LIKE ?",
                new String[] { Integer.toString(id) });
        db.close();
        Log.d(TAG, String.format("Entry removed from database: %s", id));
    }

    private void addMovieDetails(MovieDetails movieDetails) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoriteMoviesContract.MovieEntry.COLUMN_NAME_POSTER_PATH, movieDetails.poster_path);
        values.put(FavoriteMoviesContract.MovieEntry.COLUMN_NAME_OVERVIEW, movieDetails.overview);
        values.put(FavoriteMoviesContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, movieDetails.release_date);
        values.put(FavoriteMoviesContract.MovieEntry.COLUMN_NAME_ID, movieDetails.id);
        values.put(FavoriteMoviesContract.MovieEntry.COLUMN_NAME_ORIGINAL_TITLE, movieDetails.original_title);
        values.put(FavoriteMoviesContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE, movieDetails.vote_average);
        long id = db.insert(FavoriteMoviesContract.MovieEntry.TABLE_NAME, null, values);
        db.close();
        Log.d(TAG, String.format("Entry inserted into database: %d %s", id, movieDetails));
    }

    public boolean movieDetailsIsInDB(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                FavoriteMoviesContract.MovieEntry.TABLE_NAME,
                FavoriteMoviesContract.movieEntryProjectionAll,
                FavoriteMoviesContract.MovieEntry.COLUMN_NAME_ID + "=?",
                new String[] { Integer.toString(id) },
                null,
                null,
                null
        );
        boolean inDb = cursor.getCount() > 0;
        cursor.close();
        db.close();
        Log.d(TAG, String.format("Movie found in DB: %d %b", id, inDb));
        return inDb;
    }
}
