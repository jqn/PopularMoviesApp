package io.jqn.popularmoviesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.jqn.popularmoviesapp.models.Movie;

public class MainDetailActivity extends AppCompatActivity {
    public static final String TAG = MainDetailActivity.class.getSimpleName();

    private Movie mMovie;
    private ImageView mBackdrop;
    private ImageView mPosterThumbnail;
    private TextView mTitle;
    private TextView mRating;
    private TextView mDateReleased;
    private TextView mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail);

        mBackdrop = findViewById(R.id.movie_backdrop);
        mPosterThumbnail = findViewById(R.id.movie_poster_thumbnail);
        mTitle = findViewById(R.id.title);
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
            mTitle.setText(mMovie.getTitle());
            mRating.setText(mMovie.getUserRating());
            mDateReleased.setText(mMovie.getReleaseDate());
            mOverview.setText(mMovie.getOverview());
        }

    }
}
