package io.jqn.popularmoviesapp.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import io.jqn.popularmoviesapp.BuildConfig;

/**
 * This utility will be used to communicate with the MovieDB API
 */
public class NetworkUtils {

    final static String QUERY_PARAM = "api_key";
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String STATIC_MOVIE_DB_URL = "https://api.themoviedb.org";
    private static final String API_VERSION = "3";
    private static final String KEY = BuildConfig.API_KEY;

    /**
     * Builds the URL used to talk to the Movie DB API
     * using a query.
     *
     * @param mediaType The type of media that will be queried for.
     * @param filter    The type of movies that will be queried for.
     * @return The URL to query the MovieDB API
     **/
    public static URL buildURL(String mediaType, String filter) {
        Uri builtUri = Uri.parse(STATIC_MOVIE_DB_URL).buildUpon()
                .appendPath(API_VERSION)
                .appendPath(mediaType)
                .appendPath(filter)
                .appendQueryParameter(QUERY_PARAM, KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildFeatureURL(String featureType, String id, String filter ) {
        Uri builtUri = Uri.parse(STATIC_MOVIE_DB_URL).buildUpon()
        .appendPath(API_VERSION)
        .appendPath(featureType)
        .appendPath(id)
        .appendPath(filter)
        .appendQueryParameter(QUERY_PARAM, KEY)
        .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
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
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


}
