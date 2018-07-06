package io.jqn.popularmoviesapp.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.jqn.popularmoviesapp.R;
import io.jqn.popularmoviesapp.models.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.NumberViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private int mNumberItems;

    private List<Movie> mMovies;

    /**
     * Constructor for MovieAdapter. Accepts a number of itmes to display and the specification for
     * GridItemClickListener.
     *
     * @param
     */
    public MovieAdapter(List<Movie> movies) {
        mMovies = movies;
    }

    /**
     * This gets called when each new ViewHolder is crated. This happens when the RecyclerView
     * is laid out.
     */
    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        return viewHolder;
    }


    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        return mNumberItems;
    }


    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        // Get the thumbnail with Path
        String thumbnail = "https://api.themoviedb.org/3/w185" + movie.getPosterPath();

        Picasso.get()
                .load(thumbnail)
                .into(holder.listItemNumberView);
        Log.v(TAG, "poster path" + thumbnail);


    }

    class NumberViewHolder extends RecyclerView.ViewHolder {
        // Will display the position in the list, ie 0 through getItemCount() - 1
        ImageView listItemNumberView;
        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         * @param itemView The View that you inflated in
         *                 {@link MovieAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public NumberViewHolder(View itemView) {
            super(itemView);
            Log.v(TAG, "viewholder called");
            listItemNumberView = itemView.findViewById(R.id.movie_poster);
        }

        /**
         * A method we wrote for convenience. This method will take an integer as input and
         * use that integer to display the appropriate text within a list item.
         * @param listIndex Position of the item in the list
         */
//        void bind(int listIndex) {
////            listItemNumberView.setText(String.valueOf(listIndex));
////        }
    }
}
