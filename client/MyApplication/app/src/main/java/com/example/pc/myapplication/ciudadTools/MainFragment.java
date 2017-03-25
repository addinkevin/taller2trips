package com.example.pc.myapplication.ciudadTools;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.myapplication.R;

public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_ciudad, container, false);
        return myFragmentView;
    }

}