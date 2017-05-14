package com.example.pc.myapplication.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.ciudadesTools.Recorrido;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RecorridoAtraccionesAdp extends BaseAdapter {

    private final Activity activity;
    private final TripTP tripTP;
    List<Atraccion> atraccionItems; /**< Lista de todos los items de List*/
    ArrayList<Boolean> needUpdate;

    public RecorridoAtraccionesAdp(Activity activity, List<Atraccion> atraccionItems) {
        this.activity = activity;
        this.atraccionItems = atraccionItems;
        needUpdate = new ArrayList<>();

        if (!atraccionItems.isEmpty()) {
            for (int i = 0; i < atraccionItems.size(); i++) {
                needUpdate.add(false);
            }
        }
        tripTP = (TripTP) activity.getApplication();

    }

    public void add(Atraccion atraccion) {
        atraccionItems.add(atraccion);
        needUpdate.add(false);
    }

    public void setNeedUpdateToPos(int index, Boolean needUpdate) {
        this.needUpdate.set(index, needUpdate);
    }

    private class ViewHolder {
        TextView recorridoName;
    }

    @Override
    public int getCount() {
        return atraccionItems.size();
    }

    @Override
    public Object getItem(int position) {
        return atraccionItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return atraccionItems.indexOf(getItem(position));
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(R.layout.recorrido_atr_item, null, true);
            holder = new ViewHolder();
            holder.recorridoName = (TextView) view.findViewById(R.id.nombreAtr);

            final Atraccion rowPos = atraccionItems.get(position);

            needUpdate.set(position, false);

            holder.recorridoName.setText(rowPos.nombre);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }
}

