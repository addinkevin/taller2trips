package com.example.pc.myapplication.InternetTools;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.pc.myapplication.commonfunctions.Consts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by PC on 26/03/2017.
 */

public class InfoClient extends InternetClient {


    public InfoClient(Context context, String toCall, String path, Map<String, String> headerM, String rMethod, String jBody, boolean response) {
        super(context, toCall, path, headerM, rMethod, jBody ,response);
    }

    @Override
    protected void readMedia(Intent activityMsg) throws IOException {
        String jsonResponse =  readIt();
        activityMsg.putExtra(Consts.JSON_OUT, jsonResponse);
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
