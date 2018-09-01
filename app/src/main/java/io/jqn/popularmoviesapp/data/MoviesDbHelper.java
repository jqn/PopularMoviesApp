package io.jqn.popularmoviesapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import io.jqn.popularmoviesapp.models.Movie;
import java.util.ArrayList;
import java.util.List;

public class MoviesDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " + MoviesContract.FavoritesEntry.TABLE_NAME + " (" +
                MoviesContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MoviesContract.FavoritesEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, " +
                MoviesContract.FavoritesEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                MoviesContract.FavoritesEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MoviesContract.FavoritesEntry.COLUMN_MOVIE_BACKDROP + " TEXT NOT NULL, " +
                MoviesContract.FavoritesEntry.COLUMN_MOVIE_RATING + " TEXT NOT NULL, " +
                MoviesContract.FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesContract.FavoritesEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL" +
                "); ";


        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.FavoritesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public List<Movie> getFavorites() {
        List<Movie> movieList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + MoviesContract.FavoritesEntry.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                MoviesContract.FavoritesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MoviesContract.FavoritesEntry.COLUMN_MOVIE_NAME
        );

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoritesEntry.COLUMN_MOVIE_ID));
            String title = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoritesEntry.COLUMN_MOVIE_NAME));
            String posterPath = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoritesEntry.COLUMN_MOVIE_POSTER));
            String backdrop = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoritesEntry.COLUMN_MOVIE_BACKDROP));
            String userRating = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoritesEntry.COLUMN_MOVIE_RATING));
            String releaseDate = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE));
            String overview = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoritesEntry.COLUMN_MOVIE_OVERVIEW));
            Movie data = new Movie(id, title, posterPath, backdrop, userRating, releaseDate, overview);
            movieList.add(data);
        }

        return movieList;

    }

}
