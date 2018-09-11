package io.jqn.popularmoviesapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.jqn.popularmoviesapp.R;
import io.jqn.popularmoviesapp.models.Trailer;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();
    // Member variable for trailers
    private List<Trailer> mTrailers;

    private final TrailerAdapterOnClickHandler mClikcHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler) {
        this.mClikcHandler = clickHandler;
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
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        int layoutIdForListItem = R.layout.movie_trailer_item;
        boolean shouldAttachToParentImmediately = false;

        // Inflate the custom layout
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailerAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, update the contents of the ViewHolder to display the review
     * authors and contents for each particular position, using the "position" argument that
     * is conveniently passed in.
     *
     * @param TrailerAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                contents of the item at the given position in the data set.
     * @param position                The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        // Get the data model based on position
        Trailer trailer = mTrailers.get(position);

        // Set item views based on the views and data model
        //trailerAdapterViewHolder.mTrailerKey.setText(trailer.getTrailerKey());
        trailerAdapterViewHolder.mTrailerName.setText(trailer.getTrailerName());
    }

    /**
     * This method simply returns the number of items to display.
     *
     * @return The number of items available on the review section.
     */
    @Override
    public int getItemCount() {
        if (mTrailers == null) {
            Log.v(TAG, "trailers are null " );
            return 0;
        } else {
            Log.v(TAG, "trailers are not null " +  mTrailers.size() );
            return mTrailers.size();
        }
    }

    /**
     * Provide a direct reference to each of the views within a data item
     * and cache the views within the item layout for fast access
     */
    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //public final TextView mTrailerTextView;
        /**
         * Set members variables for the view to be set as the render row
         */
        //public final TextView mTrailerKey;
        public final TextView mTrailerName;

        public TrailerAdapterViewHolder(View view) {
            super(view);
            //mTrailerKey =  view.findViewById(R.id.trailer_name);
            mTrailerName =  view.findViewById(R.id.trailer_site);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.v(TAG, "child view clicked");
            int adapterPosition = getAdapterPosition();
            mClikcHandler.onClick((mTrailers.get(adapterPosition)));
        }
    }

    public void setTrailerData(List<Trailer> trailerData) {
        Log.v(TAG, "trailer data adapter *******" + trailerData);
        mTrailers = trailerData;
        notifyDataSetChanged();
    }
}
