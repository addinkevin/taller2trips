package com.example.pc.myapplication;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCommentsGet;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCommentPost;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnShare;
import com.example.pc.myapplication.adapters.CommentListAdapter;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.ciudadesTools.Comentario;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.dialogs.AlertDialog;
import com.example.pc.myapplication.dialogs.ShareDialog;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.pc.myapplication.commonfunctions.Consts.PERMISSION;
import static com.example.pc.myapplication.commonfunctions.Consts.S_FACEBOOK;
import static com.example.pc.myapplication.commonfunctions.Consts.S_TWITTER;

public class ComentariosTab extends Fragment {

    private View fragView;
    private EditText commentText;
    private Activity activity;
    private ReceiverOnShare onShare;
    private ReceiverOnCommentPost onCommentPost;
    private ReceiverOnCommentsGet onCommentGet;
    private TripTP tripTP;
    private Atraccion atraccion;
    private List<Comentario> rowsItems;
    private CommentListAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragView = inflater.inflate(R.layout.comentarios_tab, container, false);
        activity = getActivity();
        commentText = (EditText) fragView.findViewById(R.id.editText2);

        rowsItems = new ArrayList<>();
        adapter = new CommentListAdapter(activity, rowsItems);
        ListView commentsListView = (ListView) fragView.findViewById(R.id.atraccList);
        commentsListView.setAdapter(adapter);

        onShare = new ReceiverOnShare(activity);
        onCommentPost = new ReceiverOnCommentPost(activity,commentText, rowsItems, adapter, commentsListView);
        onCommentGet = new ReceiverOnCommentsGet(activity, rowsItems, adapter, commentsListView);

        tripTP = (TripTP)activity.getApplication();

        String id_atr = activity.getIntent().getStringExtra(Consts._ID);

        String urlServ = tripTP.getUrl() + Consts.RESENIA + Consts.BUSCAR + "?" + Consts.ID_ATR + "=" + id_atr;

        InternetClient client = new InfoClient(activity.getApplicationContext(),
                Consts.GET_COMMENT, urlServ, null, Consts.GET, null, true);
        client.runInBackground();

        return fragView;
    }

    public void makeComment() {
        final String comment = commentText.getText().toString();
        if (!comment.isEmpty() && atraccion != null) {
            if (tripTP.getSocialDef().equals(S_FACEBOOK)) {
                new GraphRequest(AccessToken.getCurrentAccessToken(),
                        "/" + tripTP.getUserID_fromSocial() + "/permissions",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                try {
                                    JSONArray res = response.getJSONObject().getJSONArray(Consts.DATA);
                                    int i = 0;
                                    boolean hasPerms = false;
                                    while (i < res.length() && !hasPerms) {
                                        JSONObject permission = res.getJSONObject(i);
                                        if (permission.getString(PERMISSION).equals(Consts.PUBLISH) &&
                                                permission.getString(Consts.STATUS).equals(Consts.GRANTED)) {
                                            hasPerms = true;
                                        } else {
                                            i++;
                                        }
                                    }

                                    if (hasPerms) {
                                        ShareDialog.show(getActivity(), comment, atraccion._id, atraccion.idCiudad);
                                    } else {
                                        LoginManager.getInstance().logInWithPublishPermissions(
                                                activity,
                                                Arrays.asList(Consts.PUBLISH));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ).executeAsync();
            } else if (tripTP.getSocialDef().equals(S_TWITTER)) {
                ShareDialog.show(getActivity(), comment, atraccion._id, atraccion.idCiudad);
            }
        } else {
            AlertDialog.show(activity, R.string.make_review);
        }
    }

    public void onStart() {
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(onShare,
                new IntentFilter(Consts.POST_SHARE));
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(onCommentPost,
                new IntentFilter(Consts.POST_COMMENT));
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(onCommentGet,
                new IntentFilter(Consts.GET_COMMENT));
        super.onStart();
    }

    public void onStop() {
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).unregisterReceiver(onShare);
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).unregisterReceiver(onCommentPost);
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).unregisterReceiver(onCommentGet);

        super.onStop();
    }

    public void attachAtraccion(Atraccion atraccion) {
        this.atraccion = atraccion;
    }
}
