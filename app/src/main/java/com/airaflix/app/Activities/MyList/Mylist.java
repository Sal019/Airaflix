package com.airaflix.app.Activities.MyList;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.airaflix.app.Activities.MyList.lista.favoritSeries;
import com.airaflix.app.Activities.MyList.lista.favoritlcharachter;
import com.airaflix.app.Activities.MyList.lista.favoritlmovie;
import com.airaflix.app.R;
import com.google.android.material.tabs.TabLayout;

public class Mylist extends AppCompatActivity {

   MyViewPagerAdapter myViewPagerAdapter;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ImageView backch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changestatucolor(this);
        setContentView(R.layout.activity_mylist);

        tabLayout =  findViewById(R.id.tablay);
        viewPager2 =  findViewById(R.id.viewPager);
        backch =  findViewById(R.id.backch);

        backch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
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
                    return new favoritlmovie();


                default:
                    return new favoritSeries();
            }

        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

    public static void changestatucolor(Activity activity) {
        Window window = activity.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(activity,R.color.amoled_primary_color));
        }
    }

}