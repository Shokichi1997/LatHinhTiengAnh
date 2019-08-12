package com.shokichi.gamelathinh.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shokichi.gamelathinh.R;
import com.shokichi.gamelathinh.utils.Subject;

import java.util.ArrayList;


public class SubjectItemAdapter extends ArrayAdapter<Subject>{

    public SubjectItemAdapter(@NonNull Context context, ArrayList<Subject> subjects) {
        super(context, 0,subjects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Subject currentSubject = getItem(position);
        ImageView imgSubjectIcon = listItemView.findViewById(R.id.imgIconSubject);
        if(currentSubject.hasImage()){
            imgSubjectIcon.setImageResource(currentSubject.getImageResourceId());//

        }
        else {
            imgSubjectIcon.setVisibility(View.GONE);
        }

        TextView lblNameSubject = listItemView.findViewById(R.id.lblNameSubject);
        lblNameSubject.setText(currentSubject.getMNameSubject());


        return listItemView;
    }


}
