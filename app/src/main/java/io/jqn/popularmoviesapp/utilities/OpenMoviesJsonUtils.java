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
public final class OpenMoviesJsonUtils {
    private static final String TAG = "OpenMoviesJsonUtils";

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
        final String POSTER_PATH = "poster_path";

        // Create an empty ArrayList to add movies to.
        List<Movie> movies = new ArrayList<>();

        // Create a JSONObject from the JSON response string
        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        // Extract the JSONArray associated with the key called "results",
        JSONArray movieArray = moviesJson.getJSONArray(MOVIE_LIST);


        for (int i = 0; i < movieArray.length(); i++) {

            /* Get the JSON object representing a movie */
            JSONObject singleMovie = movieArray.getJSONObject(i);

            /* Get the movie id */
            String id = singleMovie.getString(MOVIE_ID);

            /* Get the movie title */
            String title = singleMovie.getString("original_title");

            /* Get the movie poster */
            String poster = singleMovie.getString(POSTER_PATH);

            /* Get the movie overview */
            String overview = singleMovie.getString("overview");

            /**
             * Create a new Movie object with selected properties.
             */
            Movie movie = new Movie(id, title, poster, overview);

            movies.add(movie);
        }

        return movies;

    }
}
