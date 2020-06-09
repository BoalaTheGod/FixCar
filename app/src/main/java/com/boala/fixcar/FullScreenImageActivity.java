package com.boala.fixcar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

public class FullScreenImageActivity extends AppCompatActivity {
TouchImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        imageView = findViewById(R.id.fullImage);
        Picasso.get().load(getIntent().getStringExtra("imgLink")).into(imageView);
    }
}