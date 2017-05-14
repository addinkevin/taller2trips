package com.example.pc.myapplication.singletons;

import android.os.AsyncTask;

import com.example.pc.myapplication.InternetTools.DownloadInBackground;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NetClientsSingleton {

    private static NetClientsSingleton Singleton = null;
    private List<DownloadInBackground> netClients = new ArrayList<>();

    public static NetClientsSingleton getInstance() {
        if(Singleton == null)
        {
            Singleton = new NetClientsSingleton();
        }
        return Singleton;
    }

    public int size() {
        return netClients.size();
    }

    public DownloadInBackground get(int i) {
        return netClients.get(i);
    }

    public void add(DownloadInBackground internetClient) {
        netClients.add(internetClient);
    }

    public void clear() {
        netClients.clear();
    }

    public boolean isEmpty() {
        return netClients.isEmpty();
    }

    public void remove (DownloadInBackground internetClient) {
        netClients.remove(netClients);
    }

    public void remove (int i) {
        netClients.remove(i);
    }


    public void clearConnections() {
        for (int i = 0; i < size(); i++) {
            final DownloadInBackground task = get(i);
            boolean cancel = task.cancel(true);
            if (cancel) {

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            task.closeConnection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute();

            }

            System.out.println("Se canceloooooo: " + cancel);
        }
       clear();
    }
}
