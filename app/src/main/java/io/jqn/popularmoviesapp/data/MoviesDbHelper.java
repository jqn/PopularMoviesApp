package io.jqn.popularmoviesapp.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoviesDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE" + MoviesContract.FavoritesEntry.TABLE_NAME + "(" +
                MoviesContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MoviesContract.FavoritesEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, " +
                MoviesContract.FavoritesEntry.COLUMN_MOVIE_ID + "INTEGER NOT NULL" + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.FavoritesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}