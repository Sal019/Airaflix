package com.airaflix.app.Activities;


import static com.airaflix.app.Activities.MyList.lista.favoritlmovie.allseriesandmovies;
import static com.airaflix.app.JPLAYER.JSPLAYER.hideSystemPlayerUi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.airaflix.app.Activities.MyList.lista.castmovies;
import com.airaflix.app.Activities.MyList.lista.castseries;
import com.airaflix.app.Fragments.movies;
import com.airaflix.app.Models.Cast;
import com.airaflix.app.Models.SerieModel;
import com.airaflix.app.R;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Chrachters extends AppCompatActivity {

    ImageView backch,usercover,userimg;
    TextView chrachtername,nameuser,postscount;
    RelativeLayout flollownow,unflollow,progreswait,allprofil;
    ViewPager2 viewPager;
    TabLayout tablay;
    ProgressBar progwait;

    String title,type,image,castid,postkey;
    Boolean isInMyFollow = false;

    Boolean testclick = false;
    List<Cast> castModel;
    List<SerieModel> serieModel,sscount;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_chrachters);

        backch = findViewById(R.id.backch);
        usercover = findViewById(R.id.usercover);
        userimg = findViewById(R.id.userimg);
        chrachtername = findViewById(R.id.chrachtername);
        nameuser = findViewById(R.id.nameuser);
        postscount = findViewById(R.id.postscount);
        flollownow = findViewById(R.id.flollownow);
        unflollow = findViewById(R.id.unflollow);
        viewPager = findViewById(R.id.viewPager);
        tablay = findViewById(R.id.tablay);
        progreswait = findViewById(R.id.progreswait);
        progwait = findViewById(R.id.progwait);
        allprofil = findViewById(R.id.allprofil);

        backch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        //Get Intent
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        image = getIntent().getStringExtra("image");
        castid = getIntent().getStringExtra("castid");
        postkey = getIntent().getStringExtra("postkey");



        //Get Info
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            isinMyFavourite(postkey);
        }



        GetNumberofSeries(castid);





        //Text
        chrachtername.setText(title);
        nameuser.setText(title);

        //set Chrachter IMG
        Glide.with(this).load(image).into(userimg);
        Glide.with(this).load(image).into(usercover);
        userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Chrachters.this,ViewPhoto.class);
                i.putExtra("photo",image);
                startActivity(i);
            }
        });






        GetCastSeriesAndComisc();



    }



    private void GetCastSeriesAndComisc() {

        viewPager.setAdapter(new MyViewPagerAdapter(Chrachters.this));

        tablay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tablay.getTabAt(position).select();
            }
        });

    }
    public class MyViewPagerAdapter extends FragmentStateAdapter {
        public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 1:
                    return new castmovies().getintentt(castid);

                default:
                    return new castseries().getintentt(castid);


            }

        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }


    private void DeletFavourit(String postkey) {

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DatabaseReference dblikes = FirebaseDatabase.getInstance().getReference("CharchtersLikes");

        dblikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(testclick==true)
                {
                       dblikes.child(postkey).child(userid).removeValue();
                        testclick=false;


                        dblikes.child(postkey).child(userid).setValue(true);
                        testclick=false;




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void AddFavorite(String myuserid, String postkey, HashMap<String, String> DATA) {

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DatabaseReference dblikes = FirebaseDatabase.getInstance().getReference("CharchtersLikes");

        dblikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(testclick==true)
                {


                        dblikes.child(postkey).child(userid).setValue(true);
                        testclick=false;




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void GetNumberofSeries(String castid) {

        allseriesandmovies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sscount = new ArrayList<>();
                for (DataSnapshot ss:snapshot.getChildren()) {
                    SerieModel sr = ss.getValue(SerieModel.class);
                    if (sr.getCast().contains(castid)){
                        sscount.add(sr);
                    }

                }
                postscount.setText(""+sscount.size());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }





    private void isinMyFavourite(String postkey) {

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

      /*  DatabaseReference dblikes = FirebaseDatabase.getInstance().getReference("CharchtersLikes");
        dblikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(postkey).hasChild(userid))
                {

                    int likecounts=(int)dataSnapshot.child(postkey).getChildrenCount();
                    flollownow.setVisibility(View.GONE);
                    unflollow.setVisibility(View.VISIBLE);







                }
                else
                {
                    int likecounts=(int)dataSnapshot.child(postkey).getChildrenCount();
                    fanscount.setText(likecounts+"");
                    flollownow.setVisibility(View.VISIBLE);
                    unflollow.setVisibility(View.GONE);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/




        progwait.setVisibility(View.GONE);
        usercover.setVisibility(View.VISIBLE);
        allprofil.setVisibility(View.VISIBLE);
    }


}