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
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AtraccionesListAdp extends BaseAdapter {

    private final Activity activity;
    private final TripTP tripTP;
    List<Atraccion> atraccionItems; /**< Lista de todos los items de List*/
    ArrayList<Boolean> needUpdate;
    private Boolean onlyFavs = null;

    public AtraccionesListAdp(Activity activity, List<Atraccion> atraccionItems) {
        this.activity = activity;
        this.atraccionItems = atraccionItems;
        tripTP = (TripTP) activity.getApplication();
        needUpdate = new ArrayList<>();//setear cuando se necesite actualizar la view

    }

    public void add(Atraccion atraccion) {
        atraccionItems.add(atraccion);
        needUpdate.add(false);
    }

    public void setNeedUpdateToPos(int index, Boolean needUpdate) {
        this.needUpdate.set(index, needUpdate);
    }

    public void setIsFav(int index, boolean isFav) {
        atraccionItems.get(index).setIsFav(isFav);
    }

    public void setIsVisit(int index, boolean isVisit) {
        atraccionItems.get(index).setIsVisit(isVisit);
    }


    public void setId_fav(int index, String id_fav) {
        atraccionItems.get(index).setId_fav(id_fav);
    }

    public void setId_visit(int index, String id_visit) {
        atraccionItems.get(index).setId_visit(id_visit);
    }

    public void addImgToPos(Bitmap img, int imgID) {
        atraccionItems.get(imgID).fotosBitmap.add(img);
        notifyDataSetChanged();
    }

    public void setOnlyFavs(Boolean onlyFavs) {
        this.onlyFavs = onlyFavs;
    }

    private class ViewHolder {
        int position = -1;
        ImageView atraccionPic;
        ImageView favPic;
        ImageView visitPic;
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
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null || (view.getTag() != null && (!((ViewHolder) view.getTag()).setted
                || ((ViewHolder) view.getTag()).position != position
                || needUpdate.get(position)))) {
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(R.layout.tarjeta_item, null, true);
            holder = new ViewHolder();
            holder.atraccionName = (TextView) view.findViewById(R.id.textView2);
            holder.atraccionPic = (ImageView) view.findViewById(R.id.imageView);
            holder.favPic = (ImageView) view.findViewById(R.id.heartFav);
            holder.visitPic = (ImageView) view.findViewById(R.id.starVisit);

            if (onlyFavs != null) {
                if (onlyFavs)
                    holder.visitPic.setVisibility(View.GONE);
                else
                    holder.favPic.setVisibility(View.GONE);
            }

            final Atraccion rowPos = atraccionItems.get(position);

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
                                body.put(Consts.ID_ATR, rowPos._id);
                                body.put(Consts.ID_CIUDAD, rowPos.idCiudad);
                                body.put(Consts.ID_USER, tripTP.getUserID_fromServ());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Map<String,String> header = Consts.getHeaderJSON();

                            InternetClient client = new InfoClient(activity.getApplicationContext(),
                                    Consts.GEToPOST_ATR_FAV, url, header, Consts.POST, body.toString(), true, position);
                            client.createAndRunInBackground();

                        } else {
                            if (rowPos.getId_fav() != null) {
                                rowPos.setIsFav(null); //block button
                                String urlDelete = url + "/" + rowPos.getId_fav();
                                InternetClient client = new InfoClient(activity.getApplicationContext(),
                                        Consts.DELETE_ATR_FAV, urlDelete, null, Consts.DELETE, null, true, position);
                                client.createAndRunInBackground();

                            }

                        }
                    }
                }
            });

            holder.visitPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rowPos.isVisitSetted()) {
                        String url = tripTP.getUrl() + Consts.VISITADO;

                        if (!rowPos.isVisit()) {
                            rowPos.setIsVisit(null); //block button
                            JSONObject body = new JSONObject();
                            try {
                                body.put(Consts.ID_ATR, rowPos._id);
                                body.put(Consts.ID_USER, tripTP.getUserID_fromServ());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Map<String,String> header = Consts.getHeaderJSON();

                            InternetClient client = new InfoClient(activity.getApplicationContext(),
                                    Consts.GEToPOST_ATR_VISIT, url, header, Consts.POST, body.toString(), true, position);
                            client.createAndRunInBackground();

                        } else {
                            if (rowPos.getId_visit() != null) {
                                rowPos.setIsVisit(null); //block button
                                String urlDelete = url + "/" + rowPos.getId_visit();
                                InternetClient client = new InfoClient(activity.getApplicationContext(),
                                        Consts.DELETE_ATR_VISIT, urlDelete, null, Consts.DELETE, null, true, position);
                                client.createAndRunInBackground();

                            }

                        }
                    }
                }
            });

            if (!rowPos.fotosBitmap.isEmpty()) {
                holder.atraccionPic.setImageBitmap(rowPos.fotosBitmap.get(0));
                if (!tripTP.isLogin()) {
                    holder.setted =true;
                    holder.favPic.setVisibility(View.GONE);
                }
            }

            if (tripTP.isLogin() && !rowPos.fotosBitmap.isEmpty() && rowPos.isFavSetted()) {
                holder.setted =true;
            }

            if (tripTP.isLogin() && (rowPos.isFavSetted() || rowPos.isVisitSetted())) {
                if (rowPos.isFav()) {
                    holder.favPic.setImageResource(R.drawable.heart);
                } else {
                    holder.favPic.setImageResource(R.drawable.heart_outline);
                }

                if (rowPos.isVisit()) {
                    holder.visitPic.setImageResource(R.drawable.star);
                } else {
                    holder.visitPic.setImageResource(R.drawable.star_outline);
                }
            }

            holder.position = position;
            holder.atraccionName.setText(rowPos.nombre);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }
}
