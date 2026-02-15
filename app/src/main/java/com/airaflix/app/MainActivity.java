package com.airaflix.app;

import static com.airaflix.app.Config.config.SETTINGS_NAME;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.airaflix.app.Activities.Auth.LoginTojcartoon;
import com.airaflix.app.Activities.Jdownload.Downloades;
import com.airaflix.app.Activities.MyList.theList;
import com.airaflix.app.Activities.UpdateActivity;
import com.airaflix.app.Config.Animator;
import com.airaflix.app.Fragments.home;
import com.airaflix.app.Fragments.more;
import com.airaflix.app.Fragments.movies;
import com.airaflix.app.Fragments.search;
import com.airaflix.app.Fragments.series;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

public class MainActivity extends AppCompatActivity {



    private long pressedTime;
    private Toast backToast;
    private SharedPreferences sp;

    ImageView statusBar;
    ImageView navigationBar;
    BottomNavigationView navi;
    FragmentManager fragmentManager;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        setContentView(R.layout.activity_main);
        StartAppSDK.init(this, "201688868", false);
        StartAppSDK.enableReturnAds(false);
        StartAppAd.disableSplash();
        sp = getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
        firebaseFirestore = FirebaseFirestore.getInstance();

        statusBar = findViewById(R.id.status_bar);
        navigationBar = findViewById(R.id.navigation_bar);
        statusBar.getLayoutParams().height = getStatusBarHeight();
        navigationBar.getLayoutParams().height = getNavigationBarHeight();



        FirebaseApp.initializeApp(this);

        navi = findViewById(R.id.navi);

        InslizBottonBar(savedInstanceState);

        CheckIfTherNewUpdates();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            CheckIfUserPaid();
        }



    }

    private void CheckIfUserPaid() {
        DocumentReference dc = firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()){
                    String paidstat = documentSnapshot.getString("ispaid");
                    if (paidstat.equals("1")){
                        sp.edit().putString("ispaid","1").apply();
                    }else {
                        sp.edit().putString("ispaid","0").apply();
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void CheckIfTherNewUpdates() {


        DatabaseReference dbup = FirebaseDatabase.getInstance().getReference("updates").child("update1");

        dbup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String link = snapshot.child("link").getValue(String.class);
                Long version = snapshot.child("versionnumber").getValue(Long.class);

                if (version > getVersionCode()){
                    Intent i = new Intent(MainActivity.this, UpdateActivity.class);
                    i.putExtra("link",link);
                    startActivity(i );
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void InslizBottonBar(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            navi.setSelectedItemId(R.id.foru);


            fragmentManager = getSupportFragmentManager();
            home homeFragment = new home();

            fragmentManager.beginTransaction().replace(R.id.fream, homeFragment)
                    .commit();


        }

        navi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                final Fragment[] fragment = {null};
                switch (item.getItemId()) {
                    case R.id.foru:


                        fragment[0] = new home();
                        break;
                    case R.id.serie:

                        fragment[0] = new search();
                        break;
                    case R.id.movie:

                        fragment[0] = new theList();
                        break;

                    case R.id.search:

                        fragment[0] = new Downloades();
                        break;
                    case R.id.profi:
                        fragment[0] = new more();

                        break;

                }

                if (fragment[0] != null) {
                    fragmentManager = getSupportFragmentManager();

                    fragmentManager.beginTransaction().replace(R.id.fream, fragment[0])
                            .commit();
                }

                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            finish();
            return;
        } else {
            backToast = Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT);
            backToast.show();
        }


        pressedTime = System.currentTimeMillis();
    }

    private PackageInfo pInfo;
    public int getVersionCode() {
        pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {

            Log.i("MYLOG", "NameNotFoundException: "+e.getMessage());
        }
        return pInfo.versionCode;
    }

    public int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }
    Point navigationBarSize = new Point();
    public int getNavigationBarHeight() {
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navigationBarSize.y = getResources().getDimensionPixelSize(resourceId);
        }
        return navigationBarSize.y;
    }

    public void hideStatusBar(){

        if(statusBar.getAlpha() > 0) {
            Animator animator = new Animator();
            animator.AnimateOpacity(statusBar, 0, 400);
            animator.setAnimationListener(new Animator.AnimationListener() {
                @Override
                public void onAnimationFinish() {
                    statusBar.setVisibility(View.GONE);

                }
            });
        }
    }
    public void showStatusBar(){
        if(statusBar.getAlpha() < 1) {
            Animator animator = new Animator();
            animator.AnimateOpacity(statusBar, 1, 400);
            animator.setAnimationListener(new Animator.AnimationListener() {
                @Override
                public void onAnimationFinish() {
                    statusBar.setVisibility(View.VISIBLE);

                }
            });
        }
    }

   /* @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginTojcartoon.class));
        }
    }*/
}