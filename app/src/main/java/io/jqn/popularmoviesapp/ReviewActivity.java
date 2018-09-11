package io.jqn.popularmoviesapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import io.jqn.popularmoviesapp.adapter.ReviewAdapter;
import io.jqn.popularmoviesapp.models.Movie;
import io.jqn.popularmoviesapp.models.Review;
import io.jqn.popularmoviesapp.tasks.FetchReviewsTask;

public class ReviewActivity extends AppCompatActivity {
    public static final String TAG = ReviewActivity.class.getSimpleName();
    private Movie mMovie;

    /**
     * References to the RecyclerView and Adapter
     */
    private ReviewAdapter mReviewAdapter;
    private RecyclerView mRecyclerView;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        // Set action bar title
        setTitle("Reviews");
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            //mMovie = (Movie) getIntent().getExtras().getSerializable("movie");
            /*
             * Get a reference to the RecyclerView with findViewById
             */
            mRecyclerView = findViewById(R.id.movie_reviews);

            /*
             * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
             * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
             * languages.
             */
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);

            mRecyclerView.setLayoutManager(layoutManager);

            /*
             * The ReviewAdapter is responsible for linking our review data with the Views that
             * will end up displaying our review data.
             */
            mReviewAdapter = new ReviewAdapter();

            /* Setting the adapter attaches it to the RecyclerView in our layout. */
            mRecyclerView.setAdapter(mReviewAdapter);

            /*
             * The ProgressBar that will indicate to the user that we are loading data. It will be
             * hidden when no data is loading.
             *
             * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
             * circle. We didn't make the rules (or the names of Views), we just follow them.
             */
            mLoadingIndicator = findViewById(R.id.reviews_loading_indicator);


            /* Once all of our views are setup, we can load the weather data. */
            String moviedId = getIntent().getStringExtra("movieId");
            //loadReviews("movie", moviedId, "reviews");
        }
    }

    //@Override
    //public void onBackPressed(){
    //    Intent intent = new Intent();
    //    intent.putExtra("movie", mMovie);
    //    setResult(Activity.RESULT_OK, intent);
    //    finish();
    //    super.onBackPressed();
    //}

    public void loadReviews(String feature, String id, String filter) {
        NetworkInfo networkInfo = getNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new FetchReviewsTask(this).execute(feature, id, filter);
        } else {
            showSnackBar("Please check your network/wifi connection");
        }
    }

    public void setMovieReviews(List<Review> movieReviews) {
        if (movieReviews.isEmpty()) {
            showSnackBar("No review content available");
        } else {
            hideLoadingIndicator();
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            mReviewAdapter.setReviewData(movieReviews);
        }
    }

    private NetworkInfo getNetworkInfo() {
        // Get a reference to the ConnectivityManager to check state of network connectivity.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        return connMgr.getActiveNetworkInfo();
    }

    public void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    public void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    public void showSnackBar(String message) {
        View view = findViewById(R.id.details_wrapper);
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

}
