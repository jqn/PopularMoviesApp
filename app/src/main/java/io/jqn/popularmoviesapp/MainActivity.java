package io.jqn.popularmoviesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get a reference to the ImageView
        ImageView imageView = findViewById(R.id.imageCell);
        // load an image into the ImageView
        Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
    }
}
