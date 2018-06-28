package io.jqn.popularmoviesapp.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jqn on 6/16/18.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org/3";

    private static final String API_KEY = "7a821a3d4895c7c70e2dde4b875a4881";

    /* The format we want the movie API to return */
    private static final String format = "json";
    /* Query parameters */
    final static String QUERY_PARAM = "api_key";
    private static final String LANGUAGE = "en-us";
    private static final String REGION = "";
    private static final String SORT_BY = "https://api.themoviedb.org/3/movie/popular?api_key=";

    /**
     * Builds the URL used to talk to the Movie DB API
     * using a query.
     * @param movieQuery The type of movies that will be queried for.
     * @return The URL to query the MovieDB API
     **/
    public static URL buildURL(String endPoint) {
        Uri builtUri = Uri.parse("https://api.themoviedb.org/3/movie/popular").buildUpon()
                .appendQueryParameter("api_key", "7a821a3d4895c7c70e2dde4b875a4881")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     * @param url the URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading.
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                Log.v(TAG, "getResponseFromHTTp called ");

                return scanner.next();
            } else {
                return  null;
            }
        } finally {
             urlConnection.disconnect();
        }
    }

}
