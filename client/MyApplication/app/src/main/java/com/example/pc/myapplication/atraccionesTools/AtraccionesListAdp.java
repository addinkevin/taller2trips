package com.example.pc.myapplication.atraccionesTools;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ciudadesTools.Atraccion;

import java.util.List;

public class AtraccionesListAdp extends BaseAdapter {

    private final Fragment context;
    List<Atraccion> atraccionItems; /**< Lista de todos los items del ChatList*/

    public AtraccionesListAdp(Fragment context, List<Atraccion> atraccionItems) {
        this.context = context;
        this.atraccionItems = atraccionItems;

    }

    public void addImg(Bitmap img, int imgID) {
        atraccionItems.get(imgID).fotosBitmap.add(img);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView atraccionPic;
        TextView atraccionName;
        boolean setted = false;
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
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null || (view.getTag() != null && !((ViewHolder) view.getTag()).setted)) {
            LayoutInflater inflater = context.getActivity().getLayoutInflater();
            view = inflater.inflate(R.layout.atraccion_item, null, true);
            holder = new ViewHolder();
            holder.atraccionName = (TextView) view.findViewById(R.id.textView2);
            holder.atraccionPic = (ImageView) view.findViewById(R.id.imageView);
            view.setTag(holder);
            Atraccion rowPos = atraccionItems.get(position);
            if (!rowPos.fotosBitmap.isEmpty()) {
                holder.setted =true;
                holder.atraccionPic.setImageBitmap(rowPos.fotosBitmap.get(0));
            }
            holder.atraccionName.setText(rowPos.nombre);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }
}
