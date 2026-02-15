package com.airaflix.app;

import static com.airaflix.app.Activities.Settings.SettingsJcartoon.SETTINGS_NAME;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.airaflix.app.API.BEANLINKS;
import com.airaflix.app.Activities.ANOUCEPAGE;
import com.airaflix.app.Activities.MySeasons;
import com.airaflix.app.Config.HelperUtils;
import com.airaflix.app.Config.getComicsnextback;
import com.airaflix.app.Models.SerieModel;
import com.airaflix.app.Models.ServerModel;
import com.airaflix.app.Models.SkipModel;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.applovin.sdk.AppLovinSdk;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SplashScreen extends AppCompatActivity {


    HashMap<String, String> headers;
    private boolean vpnStatus;
    private HelperUtils helperUtils;


    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, initializationStatus -> {});

        AppLovinSdk.getInstance(this).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( this, configuration -> {
            //AppLovin SDK is initialized, start loading ads
        });
        StartAppSDK.init(this, getResources().getString(R.string.startapp_id), false);
        StartAppAd.disableSplash();
        StartAppSDK.enableReturnAds(false);





        headers = new HashMap<String, String>();
        headers.put("Accept","application/json");
        headers.put("User-Agent","Thunder Client (https://www.thunderclient.com)");

        initialisingVPN();



    }

    private void initialisingVPN() {

        helperUtils = new HelperUtils(SplashScreen.this);
        vpnStatus = helperUtils.isVpnConnectionAvailable();
        if (vpnStatus) {
            helperUtils.showWarningDialog(SplashScreen.this, getString(R.string.vpn_detected), getString(R.string.close_vpn));
            return;
        }else {
            InitialisingAds();
            loadlocale();

        }

    }

    private void InitalisngLanguage() {




        final String[] listlang = {"English","Spanish"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this,R.style.MyAlertDialogTheme);
        builder.setTitle("Choose Language...");
        builder.setSingleChoiceItems(listlang, -1, new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                switch (i){

                    case 0:
                        setLocale("en");
                        sp.edit().putString("my_lang","en").apply();

                        recreate();

                        break;

                    case 1:
                        setLocale("es");
                        sp.edit().putString("my_lang","es").apply();

                        recreate();
                        break;


                }
                sp.edit().putInt("choseplacesaveselect",i).apply();
                dialog.dismiss();



            }


        });
        AlertDialog md = builder.create();
        md.show();


    }

    private void InitialisingAds() {



    }

    private void InitiialisingSkip() {

        AndroidNetworking.get(BEANLINKS.BASELINK+"skipgoogle")
                .addHeaders(headers)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(SkipModel.class, new ParsedRequestListener<SkipModel>( ) {
                    @Override
                    public void onResponse(SkipModel response) {

                        String value = response.getIsgo();
                        String text = response.getText();
                        int code = response.getVersion();


                        if (code == getVersionCode()){
                            Intent intent;
                            if (value.equals("true")){
                                intent = new Intent(SplashScreen.this, MainActivity.class);
                            }else {
                                intent = new Intent(SplashScreen.this, ANOUCEPAGE.class);
                                intent.putExtra("text",text);
                            }
                            startActivity(intent);
                            finish();
                        }




                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });



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

    private void setLocale(String lang){

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration(  );
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        InitiialisingSkip();

    }

    public void loadlocale(){
        String lang = sp.getString("my_lang","");
        if (lang.equals("")) {
            Log.d("TAG", "loadlocale:  InitalisngLanguage"+lang);
            InitalisngLanguage();

        }else {
            Log.d("TAG", "loadlocale:  setLocale"+lang);

            setLocale(lang);
        }

    }









}