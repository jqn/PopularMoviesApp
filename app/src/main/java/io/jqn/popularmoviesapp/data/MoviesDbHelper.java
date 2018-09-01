package io.jqn.popularmoviesapp.data;

import android.content.ContentValues;
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
                /**
                 * FavMovieEntry did not explicitly declare a column called "_ID". However,
                 * FavMovieEntry implements the interface, "BaseColumns", which does have a field
                 * named "_ID". We use that here to designate our table's primary key.
                 */
                MoviesContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MoviesContract.FavoritesEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, " +
                MoviesContract.FavoritesEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                MoviesContract.FavoritesEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MoviesContract.FavoritesEntry.COLUMN_MOVIE_BACKDROP + " TEXT NOT NULL, " +
                MoviesContract.FavoritesEntry.COLUMN_MOVIE_RATING + " TEXT NOT NULL, " +
                MoviesContract.FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesContract.FavoritesEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                /**
                 * To ensure this table can only contain one movie entry per movie, declaring
                 * the movie_id column to bu unique. Also specify "ON CONFLICT REPLACE". This
                 * tells SQLite that if having a movie entry for a certain movie_id and
                 * attempting to insert another movie entry with that movie_id, replacing
                 * the old movie entry.
                 */
                " UNIQUE (" + MoviesContract.FavoritesEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";


        /**
         * After spelling out the SQLite table creation statement above, actually execute
         * that SQL with the execSQL method of the SQLite database object.
         */
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.FavoritesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    /**
     * Adds new favorite to the movie database
     * @param movieId the movie id from the moviedb payload
     * @param  movieName Movie name
     * @param moviePoster the path to the movie poster
     */
    public void addFavorite(String movieId, String movieName, String moviePoster, String movieBackdrop, String movieRating, String movieReleaseDate, String movieOverview) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Creates a ContentValues instance to pass the values into the insert query
        ContentValues cv = new ContentValues();
        // Calls put to insert the movie id value with the key COLUMN_MOVIE_ID
        cv.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_ID, movieId);
        // Calls put to insert the movie name value with the key COLUMN_MOVIE_NAME
        cv.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_NAME, movieName);
        // Calls put to insert the movie poster path value with the key COLUMN_MOVIE_POSTER
        cv.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_POSTER, moviePoster);
        cv.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_BACKDROP, movieBackdrop);
        cv.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_RATING, movieRating);
        cv.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE, movieReleaseDate);
        cv.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_OVERVIEW, movieOverview);

        db.insert(MoviesContract.FavoritesEntry.TABLE_NAME, null, cv);
    }

    public void removeFavorite(String movieName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = MoviesContract.FavoritesEntry.COLUMN_MOVIE_NAME + " LIKE ?";
        String[] selectionArgs = {movieName};
        db.delete(MoviesContract.FavoritesEntry.TABLE_NAME, selection, selectionArgs);

    }

    public List<Movie> getFavorites() {
        List<Movie> movieList = new ArrayList<>();

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
        cursor.close();
        db.close();

        return movieList;

    }

}
