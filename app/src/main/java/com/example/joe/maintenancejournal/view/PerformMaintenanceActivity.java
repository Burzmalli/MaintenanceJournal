package com.example.joe.maintenancejournal.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
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

        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Perform maintenance started"));
    }
}
