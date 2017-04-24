package com.example.pc.myapplication.InternetTools;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.pc.myapplication.commonfunctions.Consts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class InfoClient extends InternetClient {

    private Integer identifier = null;
    private String bloqUser = null;

    public InfoClient(Context context, String toCall, String path, Map<String, String> headerM, String rMethod, String jBody, boolean response) {
        super(context, toCall, path, headerM, rMethod, jBody ,response);
    }

    public InfoClient(Context context, String toCall, String path, Map<String, String> headerM, String rMethod, String jBody, boolean response, int identifier) {
        super(context, toCall, path, headerM, rMethod, jBody ,response);
        this.identifier = identifier;
    }

    public InfoClient(Context context, String toCall, String path, Map<String, String> headerM, String rMethod, String jBody, boolean response,String bloqUser) {
        super(context, toCall, path, headerM, rMethod, jBody ,response);
        this.bloqUser = bloqUser;

    }

    @Override
    protected void readMedia(Intent activityMsg) throws IOException {
        if (identifier == null || bloqUser == null) {
            String jsonResponse = readIt();
            activityMsg.putExtra(Consts.JSON_OUT, jsonResponse);
        } else if (identifier!= null && responseCode == 302){
            String urlRedirect = connection.getHeaderField("Location");
            activityMsg.putExtra(Consts.URL_OUT, urlRedirect);
            if (identifier != null) {
                activityMsg.putExtra(Consts.URL_ID, identifier);
            }

        } else if (bloqUser != null && responseCode == 401) {
            activityMsg.putExtra(Consts.JSON_OUT, Consts.ERR);
        }

    }

    @Override
    protected String readIt() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}
