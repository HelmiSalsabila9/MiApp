package com.hels.miapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    TextView sosmed, ig, gh, in, web;
    ImageView img_ig, img_gh, img_in, img_web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        sosmed = findViewById(R.id.sosmed);
        ig = findViewById(R.id.ig);
        gh = findViewById(R.id.gh);
        in = findViewById(R.id.in);
        web = findViewById(R.id.web);
        img_ig = findViewById(R.id.img_ig);
        img_gh = findViewById(R.id.img_gh);
        img_in = findViewById(R.id.img_in);
        img_web = findViewById(R.id.img_web);
    }
}