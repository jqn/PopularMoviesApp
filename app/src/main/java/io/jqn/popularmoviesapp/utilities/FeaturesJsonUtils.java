package io.jqn.popularmoviesapp.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.jqn.popularmoviesapp.models.Review;

public class FeaturesJsonUtils {
    private static final String TAG = FeaturesJsonUtils.class.getSimpleName();

    /**
     * This method parses JSON from a response and returns an array of strings
     * describing movie reviews.
     *
     * @param reviewsJsonStr JSON response from server.
     * @return Array of strings describing movies
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static List<Review> getFeaturesStringsFromJson(String reviewsJsonStr) throws JSONException {
        /* Movie list */
        final String REVIEW_LIST = "results";
        final String REVIEW_ID = "id";
        final String REVIVEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";
        final String REVIEW_URL = "url";

        // Create an empty ArrayList to add movies to.
        List<Review> reviews = new ArrayList<>();

        // Create a JSONObject from the JSON response string
        JSONObject reviewsJson = new JSONObject(reviewsJsonStr);

        // Extract the JSONArray associated with the key called "results",
        JSONArray reviewsArray = reviewsJson.getJSONArray(REVIEW_LIST);


        for (int i = 0; i < reviewsArray.length(); i++) {

            /* Get the JSON object representing a movie */
            JSONObject reviewJson = reviewsArray.getJSONObject(i);

            /* Get the review id */
            String id = reviewJson.getString(REVIEW_ID);

            /* Get the review author */
            String author = reviewJson.getString(REVIVEW_AUTHOR);

            /* Get the review content */
            String content = reviewJson.getString(REVIEW_CONTENT);

            /* Get the review url */
            String url = reviewJson.getString(REVIEW_URL);

            Log.v(TAG, "reviewjson ****" + url);

            /**
             * Create a new review object with selected properties.
             */
            Review review = new Review(id, author, content, url);

            reviews.add(review);
        }

        return reviews;

    }

}


