package com.example.pc.myapplication.carruselTools;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.pc.myapplication.AtraccionTab;
import com.example.pc.myapplication.FullScreenViewActivity;
import com.example.pc.myapplication.FullScreenViewPIActivity;
import com.example.pc.myapplication.PuntoInteresTab;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.singletons.ImagesSingleton;
import com.example.pc.myapplication.singletons.ImagesSingletonPI;

/**
 * Created by PC on 19/05/2017.
 */

public class CarruselPIFragment  extends Fragment{

    public Fragment newInstance(PuntoInteresTab context, int pos, float scale, Bitmap img) {
        Bundle b = new Bundle();
        b.putInt("pos", pos);
        b.putParcelable(Consts.IMG_OUT, img);
        b.putFloat("scale", scale);

        return Fragment.instantiate(context.getContext(), CarruselPIFragment.class.getName(), b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {

            return null;
        }

        LinearLayout l = (LinearLayout) inflater.inflate(R.layout.mf, container, false);

        final int pos = this.getArguments().getInt("pos");
        Bitmap img = this.getArguments().getParcelable(Consts.IMG_OUT);
        if (img != null) {

            ImageView imgView = (ImageView) l.findViewById(R.id.content);
            imgView.setImageBitmap(img);
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagesSingletonPI.getInstance().setCurrent(pos);
                    Intent fullScreen = new Intent(getActivity(), FullScreenViewPIActivity.class);
                    fullScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(fullScreen);
                }
            });
        }

        CarruselLinearLayout root = (CarruselLinearLayout) l.findViewById(R.id.root);
        float scale = this.getArguments().getFloat("scale");
        root.setScaleBoth(scale);

        return l;
    }
}
