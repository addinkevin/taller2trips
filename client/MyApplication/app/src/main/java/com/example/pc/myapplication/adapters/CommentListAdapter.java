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
        ImageView profilePic = null;
        TextView userName = null;
        TextView comment = null;
        RatingBar calificacion = null;
        boolean finish = false;
        int position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        Comentario rowPos = rowItems.get(position);

        if ( view == null || (view.getTag() != null &&
                ( !((ViewHolder) view.getTag()).finish || ((ViewHolder) view.getTag()).position != position))) {


            if (view == null) {
                view = mInflater.inflate(R.layout.review_item, null);
            }

            if (view.getTag() != null) {
                holder = (ViewHolder) view.getTag();
            } else {
                holder = new ViewHolder();
            }
            if (holder.userName == null)
                holder.userName = (TextView) view.findViewById(R.id.userName);
            if (holder.profilePic == null)
                holder.profilePic = (ImageView) view.findViewById(R.id.profPic);
            if (holder.comment == null)
                holder.comment = (TextView) view.findViewById(R.id.comment);
            if (holder.calificacion == null)
                holder.calificacion = (RatingBar) view.findViewById(R.id.calificacion);

            if (rowPos.profPic != null) {
                holder.finish = true;
            }
            holder.profilePic.setImageBitmap(rowPos.profPic);
            holder.userName.setText(rowPos.userName);
            holder.userName.setTextColor(context.getResources().getColor(R.color.com_facebook_blue));
            holder.comment.setText(rowPos.comment);
            holder.comment.setTextColor(Color.BLACK);
            holder.calificacion.setRating(rowPos.calificacion);
            holder.position = position;

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.profilePic.setImageBitmap(rowPos.profPic);
        holder.userName.setText(rowPos.userName);
        holder.userName.setTextColor(context.getResources().getColor(R.color.com_facebook_blue));
        holder.comment.setText(rowPos.comment);
        holder.comment.setTextColor(Color.BLACK);
        holder.calificacion.setRating(rowPos.calificacion);
        holder.position = position;

        return view;
    }
}