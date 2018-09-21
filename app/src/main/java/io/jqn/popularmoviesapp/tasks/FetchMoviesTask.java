package io.jqn.popularmoviesapp.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import io.jqn.popularmoviesapp.MainActivity;
import io.jqn.popularmoviesapp.models.Movie;
import io.jqn.popularmoviesapp.utilities.MoviesJsonUtils;
import io.jqn.popularmoviesapp.utilities.NetworkUtils;

// Extend AsyncTask and perform network requests
public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {
    private static final String TAG = FetchMoviesTask.class.getSimpleName();

    MainActivity mainActivity;

    public FetchMoviesTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.mainActivity.showLoadingIndicator();
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        /* if there is not endpoint there is nothing to look up */
        if (params.length == 0) {
            return null;
        }

        String mediaType = params[0];
        String contentType = params[1];
        URL moviesRequestUrl = NetworkUtils.buildURL(mediaType, contentType);

        try {
            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);

            List<Movie> movieJsonData = MoviesJsonUtils.getMoviesStringsFromJson(jsonMovieResponse);

            return movieJsonData;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movieData) {
        this.mainActivity.hideLoadingIndicator();

        if (movieData != null) {
            this.mainActivity.showMovieDataView();
            this.mainActivity.setMoviePosters(movieData);
        } else {
            this.mainActivity.showErrorMessage();
        }


    }

}
