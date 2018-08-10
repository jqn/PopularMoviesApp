package io.jqn.popularmoviesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {
    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * References to the drawer menu
     */
    private DrawerLayout mDrawerLayout;
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
        // Set action bar title
        setTitle("Popular Movies");

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // set item as selected to persist highlight
                        item.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (item.getItemId()) {
                            case R.id.set_popular:
                                Log.v(TAG, "clicked popular");
                                loadMovieData("movie", "popular");
                                return true;
                            case R.id.set_trending:
                                loadMovieData("movie", "top_rated");
                                return true;
                        }

                        return true;
                    }
                }
        );

        /**
         * Get a reference to the RecyclerView with findViewById
         */
        mRecyclerView = findViewById(R.id.movies);
        mErrorMessageDisplay = findViewById(R.id.movie_error_message_display);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(gridLayoutManager);

        /**
         * Use this setting to improve performance that changes in content do not change the child
         * layout size in the RecyclerView.
         */
        mRecyclerView.setHasFixedSize(true);

        /**
         * The movie adapter is responsible for displaying each item in the grid.
         */
        mMovieAdapter = new MovieAdapter(this);

        /**
         * Setting the adapter attaches it to the RecyclerView in the layout.
         */
        mRecyclerView.setAdapter(mMovieAdapter);

        /**
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default.
         */
        mLoadingIndicator = findViewById(R.id.grid_loading_indicator);

        /* Once all of our views are setup, we can load the movie data. */
        loadMovieData("movie", "popular");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        // If we got here the users's action was not recognized
        return super.onOptionsItemSelected(item);
    }

    /* Tell the background method to get popular movies in the background */
    private void loadMovieData(String media, String filter) {

        new FetchMoviesTask().execute(media, filter);
    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param movie The movie that was clicked
     */
    public void onClick(Movie movie) {
        Log.v(TAG, "main activity click");
        Intent movieDetailIntent = new Intent(MainActivity.this, MainDetailActivity.class);
        movieDetailIntent.putExtra("movie", movie);
        startActivity(movieDetailIntent);

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

    /**
     * This method will make the error message visible and hide the movie
     * grid View.
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
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
                mMovieAdapter.setMoviePosters(movieData);
            } else {
                showErrorMessage();
            }

        }

    }
}
