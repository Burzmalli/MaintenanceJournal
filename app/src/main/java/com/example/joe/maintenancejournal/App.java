package com.example.joe.maintenancejournal;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Joe on 3/26/2017.
 */

public class App extends Application {

    public static App sharedInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedInstance = this;

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }


}
