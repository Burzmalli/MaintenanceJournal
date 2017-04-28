package com.example.joe.maintenancejournal;

import android.app.Application;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.firebase.database.FirebaseDatabase;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Joe on 3/26/2017.
 */

public class App extends Application {

    public static App sharedInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        sharedInstance = this;

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("App Launch"));

    }
}
