package com.airaflix.app.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.airaflix.app.Activities.ShowInfoActivity;
import com.airaflix.app.DB.JseriesDB;
import com.airaflix.app.Models.CountinuModel;
import com.airaflix.app.Models.SerieModel;

import com.airaflix.app.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.HashMap;
import java.util.List;

public class SliderAdapter extends
        SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    Boolean IsInMyFavoritnow = false;
    Context context;
    List<SerieModel> serieModels;
    JseriesDB jseriesDB;
    private final int limit = 5;


    public SliderAdapter(Context context, List<SerieModel> serieModels) {

        this.context = context;
        this.serieModels = serieModels;
    }

    public void  renewItem(List<SerieModel> dataModels){
        this.serieModels = dataModels;
        notifyDataSetChanged();
    }


    public void deleteItems(int position){
        this.serieModels.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item,
                parent,false);
        return new SliderAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        SerieModel w = serieModels.get(position);
        String postkey = w.getTmdb_id();
        jseriesDB = new JseriesDB(context);
        if (FirebaseAuth.getInstance().getCurrentUser() != null){

            Chackisfavorit(FirebaseAuth.getInstance().getCurrentUser().getUid(),viewHolder,postkey);

        }


        viewHolder.title.setText(w.getTitle());
        viewHolder.tasniftt.setText(w.getGener());
        Glide.with(context).load(w.getPoster()).into(viewHolder.image_thumbnail);

        viewHolder.watchepenoew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Views").child(w.getTmdb_id()).child("views");
                db.setValue(ServerValue.increment(1));
                Intent sendDataToDetailsActivity = new Intent(viewHolder.itemView.getContext(), ShowInfoActivity.class);
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
                sendDataToDetailsActivity.putExtra("views",w.getViews());
                sendDataToDetailsActivity.putExtra("mortabit",w.getOther_season_id());
                sendDataToDetailsActivity.putExtra("place",w.getPlace());
                sendDataToDetailsActivity.putExtra("tasnif",w.getGener());
                sendDataToDetailsActivity.putExtra("youtubeid",w.getUpdated_at());
                sendDataToDetailsActivity.putExtra("isintent","1");

                viewHolder.itemView.getContext().startActivity(sendDataToDetailsActivity);




            }
        });




        viewHolder.addtomylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* if (FirebaseAuth.getInstance().getCurrentUser() == null){

                    Toast.makeText(context, "من فضلك سجل الدخول أولا", Toast.LENGTH_SHORT).show();

                }else {


                    HashMap<String,String> Alldataposter = new HashMap<>();
                    Alldataposter.put("title",w.getTitle());
                    Alldataposter.put("posterlogo",w.getPoster());
                    Alldataposter.put("poster",w.getPoster());
                    Alldataposter.put("year",w.getYear());
                    Alldataposter.put("place",w.getPlace());
                    Alldataposter.put("gener",w.getGener());
                    Alldataposter.put("serie_id",postkey);
                    Alldataposter.put("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Alldataposter.put("studio",w.getCountry());
                    Alldataposter.put("age",w.getYear());
                    Alldataposter.put("story",w.getStory());
                    Alldataposter.put("other_season_id",w.getOther_season_id());
                    Alldataposter.put("cast",w.getCast());
                    Alldataposter.put("link_id",w.getLink_id());
                    Alldataposter.put("views","1");
                    Alldataposter.put("rating","2.5");*/

                SerieModel serieModel = new SerieModel();
                serieModel.setId(w.getId());
                serieModel.setTitle(w.getTitle());
                serieModel.setPoster(w.getPoster());
                serieModel.setCover(w.getCover());
                serieModel.setYear(w.getYear());
                serieModel.setPlace(w.getPlace());
                serieModel.setGener(w.getGener());
                serieModel.setTmdb_id(w.getTmdb_id());
                serieModel.setCountry(w.getCountry( ));
                serieModel.setAge(w.getAge());
                serieModel.setStory(w.getStory());



                    if (jseriesDB.checkIfMyListExists(postkey)){

                        boolean added = jseriesDB.DeletMyListDB(postkey);
                        if (added){
                            Toast.makeText(context, "Removed from My List", Toast.LENGTH_SHORT).show();
                            viewHolder.addtofv.setImageDrawable(context.getResources().getDrawable(R.drawable.pluss));
                        }
                    }else {

                        boolean added = jseriesDB.AddtoMyListdDB(serieModel);
                        if (added){
                            Toast.makeText(context, "Added To My List", Toast.LENGTH_SHORT).show();
                            viewHolder.addtofv.setImageDrawable(context.getResources().getDrawable(R.drawable.check));
                        }

                    }







            }
        });



    }

    private void Chackisfavorit(String uid, SliderAdapterVH viewHolder, String postkey) {


        if (jseriesDB.checkIfMyListExists(postkey)){

            IsInMyFavoritnow = true;
            viewHolder.addtofv.setImageDrawable(context.getResources().getDrawable(R.drawable.check));
        }else {


            IsInMyFavoritnow = false;
            viewHolder.addtofv.setImageDrawable(context.getResources().getDrawable(R.drawable.pluss));

        }


    }


    @Override
    public int getCount() {
        if(serieModels.size() > limit){
            return limit;
        }
        else
        {
            return serieModels.size();
        }
    }

    public class SliderAdapterVH extends ViewHolder {


        ImageView image_thumbnail,addtofv;
        TextView title,tasniftt;
        RelativeLayout watchepenoew,progreswait,addtomylist;
        Boolean IsInMyFavoritnow = false;



        public SliderAdapterVH(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.season_name);
            tasniftt = itemView.findViewById(R.id.tasniftt);
            image_thumbnail = itemView.findViewById(R.id.image_thumbnail);
            watchepenoew = itemView.findViewById(R.id.watchepenoew);
            addtofv = itemView.findViewById(R.id.addtofv);
            progreswait = itemView.findViewById(R.id.progreswait);
            addtomylist = itemView.findViewById(R.id.addtomylist);

        }
    }
}
