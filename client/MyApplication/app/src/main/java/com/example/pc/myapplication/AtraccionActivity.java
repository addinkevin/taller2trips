package com.example.pc.myapplication;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.pc.myapplication.carruselTools.CarruselPagerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class AtraccionActivity extends AppCompatActivity implements OnMapReadyCallback{

    private Toolbar toolbar;

    public final static int PAGES = 5;
    // You can choose a bigger number for LOOPS, but you know, nobody will fling
    // more than 1000 times just in order to test your "infinite" ViewPager :D
    public final static int LOOPS = 1000;
    public final static int FIRST_PAGE = PAGES * LOOPS / 2;

    public CarruselPagerAdapter adapter;
    public ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atraccion);
        toolbar = (Toolbar) findViewById(R.id.include3);
        toolbar.setTitle(getString(R.string.ciudades));
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        pager = (ViewPager) findViewById(R.id.myviewpager);

        adapter = new CarruselPagerAdapter(this, this.getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setPageTransformer(false, adapter);

        // Set current item to the middle page so we can fling to both
        // directions left and right
        pager.setCurrentItem(FIRST_PAGE);

        // Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        pager.setOffscreenPageLimit(3);

        // Set margin for pages as a negative number, so a part of next and
        // previous pages will be showed
        pager.setPageMargin(-200);


       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);/*/

        LinearLayout linlaIMG = (LinearLayout) findViewById(R.id.linlaIMG);
        LinearLayout linlaVIDEO = (LinearLayout) findViewById(R.id.linlaVIDEO);
        LinearLayout linlaAUDIO = (LinearLayout) findViewById(R.id.linlaAUDIO);

        List<LinearLayout> listLinla = new ArrayList<>(3);
        listLinla.add(linlaAUDIO);
        listLinla.add(linlaIMG);
        listLinla.add(linlaVIDEO);

        // SET THE IMAGEVIEW DIMENSIONS
        int dimens = 100;
        float density = getResources().getDisplayMetrics().density;
        int finalDimens = (int)(dimens * density);
        // SET THE MARGIN
        int dimensMargin = 4;
        float densityMargin = getResources().getDisplayMetrics().density;
        int finalDimensMargin = (int) (dimensMargin * densityMargin);

        ImageView img1 = new ImageView(this);
        img1.setImageResource(R.drawable.buenosairesoriginal);
        ImageView img2 = new ImageView(this);
        img2.setImageResource(R.drawable.ba6);
        ImageView img3 = new ImageView(this);
        img3.setImageResource(R.drawable.puente_madero_noche_980_as);
        ImageView img4 = new ImageView(this);
        img4.setImageResource(R.drawable.buenosairesoriginal2);
        ImageView img5 = new ImageView(this);
        img5.setImageResource(R.drawable.buenosairesoriginal3);
        ImageView img6 = new ImageView(this);
        img6.setImageResource(R.drawable.buenosairesoriginal4);
        ImageView img7 = new ImageView(this);
        img7.setImageResource(R.drawable.buenosairesoriginal5);
        ImageView img8 = new ImageView(this);
        img8.setImageResource(R.drawable.buenosairesoriginal6);
        ImageView img9 = new ImageView(this);
        img9.setImageResource(R.drawable.buenosairesoriginal7);
        ImageView img10 = new ImageView(this);
        img10.setImageResource(R.drawable.buenosairesoriginal8);

        List<ImageView> list = new ArrayList<>(3);
        list.add(img1);
        list.add(img2);
        list.add(img3);
        list.add(img4);
        list.add(img5);
        list.add(img6);
        list.add(img7);
        list.add(img8);
        list.add(img9);
        list.add(img10);

        ImageView imgA1 = new ImageView(this);
        imgA1.setImageResource(R.drawable.buenosairesoriginal);
        ImageView imgA2 = new ImageView(this);
        imgA2.setImageResource(R.drawable.ba6);
        ImageView imgA3 = new ImageView(this);
        imgA3.setImageResource(R.drawable.puente_madero_noche_980_as);
        ImageView imgA4 = new ImageView(this);
        imgA4.setImageResource(R.drawable.buenosairesoriginal2);
        ImageView imgA5 = new ImageView(this);
        imgA5.setImageResource(R.drawable.buenosairesoriginal3);
        ImageView imgA6 = new ImageView(this);
        imgA6.setImageResource(R.drawable.buenosairesoriginal4);
        ImageView imgA7 = new ImageView(this);
        imgA7.setImageResource(R.drawable.buenosairesoriginal5);
        ImageView imgA8 = new ImageView(this);
        imgA8.setImageResource(R.drawable.buenosairesoriginal6);
        ImageView imgA9 = new ImageView(this);
        imgA9.setImageResource(R.drawable.buenosairesoriginal7);
        ImageView imgA10 = new ImageView(this);
        imgA10.setImageResource(R.drawable.buenosairesoriginal8);

        List<ImageView> listA = new ArrayList<>(3);
        listA.add(imgA1);
        listA.add(imgA2);
        listA.add(imgA3);
        listA.add(imgA4);
        listA.add(imgA5);
        listA.add(imgA6);
        listA.add(imgA7);
        listA.add(imgA8);
        listA.add(imgA9);
        listA.add(imgA10);

        ImageView imgV1 = new ImageView(this);
        imgV1.setImageResource(R.drawable.buenosairesoriginal);
        ImageView imgV2 = new ImageView(this);
        imgV2.setImageResource(R.drawable.ba6);
        ImageView imgV3 = new ImageView(this);
        imgV3.setImageResource(R.drawable.puente_madero_noche_980_as);
        ImageView imgV4 = new ImageView(this);
        imgV4.setImageResource(R.drawable.buenosairesoriginal2);
        ImageView imgV5 = new ImageView(this);
        imgV5.setImageResource(R.drawable.buenosairesoriginal3);
        ImageView imgV6 = new ImageView(this);
        imgV6.setImageResource(R.drawable.buenosairesoriginal4);
        ImageView imgV7 = new ImageView(this);
        imgV7.setImageResource(R.drawable.buenosairesoriginal5);
        ImageView imgV8 = new ImageView(this);
        imgV8.setImageResource(R.drawable.buenosairesoriginal6);
        ImageView imgV9 = new ImageView(this);
        imgV9.setImageResource(R.drawable.buenosairesoriginal7);
        ImageView imgV10 = new ImageView(this);
        imgV10.setImageResource(R.drawable.buenosairesoriginal8);

        List<ImageView> listV = new ArrayList<>(3);
        listV.add(imgV1);
        listV.add(imgV2);
        listV.add(imgV3);
        listV.add(imgV4);
        listV.add(imgV5);
        listV.add(imgV6);
        listV.add(imgV7);
        listV.add(imgV8);
        listV.add(imgV9);
        listV.add(imgV10);

        for (int i = 0; i < list.size(); i++) {
            ImageView imgUsers = list.get(i);
            LinearLayout.LayoutParams imgvwDimens = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imgUsers.setLayoutParams(imgvwDimens);

            // SET SCALETYPE
            imgUsers.setScaleType(ImageView.ScaleType.CENTER_CROP);

            LinearLayout.LayoutParams imgvwMargin = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imgvwMargin.setMargins(finalDimensMargin, finalDimensMargin, finalDimensMargin, finalDimensMargin);

            // ADD THE NEW IMAGEVIEW WITH THE PROFILE PICTURE LOADED TO THE LINEARLAYOUT
            linlaIMG.addView(imgUsers, imgvwMargin);

            imgUsers.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    System.out.println(v.getId());
                }
            });
        }

        for (int i = 0; i < listA.size(); i++) {
            ImageView imgUsers = listA.get(i);
            LinearLayout.LayoutParams imgvwDimens = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imgUsers.setLayoutParams(imgvwDimens);

            // SET SCALETYPE
            imgUsers.setScaleType(ImageView.ScaleType.CENTER_CROP);

            LinearLayout.LayoutParams imgvwMargin = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imgvwMargin.setMargins(finalDimensMargin, finalDimensMargin, finalDimensMargin, finalDimensMargin);

            // ADD THE NEW IMAGEVIEW WITH THE PROFILE PICTURE LOADED TO THE LINEARLAYOUT
            linlaAUDIO.addView(imgUsers, imgvwMargin);

            imgUsers.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    System.out.println(v.getId());
                }
            });
        }

        for (int i = 0; i < listV.size(); i++) {
            ImageView imgUsers = listV.get(i);
            LinearLayout.LayoutParams imgvwDimens = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imgUsers.setLayoutParams(imgvwDimens);

            // SET SCALETYPE
            imgUsers.setScaleType(ImageView.ScaleType.CENTER_CROP);

            LinearLayout.LayoutParams imgvwMargin = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imgvwMargin.setMargins(finalDimensMargin, finalDimensMargin, finalDimensMargin, finalDimensMargin);

            // ADD THE NEW IMAGEVIEW WITH THE PROFILE PICTURE LOADED TO THE LINEARLAYOUT
            linlaVIDEO.addView(imgUsers, imgvwMargin);

            imgUsers.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    System.out.println(v.getId());
                }
            });
        }

    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng latLng = new LatLng(-34.603089,-58.381618);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        map.addMarker(new MarkerOptions().position(latLng).flat(true));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12.0f));
    }

}
