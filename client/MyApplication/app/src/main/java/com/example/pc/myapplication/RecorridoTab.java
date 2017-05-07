package com.example.pc.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by PC on 01/05/2017.
 */

public class RecorridoTab  extends Fragment {

    private View fragView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragView = inflater.inflate(R.layout.recorrido_tab, container, false);
        return fragView;
    }
}
