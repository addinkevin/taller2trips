package com.example.pc.myapplication.InternetTools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.example.pc.myapplication.commonfunctions.Consts;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class ImageClient extends InternetClient {

    private ByteArrayOutputStream out = null;
    private int imgID;

    //imgID < 0 entonces no se necesita el id de cada imagen
    public ImageClient(Context context, String toCall, String path, Map<String, String> headerM, String rMethod, String jBody, boolean response, int imgID) {
        super(context, toCall, path, headerM, rMethod, jBody, response);
        this.imgID = imgID;
    }

    @Override
    protected void readMedia(Intent activityMsg) throws IOException {
        is = new BufferedInputStream(connection.getInputStream(), 8192);
        out = new ByteArrayOutputStream();

        byte bytes[] = new byte[8192];
        int count;
        while ((count = is.read(bytes)) != -1) {
            out.write(bytes, 0, count);
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size());
        activityMsg.putExtra(Consts.IMG_OUT, bitmap);

        if (imgID >= 0) {
            activityMsg.putExtra(Consts.IMG_ID, imgID);
        }

    }

    @Override
    protected String readIt() throws IOException {
        return null;
    }

    @Override
    public void closeConnection()  throws IOException {
        super.closeConnection();
        if (out != null) {
             out.flush();
             out.close();
        }
    }
}
