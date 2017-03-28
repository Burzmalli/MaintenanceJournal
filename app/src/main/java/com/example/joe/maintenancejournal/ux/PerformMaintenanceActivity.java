package com.example.joe.maintenancejournal.ux;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.joe.maintenancejournal.R;

/**
 * Created by Joe on 3/26/2017.
 */

public class PerformMaintenanceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform_maintenance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
