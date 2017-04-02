package com.example.joe.maintenancejournal;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.joe.maintenancejournal.data.DataMgr;

import java.util.ArrayList;
import java.util.List;

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
