package io.jqn.popularmoviesapp.models;

import java.io.Serializable;

/**
 * Model class represents a trailer
 */
public class Trailer implements Serializable {
    private String mId;
    private String mKey;
    private String mName;
    private String mSite;

    /**
     * Construct a new review object
     */
    public Trailer(String id, String key, String name, String site) {
        mId = id;
        mKey = key;
        mName = name;
        mSite = site;
    }
    // Gets the trailer ID
    public String getTrailerId() {
        return mId;
    }
    // Gets the trailer key
    public String getTrailerKey() {
        return mKey;
    }
    // Gets the trailer name
    public String getTrailerName() {
        return mName;
    }
    // Gets the trailer site
    public String getTrailerSite() {
        return  mSite;
    }
}
