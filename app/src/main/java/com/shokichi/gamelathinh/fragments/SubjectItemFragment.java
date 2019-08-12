package com.shokichi.gamelathinh.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shokichi.gamelathinh.R;
import com.shokichi.gamelathinh.activities.MainActivity;
import com.shokichi.gamelathinh.adapters.SubjectItemAdapter;
import com.shokichi.gamelathinh.utils.Subject;

import java.util.ArrayList;
import java.util.Objects;


public class SubjectItemFragment extends Fragment {
    private ListView listView;
    public SubjectItemFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = ( ViewGroup ) inflater.inflate(R.layout.fragment_level,container,false);
        listView = (ListView) rootView.findViewById(R.id.listSubject);

        final ArrayList<Subject> listSubject = new ArrayList<>();
        listSubject.add(new Subject("OFFICE",R.drawable.office));
        listSubject.add(new Subject("BUSINESS",R.drawable.business));
        listSubject.add(new Subject("TECHNICAL",R.drawable.technical));
        listSubject.add(new Subject("TRAVEL",R.drawable.travel));
        listSubject.add(new Subject("ENTERTAINMENT",R.drawable.entertainment));

        //Update later
//        listSubject.add(new Subject("PURCHASING",R.drawable.purchasing));
////        listSubject.add(new Subject("DINING OUT",R.drawable.dining_out));
////        listSubject.add(new Subject("PERSONNEL",R.drawable.personnel));
////        listSubject.add(new Subject("FINANCE AND BUDGETING",R.drawable.finance));
////        listSubject.add(new Subject("CORPORATE",R.drawable.corporate));
////        listSubject.add(new Subject("MANUFACTURING",R.drawable.manufacturing));
////        listSubject.add(new Subject("HOUSING",R.drawable.hoursing));
////        listSubject.add(new Subject("HEALTH",R.drawable.health));

        SubjectItemAdapter adapter = new SubjectItemAdapter(getActivity(),listSubject);
        listView.setAdapter(adapter);

        return rootView;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new LearnFragment();
                Bundle bundle = new Bundle();
                System.out.println("List level:"+ position+1);
                ScreenSlideFragment.level = position +1;
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
