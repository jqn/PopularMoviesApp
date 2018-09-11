package io.jqn.popularmoviesapp.tasks;

import android.os.AsyncTask;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import io.jqn.popularmoviesapp.MainDetailActivity;
import io.jqn.popularmoviesapp.models.Trailer;
import io.jqn.popularmoviesapp.utilities.FeaturesJsonUtils;
import io.jqn.popularmoviesapp.utilities.NetworkUtils;

public class FetchTrailersTask extends AsyncTask<String, Void, List<Trailer>> {
    private static final String TAG = FetchTrailersTask.class.getSimpleName();

    MainDetailActivity mainDetailActivity;

    public FetchTrailersTask(MainDetailActivity mainDetailActivity) {
        this.mainDetailActivity = mainDetailActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Trailer> doInBackground(String... params) {
        /* if there is not endpoint there is nothing to look up */
        if (params.length == 0) {
            return null;
        }

        String featureType = params[0];
        String movieId = params[1];
        String filter = params[2];

        URL trailersRequestUrl = NetworkUtils.buildFeatureURL(featureType, movieId, filter);

        try {
            String jsonTrailerResponse = NetworkUtils.getResponseFromHttpUrl(trailersRequestUrl);

            List<Trailer> trailerJsonData = FeaturesJsonUtils.getTrailerStringsFromJson(jsonTrailerResponse);

            return trailerJsonData;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    protected void onPostExecute(List<Trailer> trailerData) {
        if (trailerData != null) {
            this.mainDetailActivity.setMovieTrailers(trailerData);
        } else {
            this.mainDetailActivity.showSnackBar("No review content available");
        }

    }

}
