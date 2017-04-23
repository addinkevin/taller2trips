package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ListView;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.adapters.CommentListAdapter;
import com.example.pc.myapplication.ciudadesTools.Comentario;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.dialogs.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ReceiverOnCommentPost extends BroadcastReceiver{

    private final Activity act;
    private final EditText commentText;
    private final List<Comentario> rowsItems;
    private final CommentListAdapter adapter;
    private ListView commentsListView;

    public ReceiverOnCommentPost(Activity comentariosTab, EditText commentText, List<Comentario> rowsItems, CommentListAdapter adapter, ListView commentsListView) {
        this.act = comentariosTab;
        this.commentText = commentText;
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
                    JSONObject res = new JSONObject(jsonOut);
                    Comentario comentario = new Comentario(res);
                    rowsItems.add(comentario);

                    AlertDialog.show(act, R.string.comment_succes);

                    commentText.setText("");
                    adapter.addRowItem(rowsItems);
                    scrollMyListViewToBottom();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                AlertDialog.show(act, R.string.comment_fail);
            }
        } else {
            AlertDialog.show(act, R.string.comment_fail);
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
