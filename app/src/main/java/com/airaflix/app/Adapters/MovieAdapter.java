package com.airaflix.app.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airaflix.app.Models.SerieModel;
import com.airaflix.app.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class MovieAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<SerieModel> dramaModelList;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    public void setmodels(List<SerieModel> dramaModelList){
        this.dramaModelList = dramaModelList;
        notifyDataSetChanged();

    }



    @Override
    public int getCount() {
        if (dramaModelList != null){
            return dramaModelList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {

        if(layoutInflater == null){
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(view == null){
            view = layoutInflater.inflate(R.layout.movie_item, null);
        }

        ImageView im = view.findViewById(R.id.anime_cover);
        TextView name = view.findViewById(R.id.anime_name);

        //Get Cover
        Glide.with(context).load(dramaModelList.get(i).getPoster())
                .into(im);

        name.setText(dramaModelList.get(i).getTitle());





        return view;
    }

    public void filterList(List<SerieModel> newAnimes){
        dramaModelList = newAnimes;
        notifyDataSetChanged();
    }

}
