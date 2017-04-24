package com.example.pc.myapplication;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.mapTools.ImageStreetViewRetriever;
import com.example.pc.myapplication.mapTools.MapInfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;

public class FullImageMapActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView nombreView;
    private TextView descripView;
    private boolean hideAndVisivility = true;

    private void hideClockBateryBar(){
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_map);

        hideClockBateryBar();

        String description = getIntent().getStringExtra(Consts.DESCRIPCION);
        String nombre = getIntent().getStringExtra(Consts.NOMBRE);
        LatLng position = getIntent().getParcelableExtra(Consts.POS);

        String imageURL = "https://maps.googleapis.com/maps/api/streetview?size=800x800&location=" +
                position.latitude + "," + position.longitude + "&fov=90&heading=235&pitch=10";

        final ImageView img = (ImageView) findViewById(R.id.image1);
        ImageStreetViewRetriever asd = new ImageStreetViewRetriever() {
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                img.setImageBitmap(bitmap);
            }
        };
        img.setOnClickListener(this);

        asd.execute(imageURL);

        nombreView = (TextView) findViewById(R.id.textView3);
        nombreView.setText(nombre);

        descripView = (TextView) findViewById(R.id.textView4);
        descripView.setText(description);
    }

    @Override
    public void onClick(View v) {
        if (hideAndVisivility) {
            nombreView.setVisibility(View.GONE);
            descripView.setVisibility(View.GONE);
        } else {
            nombreView.setVisibility(View.VISIBLE);
            descripView.setVisibility(View.VISIBLE);
        }

        hideAndVisivility = !hideAndVisivility;

    }
}
