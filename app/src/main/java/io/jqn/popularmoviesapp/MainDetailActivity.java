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

    private SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail);

        mBackdrop = findViewById(R.id.movie_backdrop);
        mPosterThumbnail = findViewById(R.id.movie_poster_thumbnail);
        mRating = findViewById(R.id.rating);
        mDateReleased = findViewById(R.id.release_date);
        mOverview = findViewById(R.id.overview);

        // Create a DB helper (this will crate the DB if run for the first time)
        MoviesDbHelper dbHelper = new MoviesDbHelper(this);

        // Keep a reference to the DB until paused or killed
        mDb = dbHelper.getReadableDatabase();

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
                        addNewFavorite(mMovie.getId(), mMovie.getTitle(), mMovie.getPosterPath());
                    } else {
                        // Remove from favorites table
                        Log.v(TAG, "unchecked");
                    }
                }
            });
        }

    }

    /**
     * Adds new favorite to the movie database
     * @param movieId the movie id from the moviedb payload
     * @param  movieName Movie name
     * @param moviePoster the path to the movie poster
     */
    private long addNewFavorite(String movieId, String movieName, String moviePoster) {
        // Creates a ContentValues instance to pass the values into the insert query
        ContentValues cv = new ContentValues();
        // Calls put to insert the movie id value with the key COLUMN_MOVIE_ID
        cv.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_ID, movieId);
        // Calls put to insert the movie name value with the key COLUMN_MOVIE_NAME
        cv.put(FavoritesEntry.COLUMN_MOVIE_NAME, movieName);
        // Calls put to insert the movie poster path value with the key COLUMN_MOVIE_POSTER
        cv.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_POSTER, moviePoster);

        return mDb.insert(FavoritesEntry.TABLE_NAME, null, cv);


    }
}
