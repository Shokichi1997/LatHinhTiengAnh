package com.shokichi.gamelathinh.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ReaderDbHelper extends SQLiteOpenHelper {
    private String DB_PATH_SUFFIX = "/databases/";
    private static String DB_NAME = "engword.db";
    private SQLiteDatabase myDatabase = null;
    private Context context;

    public ReaderDbHelper(Context context) {
        super(context,DB_NAME,null,1);
        this.context = context;

        processCopy();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void processCopy() {
        try{
            File dbFile = context.getDatabasePath(DB_NAME);
            Log.v("Ten file: ",context.getApplicationInfo().dataDir+DB_PATH_SUFFIX);
            if(!dbFile.exists()){
                copyDatabaseFromAsset();
            }
        }
        catch (Exception ex){
            Log.e("Error: ",ex.toString());
        }


    }

    private void copyDatabaseFromAsset() {
        try{
            InputStream is = context.getAssets().open(DB_NAME);
            String outFileName = getDatabasePath();
            File file = new File(context.getApplicationInfo().dataDir+DB_PATH_SUFFIX);
            if(!file.exists()){
                file.mkdir();
            }
            OutputStream os = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer))>0){
                os.write(buffer,0,length);
            }
            os.flush();
            os.close();
            is.close();
        }catch (Exception ex){
            Log.e("Error: ",ex.toString());
        }
    }

    private String getDatabasePath() {
        return context.getApplicationInfo().dataDir+DB_PATH_SUFFIX+DB_NAME;
    }

    public void openDatabase(){
        String myPath = getDatabasePath();
        myDatabase = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void closeDatabase(){
        myDatabase.close();
        super.close();
    }

    public SQLiteDatabase getMyDatabase(){
        return this.myDatabase;
    }
}
