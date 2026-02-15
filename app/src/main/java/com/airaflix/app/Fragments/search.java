package com.airaflix.app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airaflix.app.API.ModelView.MovieListViewModel;
import com.airaflix.app.API.MovieApi;
import com.airaflix.app.API.Servicy;
import com.airaflix.app.Activities.ShowInfoActivity;
import com.airaflix.app.Adapters.MovieAdapter;
import com.airaflix.app.Config.Utils;
import com.airaflix.app.MainActivity;
import com.airaflix.app.Models.SerieModel;
import com.airaflix.app.R;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class search extends Fragment {


    SearchView searchview;
    GridView recentepisod_rv;

    MovieAdapter moviesAdapter;
    List<SerieModel> moviemodel;

    SearchAdapterMo searchAdapterMo;
    MovieListViewModel movieListViewModel;

    MovieApi movieApi;

    ProgressBar progresssearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_search, container, false);
        ((MainActivity)getActivity()).showStatusBar();
        movieApi = Servicy.getMovieApi();
        movieListViewModel = new ViewModelProvider(getActivity()).get(MovieListViewModel.class);
        movieListViewModel.MoviesApi(1);

        initialviews(v);




        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (moviemodel != null){
                    filter(newText);
                }
                return true;
            }
        });

        observAnyChanges();



        Utils.FixColumnsWidth(recentepisod_rv, 120, getContext());

        return  v;
    }


    public void filter(String s) {
        List<SerieModel> filtredList = new ArrayList<>();
        for (SerieModel anime : moviemodel){
            boolean v = false;
            String ss = s.toLowerCase().replaceAll(" ", "");
            String si = anime.getTitle().toLowerCase().replaceAll(" ", "");
            if (si.contains(ss)) {
                v = true;
            }

            if(v){
                filtredList.add(anime);
            }
        }

        moviemodel = filtredList;
        moviesAdapter.filterList(filtredList);
    }

    private void observAnyChanges() {

        movieListViewModel.getAllMovies().observe(getActivity( ), new Observer<List<SerieModel>>( ) {
            @Override
            public void onChanged(List<SerieModel> serieModels) {
                if (serieModels != null){
                    moviemodel = new ArrayList<>(  );
                    moviesAdapter = new MovieAdapter(getActivity());
                    progresssearch.setVisibility(View.GONE);
                    moviesAdapter.setmodels(serieModels);
                    for (SerieModel ss:
                            serieModels) {

                        moviemodel.add(ss);

                    }
                    recentepisod_rv.setAdapter(moviesAdapter);
                    recentepisod_rv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                         /*   startActivity(new Intent(getActivity(), DetailPage.class).putExtra("img",dramaModels.get(position).getImg())
                                    .putExtra("title",dramaModels.get(position).getTitle())
                                    .putExtra("link",dramaModels.get(position).getPageLink()));*/

                            SerieModel w = serieModels.get(position);


                            Intent sendDataToDetailsActivity = new Intent(getActivity(), ShowInfoActivity.class);
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

                            startActivity(sendDataToDetailsActivity);
                        }
                    });
                }

            }
        });


    }

    private void initialviews(View v) {

        recentepisod_rv = v.findViewById(R.id.recentepisod_rv);
        searchview = v.findViewById(R.id.searchview);
        progresssearch = v.findViewById(R.id.progresssearch);




        EditText editText = (EditText) searchview.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.white));
        editText.setHintTextColor(getResources().getColor(R.color.white));

        ImageView searchIcon = searchview.findViewById(androidx.appcompat.R.id.search_button);
        searchIcon.setColorFilter(getResources().getColor(R.color.white));
        ImageView closeIcon = searchview.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeIcon.setColorFilter(getResources().getColor(R.color.white));

        ImageView ss = searchview.findViewById(androidx.appcompat.R.id.search_go_btn);
        ss.setColorFilter(getResources().getColor(R.color.white));

        Typeface tf = ResourcesCompat.getFont(getActivity(),R.font.poppins_regular);

        TextView searchText = searchview.findViewById(androidx.appcompat.R.id.search_src_text);
        searchText.setTypeface(tf);

    }


    public class SearchAdapterMo extends RecyclerView.Adapter<SearchAdapterMo.MyViewHolder> {

        Context context;
        List<SerieModel> castModels;

        public SearchAdapterMo(Context context, List<SerieModel> castModels) {
            this.context = context;
            this.castModels = castModels;
        }



        @NonNull
        @Override
        public  MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seire_item_search,parent,false);
            return new  MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


            SerieModel w = castModels.get(position);

            holder.textView.setText(w.getTitle());
            if (w.getPlace().contains("movie")){
                holder.tasnif.setText("movie");
            } else if (w.getPlace().contains("serie")){
                holder.tasnif.setText("serie");
            }else  holder.tasnif.setText("anime");

            Glide.with(holder.itemView.getContext()).load(w.getPoster()).into(holder.imageView);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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

        @Override
        public int getItemCount() {
            return castModels.size();

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private ImageView imageView;
            private TextView textView,tasnif;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);


                imageView = itemView.findViewById(R.id.imageprev);
                textView = itemView.findViewById(R.id.season_name);
                tasnif = itemView.findViewById(R.id.tasnif);
            }
        }
    }





}