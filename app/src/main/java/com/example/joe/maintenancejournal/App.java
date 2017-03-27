package com.example.joe.maintenancejournal;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 3/26/2017.
 */

public class App extends Application {

    public static App sharedInstance;
    public static Activity currentActivity;
    public static List<Activity> activityStack = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        sharedInstance = this;
    }

    public void RefereshCurrentActivity() {
        //ActivityManager am = (ActivityManager)sharedInstance.getSystemService(Context.ACTIVITY_SERVICE);
        //Intent intent = am.getAppTasks().get(0).getTaskInfo().baseIntent;

        if(currentActivity != null) {
            Intent intent = currentActivity.getIntent();
            currentActivity.finish();
            startActivity(intent);
        }
    }

    public static void PushActivity(Activity activity) {
        if(activityStack == null)
            activityStack = new ArrayList<>();

        activityStack.add(activity);

        currentActivity = activityStack.get(activityStack.size()-1);
    }

    public static void PopActivity(Activity activity) {

        if(activityStack == null || activityStack.size() < 1)
            return;

        if(activityStack.contains(activity)) {
            activityStack.remove(activity);

            if(activityStack.size() > 0)
                currentActivity = activityStack.get(activityStack.size()-1);
        }
    }
}
