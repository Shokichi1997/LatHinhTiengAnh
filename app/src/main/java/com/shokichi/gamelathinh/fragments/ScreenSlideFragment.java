package com.shokichi.gamelathinh.fragments;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.shokichi.gamelathinh.R;
import com.shokichi.gamelathinh.models.ReaderDbHelper;
import com.shokichi.gamelathinh.utils.Word;

import java.util.ArrayList;
import java.util.List;


public class ScreenSlideFragment extends Fragment {
    private ImageView imgWord,imgSound;
    private TextView lblWordEng,lblPronunciation,lblDinhNghia,lblVietWord;
    private Context context;
    public static int level = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup ) inflater.inflate(R.layout.fragment_slide_change_page,container,false);
        imgWord = rootView.findViewById(R.id.imgWord);
        imgSound = rootView.findViewById(R.id.imgSound);
        lblWordEng = rootView.findViewById(R.id.lblWordEng);
        lblVietWord = rootView.findViewById(R.id.lblVietWord);
        lblDinhNghia = rootView.findViewById(R.id.lblDinhNghia);
        lblPronunciation = rootView.findViewById(R.id.lblPronunciation);
        context = getContext();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ReaderDbHelper dbHelper = new ReaderDbHelper(getContext());
        dbHelper.openDatabase();
        SQLiteDatabase database = dbHelper.getMyDatabase();

        System.out.println("level = "+level);
        List<Word> listWord = new ArrayList<>();
        Cursor cursor =  database.rawQuery("SELECT * FROM words WHERE level = ?",new String[]{String.valueOf(level)});
        while (cursor.moveToNext()){
             String engWord = cursor.getString(1);
             String vnWord = cursor.getString(2);
             String pronounce = cursor.getString(3);
             String example = cursor.getString(4);
             byte[] imagesResource = cursor.getBlob(6);
             String audioResourceName = cursor.getString(5);

            listWord.add(new Word(engWord,vnWord,pronounce,example,imagesResource,audioResourceName));
        }
        cursor.close();
        dbHelper.closeDatabase();

        //Get index of fragment
        Bundle args = getArguments();
        int indexFragment = 0;
        if(args != null){
            indexFragment = args.getInt("page");
        }

        System.out.println("size: "+listWord.size());
        Bitmap bitmap = getImageFromByte(listWord.get(indexFragment).getImagesResource());
        if(bitmap==null){
            //bitmap = default image
            bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.logo);
        }

        imgWord.setImageBitmap(bitmap);
        lblWordEng.setText(listWord.get(indexFragment).getEngWord());
        lblVietWord.setText(listWord.get(indexFragment).getVnWord());
        lblDinhNghia.setText(listWord.get(indexFragment).getExample());
        lblPronunciation.setText(listWord.get(indexFragment).getPronounce());

        //Handler play sound when click on speaker icon

        final String nameAudioResource = listWord.get(indexFragment).getAudioResourceName();
        imgSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudioFromUriResource(nameAudioResource);
            }
        });

    }

    private void playAudioFromUriResource(String nameAudioResource) {
        System.out.println("Name audio resource: "+nameAudioResource);
        Uri uri = Uri.parse("android.resource://"+ context.getPackageName() + "/raw/" + nameAudioResource);
        System.out.println("URI :"+uri.toString());
        MediaPlayer mediaPlayer = MediaPlayer.create(context,uri);
        mediaPlayer.start();
    }

    private Bitmap getImageFromByte(byte[] imagesResource){
        if(imagesResource!=null){
            return BitmapFactory.decodeByteArray(imagesResource,0,imagesResource.length);
        }
        return null;
    }


}
