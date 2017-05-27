package com.example.pc.myapplication.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.PuntoInteres;

import java.util.ArrayList;
import java.util.List;


public class PuntosInteresAdp  extends BaseAdapter {

    private final Activity activity;
    private final TripTP tripTP;
    List<PuntoInteres> atraccionItems; /**< Lista de todos los items de List*/
    ArrayList<Boolean> needUpdate;

    public PuntosInteresAdp(Activity activity, List<PuntoInteres> puntoInteres) {
        this.activity = activity;
        this.atraccionItems = puntoInteres;
        needUpdate = new ArrayList<>();

        if (!puntoInteres.isEmpty()) {
            for (int i = 0; i < puntoInteres.size(); i++) {
                needUpdate.add(false);
            }
        }
        tripTP = (TripTP) activity.getApplication();

    }

    public void add(PuntoInteres puntoInteres) {
        atraccionItems.add(puntoInteres);
        needUpdate.add(false);
    }

    public void setNeedUpdateToPos(int index, Boolean needUpdate) {
        this.needUpdate.set(index, needUpdate);
    }

    private class ViewHolder {
        TextView puntoInteresName;
        int position;
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
        if (view == null || ( view.getTag() != null &&
                ((ViewHolder)view.getTag()).position == position &&
                needUpdate.get(position))) {

            if (view == null || view.getTag() == null) {
                holder = new ViewHolder();
            } else {
                holder = (ViewHolder)view.getTag();
            }

            if (view == null) {
                LayoutInflater inflater = activity.getLayoutInflater();
                view = inflater.inflate(R.layout.recorrido_atr_item, null, true);
                holder.puntoInteresName = (TextView) view.findViewById(R.id.nombreAtr);
                view.setTag(holder);
            }

            final PuntoInteres rowPos = atraccionItems.get(position);

            needUpdate.set(position, false);

            holder.puntoInteresName.setText(rowPos.nombre);
            holder.position = position;
        } else {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }


}
