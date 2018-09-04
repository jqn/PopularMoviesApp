package io.jqn.popularmoviesapp.utilities;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import io.jqn.popularmoviesapp.MainDetailActivity;
import io.jqn.popularmoviesapp.models.Review;

public class FetchMovieFeaturesTask extends AsyncTask<String, Void, List<Review>> {
    private static final String TAG = FetchMovieFeaturesTask.class.getSimpleName();

    MainDetailActivity mainDetailActivity;
    String movieId;

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

            Log.v(TAG, "reviews response" + jsonReviewResponse);
            return null;
            //List<Review> movieJsonData = MoviesJsonUtils.getMoviesStringsFromJson(jsonMovieResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

    }

}


