package com.example.pc.myapplication.ciudadTools;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ciudadesTools.Ciudad;
import com.example.pc.myapplication.commonfunctions.Consts;

public class RecorridosFragment extends Fragment {

    private Ciudad ciudad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_recorridos, container, false);

        if (savedInstanceState != null && savedInstanceState.containsKey(Consts.CITY)) {
            ciudad = savedInstanceState.getParcelable(Consts.CITY);
        }

        return myFragmentView;
    }



    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (ciudad != null) {
            savedInstanceState.putParcelable(Consts.CITY, ciudad);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }
}