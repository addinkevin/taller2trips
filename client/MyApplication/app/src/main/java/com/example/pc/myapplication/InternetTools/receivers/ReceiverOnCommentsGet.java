package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.myapplication.InternetTools.ImageClient;
import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.adapters.CommentListAdapter;
import com.example.pc.myapplication.adapters.ScrollListMaintainer;
import com.example.pc.myapplication.ciudadesTools.Comentario;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.dialogs.AlertDialog;
import com.example.pc.myapplication.singletons.NetClientsSingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReceiverOnCommentsGet extends BroadcastReceiver{

    private final Activity activity;
    private final List<Comentario> rowsItems;
    private CommentListAdapter adapter;
    private ListView commentsListView;
    private AtomicBoolean isDownloagind;
    private TextView noCommentMssg;
    private boolean firstDownload = true;

    public ReceiverOnCommentsGet(Activity activity, List<Comentario> rowsItems, CommentListAdapter adapter, ListView commentsListView, AtomicBoolean isDownloagind, TextView noCommentMssg) {
        this.activity = activity;
        this.rowsItems = rowsItems;
        this.adapter = adapter;
        this.commentsListView = commentsListView;
        this.isDownloagind = isDownloagind;
        this.noCommentMssg = noCommentMssg;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        boolean toTheTop = false;
        if (succes) {
            String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
            if (jsonOut != null) {
                try {

                    JSONArray res = new JSONArray(jsonOut);
                    int pixels = dpToPx(100);
                    for (int i = 0; i < res.length(); i++) {
                        Comentario comentario = new Comentario(res.getJSONObject(i));
                        rowsItems.add(0, comentario);

                        if (comentario.provider.equals(Consts.S_FACEBOOK)) {
                            String urlServ = Consts.URL_FACEBOOK + comentario.idSocial + Consts.PICTURE +
                                    "?" + Consts.WIDTH + "=" + pixels + "&" + Consts.HEIGHT + "=" + pixels;

                            InternetClient client = new InfoClient(activity.getApplicationContext(),
                                    Consts.GET_PROF, urlServ, null, Consts.GET, null, true, rowsItems.size()-1);
                            NetClientsSingleton.getInstance().add(client.createTask());
                            client.runInBackground();
                        } else if (comentario.provider.equals(Consts.S_TWITTER)){

                            String urlServ = Consts.URL_TWITTER + comentario.idSocial + Consts.PROFILE_IMAGE +
                                    "?" + Consts.SIZE + "=" + Consts.ORIGINAL;

                            InternetClient client = new ImageClient(activity.getApplicationContext(),
                                    Consts.GET_USER_IMG_PROF, urlServ, null, Consts.GET, null, true, rowsItems.size() -1);
                            NetClientsSingleton.getInstance().add(client.createTask());
                            client.runInBackground();
                        }

                    }
                    if (res.length() > 0) {
                        noCommentMssg.setVisibility(View.GONE);
                        adapter.addRowItem(rowsItems);
                        if (firstDownload) {
                            firstDownload = false;
                            scrollMyListViewToBottom();
                        } else {
                            int positionBeforeReload = commentsListView.getFirstVisiblePosition() + res.length();
                            ScrollListMaintainer.maintainScrollPosition(commentsListView, positionBeforeReload);
                        }
                    } else {
                        if (firstDownload) {
                            AlertDialog.show(activity, R.string.no_comment_alert);
                        }
                        toTheTop = true;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity,"JSON err", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(activity,"JSON err", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(activity,"No se pudo conectar con el servidor", Toast.LENGTH_LONG).show();
        }

        Handler handler = new Handler();
        final boolean finalToTheTop = toTheTop;

        if (toTheTop) {
            isDownloagind.set(toTheTop);
        } else {
            handler.postDelayed(new Runnable() {
                public void run() {
                    isDownloagind.set(finalToTheTop);
                }
            }, 500);
        }
    }

    private void scrollMyListViewToBottom() {
        commentsListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                commentsListView.setSelection(adapter.getCount() - 1);
            }
        });
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}
