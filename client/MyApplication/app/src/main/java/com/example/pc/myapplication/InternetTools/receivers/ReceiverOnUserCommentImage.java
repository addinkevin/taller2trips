package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.adapters.CommentListAdapter;
import com.example.pc.myapplication.ciudadesTools.Comentario;
import com.example.pc.myapplication.commonfunctions.Consts;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ReceiverOnUserCommentImage extends BroadcastReceiver {

    private final Activity activity;
    private final ListView commentsListView;
    private final List<Comentario> rowsItems;
    private final CommentListAdapter adapter;

    public ReceiverOnUserCommentImage(Activity activity, ListView commentsListView, List<Comentario> rowsItems, CommentListAdapter adapter) {
        this.activity = activity;
        this.commentsListView = commentsListView;
        this.rowsItems = rowsItems;
        this.adapter = adapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            Bitmap imageProf = intent.getParcelableExtra(Consts.IMG_OUT);
            int imgID = intent.getIntExtra(Consts.IMG_ID, -1);
            if (imageProf != null && imgID >= 0) {
                rowsItems.get( rowsItems.size() - imgID - 1).profPic = imageProf;
                adapter.addRowItem(rowsItems);
            }
        }
    }
}
