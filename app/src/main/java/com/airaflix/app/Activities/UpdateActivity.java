package com.airaflix.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.airaflix.app.R;

public class UpdateActivity extends AppCompatActivity {

    ImageView clos;
    RelativeLayout updtnow;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        clos = findViewById(R.id.clos);
        updtnow = findViewById(R.id.updtnow);

        String link = getIntent().getStringExtra("link");


        clos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updtnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chromeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                chromeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                chromeIntent.setPackage("com.android.chrome");

                try {
                    startActivity(chromeIntent);
                } catch (ActivityNotFoundException e) {
                    Intent defaultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(defaultIntent);
                }

            }
        });

    }
}