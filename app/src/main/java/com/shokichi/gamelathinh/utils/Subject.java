package com.shokichi.gamelathinh.utils;

public class Subject {
    private static final int NO_IMAGE_PROViDED = -1;
    private String mNameSubject;
    private int imageResourceId = NO_IMAGE_PROViDED;


    public Subject(String mNameSubject,int imageResourceId){
        this.mNameSubject = mNameSubject;
        this.imageResourceId = imageResourceId;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getMNameSubject(){
        return mNameSubject;
    }

    public boolean hasImage(){
        return imageResourceId != NO_IMAGE_PROViDED;
    }
}
