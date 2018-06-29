package io.jqn.popularmoviesapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import io.jqn.popularmoviesapp.models.Movie;
import io.jqn.popularmoviesapp.utilities.NetworkUtils;
import io.jqn.popularmoviesapp.utilities.OpenMoviesJsonUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gv = findViewById(R.id.grid_view);
        gv.setAdapter(new GridViewAdapter(this));
        gv.setOnScrollListener(new ScrollListener(this));

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
