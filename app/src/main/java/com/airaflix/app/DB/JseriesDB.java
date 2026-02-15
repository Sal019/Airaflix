package com.airaflix.app.DB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


import com.airaflix.app.Models.CountinuModel;
import com.airaflix.app.Models.EpesodModel;
import com.airaflix.app.Models.SerieModel;

public class JseriesDB extends SQLiteOpenHelper {

    public static final String DBNAME = "Jserie.db";



    public static final String TABLENAMESERIES = "MyList";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String POSTERKEY = "posterkey";
    public static final String POSTER = "poster";
    public static final String YEAR = "year";
    public static final String GENER = "gener";
    public static final String STUDIO = "studio";
    public static final String AGE = "age";
    public static final String STORY = "story";
    public static final String WHERESTART = "wherestartcomics";
    public static final String PLACE = "place";
    public static final String CAST = "casts";
    public static final String MORTABIT = "mortabit";
    public static final String CHAPTERS = "link";


    //Epe Table
    public static final String ID_CHAPTER = "id";
    public static final String CHAPTER_KEY = "chapter_key";
    public static final String TITLE_CHAPTER = "title_chapter";
    public static final String COMICS_ID = "comic_id";
    public static final String PATH_CHAPTER = "path_chapter";

    public static final String TABLENAMECARTOON = "series_downloaded";
    public static final String TABLENAMEEPE = "epe_downloaded";
    public static final String TABLENAMECOUNTINIWATCHING = "countine_watching";
    public static final String TABLENAMECHECKEPE = "check_epesodes";


