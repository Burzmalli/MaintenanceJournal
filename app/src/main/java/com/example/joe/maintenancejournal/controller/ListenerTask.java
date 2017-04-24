package com.example.joe.maintenancejournal.controller;

import android.os.AsyncTask;

/**
 * Created by Joe on 4/23/2017.
 */

public class ListenerTask extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        DataMgr.LoadItems();

        return null;
    }
}
