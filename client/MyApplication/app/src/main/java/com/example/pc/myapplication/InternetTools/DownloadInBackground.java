package com.example.pc.myapplication.InternetTools;

import android.os.AsyncTask;

import java.io.IOException;

public class DownloadInBackground extends AsyncTask<String, Void, Integer> {

    private InternetClient client;

    public DownloadInBackground(InternetClient client) {
        this.client = client;
    }

    @Override
    protected Integer doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {
            client.connect();
        } catch (IOException e) {
            client.callErrorServer();
            //return -1;
        }

        return 1;
    }

    public void closeConnection() throws IOException {
        client.closeConnection();
    }

    @Override
    protected void onPostExecute(Integer result) {
    }

}