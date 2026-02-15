package com.airaflix.app.Adapters;

import static com.airaflix.app.API.BEANLINKS.getImagePathLink;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.airaflix.app.Activities.Chrachters;
import com.airaflix.app.Models.Cast;
import com.airaflix.app.Models.CastModel;
import com.airaflix.app.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.MyViewHolder> {

    Context context;
    List<CastModel> castModels;

    public CastAdapter(Context context, List<CastModel> castModels) {
        this.context = context;
        this.castModels = castModels;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chrachter_item_2,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CastModel cc = castModels.get(position);


        holder.cast_name.setText(cc.getName());
        holder.cast_role.setText(cc.getCharacter());
        Glide.with(holder.itemView.getContext()).load(getImagePathLink(cc.getProfile_path()))
                .into(holder.cast_img);



    }

    @Override
    public int getItemCount() {
        return castModels.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView cast_name,cast_role;
        ImageView cast_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cast_name = itemView.findViewById(R.id.cast_name);
            cast_role = itemView.findViewById(R.id.cast_role);
            cast_img = itemView.findViewById(R.id.cast_img);
        }
    }
}
