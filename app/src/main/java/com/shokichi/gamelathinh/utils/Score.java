package com.shokichi.gamelathinh.utils;

public class Score {
    private String time;
    private int level;

    public Score(String time,int level){
        this.time = time;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return time+"/"+level;
    }
}
