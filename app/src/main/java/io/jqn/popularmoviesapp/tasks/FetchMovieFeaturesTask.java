package io.jqn.popularmoviesapp.tasks;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import io.jqn.popularmoviesapp.MainDetailActivity;
import io.jqn.popularmoviesapp.adapter.ReviewAdapter;
import io.jqn.popularmoviesapp.models.Review;
import io.jqn.popularmoviesapp.utilities.FeaturesJsonUtils;
import io.jqn.popularmoviesapp.utilities.NetworkUtils;

public class FetchMovieFeaturesTask extends AsyncTask<String, Void, List<Review>> {
    private static final String TAG = FetchMovieFeaturesTask.class.getSimpleName();

    MainDetailActivity mainDetailActivity;

    private ReviewAdapter mReviewAdapter;

    public FetchMovieFeaturesTask(MainDetailActivity mainDetailActivity) {
        this.mainDetailActivity = mainDetailActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Show loading here
    }

    @Override
    protected List<Review> doInBackground(String... params) {
        /* If there is no endpoint there is nothing to look up */
        if (params.length == 0) {
            Log.v(TAG, "params are null");
            return null;
        }

        String featureType = params[0];
        String movieId = params[1];
        String filter = params[2];

        URL reviewRequestUrl = NetworkUtils.buildFeatureURL(featureType, movieId, filter);

        try {
            String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(reviewRequestUrl);

            List<Review> reviewJsonData = FeaturesJsonUtils.getFeaturesStringsFromJson(jsonReviewResponse);

            Log.v(TAG, "reviews List" + reviewJsonData);

            return reviewJsonData;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

    }

    @Override
    protected void onPostExecute(List<Review> reviewData) {
        Log.v(TAG, "review data task" + reviewData);
        if (reviewData != null) {
            Log.v(TAG, "we have reviews");
            this.mainDetailActivity.setMovieReviews(reviewData);
        } else {
            this.mainDetailActivity.showSnackBar("No review content available");
        }
    }

}


