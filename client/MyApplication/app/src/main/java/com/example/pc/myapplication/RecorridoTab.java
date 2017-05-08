package com.example.pc.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.singletons.PosVideoSingleton;

public class RecorridoTab  extends Fragment {

    private View fragView;
    private String _id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragView = inflater.inflate(R.layout.recorrido_tab, container, false);

        if ( _id == null || _id.isEmpty()) {
            _id = getActivity().getIntent().getStringExtra(Consts._ID);
        }

        return fragView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if ( _id != null && !_id.isEmpty())
            savedInstanceState.putString(Consts._ID, _id);

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        _id = savedInstanceState.getString(Consts._ID);
    }
}
