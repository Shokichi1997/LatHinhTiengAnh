package com.shokichi.gamelathinh.activities;



import android.content.ContextWrapper;
import android.graphics.Point;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

import com.shokichi.gamelathinh.R;
import com.shokichi.gamelathinh.fragments.HomePageFragment;
import com.shokichi.gamelathinh.statics.Until;
import com.shokichi.gamelathinh.utils.Score;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String fileName = "scorePractice.txt";
    private String filePath = "score";
    private File fileScore;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Read score file
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File director = contextWrapper.getDir(filePath,ContextWrapper.MODE_PRIVATE);
        fileScore = new File(director,fileName);

        readScore();

        HomePageFragment homePageFragment = new HomePageFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frmFragment,homePageFragment);
        fragmentTransaction.commit();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        Bundle bundle = new Bundle();
        bundle.putInt("width",width);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        writeScore();
    }

    public void writeScore(){

        try{
            FileOutputStream fos = new FileOutputStream(fileScore);
            for(int i = 0;i<Until.scoreList.size();i++){
                String data = Until.scoreList.get(i).toString();
                fos.write(data.getBytes());
            }

            fos.close();
            System.out.println("Write file is completed");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readScore(){
        try {
            FileInputStream fis = new FileInputStream(fileScore);
            DataInputStream dis = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));


            if(Until.scoreList.size()!=0){
                Until.scoreList = new ArrayList<>();
            }
            String line;
            while ((line = br.readLine())!=null){
                String[] score = line.split("/");
                Score newScore = new Score(score[0],Integer.parseInt(score[1]));
                Until.scoreList.add(newScore);
            }

            br.close();
            dis.close();
            fis.close();
            System.out.println("Read file is completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}