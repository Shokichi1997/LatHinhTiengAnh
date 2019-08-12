package com.shokichi.gamelathinh.utils;

public class Word {


    private String engWord;
    private String vnWord;
    private String pronounce;
    private String example;
    private byte[] imagesResource;
    private String audioResourceName;

    public Word(String engWord, String vnWord, String pronounce, String example, byte[] imagesResource, String audioResourceName){
        this.engWord = engWord;
        this.vnWord = vnWord;
        this.pronounce = pronounce;
        this.example = example;
        this.imagesResource = imagesResource;
        this.audioResourceName = audioResourceName;
    }

    public String getEngWord() {
        return engWord;
    }

    public String getVnWord() {
        return vnWord;
    }

    public String getPronounce() {
        return pronounce;
    }

    public String getExample() {
        return example;
    }

    public byte[] getImagesResource() {
        return imagesResource;
    }

    public String getAudioResourceName() {
        return audioResourceName;
    }

}
