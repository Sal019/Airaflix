package com.airaflix.app.Activities.Auth;



import static com.airaflix.app.API.BEANLINKS.APIKAY;
import static com.airaflix.app.Activities.Settings.SettingsJcartoon.SETTINGS_NAME;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.airaflix.app.API.MovieApi;
import com.airaflix.app.API.Respons.LoginResponse;
import com.airaflix.app.API.Respons.MovieCastRespons;
import com.airaflix.app.API.Servicy;
import com.airaflix.app.Activities.Settings.AuthManager;
import com.airaflix.app.Activities.ShowInfoActivity;
import com.airaflix.app.Adapters.CastAdapter;
import com.airaflix.app.Config.Function;
import com.airaflix.app.MainActivity;
import com.airaflix.app.Models.CastModel;
import com.airaflix.app.Models.UserModel;
import com.airaflix.app.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginTojcartoon extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;


    //google Login
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    MovieApi movieApi;

    //Facebook Login
    CallbackManager callbackManager;

    EditText emailedit,passedit;
    RelativeLayout loginbtn;
    TextView gotosignin;

    RelativeLayout logingoogle,lgointwitter,lgoinfacebook;
    AuthManager authManager ;

    String email,password;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_IMMERSIVE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.logintojcartoonac);
        FirebaseApp.initializeApp(this);
        movieApi = Servicy.getMovieApi();
        FacebookSdk.sdkInitialize(getApplicationContext());
        authManager = new AuthManager(getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE));

        gotosignin = findViewById(R.id.gotosignin);
        emailedit = findViewById(R.id.emailedit);
        passedit = findViewById(R.id.passedit);
        loginbtn = findViewById(R.id.loginbtn);
        logingoogle = findViewById(R.id.logingoogle);
        lgointwitter = findViewById(R.id.lgointwitter);
        lgoinfacebook = findViewById(R.id.lgoinfacebook);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        callbackManager = CallbackManager.Factory.create();

        gotosignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginTojcartoon.this,Signintojcartoon.class));
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailedit.getText().toString();
                password = passedit.getText().toString();

                if (email.isEmpty()){
                    Toast.makeText(LoginTojcartoon.this, "Email is Empty", Toast.LENGTH_SHORT).show();
                }else if(password.isEmpty()){
                    {
                       Toast.makeText(LoginTojcartoon.this, "Password is Empty", Toast.LENGTH_SHORT).show();
                    }

                }else {
                   PleaseWait.show(LoginTojcartoon.this);

                    signIn(email,password);
                }

            }
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        account = GoogleSignIn.getLastSignedInAccount(this);
        callbackManager = CallbackManager.Factory.create();

        logingoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSigninnow();


            }
        });


        lgoinfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginTojcartoon.this, Arrays.asList("public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });
            }
        });
    }

    private void GoogleSigninnow() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 100);

    }
    private void handleFacebookAccessToken(AccessToken token) {
        PleaseWait.show(LoginTojcartoon.this);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            HashMap<String,String> hashMap = new HashMap();
                            hashMap.put("username",null);
                            hashMap.put("fullname", mAuth.getCurrentUser().getDisplayName());
                            hashMap.put("email",mAuth.getCurrentUser().getEmail());
                            hashMap.put("password","123456789");
                            hashMap.put("cover","");
                            hashMap.put("image",mAuth.getCurrentUser().getPhotoUrl().toString());
                            hashMap.put("descprofil","");
                            hashMap.put("userid",mAuth.getCurrentUser().getUid());
                            hashMap.put("isadmin","0");
                            hashMap.put("ispaid","0");
                            hashMap.put("signinat",System.currentTimeMillis()+"");

                            chackuserisinServer(mAuth.getCurrentUser().getUid(),hashMap);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginTojcartoon.this, "Error Login with Facebook",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 100) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                account =  task.getResult(ApiException.class);
                PleaseWait.show(LoginTojcartoon.this);

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    HashMap<String,String> hashMap = new HashMap();
                                    hashMap.put("username",null);
                                    hashMap.put("fullname", mAuth.getCurrentUser().getDisplayName());
                                    hashMap.put("email",mAuth.getCurrentUser().getEmail());
                                    hashMap.put("password","123456789");
                                    hashMap.put("cover","");
                                    hashMap.put("image",mAuth.getCurrentUser().getPhotoUrl().toString());
                                    hashMap.put("descprofil","");
                                    hashMap.put("userid",mAuth.getCurrentUser().getUid());
                                    hashMap.put("isadmin","0");
                                    hashMap.put("ispaid","0");
                                    hashMap.put("signinat",System.currentTimeMillis()+"");

                                    chackuserisinServer(mAuth.getCurrentUser().getUid(),hashMap);
                                }else {
                                    Log.d("google er", task.getException().getMessage());
                                    Toast.makeText(LoginTojcartoon.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();


        if (authManager.getUserInfo().getToken() != null){
          updateUI();

        }


    }

    public void SignuptoServer(HashMap<String, String> account) {

        firebaseFirestore.collection("users").document(account.get("userid"))
                .set(account).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            updateUI();
                        }else {
                            Toast.makeText(LoginTojcartoon.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void chackuserisinServer(String userid,HashMap<String, String> account){

        DocumentReference db = firebaseFirestore.collection("users").document(account.get("userid"));


        db.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        PleaseWait.dismiss();
                        updateUI();
                    } else {
                        SignuptoServer(account);
                    }
                } else {
                    Toast.makeText(LoginTojcartoon.this,  task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void signIn(String mail, String password) {

        Log.d("TAG", "onResponse: mail"+mail);
        Log.d("TAG", "onResponse: password"+password);


        Call<LoginResponse> responsCall = movieApi
                .login(
                        mail,password
                );
        Log.d("TAG", "onResponse: "+responsCall.toString());

        responsCall.enqueue(new Callback<LoginResponse>( ) {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d("TAG", "onResponse: "+response.body());

                if (response.isSuccessful()){

                    UserModel userModel = response.body().getGetuser();
                    PleaseWait.dismiss();

                    authManager.saveSettings(userModel,response.body().getToken());
                    updateUI();

                }else {
                    PleaseWait.dismiss();


                    Toast.makeText(LoginTojcartoon.this, response.message(), Toast.LENGTH_SHORT).show();



                }




            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });



/*
        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {
                    PleaseWait.dismiss();
                    updateUI();

                } else {
                    PleaseWait.dismiss();


                        Toast.makeText(LoginTojcartoon.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();



                }


            }
        });
*/


    }

    public void updateUI() {

        finish();
        Intent intent = new Intent(LoginTojcartoon.this, MainActivity.class);
        startActivity(intent);

    }

    public static class PleaseWait {

        static Dialog wait;

        public static void show(Context context){
            wait = new Dialog(context);
            wait.setContentView(R.layout.please_wait);
            wait.setCancelable(false);
            wait.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            wait.show();
        }

        public static void dismiss(){
            wait.dismiss();
        }
    }
}