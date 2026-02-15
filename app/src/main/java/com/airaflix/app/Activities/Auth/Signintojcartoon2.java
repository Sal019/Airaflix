package com.airaflix.app.Activities.Auth;


import static com.airaflix.app.Activities.Settings.SettingsJcartoon.SETTINGS_NAME;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.airaflix.app.API.BEANLINKS;
import com.airaflix.app.API.MovieApi;
import com.airaflix.app.API.Respons.LoginResponse;
import com.airaflix.app.API.Servicy;
import com.airaflix.app.Activities.MySeasons;
import com.airaflix.app.Activities.Settings.AuthManager;
import com.airaflix.app.Config.getComicsnextback;
import com.airaflix.app.MainActivity;
import com.airaflix.app.Models.ServerModel;
import com.airaflix.app.Models.UserModel;
import com.airaflix.app.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Signintojcartoon2 extends AppCompatActivity {

    static  public  HashMap<String, String> headers;
    static  public  HashMap<String, String> infousers;

    ImageView back;
    EditText usernameedit;
    ConstraintLayout addimguser;
    RelativeLayout loginbtn;
    CircleImageView logogjj;
    TextView textmsg;
    MovieApi movieApi;


    String fullname , email ,password;
    String imagepath;

    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;
    Uri pickedImgUri ;
    Bitmap Phoroprofile;
    ByteArrayOutputStream byteArrayOutputStream;


    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;

    ProgressBar waitingpro,progressusersearch;

    ArrayList<String> usersid;
    AuthManager authManager ;

    ActivityResultLauncher<Intent> resultLauncher;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signintojcartoon2);

        back = findViewById(R.id.back);
        addimguser = findViewById(R.id.cccss);
        usernameedit = findViewById(R.id.usernameedit);
        loginbtn = findViewById(R.id.loginbtn);
        logogjj = findViewById(R.id.logogjj);
        waitingpro = findViewById(R.id.waitingpro);
        textmsg = findViewById(R.id.textmsg);
        movieApi = Servicy.getMovieApi();
        progressusersearch = findViewById(R.id.progressusersearch);

        mAuth = FirebaseAuth.getInstance();
        fullname = getIntent().getStringExtra("fullname");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        byteArrayOutputStream = new ByteArrayOutputStream();
        firebaseFirestore = FirebaseFirestore.getInstance();
        registerResult();
        authManager = new AuthManager(getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE));

        headers = new HashMap<String, String>();
        headers.put("Accept","application/json");
        headers.put("User-Agent","Thunder Client (https://www.thunderclient.com)");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addimguser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();

                /*if (Build.VERSION.SDK_INT >= 22) {

                    checkAndRequestForPermission();


                }
                else
                {
                    openGallery();
                }*/

            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if ( usernameedit.getText().toString().isEmpty()){

                    showMessage("ٍusername is empty") ;


                }else {

                    waitingpro.setVisibility(View.VISIBLE);
                    LoginTojcartoon.PleaseWait.show(Signintojcartoon2.this);

                    Chakeifusernameisexist(usernameedit.getText().toString(),email,fullname,password);
                }
            }
        });


    }

    public  boolean isIntentAvailable(Intent intent) {
        final PackageManager mgr = getPackageManager();
        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void registerResult(){
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult( ), new ActivityResultCallback<ActivityResult>( ) {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {

                    Uri imageuri = result.getData().getData();
                    logogjj.setImageURI(imageuri);

                }catch (Exception e){
                Toast.makeText(Signintojcartoon2.this, "No Image Selected", Toast.LENGTH_SHORT).show( );}
            }
        });
    }

    private void Chakeifusernameisexist(String username,String email,String fullname ,String password) {

        textmsg.setVisibility(View.INVISIBLE);

       /* DocumentReference docRef = firebaseFirestore.collection("users").document();
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserModel city = documentSnapshot.toObject(UserModel.class);


                if (city.getFullname().equals(fullname)){
                    LoginTojcartoon.PleaseWait.dismiss();

                    waitingpro.setVisibility(View.INVISIBLE);
                    textmsg.setVisibility(View.VISIBLE);



                }else {
                    waitingpro.setVisibility(View.VISIBLE);
                    textmsg.setText("Good Name ... done");
                    textmsg.setVisibility(View.VISIBLE);

                    CreateUserAccount(email,username,password,fullname);

                }
            }
        });*/



        CreateUserAccount(email,username,password,fullname);




    }

    private void CreateUserAccount(String email, final String username, String password, String fullname) {


        // this method create user account with specific email and password





        Call<LoginResponse> responsCall = movieApi
                .createacc(
                        email,password,"free","ssss",fullname
                );
        Log.d("TAG", "onResponse: "+responsCall.toString());

        responsCall.enqueue(new Callback<LoginResponse>( ) {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d("TAG", "onResponse: "+response.body());

                if (response.isSuccessful()){

                    UserModel userModel = response.body().getGetuser();
                    LoginTojcartoon.PleaseWait.dismiss();

                    authManager.saveSettings(userModel,response.body().getToken());
                    showMessage("Done Successfuly");

                    waitingpro.setVisibility(View.INVISIBLE);

                    updateUI();

                }else {
                    LoginTojcartoon.PleaseWait.dismiss();


                    Toast.makeText(Signintojcartoon2.this, response.message(), Toast.LENGTH_SHORT).show();



                }




            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });

     /*   mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {



                            // user account created successfully
                            showMessage("account created successfully....wait");
                            // after we created user account we need to update his profile picture and name
                            updateUserInfo( fullname ,password,username);



                        }
                        else
                        {

                            // account creation failed
                            showMessage(" account creation failed : " + task.getException().getMessage());

                        }
                    }
                });




*/



    }

    private void updateUserInfo(String fullname, String email, String username) {

        // first we need to upload user photo to firebase storage and get url

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // image uploaded succesfully
                // now we can get our image url

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        // uri contain user image url


                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(fullname)
                                .setPhotoUri(uri)
                                .build();

                     /*   Phoroprofile.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        final String base64Image = Base64.encodeToString(bytes,Base64.DEFAULT);*/


                        mAuth.getCurrentUser().updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            // user info updated successfully
                                            HashMap<String,String> hashMap = new HashMap();
                                            hashMap.put("username",username);
                                            hashMap.put("fullname",  fullname);
                                            hashMap.put("email", mAuth.getCurrentUser().getEmail());
                                            hashMap.put("password", password);
                                            hashMap.put("cover","");
                                            hashMap.put("image",uri.toString());
                                            hashMap.put("descprofil","");
                                            hashMap.put("userid", mAuth.getCurrentUser().getUid());
                                            hashMap.put("isking","0");
                                            hashMap.put("ispaid","0");
                                            hashMap.put("signinat",System.currentTimeMillis()+"");
                                            hashMap.put("planmode","none");

                                            firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid())
                                                    .set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                LoginTojcartoon.PleaseWait.dismiss();

                                                                showMessage("Done Successfuly");

                                                                waitingpro.setVisibility(View.INVISIBLE);

                                                                updateUI();
                                                            }else {
                                                                Toast.makeText(Signintojcartoon2.this, "Error", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });


                                        }

                                    }
                                });







                    }
                });





            }
        });





    }

    public void updateUI() {

        Intent intent = new Intent(Signintojcartoon2.this, MainActivity.class);
        startActivity(intent);
        finish();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData();
            logogjj.setImageURI(data.getData());

            try {
                Phoroprofile = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

            }catch (IOException e){
                e.printStackTrace();
            }




        }


    }


    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

    }


    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

    /*    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);*/

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(isIntentAvailable(i)){
            resultLauncher.launch(i);
            return;
        }
     /*   Intent intent = new Intent( MediaStore.ACTION_PICK_IMAGES );
        resultLauncher.launch(intent);*/
    }

    private void checkAndRequestForPermission() {


        if (ContextCompat.checkSelfPermission(Signintojcartoon2.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Signintojcartoon2.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(Signintojcartoon2.this,"أرجوك إقبل صلاحيات التطبيق",Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions(Signintojcartoon2.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else
            openGallery();

    }
}