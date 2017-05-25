package com.example.pc.myapplication;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCommentPost;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCommentsGet;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnProfPic;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnShare;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnUserCommentImage;
import com.example.pc.myapplication.adapters.CommentListAdapter;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.ciudadesTools.Comentario;
import com.example.pc.myapplication.ciudadesTools.PuntoInteres;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.dialogs.AlertDialog;
import com.example.pc.myapplication.dialogs.ShareDialog;
import com.example.pc.myapplication.singletons.NetClientsSingleton;
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
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.pc.myapplication.commonfunctions.Consts.PERMISSION;
import static com.example.pc.myapplication.commonfunctions.Consts.S_FACEBOOK;
import static com.example.pc.myapplication.commonfunctions.Consts.S_TWITTER;

/**
 * Created by PC on 19/05/2017.
 */

public class ComentariosPITab extends Fragment{

    private View fragView;
    private EditText commentText;
    private Activity activity;
    private ReceiverOnShare onShare;
    private ReceiverOnCommentPost onCommentPost;
    private ReceiverOnCommentsGet onCommentGet;
    private TripTP tripTP;
    private PuntoInteres atraccion;
    private List<Comentario> rowsItems;
    private CommentListAdapter adapter;
    private AtomicBoolean isDownloagind; // cuando esta descargando comentarios o ya no hay mas comentarios
    private ReceiverOnProfPic onProf;
    private ReceiverOnUserCommentImage onProfImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragView = inflater.inflate(R.layout.comentarios_tab, container, false);
        activity = getActivity();
        commentText = (EditText) fragView.findViewById(R.id.editText2);

        rowsItems = new ArrayList<>();
        adapter = new CommentListAdapter(activity, rowsItems);
        final ListView commentsListView = (ListView) fragView.findViewById(R.id.atraccList);
        commentsListView.setAdapter(adapter);
        TextView noCommentMssg = (TextView) fragView.findViewById(R.id.textView);

        isDownloagind = new AtomicBoolean(true);

        onShare = new ReceiverOnShare(activity);
        onCommentPost = new ReceiverOnCommentPost(activity,commentText, rowsItems, adapter, commentsListView, noCommentMssg);
        onCommentGet = new ReceiverOnCommentsGet(activity, rowsItems, adapter, commentsListView, isDownloagind, noCommentMssg);
        onProf = new ReceiverOnProfPic(activity);
        onProfImage = new ReceiverOnUserCommentImage(activity, commentsListView, rowsItems, adapter);

        tripTP = (TripTP)activity.getApplication();

        commentsListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    view.getFirstVisiblePosition();
                    // check if we reached the top or bottom of the list
                    View v = commentsListView.getChildAt(0);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (view.getFirstVisiblePosition() == 0 && offset == 0 && isDownloagind.compareAndSet(false, true)) {
                        downloadComments(String.valueOf(rowsItems.size()));
                        return;
                    }
                }
            }
        });

        View commentLN = fragView.findViewById(R.id.commentLN);
        if(!tripTP.isLogin() || tripTP.getSocialDef().isEmpty()) {
            commentLN.setVisibility(View.GONE);
        }

        downloadComments("0");

        return fragView;
    }

    public void downloadComments(String salto) {

        String id_atr = activity.getIntent().getStringExtra(Consts._ID);

        String urlServ = tripTP.getUrl() + Consts.RESENIA + Consts.BUSCAR + Consts.PAGINADO + "?" + Consts.ID_ATR + "=" + id_atr;

        Map<String,String> headers = Consts.getHeaderPaginado(salto);

        InternetClient client = new InfoClient(activity.getApplicationContext(),
                Consts.GET_COMMENT, urlServ, headers, Consts.GET, null, true);
        NetClientsSingleton.getInstance().add(client.createTask());
        client.runInBackground();
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
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(onCommentGet,
                new IntentFilter(Consts.GET_COMMENT));
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(onShare,
                new IntentFilter(Consts.POST_SHARE));
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(onCommentPost,
                new IntentFilter(Consts.POST_COMMENT));
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(onProf,
                new IntentFilter(Consts.GET_PROF));
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(onProfImage,
                new IntentFilter(Consts.GET_USER_IMG_PROF));
        super.onStart();
    }

    public void onStop() {
        unregister();
        super.onStop();
    }

    public void unregister() {
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).unregisterReceiver(onShare);
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).unregisterReceiver(onCommentPost);
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).unregisterReceiver(onCommentGet);
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).unregisterReceiver(onProf);
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).unregisterReceiver(onProfImage);
    }

    public void attachPuntoInteres(PuntoInteres atraccion) {
        this.atraccion = atraccion;
    }
}
