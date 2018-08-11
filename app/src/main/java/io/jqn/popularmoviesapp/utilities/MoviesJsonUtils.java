package io.jqn.popularmoviesapp.utilities;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.jqn.popularmoviesapp.models.Movie;

/**
 * Utility functions to handle OpenMovies JSON data.
 */
public final class MoviesJsonUtils {
    private static final String TAG = "MoviesJsonUtils";

    /**
     * This method parses JSON from a response and returns an array of strings
     * describing popular movies.
     *
     * @param moviesJsonStr JSON response from server.
     * @return Array of strings describing movies
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static List<Movie> getMoviesStringsFromJson(Context context, String moviesJsonStr) throws JSONException {
        /* Movie list */
        final String MOVIE_LIST = "results";
        final String MOVIE_ID = "id";
        final String TITLE = "title";
        final String POSTER = "poster_path";
        final String BACKDROP = "backdrop_path";
        final String OVERVIEW = "overview";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";

        // Create an empty ArrayList to add movies to.
        List<Movie> movies = new ArrayList<>();

        // Create a JSONObject from the JSON response string
        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        // Extract the JSONArray associated with the key called "results",
        JSONArray movieArray = moviesJson.getJSONArray(MOVIE_LIST);


        for (int i = 0; i < movieArray.length(); i++) {

            /* Get the JSON object representing a movie */
            JSONObject movieJson = movieArray.getJSONObject(i);
            /* Get the movie id */
            String id = movieJson.getString(MOVIE_ID);

            /* Get the movie title */
            String title = movieJson.getString(TITLE);

            /* Get the movie poster */
            String poster = movieJson.getString(POSTER);
            Log.v(TAG, "poster" + poster);

            /* Get the movie backdrop */
            String backdrop = movieJson.getString(BACKDROP);
            Log.v(TAG, "backdrop" + backdrop);

            /* Get the user rating */
            String userRating = movieJson.getString(VOTE_AVERAGE);

            /* Get the release date */
            String releaseDate = movieJson.getString(RELEASE_DATE);

            /* Get the movie overview */
            String overview = movieJson.getString(OVERVIEW);

            /**
             * Create a new Movie object with selected properties.
             */
            Movie movie = new Movie(id, title, poster, backdrop, userRating, releaseDate, overview);

            movies.add(movie);
        }

        return movies;

    }
}
