package com.example.pc.myapplication.mapTools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.myapplication.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.InputStream;
import java.net.URL;


public class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private SparseArray<View> sparseArray;
    private LayoutInflater inflater;

    public MapInfoWindowAdapter(LayoutInflater inflater){
        this.inflater = inflater;
        sparseArray = new SparseArray<>();

    }

    @Override
    public View getInfoContents(final Marker marker) {

        if (marker.getTag() != null && sparseArray.get((Integer) marker.getTag()) != null) {
            return sparseArray.get((Integer) marker.getTag());
        } else if(marker.getTag() != null) {

            View view = inflater.inflate(R.layout.map_info_content, null);
            sparseArray.put((Integer) marker.getTag(), view);

            LatLng pos = marker.getPosition();
            String imageURL = "https://maps.googleapis.com/maps/api/streetview?size=400x400&location=" +
                    pos.latitude + "," + pos.longitude + "&fov=90&heading=235&pitch=10";
            final ImageView img = (ImageView) view.findViewById(R.id.streetIMG);
            ImageStreetViewRetriever asd = new ImageStreetViewRetriever() {
                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    img.setImageBitmap(bitmap);
                    if (marker.isInfoWindowShown()) {
                        marker.showInfoWindow();
                    }
                }
            };

            asd.execute(imageURL);

            TextView tvTitle = ((TextView) view.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = ((TextView) view.findViewById(R.id.snippet));
            tvSnippet.setText(marker.getSnippet());

            return view;
        } else {
            return null;
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    private abstract class ImageStreetViewRetriever extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                return BitmapFactory.decodeStream((InputStream)url.getContent());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}