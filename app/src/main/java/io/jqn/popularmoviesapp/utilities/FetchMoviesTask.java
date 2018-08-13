package io.jqn.popularmoviesapp.utilities;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import io.jqn.popularmoviesapp.MainActivity;
import io.jqn.popularmoviesapp.models.Movie;

// Extend AsyncTask and perform network requests
public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mLoadingIndicator.setVisibility(View.VISIBLE);
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

            List<Movie> movieJsonData = MoviesJsonUtils.getMoviesStringsFromJson(MainActivity.this, jsonMovieResponse);

            return movieJsonData;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movieData) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (movieData != null) {
            showMovieDataView();
            mMovieAdapter.setMoviePosters(movieData);
        } else {
            showErrorMessage();
        }

    }

}
