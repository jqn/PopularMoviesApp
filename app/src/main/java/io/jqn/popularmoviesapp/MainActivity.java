package io.jqn.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import io.jqn.popularmoviesapp.adapter.MovieAdapter;
import io.jqn.popularmoviesapp.models.Movie;
import io.jqn.popularmoviesapp.tasks.FetchFavoritesTask;
import io.jqn.popularmoviesapp.tasks.FetchMoviesTaskLoader;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String TAG = MainActivity.class.getSimpleName();
    // Constants for logging and referring to a unique loader
    private static final int MOVIE_LOADER = 11;
    private static final int TOP_RATED_LOADER = 22;
    private static final int FAVORITE_LOADER = 0;
    /*
      References to the drawer menu
     */
    private DrawerLayout mDrawerLayout;
    /*
      References to RecyclerView and Adapter.
     */
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private GridLayoutManager mGridLayoutManager;

    private int mFavoriteLoader;
    // State
    static final String FILTER_STATE = "filter";
    String mFilterState = "popular";

    private Parcelable recyclerPosition;
    private static final String RECYCLER_POSITION = "RecyclerViewPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set action bar title
        setTitle("Popular Movies");
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
                                loadMovieData("movie", "popular");
                                mFilterState = "popular";
                                setActionBarTitle(mFilterState);
                                return true;
                            case R.id.set_trending:
                                loadMovieData("movie", "top_rated");
                                mFilterState = "top_rated";
                                setActionBarTitle(mFilterState);
                                return true;
                            case R.id.set_favorites:
                                getFavorites();
                                mFilterState = "favorites";
                                setActionBarTitle(mFilterState);
                                return true;
                        }

                        return true;
                    }
                }
        );
        // Bind Views
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mRecyclerView = findViewById(R.id.movies);
        mErrorMessageDisplay = findViewById(R.id.movie_error_message_display);
        // Layout managers
        mGridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        /*
          Use this setting to improve performance that changes in content do not change the child
          layout size in the RecyclerView.
         */
        mRecyclerView.setHasFixedSize(true);
        /*
          The movie adapter is responsible for displaying each item in the grid.
         */
        mMovieAdapter = new MovieAdapter(this);
        /*
          Setting the adapter attaches it to the RecyclerView in the layout.
         */
        mRecyclerView.setAdapter(mMovieAdapter);
        /*
          The ProgressBar that will indicate to the user that we are loading data. It will be
          hidden when no data is loading.
          Please note: This so called "ProgressBar" isn't a bar by default.
         */
        mLoadingIndicator = findViewById(R.id.grid_loading_indicator);
        //Initialize loaders
        getSupportLoaderManager().initLoader(MOVIE_LOADER, null, this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FILTER_STATE, mFilterState);
        outState.putParcelable(RECYCLER_POSITION,
                mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mFilterState = savedInstanceState.getString(FILTER_STATE);
        recyclerPosition = savedInstanceState.getParcelable(RECYCLER_POSITION);
        if (mFilterState.equals("top_rated")) {
            loadMovieData("movie", "top_rated");
        } else if (mFilterState.equals("favorites")) {
            loadMovieData("movie", "favorites");
        } else {
            loadMovieData("movie", "popular");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerPosition != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerPosition);
        }
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

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        showLoadingIndicator();
        if (id == MOVIE_LOADER) {
            return new FetchMoviesTaskLoader(this, "popular");
        } else if (id == TOP_RATED_LOADER) {
            return new FetchMoviesTaskLoader(this, "top_rated");
        } else {
            return new FetchFavoritesTask(this);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
        //After getting result we will update our UI here
        if (data == null) {
            return;
        }
        setMoviePosters(data);
        hideLoadingIndicator();

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {
        //Leave it for now as it is
    }

    /* Tell the background method to get popular movies in the background */
    private void loadMovieData(String media, String filter) {
        if (filter.equals("top_rated")) {
            getSupportLoaderManager().initLoader(TOP_RATED_LOADER, null, this);

        } else if (filter.equals("favorites")) {
            getSupportLoaderManager().initLoader(FAVORITE_LOADER, null, this);
        } else {
            getSupportLoaderManager().initLoader(MOVIE_LOADER, null, this);
        }

    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param movie The movie that was clicked
     */
    public void onClick(Movie movie) {
        Intent movieDetailIntent = new Intent(MainActivity.this, MainDetailActivity.class);
        movieDetailIntent.putExtra("movie", movie);
        startActivity(movieDetailIntent);

    }

    public void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    public void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    /**
     * This method will make the View for the movie data visible and
     * hide the error message.
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    public void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void setMoviePosters(List<Movie> movieData) {
        mMovieAdapter.setMoviePosters(movieData);
        mMovieAdapter.notifyDataSetChanged();
        if (recyclerPosition != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerPosition);
        }
    }

    /**
     * This method will make the error message visible and hide the movie
     * grid View.
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    public void showErrorMessage() {
        /* First hide currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    protected void getFavorites() {
        if (getSupportLoaderManager().getLoader(mFavoriteLoader) == null) {
            getSupportLoaderManager().initLoader(FAVORITE_LOADER, null, this).forceLoad();
        } else {
            getSupportLoaderManager().getLoader(FAVORITE_LOADER).forceLoad();
        }
    }

    public void setActionBarTitle(String title) {
        switch (title) {
            case "popular":
                setTitle("Popular");
                break;
            case "top_rated":
                setTitle("Top Rated");
                break;
            case "favorites":
                setTitle("Favorites");
                break;
        }

    }

}
