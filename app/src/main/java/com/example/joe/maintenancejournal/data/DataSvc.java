package com.example.joe.maintenancejournal.data;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by jowillia on 3/30/2017.
 */

public class DataSvc extends IntentService {

    public DataSvc(String name) {
        super(name);
    }

    @Override
    public void onCreate() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
