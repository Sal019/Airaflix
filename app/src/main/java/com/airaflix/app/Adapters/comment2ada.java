package com.airaflix.app.Adapters;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.airaflix.app.Models.ComnModel;
import com.airaflix.app.Models.UserModel;
import com.airaflix.app.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class comment2ada extends RecyclerView.Adapter<comment2ada.CommentViewHolder> {


    private static final String LIKESCOMMENTFIREBASE = "likesComments";
    private static final String DISLIKESCOMMENTFIREBASE = "dislikesComments";
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final int WEEK_MILLIS = 7 * DAY_MILLIS ;

    private Context mContext;
    private List<ComnModel> mData;
    String PostKey;


    public comment2ada(Context mContext, List<ComnModel> mData, String PostKey) {
        this.mContext = mContext;
        this.mData = mData;
        this.PostKey = PostKey;

    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_comt,parent,false);
        return new CommentViewHolder(row);
    }



    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {


        final String uid = mData.get(position).getUid();


        holder.comment_content.setText(mData.get(position).getContent());
        holder.comicstitle.setText(mData.get(position).getUsername());

        Glide.with(holder.itemView.getContext()).load(mData.get(position).getImgurl()).into(holder.userimage);

        holder.datetoadded.setText(" منذ " + getTimeAgo(Long.parseLong(mData.get(position).getAddat())));

        String miniID = uid.substring(1,5);
        String usernm = "@jcartoon_"+miniID;
        holder.userid.setText(usernm);





        if (FirebaseAuth.getInstance().getCurrentUser() == null){

            String postkey = mData.get(position).getPosterid();


            holder.getlikestatusnouser(postkey);
            holder.getDislikestatusnouser(postkey);




        }else {

            String userid = holder.currentUser.getUid();
            String postkey = mData.get(position).getPosterid();


            holder.getlikestatus(userid,postkey);
            holder.getDislikestatus(userid,postkey);



        }



        holder.likecomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                holder.testclick=true;



                if (FirebaseAuth.getInstance().getCurrentUser() == null){

                    Toast.makeText(mContext, "Login !!", Toast.LENGTH_SHORT).show();


                }else {

                    String userid = holder.currentUser.getUid();
                    String postkey = mData.get(position).getPosterid();


                    DatabaseReference dblikes = FirebaseDatabase.getInstance().getReference(LIKESCOMMENTFIREBASE);


                    dblikes.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(holder.testclick==true)
                            {
                                if(snapshot.child(mData.get(position).getId()).hasChild(userid))
                                {
                                    dblikes.child(mData.get(position).getId()).child(userid).removeValue();
                                    holder.testclick=false;
                                }
                                else
                                {

                                        dblikes.child(mData.get(position).getId()).child(userid).setValue(true);
                                        holder.testclick=false;


                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }





            }
        });
        holder.unlikecomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (FirebaseAuth.getInstance().getCurrentUser() == null){

                    Toast.makeText(mContext, "Login Fierst !!", Toast.LENGTH_SHORT).show();
                }else {


                    String userid = holder.currentUser.getUid();
                    String postkey = mData.get(position).getPosterid();


                    holder.testclick=true;

                    DatabaseReference dblikes = FirebaseDatabase.getInstance().getReference(DISLIKESCOMMENTFIREBASE);


                    dblikes.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (holder.testclick == true) {
                                if (snapshot.child(mData.get(position).getId()).hasChild(userid)) {
                                    dblikes.child(mData.get(position).getId()).child(userid).removeValue();
                                    holder.testclick = false;
                                } else {

                                        dblikes.child(mData.get(position).getId()).child(userid).setValue(true);
                                        holder.testclick = false;


                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });



    }






    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{


        FirebaseAuth mAuth;
        FirebaseUser currentUser;
        Boolean testclick=false;

        TextView comicstitle,datetoadded,comment_content,likecount,unlikecount;
        ImageView userimage;
        ImageView morecomments;
        ImageView likecomment,unlikecomment;
        TextView userid;


        public CommentViewHolder(View itemView) {
            super(itemView);
            comicstitle = itemView.findViewById(R.id.comicstitle);
            datetoadded = itemView.findViewById(R.id.datetoadded);
            userid = itemView.findViewById(R.id.userid);
            comment_content = itemView.findViewById(R.id.comment_content);
            userimage = itemView.findViewById(R.id.userimage);
            likecomment = itemView.findViewById(R.id.likecomment);
            unlikecomment = itemView.findViewById(R.id.unlikecomment);
            likecount = itemView.findViewById(R.id.likecount);
            unlikecount = itemView.findViewById(R.id.unlikecount);
            morecomments = itemView.findViewById(R.id.morecomments);


            try {
                if (FirebaseAuth.getInstance().getCurrentUser() != null){

                    mAuth = FirebaseAuth.getInstance();
                    currentUser = mAuth.getCurrentUser();
                }


            }catch (Exception e){
                e.printStackTrace();
            }


        }





        public void getlikestatus(String userid, String postkey) {


            int position = getAdapterPosition();

            DatabaseReference dblikes = FirebaseDatabase.getInstance().getReference(LIKESCOMMENTFIREBASE);
            dblikes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(mData.get(position).getId()).hasChild(userid))
                    {

                        int likecounts=(int)dataSnapshot.child(mData.get(position).getId()).getChildrenCount();
                        likecount.setText(likecounts+"");
//                        like.setImageResource(R.drawable.ic_favorite_active);
                        likecomment.setImageResource(R.drawable.heartfill);






                    }
                    else
                    {
                        int likecounts=(int)dataSnapshot.child(mData.get(position).getId()).getChildrenCount();
                        likecount.setText(likecounts+"");
//                        like.setImageResource(R.drawable.ic_favorite_default);
                        likecomment.setImageResource(R.drawable.heart);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            dblikes.keepSynced(true);

        }

        public void getDislikestatus(String userid, String postkey) {

            int position = getAdapterPosition();

            DatabaseReference dblikes = FirebaseDatabase.getInstance().getReference(DISLIKESCOMMENTFIREBASE);
            dblikes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(mData.get(position).getId()).hasChild(userid))
                    {
                        int likecount=(int)dataSnapshot.child(mData.get(position).getId()).getChildrenCount();
                        unlikecount.setText(likecount+"");
                        unlikecomment.setImageResource(R.drawable.brokenheart);
                    }
                    else
                    {
                        int likecount=(int)dataSnapshot.child(mData.get(position).getId()).getChildrenCount();
                        unlikecount.setText(likecount+"");
                        unlikecomment.setImageResource(R.drawable.brokenheartempty);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            dblikes.keepSynced(true);

        }


        public void getlikestatusnouser(String postkey) {


            int position = getAdapterPosition();

            DatabaseReference dblikes = FirebaseDatabase.getInstance().getReference(LIKESCOMMENTFIREBASE)
                    .child(mData.get(position).getId());

            dblikes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        int ratecoount = (int)dataSnapshot.getChildrenCount();
                        likecount.setText(ratecoount+"");
                    }
                    else
                    {

                        likecount.setText("0");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            dblikes.keepSynced(true);

        }

        public void getDislikestatusnouser(String postkey) {

            int position = getAdapterPosition();

            DatabaseReference dblikes = FirebaseDatabase.getInstance().getReference(DISLIKESCOMMENTFIREBASE)
                    .child(mData.get(position).getId());


            dblikes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists())
                    {

                        int ratecoount = (int)dataSnapshot.getChildrenCount();
                        unlikecount.setText(ratecoount+"");
                    }
                    else
                    {

                        unlikecount.setText("0");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            dblikes.keepSynced(true);

        }
    }




    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();
        return date;


    }


    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            //if timestamp given in seconds, convert to millis time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize

        final long diff = now - time;

        if (diff < MINUTE_MILLIS)
        { return " الأن "; }
        else if (diff < 2 * MINUTE_MILLIS) { return " دقيقة "; }
        else if (diff < 50 * MINUTE_MILLIS) { return diff / MINUTE_MILLIS + " دقائق"; }
        else if (diff < 90 * MINUTE_MILLIS) { return " ساعة "; }
        else if (diff < 24 * HOUR_MILLIS) { return diff / HOUR_MILLIS + " ساعات "; }
        else if (diff < 48 * HOUR_MILLIS) { return " البارحة "; }
        else { return diff / DAY_MILLIS + " أيام "; }
    }



}
