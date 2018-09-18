package io.jqn.popularmoviesapp.tasks;

import android.os.AsyncTask;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import io.jqn.popularmoviesapp.MainDetailActivity;
import io.jqn.popularmoviesapp.models.Review;
import io.jqn.popularmoviesapp.utilities.FeaturesJsonUtils;
import io.jqn.popularmoviesapp.utilities.NetworkUtils;

public class FetchMovieFeaturesTask extends AsyncTask<String, Void, List<Review>> {
    private static final String TAG = FetchMovieFeaturesTask.class.getSimpleName();

    MainDetailActivity mainDetailActivity;

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
            return null;
        }

        String featureType = params[0];
        String movieId = params[1];
        String filter = params[2];

        URL reviewRequestUrl = NetworkUtils.buildFeatureURL(featureType, movieId, filter);

        try {
            String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(reviewRequestUrl);

            List<Review> reviewJsonData = FeaturesJsonUtils.getFeaturesStringsFromJson(jsonReviewResponse);

            return reviewJsonData;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

    }

    @Override
    protected void onPostExecute(List<Review> reviewData) {
        if (reviewData != null) {
            this.mainDetailActivity.setMovieReviews(reviewData);
        } else {
            this.mainDetailActivity.showSnackBar("No review content available");
        }
    }

}


