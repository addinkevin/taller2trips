package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pc.myapplication.adapters.CommentListAdapter;
import com.example.pc.myapplication.ciudadesTools.Comentario;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ReceiverOnCommentsGet extends BroadcastReceiver{

    private final Activity activity;
    private final List<Comentario> rowsItems;
    private CommentListAdapter adapter;
    private ListView commentsListView;

    public ReceiverOnCommentsGet(Activity activity, List<Comentario> rowsItems, CommentListAdapter adapter, ListView commentsListView) {
        this.activity = activity;
        this.rowsItems = rowsItems;
        this.adapter = adapter;
        this.commentsListView = commentsListView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
            if (jsonOut != null) {
                try {
                    JSONArray res = new JSONArray(jsonOut);
                    for (int i = 0; i < res.length(); i++) {
                        Comentario comentario = new Comentario(res.getJSONObject(i));
                        rowsItems.add(comentario);
                    }
                    adapter.addRowItem(rowsItems);
                    scrollMyListViewToBottom();
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

}
