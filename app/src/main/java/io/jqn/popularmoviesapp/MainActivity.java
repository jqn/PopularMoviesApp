package io.jqn.popularmoviesapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import java.net.URL;

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
    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            /* if there is not endpoint there is nothing to look up */
            if (params.length == 0) {
                return null;
            }

            String endpoint = params[0];
            String contentType = params[1];
            URL moviesRequestUrl = NetworkUtils.buildURL(endpoint, contentType);

            try {
                String jsonMovieREsponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
                String[] movieJsonData = OpenMoviesJsonUtils.getMoviesStringsFromJson(MainActivity.this, jsonMovieREsponse);
                return movieJsonData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] moviesData) {
            if (moviesData != null) {
                /**
                 * Iterate through array and append the strings to the GridView
                 */
                for (String moviesString : moviesData) {
                    Log.d(TAG, "onPostExecute: " + moviesString);
                }
            }

        }

    }
}
