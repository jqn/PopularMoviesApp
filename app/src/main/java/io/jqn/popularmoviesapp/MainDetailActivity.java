package io.jqn.popularmoviesapp;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.jqn.popularmoviesapp.adapter.ReviewAdapter;
import io.jqn.popularmoviesapp.adapter.TrailerAdapter;
import io.jqn.popularmoviesapp.data.MoviesContract;
import io.jqn.popularmoviesapp.models.Movie;
import io.jqn.popularmoviesapp.models.Review;
import io.jqn.popularmoviesapp.models.Trailer;
import io.jqn.popularmoviesapp.tasks.FetchMovieFeaturesTask;
import io.jqn.popularmoviesapp.tasks.FetchTrailersTask;

public class MainDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    public static final String TAG = MainDetailActivity.class.getSimpleName();

    private Movie mMovie;
    private ImageView mBackdrop;
    private ImageView mPosterThumbnail;
    private TextView mRating;
    private TextView mDateReleased;
    private TextView mOverview;

    private RecyclerView mRecyclerView;
    private RecyclerView mTrailerRecyclerView;
    private RecyclerView.LayoutManager mLayoutmanager;

    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView.LayoutManager mLayoutmanagerTrailers;

    private final String BASE_YOUTUBE_URL_APP = "vnd.youtube:";
    private final String BASE_YOUTUBE_URL_WEB = "http://www.youtube.com/watch?v=";

    private BottomSheetBehavior mReviewBottomSheetBehavior;
    private BottomSheetBehavior mTrailerBottomSheetBehavior;

    public static Parcelable mTrailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail);

        mBackdrop = findViewById(R.id.movie_backdrop);
        mPosterThumbnail = findViewById(R.id.movie_poster_thumbnail);
        mRating = findViewById(R.id.rating);
        mDateReleased = findViewById(R.id.release_date);
        mOverview = findViewById(R.id.overview);

        mMovie = (Movie) getIntent().getExtras().getSerializable("movie");

        Picasso.get().load("http://image.tmdb.org/t/p/w1280".concat(mMovie.getBackdropPath())).into(mBackdrop);
        Picasso.get().load("http://image.tmdb.org/t/p/w500".concat(mMovie.getPosterPath())).into(mPosterThumbnail);

        setTitle(mMovie.getTitle());
        String userRating = mMovie.getUserRating();
        String newUserRating = userRating + "/10";

        mRating.setText(newUserRating);
        mDateReleased.setText(mMovie.getReleaseDate());
        mOverview.setText(mMovie.getOverview());

        ToggleButton toggle = findViewById(R.id.favorite_toggle);

        SharedPreferences preferences = getSharedPreferences("favoritePrefs", Context.MODE_PRIVATE);

        boolean checked = preferences.getBoolean(mMovie.getId(), false);

        toggle.setChecked(checked);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences;
                if (isChecked) {
                    sharedPreferences = getSharedPreferences("favoritePrefs", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(mMovie.getId(), true);
                    editor.commit();

                    /// New content values object
                    ContentValues contentValues = new ContentValues();

                    // Add to favorites table
                    contentValues.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_ID, mMovie.getId());
                    contentValues.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_NAME, mMovie.getTitle());
                    contentValues.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_OVERVIEW, mMovie.getOverview());
                    contentValues.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE, mMovie.getReleaseDate());
                    contentValues.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_RATING, mMovie.getUserRating());
                    contentValues.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_BACKDROP, mMovie.getBackdropPath());
                    contentValues.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_POSTER, mMovie.getPosterPath());

                    Uri uri = getContentResolver().insert(MoviesContract.FavoritesEntry.CONTENT_URI, contentValues);

                } else {
                    SharedPreferences editor = getSharedPreferences("favoritePrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = editor.edit();
                    edit.putBoolean(mMovie.getId(), false);
                    edit.apply();

                    // Remove from favorites table
                    String id = mMovie.getId();
                    Uri uri = MoviesContract.FavoritesEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(id).build();
                    getContentResolver().delete(uri, null, null);
                }
            }
        });
        // Bottom sheets
        View reviewBottomSheet = findViewById(R.id.reviews_bottom_sheet);
        mReviewBottomSheetBehavior = BottomSheetBehavior.from(reviewBottomSheet);

        View trailerBottomSheet = findViewById(R.id.trailers_bottom_sheet);
        mTrailerBottomSheetBehavior = BottomSheetBehavior.from(trailerBottomSheet);

        // RecyclerViews
        mRecyclerView = findViewById(R.id.movie_reviews_bottom);
        mReviewAdapter = new ReviewAdapter();
        mRecyclerView.setAdapter(mReviewAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mTrailerRecyclerView = findViewById(R.id.movie_trailers_bottom);
        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mTrailerRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        // LayoutManagers
        mLayoutmanager = new LinearLayoutManager(this);
        mLayoutmanagerTrailers = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutmanager);
        mTrailerRecyclerView.setLayoutManager(mLayoutmanagerTrailers);

        final Button reviews = findViewById(R.id.reviews);

        Button trailers = findViewById(R.id.trailers);

        trailers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTrailers("movie", mMovie.getId(), "videos");
            }
        });

        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadReviews("movie", mMovie.getId(), "reviews");
            }
        });


    }

    public void loadReviews(String feature, String id, String filter) {
        NetworkInfo networkInfo = getNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new FetchMovieFeaturesTask(this).execute(feature, id, filter);
        } else {
            showSnackBar("Please check your connection");
        }
    }

    public void loadTrailers(String feature, String id, String filter) {
        NetworkInfo networkInfo = getNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new FetchTrailersTask(this).execute(feature, id, filter);
        } else {
            showSnackBar("Please check your connection");
        }
    }

    public void onClick(Trailer trailer) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_YOUTUBE_URL_APP + trailer.getTrailerKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(BASE_YOUTUBE_URL_WEB + trailer.getTrailerKey()));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    public void setMovieReviews(List<Review> movieReviews) {
        if (movieReviews.isEmpty()) {
            showSnackBar("No review content available");
        } else {
            mReviewAdapter.setReviewData(movieReviews);
            mReviewBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public void setMovieTrailers(List<Trailer> movieTrailers) {
        if (movieTrailers.isEmpty()) {
            showSnackBar("No review content available");
        } else {
            mTrailerAdapter.setTrailerData(movieTrailers);
            mTrailerBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

    }

    public void showSnackBar(String message) {
        View view = findViewById(R.id.details_wrapper);
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }


    private NetworkInfo getNetworkInfo() {
        // Get a reference to the ConnectivityManager to check state of network connectivity.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        return connMgr.getActiveNetworkInfo();
    }


}
