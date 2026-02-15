package com.airaflix.app.Activities.Jdownload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airaflix.app.Config.Utils;
import com.airaflix.app.JPLAYER.JSPLAYER;
import com.airaflix.app.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.Status;
import com.tonyodev.fetch2core.DownloadBlock;
import com.tonyodev.fetch2core.Func;

import java.io.File;
import java.util.List;

public class DownloadList extends AppCompatActivity {

    RecyclerView download_list;

    Fetch fetch;

    DownloadListAdapter adapter;

    List<Download> downloads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_list);

        download_list = findViewById(R.id.download_recycle);
        download_list.setLayoutManager(new LinearLayoutManager(this));

        fetch = DownloadManage.getInstance(this).getFetch();

        fetch.getDownloads(new Func<List<Download>>() {
            @Override
            public void call(@NonNull List<Download> result) {
                downloads = result;
                adapter = new DownloadListAdapter(result, DownloadList.this);
                download_list.setAdapter(adapter);
            }
        });

        fetch.addListener(new FetchListener() {
            @Override
            public void onAdded(@NonNull Download download) {

            }

            @Override
            public void onQueued(@NonNull Download download, boolean b) {

            }

            @Override
            public void onWaitingNetwork(@NonNull Download download) {

            }

            @Override
            public void onCompleted(@NonNull Download download) {
                for(int i = 0; i < downloads.size(); i++) {
                    if (downloads.get(i).getId() == download.getId()){
                        downloads.set(i, download);
                        adapter.notifyItemChanged(i, download);
                    }
                }
            }

            @Override
            public void onError(@NonNull Download download, @NonNull Error error, @Nullable Throwable throwable) {

            }

            @Override
            public void onDownloadBlockUpdated(@NonNull Download download, @NonNull DownloadBlock downloadBlock, int i) {

            }

            @Override
            public void onStarted(@NonNull Download download, @NonNull List<? extends DownloadBlock> list, int i) {

            }

            @Override
            public void onProgress(@NonNull Download download, long l, long l1) {
                for(int i = 0; i < downloads.size(); i++) {
                    if (downloads.get(i).getId() == download.getId()){
                        downloads.set(i, download);
                        adapter.notifyItemChanged(i, download);
                    }
                }
            }

            @Override
            public void onPaused(@NonNull Download download) {
                for(int i = 0; i < downloads.size(); i++) {
                    if (downloads.get(i).getId() == download.getId()){
                        downloads.set(i, download);
                        adapter.notifyItemChanged(i, download);
                    }
                }
            }

            @Override
            public void onResumed(@NonNull Download download) {
                for(int i = 0; i < downloads.size(); i++) {
                    if (downloads.get(i).getId() == download.getId()){
                        downloads.set(i, download);
                        adapter.notifyItemChanged(i, download);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull Download download) {

            }

            @Override
            public void onRemoved(@NonNull Download download) {

            }

            @Override
            public void onDeleted(@NonNull Download download) {
            }
        });

        setTitle("Downloads");
    }

    private Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.download_menu, menu);

        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete_all_bt){
           dialogBeforeDeleteAll();
        }

        return super.onOptionsItemSelected(item);
    }

    public void dialogBeforeDeleteAll(){
        Dialog ays = new Dialog(DownloadList.this);
        ays.setContentView(R.layout.delete_are_you_sure);
        ays.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ays.show();

        downloads.clear();

        adapter.notifyDataSetChanged();

        View delete = ays.findViewById(R.id.delete_file);
        View close = ays.findViewById(R.id.close);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetch.deleteAll();
                ays.dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetch.removeAll();
                ays.dismiss();
            }
        });
    }
    public void dialogBeforeDelete(int id){
        Dialog ays = new Dialog(DownloadList.this);
        ays.setContentView(R.layout.delete_are_you_sure);
        ays.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ays.show();

        for(int i = 0; i < downloads.size(); i++) {
            if (downloads.get(i).getId() == id){
                downloads.remove(i);
                adapter.notifyDataSetChanged();
            }
        }

        View delete = ays.findViewById(R.id.delete_file);
        View close = ays.findViewById(R.id.close);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetch.delete(id);
                ays.dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetch.remove(id);
                ays.dismiss();
            }
        });
    }

    public class DownloadListAdapter extends RecyclerView.Adapter<DownloadListAdapter.Holder> {

        private Context context;
        private List<Download> downloads;

        public DownloadListAdapter(List<Download> downloads, Context context){
            this.context = context;
            this.downloads = downloads;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.download_list_item, parent, false);

            return new Holder(view);
        }

        private void fileNotFound(){
            Toast.makeText(context, "File Not Found", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            final Download download = downloads.get(position);

            holder.anime.setText(download.getHeaders().get("anime"));
            holder.episode.setText(download.getHeaders().get("episode"));
            if(download.getProgress() > 0) {
                holder.progress.setText(download.getProgress() + "%");
            }else{
                holder.progress.setText("0%");
            }
            holder.progress_bar.setProgress(downloads.get(position).getProgress());
            holder.speed.setText(Utils.SpeedCal(download.getDownloadedBytesPerSecond()));

            if(download.getStatus() == Status.PAUSED){
                holder.pause_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                holder.pause_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fetch.resume(download.getId());
                    }
                });
            }else{
                holder.pause_play.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                holder.pause_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fetch.pause(download.getId());
                    }
                });
            }

            if(download.getStatus() == Status.COMPLETED){
                File file = new File(download.getFile());
                if(file.exists()) {
                    MediaMetadataRetriever media = new MediaMetadataRetriever();
                    media.setDataSource(download.getFile());
                    Bitmap extractedImage = media.getFrameAtTime(4 * 60 * 1000 * 1000);

                    holder.thumbnail.setImageBitmap(extractedImage);
                }
                holder.pause_play.setVisibility(View.GONE);

                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBeforeDelete(download.getId());
                    }
                });

                holder.click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File file = new File(download.getFile());
                        if(file.exists()) {
                            Intent intent = new Intent(DownloadList.this, JSPLAYER.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("url", download.getFile());
                            intent.putExtra("name", download.getHeaders().get("anime"));
                            intent.putExtra("eName","Episode "+download.getHeaders().get("episode"));
                            intent.putExtra("message", "local");





                            startActivity(intent);
                        }
                        else{
                            fileNotFound();
                        }
                    }
                });
            }else{
                holder.thumbnail.setImageDrawable(new ColorDrawable(getResources().getColor(R.color.black_200)));
                holder.pause_play.setVisibility(View.VISIBLE);

                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fetch.remove(download.getId());
                        for(int i = 0; i < downloads.size(); i++) {
                            if (downloads.get(i).getId() == download.getId()){
                                downloads.remove(i);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return downloads.size();
        }

        public class Holder extends RecyclerView.ViewHolder{
            TextView anime;
            TextView episode;
            TextView speed;
            TextView progress;

            LinearProgressIndicator progress_bar;

            ImageView thumbnail;
            ImageView pause_play;
            ImageView delete;
            ImageView click;

            public Holder(View itemView) {
                super(itemView);
                anime = itemView.findViewById(R.id.anime);
                episode = itemView.findViewById(R.id.episode);
                speed = itemView.findViewById(R.id.speed);
                progress = itemView.findViewById(R.id.progress_text);

                progress_bar = itemView.findViewById(R.id.progress_bar);

                thumbnail = itemView.findViewById(R.id.thumbnail);

                pause_play = itemView.findViewById(R.id.play_pause_download);

                delete = itemView.findViewById(R.id.delete_download);

                click = itemView.findViewById(R.id.click);
            }
        }
    }
}