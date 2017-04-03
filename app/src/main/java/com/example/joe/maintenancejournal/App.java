package com.example.joe.maintenancejournal;

import android.app.Application;

import com.example.joe.maintenancejournal.controller.DataMgr;

/**
 * Created by Joe on 3/26/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DataMgr.SetContext(this);
    }


}
