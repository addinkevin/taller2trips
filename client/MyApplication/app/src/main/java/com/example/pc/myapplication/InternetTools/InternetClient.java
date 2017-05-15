package com.example.pc.myapplication.InternetTools;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.example.pc.myapplication.commonfunctions.Consts;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public abstract class InternetClient {

    private static final int timeOUT_R = 20000;
    private static final int timeOUT_C = 25000;
    protected final Context context;
    private final String toCall;

    protected InputStream is;
    protected HttpURLConnection connection;
    private String nURL;

    private String jsonBody;
    private String requestMethod;
    private Map<String, String> headers; //headers de la consulta HTTP
    private boolean expectResponse;
    protected int responseCode;
    protected Integer identifier = null;

    public static final String CONNECTION = "Connection";
    private DownloadInBackground task = null;

    public InternetClient(Context context, String toCall, String path, Map<String, String> headerM, String rMethod, String jBody, boolean response) {
        this.context = context;
        this.toCall = toCall;
        nURL = path;
        requestMethod = rMethod;
        jsonBody = jBody;
        headers = headerM;
        is = null;
        connection = null;
        expectResponse = response;
        responseCode = 0;
    }

    public void connect() throws IOException {
        try {
            URL urlOK = new URL(nURL);
            connection = (HttpURLConnection) urlOK.openConnection();

            connection.setReadTimeout(timeOUT_R /* milliseconds */);
            connection.setConnectTimeout(timeOUT_C /* milliseconds */);
            connection.setRequestMethod(requestMethod);

            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            if (jsonBody != null){
                connection.setDoOutput(true);
                byte[] outputInBytes = jsonBody.getBytes("UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write( outputInBytes );
                os.close();
            } else {
                connection.setDoOutput(false);
            }

            connection.connect();

            responseCode = connection.getResponseCode();

            Log.i(CONNECTION, "code: " + responseCode);

            Intent activityMsg = new Intent(toCall);
            activityMsg.putExtra(Consts.RESPONSE, responseCode);

            if (connection.getHeaderFields().containsKey("Location")) {
                String urlRedirect = connection.getHeaderField("Location");
                activityMsg.putExtra(Consts.URL_OUT, urlRedirect);
            }

            if (identifier != null) {
                activityMsg.putExtra(Consts.URL_ID, identifier);
            }

            if ( responseCode < 300 && responseCode >= 200 ) {
                if (expectResponse) {
                    readMedia(activityMsg);
                }
                activityMsg.putExtra(Consts.SUCESS, true);
            } else {
                activityMsg.putExtra(Consts.SUCESS, false);
            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(activityMsg);
        } finally {
            closeConnection();
        }
    }

    protected abstract void readMedia(Intent activityMsg) throws IOException;

    public void closeConnection() throws IOException {
        if (connection != null) {
            connection.disconnect();
        }

        if (is != null) {
            is.close();
        }
    }

    protected abstract String readIt() throws IOException;

    public void createAndRunInBackground() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() && task == null) {
            task = new DownloadInBackground(this);
            task.execute();
        } else {
            callErrorServer();
        }
    }

    public DownloadInBackground createTask() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            task = new DownloadInBackground(this);
        } else {
            callErrorServer();
        }

        return task;
    }

    public void runInBackground() {
        if (task != null) {
            task.execute();
        }
    }

    public void callErrorServer() {
        Intent activityMsg = new Intent(toCall);
        activityMsg.putExtra(Consts.SUCESS, false);
        activityMsg.putExtra(Consts.RESULT, true); // error de server
        LocalBroadcastManager.getInstance(context).sendBroadcast(activityMsg);
    }
}
