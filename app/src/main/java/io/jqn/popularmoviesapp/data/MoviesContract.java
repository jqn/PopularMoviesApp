package io.jqn.popularmoviesapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines tables and column names for the movie database.
 */
public class MoviesContract {
    public static final String AUTHORITY = "io.jqn.popularmoviesapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    // Inner class FavoritesEntry implements BaseColumns interface
    public static final class FavoritesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        // Static final members for table names and each db column
        public static final String TABLE_NAME = "favorites";
        // COLUMN_MOVIE_NAME -> movieid
        public static final String COLUMN_MOVIE_ID = "movieId";
        // COLUMN_MOVIE_NAME -> movieName
        public static final String COLUMN_MOVIE_NAME = "movieName";
        // COLUMN_MOVIE_NAME -> moviePoster
        public static final String COLUMN_MOVIE_POSTER = "moviePoster";
        // COLUMN_MOVIE_NAME -> movieBackdrop
        public static final String COLUMN_MOVIE_BACKDROP = "movieBackdrop";
        // COLUMN_MOVIE_NAME -> movieRating
        public static final String COLUMN_MOVIE_RATING = "movieRating";
        // COLUMN_MOVIE_NAME -> movieReleaseDate
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movieReleaseDate";
        // COLUMN_MOVIE_NAME -> movieOverview
        public static final String COLUMN_MOVIE_OVERVIEW = "movieOverview";
    }
}
