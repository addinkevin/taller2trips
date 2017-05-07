package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pc.myapplication.InternetTools.ImageClient;
import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.adapters.CommentListAdapter;
import com.example.pc.myapplication.ciudadesTools.Comentario;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.dialogs.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ReceiverOnCommentPost extends BroadcastReceiver{

    private final Activity act;
    private final EditText commentEditText;
    private final List<Comentario> rowsItems;
    private final CommentListAdapter adapter;
    private ListView commentsListView;
    private TextView noCommentMssg;

    public ReceiverOnCommentPost(Activity comentariosTab, EditText commentText, List<Comentario> rowsItems, CommentListAdapter adapter, ListView commentsListView, TextView noCommentMssg) {
        this.act = comentariosTab;
        this.commentEditText = commentText;
        this.rowsItems = rowsItems;
        this.adapter = adapter;
        this.commentsListView = commentsListView;
        this.noCommentMssg = noCommentMssg;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        int response = intent.getIntExtra(Consts.RESPONSE, -1);
        if (succes && response != -1 && response != 401) {
            String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
            if ( jsonOut != null ) {
                try {
                    JSONObject res = new JSONObject(jsonOut);
                    Comentario comentario = new Comentario(res);
                    rowsItems.add(comentario);
                    int pixels = dpToPx(100);
                    if (comentario.provider.equals(Consts.S_FACEBOOK)) {
                        String urlServ = Consts.URL_FACEBOOK + comentario.idSocial + Consts.PICTURE +
                                "?" + Consts.WIDTH + "=" + pixels + "&" + Consts.HEIGHT + "=" + pixels;

                        InternetClient client = new InfoClient(act.getApplicationContext(),
                                Consts.GET_PROF, urlServ, null, Consts.GET, null, true, 0);
                        client.runInBackground();
                    } else if (comentario.provider.equals(Consts.S_TWITTER)){

                        String urlServ = Consts.URL_TWITTER + comentario.idSocial + Consts.PROFILE_IMAGE +
                                "?" + Consts.SIZE + "=" + Consts.ORIGINAL;

                        InternetClient client = new ImageClient(act.getApplicationContext(),
                                Consts.GET_USER_IMG_PROF, urlServ, null, Consts.GET, null, true, 0);
                        client.runInBackground();
                    }

                    AlertDialog.show(act, R.string.comment_succes);

                    commentEditText.setText("");
                    adapter.addRowItem(rowsItems);
                    noCommentMssg.setVisibility(View.GONE);
                    scrollMyListViewToBottom();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                AlertDialog.show(act, R.string.comment_fail);
            }
        } else if (response == 401){
            AlertDialog.show(act, R.string.banned);
        } else{
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

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = act.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
