package com.example.pc.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ciudadesTools.Comentario;

import java.util.ArrayList;
import java.util.List;

public class CommentListAdapter extends BaseAdapter {

    Context context;
    List<Comentario> rowItems;     /**< Lista de todos los items del CommentList*/

    private final static String ADAPTER_TAG = "CustomAdapter CHATLIST";

    public CommentListAdapter(Context context, List<Comentario> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    public void addRowItem(List<Comentario> newRows) {
        List<Comentario> aux = new ArrayList<>(newRows);
        rowItems.clear();
        rowItems.addAll(aux);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    private class ViewHolder {
        ImageView profilePic;
        TextView userName;
        TextView comment;
        RatingBar calificacion;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.review_item, null);
            holder = new ViewHolder();

            holder.userName = (TextView) convertView.findViewById(R.id.userName);
            holder.profilePic = (ImageView) convertView.findViewById(R.id.profPic);
            holder.comment = (TextView) convertView.findViewById(R.id.comment);
            holder.calificacion = (RatingBar) convertView.findViewById(R.id.calificacion);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Comentario rowPos = rowItems.get(position);
        holder.profilePic.setImageBitmap(rowPos.profPic);
        holder.userName.setText(rowPos.userName);
        holder.userName.setTextColor(context.getResources().getColor(R.color.com_facebook_blue));
        holder.comment.setText(rowPos.comment);
        holder.comment.setTextColor(Color.BLACK);
        holder.calificacion.setRating(rowPos.calificacion);

        return convertView;
    }
}