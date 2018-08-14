package io.jqn.popularmoviesapp.utilities;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import io.jqn.popularmoviesapp.MainActivity;
import io.jqn.popularmoviesapp.adapter.MovieAdapter;
import io.jqn.popularmoviesapp.models.Movie;

// Extend AsyncTask and perform network requests
public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {
    private static final String TAG = "FetchMoviesTask";

    /**
     * References to the RecyclerView and Adapter
     */
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private MainActivity mainActivity;

    public FetchMoviesTask() {
        this.mainActivity = mainActivity;

    }

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

            List<Movie> movieJsonData = MoviesJsonUtils.getMoviesStringsFromJson(mainActivity, jsonMovieResponse);

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
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mMovieAdapter.setMoviePosters(movieData);
        } else {
            /* First hide currently visible data */
            mRecyclerView.setVisibility(View.INVISIBLE);
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
        }

    }

}
