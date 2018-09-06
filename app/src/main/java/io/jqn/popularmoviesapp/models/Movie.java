package io.jqn.popularmoviesapp.models;

import java.io.Serializable;

/**
 * Model class for a movie
 */
public class Movie implements Serializable {

    private String mTitle;
    private String mId;
    private String mPosterPath;
    private String mBackdrop;
    private String mUserRating;
    private String mReleaseDate;
    private String mOverview;


    /**
     * Construct a new Movie object
     */
    public Movie(String id, String title, String posterPath, String backdrop, String userRating, String releaseDate, String overview) {
        mId = id;
        mTitle = title;
        mPosterPath = posterPath;
        mBackdrop = backdrop;
        mUserRating = userRating;
        mReleaseDate = releaseDate;
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

    public String getBackdropPath() {
        return mBackdrop;
    }

    public String getUserRating() {
        return mUserRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getOverview() {
        return mOverview;
    }


}
