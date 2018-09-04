package io.jqn.popularmoviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import io.jqn.popularmoviesapp.data.MoviesContract;
import io.jqn.popularmoviesapp.data.MoviesDbHelper;
import io.jqn.popularmoviesapp.models.Movie;
import io.jqn.popularmoviesapp.utilities.FetchMovieFeaturesTask;

import static io.jqn.popularmoviesapp.data.MoviesContract.*;

public class MainDetailActivity extends AppCompatActivity {
    public static final String TAG = MainDetailActivity.class.getSimpleName();

    private Movie mMovie;
    private ImageView mBackdrop;
    private ImageView mPosterThumbnail;
    private TextView mRating;
    private TextView mDateReleased;
    private TextView mOverview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail);

        mBackdrop = findViewById(R.id.movie_backdrop);
        mPosterThumbnail = findViewById(R.id.movie_poster_thumbnail);
        mRating = findViewById(R.id.rating);
        mDateReleased = findViewById(R.id.release_date);
        mOverview = findViewById(R.id.overview);


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            intentThatStartedThisActivity.hasExtra("movie");
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
            Log.v(TAG, "favorite" + checked);
            toggle.setChecked(checked);

            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    /* Create a DB helper (this will crate the DB if run for the first time) */
                    MoviesDbHelper db = new MoviesDbHelper(getApplicationContext());
                    SharedPreferences sharedPreferences;
                    if (isChecked) {
                        sharedPreferences = getSharedPreferences("favoritePrefs", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(mMovie.getId(), true);
                        editor.commit();

                        // Add to favorites table
                        Log.v(TAG, "insert favorite");
                        db.addFavorite(mMovie.getId(), mMovie.getTitle(), mMovie.getPosterPath(), mMovie.getBackdropPath(), mMovie.getUserRating(), mMovie.getReleaseDate(), mMovie.getOverview());
                    } else {
                        SharedPreferences editor = getSharedPreferences("favoritePrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = editor.edit();
                        edit.putBoolean(mMovie.getId(), false);
                        edit.apply();

                        // Remove from favorites table
                        Log.v(TAG, "delete favorite");
                        db.removeFavorite(mMovie.getTitle());
                    }
                }
            });

            Button reviews = findViewById(R.id.reviews);

            Button trailers = findViewById(R.id.trailers);

            trailers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG, "trailers clicked");
                    loadTrailers("movie", mMovie.getId(), "videos");
                }
            });

            reviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG, "reviews clicked");
                    loadReviews("movie", mMovie.getId(), "reviews");
                }
            });
        }

    }

    public void loadReviews(String feature, String id, String filter) {
        new FetchMovieFeaturesTask(this).execute(feature, id, filter);
    }

    public void loadTrailers(String feature, String id, String filter) {
        new FetchMovieFeaturesTask(this).execute(feature, id, filter);
    }

}
