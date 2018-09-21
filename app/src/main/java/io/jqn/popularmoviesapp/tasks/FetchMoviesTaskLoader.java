package io.jqn.popularmoviesapp.tasks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import io.jqn.popularmoviesapp.models.Movie;
import io.jqn.popularmoviesapp.utilities.MoviesJsonUtils;
import io.jqn.popularmoviesapp.utilities.NetworkUtils;

public class FetchMoviesTaskLoader extends AsyncTaskLoader<List<Movie>> {
    private static final String TAG = FetchMoviesTaskLoader.class.getSimpleName();
    public static final int ID = 101;
    private final String mFilter;
    List<Movie> mGithubJson;


    public FetchMoviesTaskLoader(@NonNull Context context, String filter) {
        super(context);
        mFilter = filter;
    }

    @Override
    protected void onStartLoading() {
        //forceLoad();
        if (mGithubJson != null) {
            Log.v(TAG, "* json cached");
            deliverResult(mGithubJson);
        } else {
            Log.v(TAG, "* json not cached");

            forceLoad();
        }
    }

    @Override
    public List<Movie> loadInBackground() {
        if (mFilter == null) {
            return null;
        }
        URL moviesRequestUrl = NetworkUtils.buildURL("movie", mFilter);
        try {
            Log.v(TAG, "* calling for json");
            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);

            List<Movie> movieJsonData = MoviesJsonUtils.getMoviesStringsFromJson(jsonMovieResponse);

            return movieJsonData;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public void deliverResult(List<Movie> githubJson) {
        mGithubJson = githubJson;
        Log.v(TAG, "* delivering result " + mGithubJson);

        super.deliverResult(githubJson);
    }
}