    public JseriesDB(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {



        String series = "create table "+TABLENAMESERIES+" ( id integer primary key autoincrement," +
                TITLE+" text," +  POSTERKEY+" text," + POSTER +" text," + YEAR +" text," +GENER +" text,"+STUDIO +" text,"
                +AGE +" text,"+STORY +" text,"+PLACE +" text,"+CAST +" text,"+MORTABIT +" text,link text,views text,rating text,userid text)";

        db.execSQL(series);


        String qry_epe = "create table "+TABLENAMEEPE+" ( id integer primary key autoincrement," +
                TITLE_CHAPTER+" text,"+
                CHAPTER_KEY+" text,"
                + COMICS_ID +" text,"
                + PATH_CHAPTER +" text)";

        db.execSQL(qry_epe);


        String cartoon = "create table "+TABLENAMECARTOON+" ( id integer primary key autoincrement," +
                TITLE+" text," +  POSTERKEY+" text," + POSTER +" text," + YEAR +" text," +GENER +" text,"+STUDIO +" text,"
                +AGE +" text,"+STORY +" text,"+WHERESTART +" text,"+PLACE +" text,"+CAST +" text,"+MORTABIT +" text,"+CHAPTERS +" text)";

        db.execSQL(cartoon);

        String COUNTINIWATCHING = "create table "+TABLENAMECOUNTINIWATCHING+" ( id integer primary key autoincrement," +
                TITLE+" text," +  POSTERKEY+" text," + POSTER +" text," + YEAR +" text," +GENER +" text,"+STUDIO +" text,"
                +AGE +" text,"+STORY +" text,"+WHERESTART +" text,"+PLACE +" text,"+CAST +" text,"+MORTABIT +" text,"+CHAPTERS +" text , epename text" +
                ", wherehestop text , totallenght text , type text ,fhd text,hd text ,sd text ,qd text , postkey text, position text)";

        db.execSQL(COUNTINIWATCHING);

        String CHECKEPE = "create table "+TABLENAMECHECKEPE+" ( id integer primary key autoincrement," +
                TITLE+" text," +  POSTERKEY+" text,epename text, epepostkey text)";

        db.execSQL(CHECKEPE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String qru = "DROP TABLE IF EXISTS "+TABLENAMECARTOON;

            db.execSQL(qru);

        onCreate(db);
    }


    //Add
    public boolean AddtoMyListdDB(SerieModel comicsModel){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TITLE,comicsModel.getTitle());
        cv.put(POSTERKEY,comicsModel.getTmdb_id());
        cv.put(POSTER,comicsModel.getPoster());
        cv.put(YEAR,comicsModel.getYear());
        cv.put(GENER,comicsModel.getGener());
        cv.put(STUDIO,comicsModel.getCountry());
        cv.put(AGE,comicsModel.getAge());
        cv.put(STORY,comicsModel.getStory());
        cv.put(PLACE,comicsModel.getPlace());
        cv.put(CAST,comicsModel.getCast());
        cv.put(MORTABIT,comicsModel.getOther_season_id());
        cv.put(CHAPTERS,comicsModel.getLink_id());
        cv.put("views",comicsModel.getViews());
        cv.put("rating",comicsModel.getUpdated_at());
        cv.put("userid",comicsModel.getCreated_at());

        float res = db.insert(TABLENAMESERIES,null,cv);

        if (res == -1){

            return false;

        }else {

            return true;

        }

    }
    public boolean AddtoSeriesDownloadedDB(SerieModel comicsModel){

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues cv = new ContentValues();
        cv.put(TITLE,comicsModel.getTitle());
        cv.put(POSTERKEY,comicsModel.getTmdb_id());
        cv.put(POSTER,comicsModel.getPoster());
        cv.put(YEAR,comicsModel.getYear());
        cv.put(GENER,comicsModel.getGener());
        cv.put(STUDIO,comicsModel.getCountry());
        cv.put(AGE,comicsModel.getAge());
        cv.put(STORY,comicsModel.getStory());
        cv.put(WHERESTART,comicsModel.getPlace());
        cv.put(PLACE,comicsModel.getPlace());
        cv.put(CAST,comicsModel.getCast());
        cv.put(MORTABIT,comicsModel.getOther_season_id());
        cv.put(CHAPTERS,comicsModel.getLink_id());

        float res = db.insert(TABLENAMECARTOON,null,cv);

        if (res == -1){

            return false;

        }else {

            return true;

        }

    }
    public boolean AddCountineWatchingDB(CountinuModel comicsModel){

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues cv = new ContentValues();
        cv.put(TITLE,comicsModel.getTitle());
        cv.put(POSTERKEY,comicsModel.getId());
        cv.put(POSTER,comicsModel.getPoster());
        cv.put(YEAR,comicsModel.getYear());
        cv.put(GENER,comicsModel.getGener());
        cv.put(STUDIO,comicsModel.getCountry());
        cv.put(AGE,comicsModel.getAge());
        cv.put(STORY,comicsModel.getStory());
        cv.put(WHERESTART,comicsModel.getPlace());
        cv.put(PLACE,comicsModel.getPlace());
        cv.put(CAST,comicsModel.getTmdb_id());
        cv.put(MORTABIT,comicsModel.getSeason_episodes());
        cv.put(CHAPTERS,comicsModel.getServer_url());

        cv.put("epename",comicsModel.getServer_lebel());
        cv.put("wherehestop",comicsModel.getCurrent_position());
        cv.put("totallenght",comicsModel.getFull_duration());
        cv.put("fhd",comicsModel.getServer_url());
        cv.put("hd",comicsModel.getSeason_name());
        cv.put("sd",comicsModel.getServer_source());
        cv.put("postkey",comicsModel.getId());
        cv.put("position",comicsModel.getPosition());

        float res = db.insert(TABLENAMECOUNTINIWATCHING,null,cv);

        if (res == -1){

            return false;

        }else {

            return true;

        }

    }
    public boolean AddToEpesode(EpesodModel epesodModel){

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues cv = new ContentValues();
        cv.put(TITLE_CHAPTER,epesodModel.getEpetitle());
        cv.put(COMICS_ID,epesodModel.getId());
        cv.put(PATH_CHAPTER,epesodModel.getServer2());
        cv.put(CHAPTER_KEY,epesodModel.getPostkey());


        float res = db.insert(TABLENAMEEPE,null,cv);

        if (res == -1){

            return false;

        }else {

            return true;

        }

    }



    //Get
    public Cursor GetMyListDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        String qry = "select * from "+TABLENAMESERIES+" order by id desc";
        Cursor cursor = db.rawQuery(qry,null);

        return  cursor;
    }
    public Cursor GetDownloadsDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        String qry = "select * from "+TABLENAMECARTOON+" order by id desc";
        Cursor cursor = db.rawQuery(qry,null);

