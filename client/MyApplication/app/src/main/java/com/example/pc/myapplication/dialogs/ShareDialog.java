package com.example.pc.myapplication.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;

public class ShareDialog {

    public static void show(final Activity act, final String comment, final String atraccion, final String ciudad) {

        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        LayoutInflater infl = act.getLayoutInflater();
        View view = infl.inflate(R.layout.share_dialog,null);
        final RatingBar ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);
        final TripTP tripTP = (TripTP) act.getApplication();
        builder.setTitle(R.string.app_name);
        builder.setView(view);
        builder.setMessage(R.string.share_dialog)
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }

                }).setNeutralButton(R.string.comment, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendToServer(atraccion,comment,ciudad,(int)ratingBar.getRating(),tripTP,act);
                        dialog.dismiss();

                    }

                }).setPositiveButton(R.string.share_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!tripTP.hasMultipleAccounts()) {
                            shareSplex(comment,tripTP,act,atraccion,ciudad,(int)ratingBar.getRating(),true);
                        } else {
                            showSelectAccount(act,comment,atraccion,ciudad, (int)ratingBar.getRating());
                        }
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    private static void shareSplex(String comment, TripTP tripTP, Activity act, String atraccion, String ciudad, int ratingBar, boolean shareHere) {
        String urlSplex = Consts.SPLEX_URL + Consts.POSTS;
        Map<String, String> header = Consts.getSplexHeader(tripTP);

        JSONObject reqSplex = new JSONObject();

        try {
            reqSplex.put(Consts.TEXT, comment);
            if (shareHere) {
                JSONArray array = new JSONArray();
                array.put(tripTP.getSocialDefID());
                reqSplex.put(Consts.SOCIAL_ACCOUNTS, array);
            }

            InternetClient client = new InfoClient(act.getApplicationContext(),
                    Consts.POST_SHARE, urlSplex, header, Consts.POST, reqSplex.toString(), true);
            client.runInBackground();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendToServer(atraccion, comment, ciudad, ratingBar, tripTP, act);
    }

    public static void showSelectAccount(final Activity act, final String comment, final String atraccion, final String ciudad, final int rating) {

        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        LayoutInflater infl = act.getLayoutInflater();
        View view = infl.inflate(R.layout.share_dialog,null);
        RatingBar ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);
        ratingBar.setVisibility(View.GONE);
        final TripTP tripTP = (TripTP) act.getApplication();
        builder.setTitle(R.string.app_name);
        builder.setView(view);
        builder.setMessage(R.string.share_accounts_dialog)
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).setNeutralButton(R.string.share_hear_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shareSplex(comment,tripTP,act,atraccion,ciudad,rating,true);
                        dialog.dismiss();
                    }
                }).setPositiveButton(R.string.share_all_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        shareSplex(comment,tripTP,act,atraccion,ciudad,rating,false);
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    public static void sendToServer(String atraccion, String comment, String ciudad, int ratingBar, TripTP tripTP, Activity act) {
        JSONObject reqServ = new JSONObject();
        try {
            String idioma = URLEncoder.encode(Locale.getDefault().getLanguage().toLowerCase(), "utf-8");
            reqServ.put(Consts.ID_USER, tripTP.getUserID_fromServ());
            reqServ.put(Consts.DESCRIPCION, comment);
            reqServ.put(Consts.ID_CIUDAD, ciudad);
            reqServ.put(Consts.ID_ATR, atraccion);
            reqServ.put(Consts.CALIFICACION, ratingBar);
            reqServ.put(Consts.IDIOMA, idioma);

            if (tripTP.getSocialDef().equals(Consts.S_TWITTER)) {
                reqServ.put(Consts.ID_USER_SOCIAL, tripTP.getScreenName());
                reqServ.put(Consts.NAME, tripTP.getScreenName());
                reqServ.put(Consts.PROVIDER, tripTP.getSocialDef());
            } else if (tripTP.getSocialDef().equals(Consts.S_FACEBOOK)){
                reqServ.put(Consts.ID_USER_SOCIAL, tripTP.getUserID_fromSocial());
                reqServ.put(Consts.NAME, tripTP.getNameFB());
                reqServ.put(Consts.PROVIDER, tripTP.getSocialDef());
            } else {
                reqServ.put(Consts.ID_USER_SOCIAL, "");
                reqServ.put(Consts.NAME, "");
                reqServ.put(Consts.PROVIDER, "");
            }

            String urlServ = tripTP.getUrl() + Consts.RESENIA;

            Map<String,String> header = Consts.getHeaderJSON();

            InternetClient client = new InfoClient(act.getApplicationContext(),
                    Consts.POST_COMMENT, urlServ, header, Consts.POST, reqServ.toString(), true, Consts.BLOQ_USER);
            client.runInBackground();

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}