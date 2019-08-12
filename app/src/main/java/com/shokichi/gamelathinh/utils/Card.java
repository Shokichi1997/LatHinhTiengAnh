package com.shokichi.gamelathinh.utils;

import android.graphics.Bitmap;
import android.widget.Button;

public class Card {
    private int x,y;
    private Button card;


    /**
     * contructer for card with audio file
     * @param x is row index of table
     * @param y is column index of table
     * @param card is button to handle click event*/
    public Card(int x,int y, Button card){
        this.x = x;
        this.y = y;
        this.card = card;
    }

    /**
     * @return button of Card*/
    public Button getButton() {
        return card;
    }

    /**
     * @return row index of card in table*/
    public int getX() {
        return x;
    }

    /**
     * @return column index of card in table*/
    public int getY() {
        return y;
    }
}
