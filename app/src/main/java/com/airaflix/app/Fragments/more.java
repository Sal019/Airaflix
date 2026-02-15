package com.airaflix.app.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.airaflix.app.Activities.Settings.SettingsJcartoon.SETTINGS_NAME;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Browser;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airaflix.app.API.BEANLINKS;
import com.airaflix.app.Activities.Auth.LoginTojcartoon;
import com.airaflix.app.Activities.Auth.MyProfile;
import com.airaflix.app.Activities.Jdownload.DownloadList;
import com.airaflix.app.Activities.MyList.Mylist;
import com.airaflix.app.Activities.Premium;
import com.airaflix.app.Activities.Settings.AuthManager;
import com.airaflix.app.Activities.Settings.SettingsJcartoon;
import com.airaflix.app.MainActivity;
import com.airaflix.app.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class more extends Fragment {

    private SharedPreferences sp;

    LinearLayout mywaitingd;
    RelativeLayout gositting,gototelegr,gotosite,goplans,mylistgo,appvaruser,logout,appvar;
    TextView loginanad,username;
    ImageView profileuser;
    final String[] ss = {"en","es"};
    int choseplacesaveselect;

    AutoCompleteTextView auto_complet;
    int Count = 1;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        ((MainActivity)getActivity()).showStatusBar();
        sp = getActivity().getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);


        mylistgo = view.findViewById(R.id.mylistgo);
        mywaitingd = view.findViewById(R.id.mywaitingd);
        gotosite = view.findViewById(R.id.gotosite);
        appvar = view.findViewById(R.id.appvar);
        gositting = view.findViewById(R.id.gositting);
        gototelegr = view.findViewById(R.id.gototelegr);
        profileuser = view.findViewById(R.id.profileuser);
        appvaruser = view.findViewById(R.id.appvaruser);
        goplans = view.findViewById(R.id.goplans);
        loginanad = view.findViewById(R.id.loginanad);
        username = view.findViewById(R.id.username);
        auto_complet = view.findViewById(R.id.auto_complet);
        logout = view.findViewById(R.id.logout);


        choseplacesaveselect = sp.getInt("choseplacesaveselect",0);


        InstalLang();
        CheckIfLogin();


        mylistgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Mylist.class));
            }
        });
        mywaitingd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DownloadList.class));
            }
        });
        gositting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingsJcartoon.class));
            }
        });
        goplans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Premium.class));
            }
        });
        gototelegr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tgintent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getActivity().getResources().getString(R.string.telegram)));
                tgintent.setPackage("org.telegram.messenger");
                startActivity(tgintent);

            }
        });
        gotosite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString = getActivity().getResources().getString(R.string.site);
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                intent1.putExtra(Browser.EXTRA_APPLICATION_ID, "com.android.chrome");
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setPackage("com.android.chrome");
                startActivity(intent1);
            }
        });

        return view;
    }

    private void InstalLang() {

        ArrayList<String> listlang = new ArrayList<>();
        listlang.add("English");
        listlang.add("Spanish");



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.custom_spinner,listlang);


            auto_complet.setAdapter(adapter);
            auto_complet.setOnItemClickListener(new AdapterView.OnItemClickListener( ) {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    setLocale(ss[position],position);

                }
            });




    }

    private void setLocale(String lang, int position){

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration(  );
        configuration.locale = locale;
        getActivity().getResources().updateConfiguration(configuration,getActivity().getResources().getDisplayMetrics());
        sp.edit().putString("my_lang",lang).apply();
        sp.edit().putInt("choseplacesaveselect",position).apply();
        getActivity().recreate();


    }
    private void CheckIfLogin() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept","application/json");
        headers.put("User-Agent","Thunder Client (https://www.thunderclient.com)");


        if (new AuthManager(getActivity().getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE)).getUserInfo().getToken() == null){
            loginanad.setText("Login Now");
            loginanad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), LoginTojcartoon.class));
                }
            });

        } else {

            appvar.setVisibility(View.GONE);
            appvaruser.setVisibility(View.VISIBLE);
            username.setText(new AuthManager(getActivity().getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE)).getUserInfo().getName());
            logout.setOnClickListener(new View.OnClickListener( ) {
                @Override
                public void onClick(View v) {
                    appvar.setVisibility(View.VISIBLE);
                    appvaruser.setVisibility(View.GONE);

                    new AuthManager(getActivity().getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE)).deleteAuth();
                    AndroidNetworking.get(BEANLINKS.BASELINK+"logout")
                            .addHeaders(headers)
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsJSONArray(new JSONArrayRequestListener( ) {
                                @Override
                                public void onResponse(JSONArray response) {

                                    Log.d("TAG", "onResponse: +   "+response);


                                }

                                @Override
                                public void onError(ANError anError) {

                                }
                            });


                }
            });
        }

    }
}