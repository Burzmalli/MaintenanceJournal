package com.example.joe.maintenancejournal.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by jowillia on 4/3/2017.
 */

public class DataUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newIntent = new Intent();
        newIntent.setAction(DataMgr.DATA_UPDATE_COMPLETE);
        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(newIntent);
    }
}
