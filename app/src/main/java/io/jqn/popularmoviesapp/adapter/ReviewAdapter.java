package io.jqn.popularmoviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.jqn.popularmoviesapp.R;
import io.jqn.popularmoviesapp.models.Review;

/**
 * Create the basic adapter extending from RecylcerView.Adapter
 * This populates data into the RecyclerView
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();
    // Member variable for reviews
    private List<Review> mReviews;


    public ReviewAdapter() {

    }

    /**
     * This gets called when ach new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If RecyclerView has more than one type of item (which this one don't)
     *                  this viewType can be used to provide a different layout.
     * @return A new ReviewAdapterViewHolder that holds the View for each list item
     */
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        int layoutIdForListItem = R.layout.movie_review_item;
        boolean shouldAttachToParentImmediately = false;

        // Inflate the custom layout
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, update the contents of the ViewHolder to display the review
     * authors and contents for each particular position, using the "position" argument that
     * is conveniently passed in.
     *
     * @param reviewAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                contents of the item at the given position in the data set.
     * @param position                The position of the item within the adapter's data set.
     *                                //
     */
    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder reviewAdapterViewHolder, int position) {
        // Get the data model based on position
        Review review = mReviews.get(position);

        // Set item views based on the views and data model
        reviewAdapterViewHolder.mReviewAuthor.setText(review.getReviewAuthor());
        reviewAdapterViewHolder.mReviewContent.setText(review.getReviewContent());
    }

    /**
     * This method simply returns the number of items to display.
     *
     * @return The number of items available on the review section.
     */
    @Override
    public int getItemCount() {
        if (mReviews == null) {
            return 0;
        } else {
            return mReviews.size();
        }
    }

    public void setReviewData(List<Review> reviewData) {
        mReviews = reviewData;
        notifyDataSetChanged();
    }

    /**
     * Provide a direct reference to each of the views within a data item
     * and cache the views within the item layout for fast access
     */
    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        /**
         * Set members variables for the view to be set as the render row
         */
        public final TextView mReviewAuthor;
        public final TextView mReviewContent;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            mReviewAuthor = view.findViewById(R.id.review_author);
            mReviewContent = view.findViewById(R.id.review_content);
        }
    }
}