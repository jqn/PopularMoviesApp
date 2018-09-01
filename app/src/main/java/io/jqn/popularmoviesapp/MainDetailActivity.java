package io.jqn.popularmoviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import io.jqn.popularmoviesapp.data.MoviesContract;
import io.jqn.popularmoviesapp.data.MoviesDbHelper;
import io.jqn.popularmoviesapp.models.Movie;

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
            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // Add to favorites table
                        Log.v(TAG, "checked");
                        // Create a DB helper (this will crate the DB if run for the first time)
                        MoviesDbHelper db = new MoviesDbHelper(getApplicationContext());
                        db.addFavorite(mMovie.getId(), mMovie.getTitle(), mMovie.getPosterPath(), mMovie.getBackdropPath(), mMovie.getUserRating(), mMovie.getReleaseDate(), mMovie.getOverview());
                    } else {
                        // Remove from favorites table
                        Log.v(TAG, "unchecked");
                    }
                }
            });
        }

    }

}
