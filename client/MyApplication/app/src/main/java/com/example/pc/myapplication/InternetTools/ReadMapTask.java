package com.example.pc.myapplication.InternetTools;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pc.myapplication.commonfunctions.PathJSONParser;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by PC on 03/04/2017.
 */

public class ReadMapTask  extends AsyncTask<String, Void , String> {

    private GoogleMap googleMap;
    private Context mapLN;

    public ReadMapTask(GoogleMap googleMap, Context mapLN) {

        this.googleMap = googleMap;
        this.mapLN = mapLN;
    }

    @Override
    protected String doInBackground(String... url) {
        // TODO Auto-generated method stub
        String data = "";
        try {
            Log.i("Background Task GPS", "Inicia la busqueda de mejor ruta.");
            MapHttpConnection http = new MapHttpConnection();
            data = http.readUr(url[0]);
        } catch (Exception e) {
            // TODO: handle exception
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        new ParserTask().execute(result);
    }

    private class ParserTask extends AsyncTask<String,Integer, List<List<HashMap<String , String >>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(4);
                polyLineOptions.color(Color.BLUE);
            }

            if (polyLineOptions == null) {
                Log.i("Background Task GPS", "No se pudo encontrar una ruta.");
                Toast.makeText(mapLN,"No se pudo encontrar una ruta", Toast.LENGTH_LONG).show();
            } else {
                Log.i("Background Task GPS", "Mejor ruta encotrada.");
                googleMap.addPolyline(polyLineOptions);
            }

        }}

}
