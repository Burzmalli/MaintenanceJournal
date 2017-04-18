package com.example.joe.maintenancejournal.view;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.joe.maintenancejournal.R;
import com.example.joe.maintenancejournal.controller.DataMgr;
import com.example.joe.maintenancejournal.controller.DataUpdateReceiver;

/**
 * Created by jowillia on 4/10/2017.
 */

public class SyncSpinner extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sync_spinner);

        IntentFilter filter = new IntentFilter(".claros.Broadcasts.SyncReceiver");

        registerReceiver(onEvent, filter);

        if(!DataMgr.mSyncing)
            finish();
    }

    private DataUpdateReceiver onEvent=new DataUpdateReceiver() {
        public void onReceive(Context ctxt, Intent i) {

            finish();
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(onEvent);

        super.onDestroy();
    }

}
