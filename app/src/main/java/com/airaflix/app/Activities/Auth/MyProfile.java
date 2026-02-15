package com.airaflix.app.Activities.Auth;


import static com.airaflix.app.Activities.Settings.SettingsJcartoon.SETTINGS_NAME;
import static com.airaflix.app.JPLAYER.JSPLAYER.hideSystemPlayerUi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.airaflix.app.API.BEANLINKS;
import com.airaflix.app.Activities.Settings.AuthManager;
import com.airaflix.app.Activities.ViewPhoto;
import com.airaflix.app.Models.UserModel;
import com.airaflix.app.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends AppCompatActivity {


    CircleImageView userimg;
    ImageView verify,backch,usercover,morenow;
    TextView nameuser,addate,followingcount,followescount,postscount,commentcount,
            noshowlist,nolist,replaycount,ratecount,nadriatcount,emailshow;
    TextView profildesc,goplans1;
    RecyclerView favoritcartrecy,favormovirecy,castfavo;
    BottomSheetDialog mylistdialog;
    ProgressBar progwait;



    RelativeLayout titlera,flollownow,unflollow,mylists,progreswait,allprofil,mylisto,outsign;
    LinearLayout allmypost,myfollowrslin,creatpost,editacc,creatnadaria;

    ScrollView scrollvv;

    String MyID;

    List<UserModel> userModels;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        setContentView(R.layout.activity_my_profile);

        //Linear
        scrollvv = findViewById(R.id.scrollvv);
        titlera = findViewById(R.id.titlera);
        flollownow = findViewById(R.id.flollownow);
        unflollow = findViewById(R.id.unflollow);
        mylisto = findViewById(R.id.mylisto);
        progreswait = findViewById(R.id.progreswait);
        allmypost = findViewById(R.id.allmypost);
        allprofil = findViewById(R.id.allprofil);
        outsign = findViewById(R.id.outsign);



        userimg = findViewById(R.id.userimg);
        backch = findViewById(R.id.backch);
        verify = findViewById(R.id.verify);
        nameuser = findViewById(R.id.nameuser);
        usercover = findViewById(R.id.usercover);
        addate = findViewById(R.id.addate);
        profildesc = findViewById(R.id.profildesc);

        postscount = findViewById(R.id.postscount);

        morenow = findViewById(R.id.morenow);
        progwait = findViewById(R.id.progwait);

        emailshow = findViewById(R.id.emailshow);
        goplans1 = findViewById(R.id.goplans1);



        backch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        MyID = getIntent().getStringExtra("userid");


        GetUserInfo(new AuthManager(getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE)).getUserInfo());



       /* GetCommentsCount(MyID);
        GetReplayCount(MyID);
        GetRateCount(MyID);
        GetNadariatCount(MyID);
        GetPostCount(MyID);
        GetUserFollowing(MyID);
        GetUserFollowers(MyID);*/
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept","application/json");
        headers.put("User-Agent","Thunder Client (https://www.thunderclient.com)");


        outsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AndroidNetworking.get(BEANLINKS.BASELINK+"logout")
                        .addHeaders(headers)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener( ) {
                            @Override
                            public void onResponse(JSONArray response) {

                                Log.d("TAG", "onResponse: +   "+response);
                                finish();

                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });


            }
        });




     /*   editacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfile.this, EditProfil.class);
                intent.putExtra("fullname",fullname);
                intent.putExtra("username",specialusername);
                intent.putExtra("cover",cover);
                intent.putExtra("profile",imageprofil);
                intent.putExtra("desc",desc);
                intent.putExtra("email",email);
                intent.putExtra("password",password);
                intent.putExtra("isking",isking);
                intent.putExtra("showlist",showlist);

                startActivity(intent);

            }
        });*/


    /*    morenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylistdialog = new BottomSheetDialog(MyProfile.this,R.style.BottomSheetStyle);
                View listfav = LayoutInflater.from(MyProfile.this).inflate(R.layout.bottomsheet_postmore,(LinearLayout)v.findViewById(R.id.sheet));

                RelativeLayout deletpost = listfav.findViewById(R.id.deletpost);
                deletpost.setVisibility(View.GONE);
                RelativeLayout editpost = listfav.findViewById(R.id.editpost);
                editpost.setVisibility(View.GONE);
                RelativeLayout copytext = listfav.findViewById(R.id.copytext);
                copytext.setVisibility(View.GONE);



                RelativeLayout reportpost = listfav.findViewById(R.id.reportpost);
                reportpost.setVisibility(View.GONE);
                RelativeLayout sharethepost = listfav.findViewById(R.id.sharethepost);

                if (FirebaseAuth.getInstance().getCurrentUser() != null){





                    //Share text
                    sharethepost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mylistdialog.cancel();
                            SharePost(MyID,desc,imageprofil,fullname);
                        }
                    });




                }




                mylistdialog.setContentView(listfav);
                mylistdialog.show();
            }
        });*/



    }

    private void GetUserInfo(UserModel userModel) {



            emailshow.setText(userModel.getEmail());
            nameuser.setText(userModel.getName());


            goplans1.setVisibility(View.GONE);


            //Image Profil
            Glide.with(MyProfile.this).load(userModel.getProfil()).into(userimg);

            //Image Cover

          Glide.with(MyProfile.this).load(R.drawable.spbg).into(usercover);



            userimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MyProfile.this, ViewPhoto.class);
                    i.putExtra("photo",userModel.getProfil());
                    startActivity(i);
                }
            });




                profildesc.setText("");



                verify.setVisibility(View.GONE);





            addate.setText("");

            progwait.setVisibility(View.GONE);
            usercover.setVisibility(View.VISIBLE);
            allprofil.setVisibility(View.VISIBLE);




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



    public static String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("yyyy-MM-dd",calendar).toString();
        return date;


    }



    private void SharePost(String postkey, String text, String image, String fullname) {

        // created long link
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://jcartoonprofil.page.link/?id="+postkey))
                .setDynamicLinkDomain("jcartoonprofil.page.link")

                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("jcartoonprofil.page.link").build())


                .setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder()
                        .setTitle(" حساب "+fullname)
                        .setDescription(text)
                        .setImageUrl(Uri.parse(image))
                        .build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();


        //for creating short link
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLink.getUri())
                .buildShortDynamicLink()
                .addOnCompleteListener((Activity) MyProfile.this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {

                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();



                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT,shortLink.toString());
                            intent.setType("text/plain");
                            startActivity(intent);
                        } else {
                            Toast.makeText(MyProfile.this,"error :"+task.getException(), Toast.LENGTH_SHORT).show();

                            // Error
                            // ...
                        }
                    }
                });
    }




}