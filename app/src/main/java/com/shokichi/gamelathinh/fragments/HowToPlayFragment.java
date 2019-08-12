package com.shokichi.gamelathinh.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shokichi.gamelathinh.R;
import com.shokichi.gamelathinh.statics.Until;

public class HowToPlayFragment extends Fragment {
    Button btnReturnHome;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = ( ViewGroup ) inflater.inflate(R.layout.fragment_how_to_play,container,false);
        btnReturnHome = root.findViewById(R.id.btnReturnHome);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Until.removeFragment(getFragmentManager(),HowToPlayFragment.this);
            }
        });
    }


}
