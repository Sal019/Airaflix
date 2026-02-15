package com.airaflix.app.Activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.airaflix.app.Adapters.comment2ada;
import com.airaflix.app.Models.ComnModel;
import com.airaflix.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Comment extends AppCompatActivity {

    String posterid,posteruser,isintant,postkeyfromlink;



    EditText editcomment;
    ImageButton imageButton;
    TextView title_details,nocomments;
    CheckBox chakedisburn;
    RecyclerView rv_comment;
    ProgressBar progressBar;
    LinearLayout le_burn;
    RelativeLayout com_sec;
    ImageView back;

    String uID;
    int currentLine;

    DatabaseReference db;

    List<ComnModel> Comments;



    SwipeRefreshLayout refreshthis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        setContentView(R.layout.activity_comment);

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }


        isintant = getIntent().getStringExtra("isintant");

        editcomment = findViewById(R.id.editcomment);
        editcomment.clearFocus();

        imageButton = findViewById(R.id.imageButton);
        title_details = findViewById(R.id.comments_counter);
        chakedisburn = findViewById(R.id.chakedisburn);
        rv_comment = findViewById(R.id.rv_comment);
        progressBar = findViewById(R.id.progressBar);
        nocomments = findViewById(R.id.nocomments);
        le_burn = findViewById(R.id.le_burn);
        refreshthis = findViewById(R.id.refreshthis);
        com_sec = findViewById(R.id.com_sec);
        back = findViewById(R.id.back);


        editcomment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    currentLine = currentLine - before;
                } else {
                    le_burn.setVisibility(View.VISIBLE);
                    currentLine = currentLine + count;
                }
                title_details.setText(currentLine+"/400");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editcomment.getText().toString().isEmpty()){
                    Toast.makeText(Comment.this, "إملأ الفراغ أولا", Toast.LENGTH_SHORT).show();
                }else {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                        if (chakedisburn.isChecked()){
                            String timeStamp = uID+System.currentTimeMillis();
                            HashMap<String,String> Alldataposter = new HashMap<>();

                            Alldataposter.put("content",editcomment.getText().toString());
                            Alldataposter.put("posterid",posterid);
                            Alldataposter.put("commentid",timeStamp);
                            Alldataposter.put("uid",uID );
                            Alldataposter.put("username",FirebaseAuth.getInstance().getCurrentUser().getDisplayName() );
                            Alldataposter.put("imgurl",FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
                            Alldataposter.put("addat", String.valueOf(System.currentTimeMillis()));
                            AddComment(Alldataposter);


                            editcomment.getText().clear();
                            GetComments(posterid);
                        }else {
                            String timeStamp = uID+System.currentTimeMillis();
                            HashMap<String,String> Alldatacomments = new HashMap<>();

                            Alldatacomments.put("content",editcomment.getText().toString());
                            Alldatacomments.put("posterid",posterid);
                            Alldatacomments.put("commentid",timeStamp);
                            Alldatacomments.put("uid",uID );
                            Alldatacomments.put("username",FirebaseAuth.getInstance().getCurrentUser().getDisplayName() );
                            Alldatacomments.put("imgurl",FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString() );
                            Alldatacomments.put("addat", String.valueOf(System.currentTimeMillis()));
                            AddComment(Alldatacomments);
                            editcomment.getText().clear();
                            GetComments(posterid);

                        }


                    }else {
                        Toast.makeText(Comment.this, "سجل الدخول إلي التطبيق أولا", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });




        if (isintant!=null){
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            posterid = getIntent().getStringExtra("poste");
            posteruser = getIntent().getStringExtra("posteruser");

            db = FirebaseDatabase.getInstance().getReference("comments").child(posterid);

            GetComments(posterid);
            refreshthis.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    GetComments(posterid);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshthis.setRefreshing(false);

                            //Do your work
                        }
                    },1500);

                }
            });


        }





    }



    public void AddComment(HashMap<String, String> Alldataposter){



        db.push().setValue(Alldataposter).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Toast.makeText(Comment.this, "تم إضافة التعليق بنجاح", Toast.LENGTH_SHORT).show();
                    nocomments.setVisibility(View.INVISIBLE);
                    HashMap<String,String> notif = new HashMap<>();


                }else {
                    Toast.makeText(Comment.this, "حدث خطأ", Toast.LENGTH_SHORT).show();


                }
            }
        });


    }

    public void GetComments(String posterID){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Comment.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rv_comment.setLayoutManager(linearLayoutManager);

        rv_comment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy >0) {
                    // Scroll Down
                    hideController();
                }
                else if (dy <0) {
                    // Scroll Up
                    showController();
                }
            }
        });


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Comments = new ArrayList<>();
                progressBar.setVisibility(View.GONE);
                for (DataSnapshot sn:snapshot.getChildren()) {
                    ComnModel NewComments = sn.getValue(ComnModel.class);
                    Comments.add(NewComments);

                }
                rv_comment.setAdapter(new comment2ada(Comment.this,Comments,posterid));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void showController() {
        ObjectAnimator.ofFloat(com_sec, "translationY", com_sec.getHeight(), 0).start();
//        ObjectAnimator.ofFloat(rlBright, "translationX", 0).start();
    }

    private void hideController() {
        ObjectAnimator.ofFloat(com_sec, "translationY", 0, com_sec.getHeight()).start();
//        ObjectAnimator.ofFloat(rlBright, "translationX", -rlBright.getWidth()).start();
    }
    private void showMessage(String message) {

        Toast.makeText(this,message, Toast.LENGTH_LONG).show();

    }



}