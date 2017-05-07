package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.pc.myapplication.MainActivity;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.commonfunctions.Consts;

import de.hdodenhof.circleimageview.CircleImageView;


public class ReceiverOnUserImage extends BroadcastReceiver {

    private final Activity activity;
    private final View headerView;
    private final TripTP tripTP;

    public ReceiverOnUserImage(Activity activity, View headerView) {
        this.activity = activity;
        this.headerView = headerView;
        tripTP = (TripTP) activity.getApplication();

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            Bitmap imageUser = intent.getParcelableExtra(Consts.IMG_OUT);
            int imgID = intent.getIntExtra(Consts.IMG_ID, -1);
            if (imageUser != null && imgID >= 0) {
                if (imgID == Consts.PROF_IMG) {
                    CircleImageView img = (CircleImageView) headerView.findViewById(R.id.profile_image);
                    img.setImageBitmap(imageUser);
                    tripTP.setImgProf(imageUser);
                } else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        BitmapDrawable ob = new BitmapDrawable(activity.getResources(), imageUser);
                        headerView.setBackground(ob);
                    } else {
                        BitmapDrawable ob = new BitmapDrawable(imageUser);
                        headerView.setBackgroundDrawable(ob);
                    }

                    tripTP.setImgBanner(imageUser);

                }


            } else {
                Toast.makeText(activity,"No imagen", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(activity,"No se pudo conectar con Splex", Toast.LENGTH_LONG).show();
        }
    }
}
