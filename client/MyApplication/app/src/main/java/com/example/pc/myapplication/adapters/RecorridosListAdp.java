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
import java.util.List;
import java.util.Map;

public class RecorridosListAdp extends BaseAdapter {

    private final Activity activity;
    private final TripTP tripTP;
    private List<Recorrido> atraccionItems; /**< Lista de todos los items de List*/
    private ArrayList<Boolean> needUpdate;

    public RecorridosListAdp(Activity activity, List<Recorrido> atraccionItems) {
        this.activity = activity;
        this.atraccionItems = atraccionItems;
        tripTP = (TripTP) activity.getApplication();
        needUpdate = new ArrayList<>();//setear cuando se necesite actualizar la view

    }

    public void add(Recorrido atraccion) {
        atraccionItems.add(atraccion);
        needUpdate.add(false);
    }

    public void setNeedUpdateToPos(int index, Boolean needUpdate) {
        this.needUpdate.set(index, needUpdate);
    }

    public void setIsFav(int index, boolean isFav) {
        atraccionItems.get(index).setIsFav(isFav);
    }

    public void setId_fav(int index, String id_fav) {
        atraccionItems.get(index).setId_fav(id_fav);
    }

    public void addImgToPos(Bitmap img, int imgID) {
        atraccionItems.get(imgID).setFotoRecorrido(img);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        int position = -1;
        ImageView recorridoPic;
        ImageView favPic;
        TextView recorridoName;
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
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null || (view.getTag() != null && (!((ViewHolder) view.getTag()).setted
                || ((ViewHolder) view.getTag()).position != position
                || needUpdate.get(position)))) {
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(R.layout.tarjeta_item, null, true);
            holder = new ViewHolder();
            holder.recorridoName = (TextView) view.findViewById(R.id.textView2);
            holder.recorridoPic = (ImageView) view.findViewById(R.id.imageView);
            holder.favPic = (ImageView) view.findViewById(R.id.heartFav);

            final Recorrido rowPos = atraccionItems.get(position);

            needUpdate.set(position, false);

            holder.favPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rowPos.isFavSetted()) {
                        String url = tripTP.getUrl() + Consts.FAVS;

                        if (!rowPos.isFav()) {
                            rowPos.setIsFav(null); //block button
                            JSONObject body = new JSONObject();
                            try {
                                body.put(Consts.ID_RECORRIDO, rowPos.get_id());
                                body.put(Consts.ID_CIUDAD, rowPos.getId_ciudad());
                                body.put(Consts.ID_USER, tripTP.getUserID_fromServ());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Map<String,String> header = Consts.getHeaderJSON();

                            InternetClient client = new InfoClient(activity.getApplicationContext(),
                                    Consts.GEToPOST_REC_FAV, url, header, Consts.POST, body.toString(), true, position);
                            client.createAndRunInBackground();

                        } else {
                            if (rowPos.getId_fav() != null) {
                                rowPos.setIsFav(null); //block button
                                String urlDelete = url + "/" + rowPos.getId_fav();
                                InternetClient client = new InfoClient(activity.getApplicationContext(),
                                        Consts.DELETE_REC_FAV, urlDelete, null, Consts.DELETE, null, true, position);
                                client.createAndRunInBackground();

                            }

                        }
                    }
                }
            });

            if (rowPos.hasPhoto()) {
                holder.recorridoPic.setImageBitmap(rowPos.getFotoRecorrido());
                if (!tripTP.isLogin()) {
                    holder.setted =true;
                    holder.favPic.setVisibility(View.GONE);
                }
            }

            if (tripTP.isLogin() && rowPos.hasPhoto() && rowPos.isFavSetted()) {
                holder.setted =true;
            }

            if (tripTP.isLogin() && rowPos.isFavSetted()) {
                if (rowPos.isFav()) {
                    holder.favPic.setImageResource(R.drawable.heart);
                } else {
                    holder.favPic.setImageResource(R.drawable.heart_outline);
                }
            }

            holder.position = position;
            holder.recorridoName.setText(rowPos.getNombre());
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
            Recorrido rowPos = atraccionItems.get(position);

            if (tripTP.isLogin() && rowPos.isFavSetted()) {
                if (rowPos.isFav()) {
                    holder.favPic.setImageResource(R.drawable.heart);
                } else {
                    holder.favPic.setImageResource(R.drawable.heart_outline);
                }
            }
        }

        return view;
    }
}

