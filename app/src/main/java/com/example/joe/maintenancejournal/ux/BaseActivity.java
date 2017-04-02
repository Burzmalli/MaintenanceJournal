package com.example.joe.maintenancejournal.ux;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.joe.maintenancejournal.R;
import com.example.joe.maintenancejournal.data.DataMgr;

/**
 * Created by Joe on 3/26/2017.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataMgr.SetContext(this);

        IntentFilter ifilter=new IntentFilter(DataMgr.DATA_UPDATE_COMPLETE);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onEvent, ifilter);
    }

    @Override
    protected void onStop() {
        DataMgr.CancelPendingRequests();

        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onEvent);

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        DataMgr.CancelPendingRequests();

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private BroadcastReceiver onEvent=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent i) {

            Toast.makeText(ctxt, "Download Complete",
                    Toast.LENGTH_LONG).show();
        }
    };
}
