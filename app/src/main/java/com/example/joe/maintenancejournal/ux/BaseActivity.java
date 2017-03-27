package com.example.joe.maintenancejournal.ux;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.joe.maintenancejournal.App;

/**
 * Created by Joe on 3/26/2017.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.PushActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        App.PopActivity(this);
    }
}
