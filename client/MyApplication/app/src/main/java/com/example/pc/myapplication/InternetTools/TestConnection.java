package com.example.pc.myapplication.InternetTools;

import android.content.Context;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by PC on 09/04/2017.
 */

public class TestConnection {


    private static final int timeOUT_R = 20000;
    private static final int timeOUT_C = 25000;
    protected final Context context;
    private final View view;
    private final String toCall;

    protected InputStream is;
    protected HttpURLConnection connection;
    private String nURL;

    private String jsonBody;
    private String requestMethod;
    private Map<String, String> headers; //headers de la consulta HTTP
    private boolean expectResponse;
    private int responseCode;

    public static final String CONNECTION = "Connection";

    public TestConnection(Context context, View view, String toCall, String path, Map<String, String> headerM, String rMethod, String jBody, boolean response) {
        this.context = context;
        this.view = view;
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

            if (jsonBody != null) {
                connection.setDoOutput(true);
                byte[] outputInBytes = jsonBody.getBytes("UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write(outputInBytes);
                os.close();
            } else {
                connection.setDoOutput(false);
            }

            connection.connect();

            responseCode = connection.getResponseCode();

    }

}