package io.jqn.popularmoviesapp.utilities;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import io.jqn.popularmoviesapp.MainActivity;
import io.jqn.popularmoviesapp.models.Movie;

// Extend AsyncTask and perform network requests
public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {
    private static final String TAG = "FetchMoviesTask";
    private final WeakReference<MainActivity> mMainActivity;

    public FetchMoviesTask(MainActivity activity) {
        mMainActivity = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mMainActivity.get().showLoadingIndicator();
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        /* if there is not endpoint there is nothing to look up */
        if (params.length == 0) {
            return null;
        }

        String endpoint = params[0];
        String contentType = params[1];
        URL moviesRequestUrl = NetworkUtils.buildURL(endpoint, contentType);

        try {
            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);

            List<Movie> movieJsonData = MoviesJsonUtils.getMoviesStringsFromJson(mMainActivity.get().getApplicationContext(), jsonMovieResponse);

            return movieJsonData;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movieData) {
        mMainActivity.get().hideLoadingIndicator();
        if (movieData != null) {
            mMainActivity.get().showMovieDataView();
            mMainActivity.get().setMoviePosters(movieData);
        } else {
            mMainActivity.get().showErrorMessage();
        }

    }

}
