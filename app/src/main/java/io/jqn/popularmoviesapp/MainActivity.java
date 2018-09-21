package io.jqn.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private static final int MOVIE_LOADER = 22;
    private static final int FAVORITE_LOADER = 0;
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
    private GridLayoutManager mGridLayoutManager;

    private int mFavoriteLoader;
    // Queries
    private static final String SEARCH_QUERY_URL_MOVIE_MEDIA = "movie";
    private static final String SEARCH_QUERY_URL_FILTER_POPULAR = "popular";
    private static final String SEARCH_QUERY_URL_FILTER_TOP_RATED = "top_rated";
    // State
    static final String FILTER_STATE = "filter";
    String mFilterState = "popular";
    private static final String MOVIE_LIST_STATE_KEY = "MOVIE_LIST_STATE";
    private static Parcelable mMovieListState;

    private Parcelable recyclerPosition;
    private static final String RECYCLER_POSITION = "RecyclerViewPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set action bar title
        setTitle("Popular Movies");
        // Initialize drawer navigation
        //NavigationView navigationView = findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(
        //        new NavigationView.OnNavigationItemSelectedListener() {
        //            @Override
        //            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //                // set item as selected to persist highlight
        //                item.setChecked(true);
        //                // close drawer when item is tapped
        //                mDrawerLayout.closeDrawers();
        //
        //                switch (item.getItemId()) {
        //                    case R.id.set_popular:
        //                        loadMovieData("movie", "popular");
        //                        mFilterState = "popular";
        //                        setActionBarTitle(mFilterState);
        //                        return true;
        //                    case R.id.set_trending:
        //                        //loadMovieData("movie", "top_rated");
        //                        mFilterState = "top_rated";
        //                        setActionBarTitle(mFilterState);
        //                        return true;
        //                    case R.id.set_favorites:
        //                        //getFavorites();
        //                        mFilterState = "favorites";
        //                        setActionBarTitle(mFilterState);
        //                        return true;
        //                }
        //
        //                return true;
        //            }
        //        }
        //);
        // Bind Views
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mRecyclerView = findViewById(R.id.movies);
        mErrorMessageDisplay = findViewById(R.id.movie_error_message_display);

        mGridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

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

        getSupportLoaderManager().initLoader(MOVIE_LOADER, null, this);
        Log.v(TAG, "* oncreate called ");

        //if (savedInstanceState != null) {
        //    Log.v(TAG, "* oncreate saved state in not null");
        //    getSupportLoaderManager().restartLoader(MOVIE_LOADER, null, this);
        //    //getSupportLoaderManager().getLoader(MOVIE_LOADER);
        //} else {
        //    Log.v(TAG, "* oncreate saved state in null");
        //    getSupportLoaderManager().initLoader(MOVIE_LOADER, null, this);
        //
        //}
        //} else {
        //
        //}
        //getSupportLoaderManager().initLoader(MOVIE_LOADER, null, this);
        //    mFilterState = savedInstanceState.getString(FILTER_STATE);
        //    if (mFilterState.equals("favorites")) {
        //        setActionBarTitle(mFilterState);
        //        getFavorites();
        //    } else {
        //        loadMovieData("movie", mFilterState);
        //        setActionBarTitle(mFilterState);
        //    }
        //} else {
        //    loadMovieData("movie", "popular");
        //}
        //mFavoriteLoader = FetchFavoritesTask.ID;
        // Initialize loaders
        //getSupportLoaderManager().initLoader(MOVIE_LOADER, null, this).forceLoad();
        //Log.v(TAG, "oncreate initialize loader ");
        //getSupportLoaderManager().initLoader(MOVIE_LOADER, null, this);
        //loadMovieData("movie", mFilterState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState called");
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECYCLER_POSITION,
                mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(RECYCLER_POSITION)) {
            recyclerPosition = savedInstanceState.getParcelable(RECYCLER_POSITION);
            Log.v(TAG, "onSaveInstanceState called" + recyclerPosition);

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "on start called");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);

        MenuItem popular = menu.getItem(0);
        MenuItem top_rated = menu.getItem(1);
        MenuItem favorite = menu.getItem(2);


        // Check the correct radio button in the menu.
        switch (mFilterState) {
            case "popular":
                popular.setChecked(true);
                break;
        }
        //inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_popular:
                //mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        // If we got here the users's action was not recognized
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.v(TAG, "* oncreate loader called ");

        //showLoadingIndicator();
        if (id == MOVIE_LOADER) {
            return new FetchMoviesTaskLoader(this, "popular");
        } else if (id == FAVORITE_LOADER) {
            return new FetchFavoritesTask(this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
        Log.v(TAG, "* onLoadFinished loader called ");

        //After getting result we will update our UI here
        if (data == null) {
            hideLoadingIndicator();
            return;
        }

        setMoviePosters(data);
        //restoreLayoutManagerPosition();

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {
        Log.v(TAG, "* onloader reset loader called ");

        //Leave it for now as it is
        //restoreLayoutManagerPosition();
    }

    /* Tell the background method to get popular movies in the background */
    private void loadMovieData(String media, String filter) {
        getSupportLoaderManager().initLoader(MOVIE_LOADER, null, this);

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
        Log.v(TAG, "* setting movie views");

    }

    public void setMoviePosters(List<Movie> movieData) {
        mMovieAdapter.setMoviePosters(movieData);
        mMovieAdapter.notifyDataSetChanged();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Log.i("tag", "This'll run 300 milliseconds later");
                        mRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerPosition);
                    }
                },
                200);
        //hideLoadingIndicator();
        //if (recyclerPosition != null) {
        //    //getSupportLoaderManager().restartLoader(MOVIE_LOADER, null, this);
        //    mGridLayoutManager.onRestoreInstanceState(recyclerPosition);
        //}
        //
        //if (recyclerPosition != null) {
        //    Log.v(TAG, "* list state is not null");
        //    mMovieAdapter.notifyDataSetChanged();
        //    new android.os.Handler().postDelayed(
        //            new Runnable() {
        //                public void run() {
        //                    Log.i("tag", "This'll run 300 milliseconds later");
        //                    mRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerPosition);
        //                }
        //            },
        //            2000);
        //
        //} else {
        //    Log.v(TAG, "* list state is null");
        //    mMovieAdapter.notifyDataSetChanged();
        //
        //}

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
