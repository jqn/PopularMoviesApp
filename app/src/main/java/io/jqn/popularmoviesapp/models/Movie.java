package io.jqn.popularmoviesapp.models;

import java.io.Serializable;

/**
 * Model class for a movie
 */
public class Movie implements Serializable {

    private String mTitle;
    private String mId;
    private String mPosterPath;
    private String mOverview;


    /**
     * Construct a new Movie object
     */
    public Movie(String id, String title, String posterPath, String overview) {
        mId = id;
        mTitle = title;
        mPosterPath = posterPath;
        mOverview = overview;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPosterPath() {

        return mPosterPath;
    }


    public String getOverview() {
        return mOverview;
    }


}
