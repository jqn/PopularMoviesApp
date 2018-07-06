package io.jqn.popularmoviesapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.jqn.popularmoviesapp.adapter.MovieAdapter;
import io.jqn.popularmoviesapp.models.Movie;
import io.jqn.popularmoviesapp.utilities.MoviesJsonUtils;
import io.jqn.popularmoviesapp.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * References to RecyclerView and Adapter.
     */
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Get a reference to the recyclerview with findViewById
         */
        mRecyclerView = findViewById(R.id.movies);
        mErrorMessageDisplay = findViewById(R.id.movie_error_message_display);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        mRecyclerView.setLayoutManager(gridLayoutManager);

        List<Movie> movies = new ArrayList<>();
        /**
         * Use this setting to improve performance that changes in content do not change the child
         * layout size in the RecyclerView.
         */
        mRecyclerView.setHasFixedSize(true);

        /**
         * The movie adapter is responsible for displaying each item in the grid.
         */

        mMovieAdapter = new MovieAdapter();

        mRecyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = findViewById(R.id.grid_loading_indicator);

        loadMovieData();
    }

    /* Tell the background method to get popular movies in the background */
    private void loadMovieData() {
        new FetchMoviesTask().execute("movie", "popular");
    }

    /**
     * This method will make the View for the movie data visible and
     * hide the error message.
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First hide currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

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
                int count = movieData.size();
                ArrayList<String> array = new ArrayList<>(count);

                /**
                 * Iterate through array and append the strings to the GridView
                 */
                for (int i = 0; i < movieData.size(); i++) {
                    Log.v(TAG, "poster path " + movieData.get(i).getPosterPath());
                    String mMoviePosterPath = "http://image.tmdb.org/t/p/w780/".concat(movieData.get(i).getPosterPath());
                    array.add(i, mMoviePosterPath);
                }
                mMovieAdapter.setMovieposters(array);
            } else {
                showErrorMessage();
            }

        }

    }
}
