package com.example.joe.maintenancejournal.controller;

import android.content.Intent;
import android.os.AsyncTask;

import com.example.joe.maintenancejournal.App;

/**
 * Created by jowillia on 4/19/2017.
 */

public class DataSetup extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        try {
            Thread.sleep(5000);
        } catch(Exception ex) {

        }

        return this;
    }

    @Override
    protected void onPostExecute(Object result) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.example.joe.maintenancejournal.DATA_UPDATED");
        App.sharedInstance.sendBroadcast(broadcastIntent);
    }
}
