package com.shokichi.gamelathinh.statics;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.shokichi.gamelathinh.R;
import com.shokichi.gamelathinh.utils.Score;

import java.util.ArrayList;
import java.util.List;

public class Until {
    public static List<Score> scoreList = new ArrayList<>();

    public static void replaceFragment(FragmentManager manager,Fragment fragment) {
        FragmentTransaction transaction = null;
        if (manager != null) {
            transaction = manager.beginTransaction();
            transaction.replace(R.id.frmFragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public static void removeFragment(FragmentManager manager,Fragment fragment) {
        FragmentTransaction transaction = null;
        if (manager != null) {
            transaction = manager.beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
            manager.popBackStack();
        }
    }
}
