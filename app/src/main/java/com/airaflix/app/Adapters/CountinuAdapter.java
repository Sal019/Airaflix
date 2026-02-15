package com.airaflix.app.Adapters;


import static com.airaflix.app.Activities.MyList.lista.favoritlmovie.LinkDb;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.airaflix.app.Activities.Auth.LoginTojcartoon;
import com.airaflix.app.Activities.EpesodTAFASIL;
import com.airaflix.app.Activities.ShowInfoActivity;
import com.airaflix.app.Config.getComicsnextback;
import com.airaflix.app.JPLAYER.JSPLAYER;
import com.airaflix.app.JPLAYER.WebPlayer;
import com.airaflix.app.Models.CountinuModel;
import com.airaflix.app.Models.EpesodModel;
import com.airaflix.app.Models.SeasonModel;
import com.airaflix.app.Models.SerieModel;
import com.airaflix.app.Models.ServerModel;
import com.airaflix.app.R;
import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CountinuAdapter extends RecyclerView.Adapter<CountinuAdapter.Myview> {

    List<CountinuModel> countinuModels;
    Context context;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference rexomnd;
    List<ServerModel> eemodles;


    public CountinuAdapter(List<CountinuModel> countinuModels, Context context) {
        this.countinuModels = countinuModels;
        this.context = context;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seire_item_countinu, parent, false);
        return new Myview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myview holder, int position) {

        CountinuModel w = countinuModels.get(position);


        int ff = position;
        if (w.getPlace().contains("tv")){
            holder.textView.setText(w.getSeason_name()+" "+w.getServer_lebel());

        }else {
            holder.textView.setText("movie");

        }



        Glide.with(holder.itemView.getContext()).load(w.getPoster()).into(holder.imageView);


        holder.progrecurr.setMax( Integer.parseInt(w.getFull_duration()));
        holder.progrecurr.setProgress( Integer.parseInt(w.getCurrent_position()));

        holder.gotoseries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginTojcartoon.PleaseWait.show(context);

                if (w.getServer_source().equals("Mp4") || w.getServer_source().equals("M3u8")){

                    LoginTojcartoon.PleaseWait.dismiss();

                    watchVideo(w);


                }else if(w.getServer_source().equals("Embed")  || w.getServer_source().equals("StreamSB")  || w.getServer_source().equals("StreamTape")  || w.getServer_source().equals("DoodStream")){
                    LoginTojcartoon.PleaseWait.dismiss();

                   context.startActivity(new Intent(context, WebPlayer.class)
                            .putExtra("link",w.getServer_url()));


                } else  {
                    LoginTojcartoon.PleaseWait.dismiss();


                    SerieModel tvmodel = new SerieModel();
                    tvmodel.setId(w.getId());
                    tvmodel.setTitle(w.getTitle());
                    tvmodel.setPoster(w.getPoster());
                    tvmodel.setCover(w.getCover());
                    tvmodel.setYear(w.getYear());
                    tvmodel.setPlace(w.getPlace());
                    tvmodel.setGener(w.getGener());
                    tvmodel.setTmdb_id(w.getTmdb_id());
                    tvmodel.setCountry(w.getCountry( ));
                    tvmodel.setAge(w.getAge());
                    tvmodel.setStory(w.getStory());


                    SeasonModel seasonModel = new SeasonModel();
                    seasonModel.setId(w.getSeason_id());
                    seasonModel.setName(w.getSeason_name());
                    seasonModel.setEpisodes(w.getSeason_episodes());
                    seasonModel.setStatus(w.getSeason_status());



                    ServerModel chaptermodelList = new ServerModel();
                    chaptermodelList.setId(w.getServer_id());
                    chaptermodelList.setLebel(w.getServer_lebel());
                    chaptermodelList.setSource(w.getServer_source());
                    chaptermodelList.setUrl(w.getServer_url());


                    Intent sendDataToDetailsActivity = new Intent(holder.itemView.getContext(), EpesodTAFASIL.class);
                    sendDataToDetailsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



                    sendDataToDetailsActivity.putExtra("tvmodel",tvmodel);
                    sendDataToDetailsActivity.putExtra("seasonmodel",seasonModel);
                    sendDataToDetailsActivity.putExtra("episodesmodel", chaptermodelList);


                    holder.itemView.getContext().startActivity(sendDataToDetailsActivity);

                }




            }
        });
        holder.gotosieir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent sendDataToDetailsActivity = new Intent(holder.itemView.getContext(), ShowInfoActivity.class);
                sendDataToDetailsActivity.putExtra("title",w.getTitle());
                sendDataToDetailsActivity.putExtra("id",w.getId());
                sendDataToDetailsActivity.putExtra("cover",w.getPoster());
                sendDataToDetailsActivity.putExtra("poste",w.getPoster());
                sendDataToDetailsActivity.putExtra("link",w.getServer_url());
                sendDataToDetailsActivity.putExtra("postkey",w.getTmdb_id());
                sendDataToDetailsActivity.putExtra("age",w.getAge());
                sendDataToDetailsActivity.putExtra("studio",w.getCountry());
                sendDataToDetailsActivity.putExtra("date",w.getYear());
                sendDataToDetailsActivity.putExtra("desc",w.getStory());
                sendDataToDetailsActivity.putExtra("cast",w.getTmdb_id());
                sendDataToDetailsActivity.putExtra("mortabit",w.getTmdb_id());
                sendDataToDetailsActivity.putExtra("place",w.getPlace());
                sendDataToDetailsActivity.putExtra("tasnif",w.getGener());
                sendDataToDetailsActivity.putExtra("views",w.getViews());
                sendDataToDetailsActivity.putExtra("youtubeid","");
                sendDataToDetailsActivity.putExtra("isintent","1");


                holder.itemView.getContext().startActivity(sendDataToDetailsActivity);


            }
        });



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });

    }

    private void watchVideo(CountinuModel w) {
        SerieModel tvmodel = new SerieModel();
        tvmodel.setId(w.getId());
        tvmodel.setTitle(w.getTitle());
        tvmodel.setPoster(w.getPoster());
        tvmodel.setCover(w.getCover());
        tvmodel.setYear(w.getYear());
        tvmodel.setPlace(w.getPlace());
        tvmodel.setGener(w.getGener());
        tvmodel.setTmdb_id(w.getTmdb_id());
        tvmodel.setCountry(w.getCountry( ));
        tvmodel.setAge(w.getAge());
        tvmodel.setStory(w.getStory());


        SeasonModel seasonModel = new SeasonModel();
        seasonModel.setId(w.getSeason_id());
        seasonModel.setName(w.getSeason_name());
        seasonModel.setEpisodes(w.getSeason_episodes());
        seasonModel.setStatus(w.getSeason_status());



        ServerModel serverModel = new ServerModel();
        serverModel.setId(w.getServer_id());
        serverModel.setLebel(w.getServer_lebel());
        serverModel.setSource(w.getServer_source());
        serverModel.setUrl(w.getServer_url());

        Intent sendDataToDetailsActivity = new  Intent(context, JSPLAYER.class);
        sendDataToDetailsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



        sendDataToDetailsActivity.putExtra("tvmodel", tvmodel);
        sendDataToDetailsActivity.putExtra("seasonmodel",seasonModel);
        sendDataToDetailsActivity.putExtra("episodesmodel", serverModel);
        sendDataToDetailsActivity.putExtra("message","");

        context.startActivity(sendDataToDetailsActivity);


    }

    private void GetEpes(String link, Myview holder,CountinuModel e) {

        eemodles = new ArrayList<>();

        LinkDb.child(link).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot postsnap:snapshot.getChildren()){


                    ServerModel epesodModel = postsnap.getValue(ServerModel.class);


                    eemodles.add(epesodModel);

                }

                getComicsnextback.Epesodes.clear();
                getComicsnextback.Epesodes = eemodles;
                LoginTojcartoon.PleaseWait.dismiss();
                GoToEpes(holder,e);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

    }

    private void GoToEpes(Myview holder, CountinuModel w) {

        Intent sendDataToDetailsActivity = new Intent(holder.itemView.getContext(), EpesodTAFASIL.class);
        /*sendDataToDetailsActivity.putExtra("tvmodel",tvmodel);
        sendDataToDetailsActivity.putExtra("seasonmodel",seasonModel);
        sendDataToDetailsActivity.putExtra("episodesmodel", chaptermodelList.get(position));*/

        holder.itemView.getContext().startActivity(sendDataToDetailsActivity);
    }

    @Override
    public int getItemCount() {
        return countinuModels.size();
    }

    public class Myview extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;
        LinearProgressIndicator progrecurr;
        LinearLayout gotosieir;
        ConstraintLayout gotoseries;


        public Myview(@NonNull View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.imageprev);
            textView = itemView.findViewById(R.id.season_name);
            gotosieir = itemView.findViewById(R.id.gotosieir);
            progrecurr = itemView.findViewById(R.id.progrecurr);
            gotoseries = itemView.findViewById(R.id.gotoseries);
        }
    }
}
