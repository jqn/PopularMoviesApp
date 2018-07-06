package io.jqn.popularmoviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import io.jqn.popularmoviesapp.R;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private ArrayList<String> mMoviePosters = new ArrayList<>();

    /**
     * Cache the children views for a forecast grid item
     */
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mMoviePosterImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mMoviePosterImageView = view.findViewById(R.id.movie_poster);
        }
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForGridItem = R.layout.movie_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForGridItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param movieAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder (MovieAdapterViewHolder movieAdapterViewHolder, int position) {
//        Log.v(TAG, "setting views");
//        setMoviePostersOnline(movieAdapterViewHolder, position);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mMoviePosters) return 0;
        return mMoviePosters.size();
    }

    /**
     * This method is used to set movie posters on a MovieAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new movieAdapter to display it.
     *
     * @param moviePosters The new movie data to be displayed.
     */
    public void setMovieposters(ArrayList<String> moviePosters) {
        for (int i = 0; i < moviePosters.size(); i++) {
            Log.v(TAG, "setmovieposters " + moviePosters.get(i));
        }
        Log.v(TAG, "setting movie posters" + moviePosters.size());
        mMoviePosters.clear();
        mMoviePosters.addAll(moviePosters);
        notifyDataSetChanged();
    }
}
