package com.airaflix.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airaflix.app.Activities.Generpage;
import com.airaflix.app.Models.CatigoriesModel;
import com.airaflix.app.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {

    Context context;
    List<CatigoriesModel> catigoriesModels;

    public CategoriesAdapter(Context context, List<CatigoriesModel> catigoriesModels) {
        this.context = context;
        this.catigoriesModels = catigoriesModels;
    }

    @NonNull
    @Override
    public CategoriesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.MyViewHolder holder, int position) {

        CatigoriesModel cat = catigoriesModels.get(position);

        holder.cat_name.setText(cat.getName());
        Glide.with(holder.itemView.getContext()).load(cat.getIcon()).into(holder.cat_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Generpage.class);
                intent.putExtra("gener",cat.getName());
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return catigoriesModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView cat_img;
        TextView cat_name;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cat_img = itemView.findViewById(R.id.cat_img);
            cat_name = itemView.findViewById(R.id.cat_name);
        }
    }
}
