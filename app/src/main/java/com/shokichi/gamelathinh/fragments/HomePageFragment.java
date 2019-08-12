package com.shokichi.gamelathinh.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shokichi.gamelathinh.R;


public class HomePageFragment extends Fragment  {
    private Button btnStart, btnExit, btnScore, btnAbout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup viewGroup = ( ViewGroup ) inflater.inflate(R.layout.fragment_home_page, container, false);
        btnStart = viewGroup.findViewById(R.id.btnStart);
        btnExit = viewGroup.findViewById(R.id.btnExit);
        btnScore = viewGroup.findViewById(R.id.btnScore);
        btnAbout = viewGroup.findViewById(R.id.btnAbout);
        return viewGroup;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubjectItemFragment fragment = new SubjectItemFragment();
                 replaceFragment(fragment);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Thoát");
                builder.setMessage("Bạn chắc chắn muốn thoát không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScoreScreenFragment fragment = new ScoreScreenFragment();
                replaceFragment(fragment);
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new HowToPlayFragment();
                replaceFragment(fragment);
            }
        });
    }




    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = null;
        if (getFragmentManager() != null) {
            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frmFragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

}
