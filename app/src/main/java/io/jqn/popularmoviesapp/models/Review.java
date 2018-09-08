package io.jqn.popularmoviesapp.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Model class represents a review
 */
public class Review implements Serializable {
    private String mId;
    private String mAuthor;
    private String mContent;
    private String mUrl;

    /**
     * Construct a new review object
     */
    public Review(String id, String author, String content, String url) {
        mId = id;
        mAuthor = author;
        mContent = content;
        mUrl = url;
    }
    // Gets the review ID
    public String getReviewId() {
        return mId;
    }
    // Gets the author name
    public String getReviewAuthor() {
        return mAuthor;
    }
    // Gets the review content
    public String getReviewContent() {
        return mContent;
    }
    // Gets the review url
    public String getReviewUrl() {
        return  mUrl;
    }

}
