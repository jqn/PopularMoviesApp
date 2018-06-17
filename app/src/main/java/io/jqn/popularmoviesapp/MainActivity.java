package io.jqn.popularmoviesapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.net.URL;

import io.jqn.popularmoviesapp.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gv = findViewById(R.id.grid_view);
        gv.setAdapter(new GridViewAdapter(this));
        gv.setOnScrollListener(new ScrollListener(this));

        loadMovieData();
    }

    private void loadMovieData() {
        new FetchMoviesTask().execute("movie/popular");
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String endpoint = params[0];
            URL moviesRequestUrl = NetworkUtils.buildURL(endpoint);

            try {
                NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
//                String moviesJsonResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);

                return null;

            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
