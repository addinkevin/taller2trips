package com.example.pc.myapplication.InternetTools;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.pc.myapplication.commonfunctions.Consts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by PC on 26/03/2017.
 */

public class VideoClient extends InternetClient {

    private String idFile;

    public VideoClient(Context context, View view, String toCall, String path, Map<String, String> headerM, String rMethod, String jBody, boolean response, String idFile) {
        super(context, view, toCall, path, headerM, rMethod, jBody, response);
        this.idFile = idFile;
    }

    @Override
    protected void readMedia(Intent activityMsg) throws IOException {
        File outputDir = context.getCacheDir(); // context being the Activity pointer
        File outputFile = File.createTempFile(idFile, Consts.EXT, outputDir);

        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File (root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, idFile + Consts.EXT);

        FileOutputStream fileOutput = new FileOutputStream(file);

        is = connection.getInputStream();

        //create a buffer...
        byte[] buffer = new byte[1024];
        int bufferLength;

        while ( (bufferLength = is.read(buffer)) > 0 ) {
            fileOutput.write(buffer, 0, bufferLength);
        }
        //close the output stream when complete //
        fileOutput.close();

        activityMsg.putExtra(Consts.FILE_OUT, file.getAbsolutePath());

    }

    @Override
    protected String readIt() throws IOException {
        return null;
    }
}
