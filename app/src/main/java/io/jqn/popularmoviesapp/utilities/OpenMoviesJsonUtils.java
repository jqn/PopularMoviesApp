package io.jqn.popularmoviesapp.utilities;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility functions to handle OpenMovies JSON data.
 */
public final class OpenMoviesJsonUtils {
    private static final String TAG = "OpenMoviesJsonUtils";
    /**
     * This method parses JSON from a response and returns an array of strings
     * describing popular movies.
     * @param moviesJsonStr JSON response from server.
     * @return Array of strings describing movies
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static String[] getMoviesStringsFromJson(Context context, String moviesJsonStr) throws JSONException {
        /* Movie list */
        final String MOVIE_LIST = "results";
        final String MOVIE_ID = "id";
        final String POSTER_PATH = "poster_path";

        /* String array to hold each movie's data String */
        String[] parsedMovieData = null;

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        JSONArray movieArray = moviesJson.getJSONArray(MOVIE_LIST);

        parsedMovieData = new String[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {
            String movieID;
            String poster;

            /* Get the JSON object representing the movie */
            JSONObject movie = movieArray.getJSONObject(i);

            /* Get the movie id */
            movieID = movie.getString(MOVIE_ID);
            /* Get the movie poster */
            poster = movie.getString(POSTER_PATH);

            Log.v(TAG, "movie id " + movieID);

            parsedMovieData[i] = "id" + movieID;
        }

        return parsedMovieData;

    }
}
