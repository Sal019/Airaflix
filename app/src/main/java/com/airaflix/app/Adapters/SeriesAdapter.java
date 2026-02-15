package com.airaflix.app.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.airaflix.app.Activities.ShowInfoActivity;
import com.airaflix.app.Models.SerieModel;
import com.airaflix.app.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.List;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.MyViewHolder> {

    private Context context;
    List<SerieModel> serieModels;
    int type = 0;

    public SeriesAdapter(Context context, List<SerieModel> serieModels, int type) {
        this.context = context;
        this.serieModels = serieModels;
        this.type = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 0){
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.seire_item_search, parent, false));

        }else if (viewType == 1){
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.seire_item_big, parent, false));

        }else {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.seire_item_search, parent, false));

        }



    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.item_animation_fall_down);
        SerieModel w = serieModels.get(position);

        if (getItemViewType(position) == 0){
            holder.textView.setText(w.getTitle());
            Glide.with(holder.itemView.getContext()).load(w.getPoster()).into(holder.imageView);



            if (w.getPlace().contains("movie")){
                holder.tasnif.setText("movie");
            } else if (w.getPlace().contains("serie")){
                holder.tasnif.setText("serie");
            }else  holder.tasnif.setText("anime");


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Views").child(w.getTmdb_id()).child("views");
                    db.setValue(ServerValue.increment(1));

                    Intent sendDataToDetailsActivity = new Intent(holder.itemView.getContext(), ShowInfoActivity.class);
                    sendDataToDetailsActivity.putExtra("title",w.getTitle());
                    sendDataToDetailsActivity.putExtra("id",w.getId());
                    sendDataToDetailsActivity.putExtra("cover",w.getPoster());
                    sendDataToDetailsActivity.putExtra("poste",w.getPoster());
                    sendDataToDetailsActivity.putExtra("link",w.getLink_id());
                    sendDataToDetailsActivity.putExtra("postkey",w.getTmdb_id());
                    sendDataToDetailsActivity.putExtra("age",w.getAge());
                    sendDataToDetailsActivity.putExtra("studio",w.getCountry());
                    sendDataToDetailsActivity.putExtra("date",w.getYear());
                    sendDataToDetailsActivity.putExtra("desc",w.getStory());
                    sendDataToDetailsActivity.putExtra("cast",w.getCast());
                    sendDataToDetailsActivity.putExtra("mortabit",w.getOther_season_id());
                    sendDataToDetailsActivity.putExtra("place",w.getPlace());
                    sendDataToDetailsActivity.putExtra("tasnif",w.getGener());
                    sendDataToDetailsActivity.putExtra("views",w.getViews());
                    sendDataToDetailsActivity.putExtra("youtubeid",w.getUpdated_at());
                    sendDataToDetailsActivity.putExtra("isintent","1");

                    holder.itemView.getContext().startActivity(sendDataToDetailsActivity);


                }
            });
        }else if (getItemViewType(position) == 1){
            holder.textView.setText(w.getTitle());
            Glide.with(holder.itemView.getContext()).load(w.getPoster()).into(holder.imageView);



            if (w.getPlace().contains("movie")){
                holder.tasnif.setText("movie");
            } else if (w.getPlace().contains("serie")){
                holder.tasnif.setText("serie");
            }else  holder.tasnif.setText("anime");


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {




                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Views").child(w.getTmdb_id()).child("views");
                    db.setValue(ServerValue.increment(1));
                    Intent sendDataToDetailsActivity = new Intent(holder.itemView.getContext(), ShowInfoActivity.class);
                    sendDataToDetailsActivity.putExtra("title",w.getTitle());
                    sendDataToDetailsActivity.putExtra("id",w.getId());
                    sendDataToDetailsActivity.putExtra("cover",w.getPoster());
                    sendDataToDetailsActivity.putExtra("poste",w.getPoster());
                    sendDataToDetailsActivity.putExtra("link",w.getLink_id());
                    sendDataToDetailsActivity.putExtra("postkey",w.getTmdb_id());
                    sendDataToDetailsActivity.putExtra("age",w.getAge());
                    sendDataToDetailsActivity.putExtra("studio",w.getCountry());
                    sendDataToDetailsActivity.putExtra("date",w.getYear());
                    sendDataToDetailsActivity.putExtra("rat","");
                    sendDataToDetailsActivity.putExtra("desc",w.getStory());
                    sendDataToDetailsActivity.putExtra("cast",w.getCast());
                    sendDataToDetailsActivity.putExtra("mortabit",w.getOther_season_id());
                    sendDataToDetailsActivity.putExtra("place",w.getPlace());
                    sendDataToDetailsActivity.putExtra("tasnif",w.getGener());
                    sendDataToDetailsActivity.putExtra("isintent","1");

                    holder.itemView.getContext().startActivity(sendDataToDetailsActivity);


                }
            });
        }





        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public int getItemCount() {
        return serieModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView,back;
        private TextView textView,tasnif;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageprev);
            textView = itemView.findViewById(R.id.season_name);
            back = itemView.findViewById(R.id.imageVie3);
            tasnif = itemView.findViewById(R.id.tasnif);


        }
    }
}
