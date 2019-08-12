package com.shokichi.gamelathinh.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.shokichi.gamelathinh.R;


public class ChoosePraticeFragment extends Fragment {
    ImageButton btnGoToGame;
    Context context;
    FrameLayout frameLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameLayout = getActivity().findViewById(R.id.frmFragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup rootView = ( ViewGroup ) inflater.inflate(R.layout.fragment_choose_pratice,container,false);
        btnGoToGame = rootView.findViewById(R.id.btnGoToGame);

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnGoToGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Fragment fragment = new PraticeGameFragment();
            replaceFragment(fragment);
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = null;
        if (getFragmentManager() != null) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            transaction = manager.beginTransaction();
            transaction.replace(R.id.frmFragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }



}
