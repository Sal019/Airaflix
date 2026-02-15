package com.airaflix.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airaflix.app.R;

import java.util.List;

public class Premium extends AppCompatActivity {

    ImageView gomyacc1;
    RelativeLayout goplans1;
    RelativeLayout goplans2;
    RelativeLayout goplans3;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        gomyacc1 = findViewById(R.id.gomyacc1);
        goplans1 = findViewById(R.id.goplans1);
        goplans2 = findViewById(R.id.goplans2);
        goplans3 = findViewById(R.id.goplans3);

        gomyacc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });goplans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGmail("plan1");
            }
        });goplans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGmail("plan2");
            }
        });goplans3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGmail("plan3");
            }
        });

    }


    private void OpenGmail(String plantype) {

        String message;

        switch (plantype) {
            case "plan1":
                message = "You have a basic plan.";
                break;
            case "plan2":
                message = "You have a premium plan.";
                break;
            case "plan3":
                message = "You have an ultimate plan.";
                break;
            default:
                message = "Invalid plan type.";
                break;
        }

        String email = "jcartoon2022@gmail.com";
        String subject = "Premium Account";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        // Check if the Gmail app is installed on the device
        boolean installed = isAppInstalled("com.google.android.gm");
        if (installed) {
            intent.setPackage("com.google.android.gm");
            startActivity(intent);
        } else {
            // If the app is not installed, open the email app
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("mailto:"+email));
            startActivity(intent);
        }



    }

    private boolean isAppInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {

            return false;
        }
    }
}