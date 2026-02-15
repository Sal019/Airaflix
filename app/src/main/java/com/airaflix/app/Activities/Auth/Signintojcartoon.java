package com.airaflix.app.Activities.Auth;

import static com.airaflix.app.Activities.Auth.Signintojcartoon2.headers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airaflix.app.API.BEANLINKS;
import com.airaflix.app.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.regex.Pattern;


public class Signintojcartoon extends AppCompatActivity {

    ImageView back;
    EditText onenameedit,emaileditnew,passedit;
    RelativeLayout gotopagename;

    String fullname , email ,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_IMMERSIVE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signintojcartoon);

        back = findViewById(R.id.back);
        gotopagename = findViewById(R.id.gotopagename);
        onenameedit = findViewById(R.id.onenameedit);
        emaileditnew = findViewById(R.id.emaileditnew);
        passedit = findViewById(R.id.passedit);



        gotopagename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname = onenameedit.getText().toString();
                email = emaileditnew.getText().toString();
                password = passedit.getText().toString();

                if (email.isEmpty() ){

                    Toast.makeText(Signintojcartoon.this, "Email is empty ", Toast.LENGTH_SHORT).show();

                }else if(password.isEmpty()  ){

                        Toast.makeText(Signintojcartoon.this, "Password Is empty or less than 6 chrachters", Toast.LENGTH_SHORT).show();


                }else if( !isValid(email) ){

                        Toast.makeText(Signintojcartoon.this, " Not valid Email", Toast.LENGTH_SHORT).show();


                }else if( password.length() < 6  ){

                        Toast.makeText(Signintojcartoon.this, " Password less than 6 chrachters", Toast.LENGTH_SHORT).show();


                }else if(fullname.isEmpty()){

                        Toast.makeText(Signintojcartoon.this, "Full Name is Empty", Toast.LENGTH_SHORT).show();


                }else {
                    LoginTojcartoon.PleaseWait.show(Signintojcartoon.this);
                    AndroidNetworking.get(BEANLINKS.BASELINK+"chackeemail/{email}")
                            .addPathParameter("email",email)
                            .addHeaders(headers)
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener( ) {
                                @Override
                                public void onResponse(JSONObject response) {

                                    Log.d("TAG", "onResponse: "+response.toString());
                                    if (response.toString().contains("email existe")){
                                        Toast.makeText(Signintojcartoon.this, "Email Already exist", Toast.LENGTH_SHORT).show( );
                                    }else {
                                        LoginTojcartoon.PleaseWait.dismiss();
                                        Intent sendDataToDetailsActivity = new Intent(Signintojcartoon.this, Signintojcartoon2.class);
                                        sendDataToDetailsActivity.putExtra("fullname",fullname);
                                        sendDataToDetailsActivity.putExtra("email",email);
                                        sendDataToDetailsActivity.putExtra("password",password);
                                        startActivity(sendDataToDetailsActivity);
                                    }


                                }

                                @Override
                                public void onError(ANError anError) {

                                }
                            });

                  /* */

                }

            }
        });







        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}