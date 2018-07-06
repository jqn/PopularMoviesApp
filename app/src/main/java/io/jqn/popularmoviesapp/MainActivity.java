package io.jqn.popularmoviesapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.jqn.popularmoviesapp.adapter.MovieAdapter;
import io.jqn.popularmoviesapp.models.Movie;
import io.jqn.popularmoviesapp.utilities.NetworkUtils;
import io.jqn.popularmoviesapp.utilities.OpenMoviesJsonUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * References to RecyclerView and Adapter.
     */
    private MovieAdapter mAdapter;
    private RecyclerView mMoviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Get a reference to the recyclerview with findViewById
         */
        mMoviesList = findViewById(R.id.movies);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        mMoviesList.setLayoutManager(gridLayoutManager);

        List<Movie> movies = new ArrayList<>();
        /**
         * Use this setting to improve performance that changes in content do not change the child
         * layout size in the RecyclerView.
         */
        mMoviesList.setHasFixedSize(true);
        /**
         * The movie adapter is responsible for displaying each item in the grid.
         */
        mAdapter = new MovieAdapter(movies);

        mMoviesList.setAdapter(mAdapter);

        loadMovieData();
    }

    /* Tell the background method to get popular movies in the background */
    private void loadMovieData() {
        new FetchMoviesTask().execute("movie", "popular");
    }

    // Extend AsyncTask and perform network requests
    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

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

                List<Movie> movieJsonData = OpenMoviesJsonUtils.getMoviesStringsFromJson(MainActivity.this, jsonMovieResponse);

                return movieJsonData;

            } catch (Exception e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movieData) {
            if (movieData != null) {
                /**
                 * Iterate through array and append the strings to the GridView
                 */
                for (int i = 0; i < movieData.size(); i++) {
                    Log.v(TAG, "title " + movieData.get(i).getTitle());
                }
            }

        }

    }
}
