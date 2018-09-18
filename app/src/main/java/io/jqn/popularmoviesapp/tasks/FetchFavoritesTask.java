package io.jqn.popularmoviesapp.tasks;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.jqn.popularmoviesapp.data.MoviesContract;
import io.jqn.popularmoviesapp.models.Movie;

public class FetchFavoritesTask extends AsyncTaskLoader<List<Movie>> {
    private static final String TAG = FetchFavoritesTask.class.getSimpleName();
    public static final int ID = 103;

    private Context context;

    public FetchFavoritesTask(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public List<Movie> loadInBackground() {
        List<Movie> list = new ArrayList<>();
        try {
            Cursor cursor = context.getContentResolver().query(MoviesContract.FavoritesEntry.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoritesEntry.COLUMN_MOVIE_ID));
                String poster = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoritesEntry.COLUMN_MOVIE_POSTER));
                String backdrop = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoritesEntry.COLUMN_MOVIE_BACKDROP));
                String title = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoritesEntry.COLUMN_MOVIE_NAME));
                String release = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE));
                String vote = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoritesEntry.COLUMN_MOVIE_RATING));
                String plot = cursor.getString(cursor.getColumnIndex(MoviesContract.FavoritesEntry.COLUMN_MOVIE_OVERVIEW));
                list.add(new Movie(id, title, poster, backdrop, release, vote, plot));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Log.v(TAG, "favorites " + list);
        return list;
    }

}
