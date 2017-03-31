package com.example.pc.myapplication.carruselTools;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pc.myapplication.AtraccionActivity;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.commonfunctions.Consts;

public class CarruselFragment extends Fragment {

    boolean setted = false;

    public Fragment newInstance(AtraccionActivity context, int pos, float scale, Bitmap img) {
        Bundle b = new Bundle();
        b.putInt("pos", pos);
        b.putParcelable(Consts.IMG_OUT, img);
        b.putFloat("scale", scale);
        Log.i("IMGConn", "Nueva instancia ");
        if (img == null) {
            Log.i("IMGConn", "Sin imagen ");
        } else {
            setted = true;
            Log.i("IMGConn", "con imagen");
        }
        return Fragment.instantiate(context, CarruselFragment.class.getName(), b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("IMGConn", "crea view");
        if (container == null) {
            Log.i("IMGConn", "vacia");
            return null;
        }

        LinearLayout l = (LinearLayout) inflater.inflate(R.layout.mf, container, false);

        //int pos = this.getArguments().getInt("pos");
        //TextView tv = (TextView) l.findViewById(R.id.text);
        //tv.setText("Position = " + pos);
        Log.i("IMGConn", "recumera argumento");

        Bitmap img = this.getArguments().getParcelable(Consts.IMG_OUT);
        if (img != null) {
            Log.i("IMGConn", "tiene imagen en argumento");
            ImageView imgView = (ImageView) l.findViewById(R.id.content);
            imgView.setImageBitmap(img);
        }

        CarruselLinearLayout root = (CarruselLinearLayout) l.findViewById(R.id.root);
        float scale = this.getArguments().getFloat("scale");
        root.setScaleBoth(scale);

        return l;
    }
}