        return  cursor;
    }


    public Cursor GetCountinuDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        String qry = "select * from "+TABLENAMECOUNTINIWATCHING+" order by id desc";
        Cursor cursor = db.rawQuery(qry,null);

        return  cursor;
    }
    public Cursor GetCheckEPeDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        String qry = "select * from "+TABLENAMECHECKEPE+" order by id desc";
        Cursor cursor = db.rawQuery(qry,null);

        return  cursor;
    }
    public Cursor GetEpesDB(String comics_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLENAMEEPE+" WHERE "+COMICS_ID+" = ?";
        String[] selectionArgs = { comics_id };
        Cursor cursor = db.rawQuery(query, selectionArgs);


        return  cursor;
    }

    //update
    public boolean UpdateMyListDB(SerieModel comicsModel){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TITLE,comicsModel.getTitle());
        cv.put(POSTERKEY,comicsModel.getTmdb_id());
        cv.put(POSTER,comicsModel.getPoster());
        cv.put(YEAR,comicsModel.getYear());
        cv.put(GENER,comicsModel.getGener());
        cv.put(STUDIO,comicsModel.getCountry());
        cv.put(AGE,comicsModel.getAge());
        cv.put(STORY,comicsModel.getStory());
        cv.put(WHERESTART,comicsModel.getPlace());
        cv.put(PLACE,comicsModel.getPlace());
        cv.put(CAST,comicsModel.getCast());
        cv.put(MORTABIT,comicsModel.getOther_season_id());
        cv.put(CHAPTERS,comicsModel.getLink_id());
        cv.put("views",comicsModel.getViews());
        cv.put("rating",comicsModel.getUpdated_at());
        cv.put("userid",comicsModel.getCreated_at());

        float res = db.update(TABLENAMESERIES,cv,POSTERKEY+" =?",new String[] {comicsModel.getTmdb_id()});

        if (res == -1){

            return false;

        }else {

            return true;

        }

    }
    public boolean UpdatedownloadDB(SerieModel comicsModel){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TITLE,comicsModel.getTitle());
        cv.put(POSTERKEY,comicsModel.getTmdb_id());
        cv.put(POSTER,comicsModel.getPoster());
        cv.put(YEAR,comicsModel.getYear());
        cv.put(GENER,comicsModel.getGener());
        cv.put(STUDIO,comicsModel.getCountry());
        cv.put(AGE,comicsModel.getAge());
        cv.put(STORY,comicsModel.getStory());
        cv.put(WHERESTART,comicsModel.getPlace());
        cv.put(PLACE,comicsModel.getPlace());
        cv.put(CAST,comicsModel.getCast());
        cv.put(MORTABIT,comicsModel.getOther_season_id());
        cv.put(CHAPTERS,comicsModel.getLink_id());

        float res = db.update(TABLENAMECARTOON,cv,POSTERKEY+" =?",new String[] {comicsModel.getTmdb_id()});

        if (res == -1){

            return false;

        }else {

            return true;

        }

    }
    public boolean UpdateCountinuDB(CountinuModel comicsModel){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TITLE,comicsModel.getTitle());
        cv.put(POSTERKEY,comicsModel.getId());
        cv.put(POSTER,comicsModel.getPoster());
        cv.put(YEAR,comicsModel.getYear());
        cv.put(GENER,comicsModel.getGener());
        cv.put(STUDIO,comicsModel.getCountry());
        cv.put(AGE,comicsModel.getAge());
        cv.put(STORY,comicsModel.getStory());
        cv.put(WHERESTART,comicsModel.getPlace());
        cv.put(PLACE,comicsModel.getPlace());
        cv.put(CAST,comicsModel.getTmdb_id());
        cv.put(MORTABIT,comicsModel.getSeason_episodes());
        cv.put(CHAPTERS,comicsModel.getServer_url());

        cv.put("epename",comicsModel.getServer_lebel());
        cv.put("wherehestop",comicsModel.getCurrent_position());
        cv.put("totallenght",comicsModel.getFull_duration());
        cv.put("fhd",comicsModel.getServer_url());
        cv.put("postkey",comicsModel.getId());
        cv.put("position",comicsModel.getPosition());


        float res = db.update(TABLENAMECOUNTINIWATCHING,cv,POSTERKEY+" =?",new String[] {comicsModel.getId()});

        if (res == -1){

            return false;

        }else {

            return true;

        }

    }

    public boolean UpdateEpe(EpesodModel epesodModel){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TITLE_CHAPTER,epesodModel.getEpetitle());
        cv.put(COMICS_ID,epesodModel.getId());
        cv.put(PATH_CHAPTER,epesodModel.getServer2());
        cv.put(CHAPTER_KEY,epesodModel.getPostkey());

        float res = db.update(TABLENAMEEPE,cv,CHAPTER_KEY+" =?",new String[] {epesodModel.getPostkey()});

        if (res == -1){

            return false;

        }else {

            return true;

        }

    }

    //Delet
    public boolean DeletMyListDB(String postkey){
        SQLiteDatabase db = this.getWritableDatabase();
        float res = db.delete(TABLENAMESERIES,POSTERKEY+" = ?",new String[] {postkey});

        return res != -1;

    }
    public boolean DeletdownloadDB(String postkey){
        SQLiteDatabase db = this.getWritableDatabase();
        float res = db.delete(TABLENAMECARTOON,POSTERKEY+" = ?",new String[] {postkey});

        return res != -1;

    }
    public boolean DeletCountinuDB(String postkey){
        SQLiteDatabase db = this.getWritableDatabase();
        float res = db.delete(TABLENAMECOUNTINIWATCHING,"id = ?",new String[] {postkey});

        return res != -1;

    }
    public boolean DeletCheckEpeDB(String postkey){
        SQLiteDatabase db = this.getWritableDatabase();
        float res = db.delete(TABLENAMECHECKEPE,"epepostkey = ?",new String[] {postkey});

        return res != -1;

    }
    public boolean DeletEpe(String postkey){
        SQLiteDatabase db = this.getWritableDatabase();
        float res = db.delete(TABLENAMEEPE,ID_CHAPTER+" = ?",new String[] {postkey});
        return res != -1;

    }



    public boolean checkIfMyListExists(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLENAMESERIES, new String[] {POSTERKEY}, POSTERKEY + " =?",
                new String[] {id}, null, null, null, null);
        boolean exists = (cursor.getCount() > 0);

        return exists;
    }
    public boolean checkIfdownloadExists(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLENAMECARTOON, new String[] {POSTERKEY}, POSTERKEY + " =?",
                new String[] {id}, null, null, null, null);
        boolean exists = (cursor.getCount() > 0);

        return exists;
    }
    public boolean checkIfCountineExists(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLENAMECOUNTINIWATCHING, new String[] {POSTERKEY}, POSTERKEY + " =?",
                new String[] {id}, null, null, null, null);
        boolean exists = (cursor.getCount() > 0);

        return exists;
    }
    public boolean checkIfCheckEpeExists(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLENAMECHECKEPE, new String[] {"epepostkey"},"epepostkey =?",
                new String[] {id}, null, null, null, null);
        boolean exists = (cursor.getCount() > 0);

        return exists;
    }
    public boolean checkIfEpeExists(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLENAMEEPE, new String[] {CHAPTER_KEY}, CHAPTER_KEY + " =?",
                new String[] {id}, null, null, null, null);
        boolean exists = (cursor.getCount() > 0);

        return exists;
    }
}
