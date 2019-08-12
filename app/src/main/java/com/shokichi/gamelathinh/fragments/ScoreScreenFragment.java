package com.shokichi.gamelathinh.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shokichi.gamelathinh.R;
import com.shokichi.gamelathinh.statics.Until;
import com.shokichi.gamelathinh.utils.Score;

import java.util.List;

public class ScoreScreenFragment extends Fragment{
    TextView lblTime1,lblTime2,lblTime3,lblTime4,lblTime5;
    TextView lblLevel1,lblLevel2,lblLevel3,lblLevel4,lblLevel5;
    Button btnReturnHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = ( ViewGroup ) inflater.inflate(R.layout.fragment_score,container,false);

        //Get level
        lblLevel1 = root.findViewById(R.id.lblMucLevel1);
        lblLevel2 = root.findViewById(R.id.lblMucLevel2);
        lblLevel3 = root.findViewById(R.id.lblMucLevel3);
        lblLevel4 = root.findViewById(R.id.lblMucLevel4);
        lblLevel5 = root.findViewById(R.id.lblMucLevel5);

        //Get time
        lblTime1 = root.findViewById(R.id.lblThoiGian1);
        lblTime2 = root.findViewById(R.id.lblThoiGian2);
        lblTime3 = root.findViewById(R.id.lblThoiGian3);
        lblTime4 = root.findViewById(R.id.lblThoiGian4);
        lblTime5 = root.findViewById(R.id.lblThoiGian5);

        btnReturnHome = root.findViewById(R.id.btnTroVeTuScore);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Score> scores = Until.scoreList;
        sortListScore(scores);

        setScoreToScreen(scores);

        btnReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Until.removeFragment(getFragmentManager(),ScoreScreenFragment.this);
            }
        });

    }

    private void sortListScore(List<Score> scores) {
        for(int i = 0;i<scores.size()-1;i++){
            for(int j=i+1;j<scores.size();j++){
                String[] scoreA = scores.get(i).getTime().split(":");
                String[] scoreB = scores.get(j).getTime().split(":");

                int minuteA,minuteB,secondA,secondB;
                minuteA = Integer.parseInt(scoreA[0].trim());
                minuteB = Integer.parseInt(scoreB[0].trim());
                secondA = Integer.parseInt(scoreA[1].trim());
                secondB = Integer.parseInt(scoreB[1].trim());

                if (minuteA > minuteB) {
                    hoanDoi(scores, i, j);
                } else if(minuteA == minuteB){
                    if(secondA > secondB){
                        hoanDoi(scores,i,j);
                    }
                }

            }
        }

        //Remove item has index > 5
        for(int i = scores.size() - 1;i>=5;i--){
            scores.remove(scores.size() - 1);
        }
    }

    private void setScoreToScreen(List<Score> scores) {
        if(scores.size()>=1){
            lblTime1.setText( scores.get(0).getTime());
            lblLevel1.setText(String.valueOf(scores.get(0).getLevel()));
        }

        if(scores.size()>=2) {
            lblTime2.setText( scores.get(1).getTime());
            lblLevel2.setText(String.valueOf(scores.get(1).getLevel()));
        }

        if(scores.size()>=3){
            lblTime3.setText( scores.get(2).getTime());
            lblLevel3.setText(String.valueOf(scores.get(2).getLevel()));
        }

        if(scores.size()>=4){
            lblTime4.setText( scores.get(3).getTime());
            lblLevel4.setText(String.valueOf(scores.get(3).getLevel()));
        }

        if(scores.size()>=5){
            lblTime5.setText( scores.get(4).getTime());
            lblLevel5.setText(String.valueOf(scores.get(4).getLevel()));
        }
    }

    private void hoanDoi(List<Score> scores, int i, int j) {
        Score temp = scores.get(i);
        scores.set(i,scores.get(j));
        scores.set(j,temp);
    }
}
