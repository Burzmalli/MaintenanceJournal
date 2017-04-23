package com.example.joe.maintenancejournal.controller;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.joe.maintenancejournal.App;
import com.example.joe.maintenancejournal.model.MaintenanceItem;
import com.example.joe.maintenancejournal.model.MaintenanceTask;
import com.example.joe.maintenancejournal.model.TaskEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Joe on 4/22/2017.
 */

public class DataService extends Service {

    @Override
    public void onCreate() {

        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);

        LoadItems();

        return START_STICKY;
    }

    public void LoadItems() {

        try {
            Thread.sleep(5000);
        } catch(Exception ex) {

        }

        DataMgr.mItemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataMgr.Items.clear();

                GenericTypeIndicator<Map<String, MaintenanceItem>> itemList = new GenericTypeIndicator<Map<String, MaintenanceItem>>() {};

                Map<String, MaintenanceItem> items = dataSnapshot.getValue(itemList);

                if(items == null) return;
                for(Map.Entry<String, MaintenanceItem> entry : items.entrySet()) {
                    DataMgr.Items.add(entry.getValue());
                }

                Collections.sort(DataMgr.Items);

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.example.joe.maintenancejournal.DATA_UPDATED");
                App.sharedInstance.sendBroadcast(broadcastIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Read failed: " + databaseError.getCode());

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.example.joe.maintenancejournal.DATA_UPDATED");
                App.sharedInstance.sendBroadcast(broadcastIntent);
            }
        });

        DataMgr.mTaskRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataMgr.Tasks.clear();

                GenericTypeIndicator<Map<String, MaintenanceTask>> taskList = new GenericTypeIndicator<Map<String, MaintenanceTask>>() {};

                Map<String, MaintenanceTask> tasks = dataSnapshot.getValue(taskList);

                if(tasks == null) return;
                for(Map.Entry<String, MaintenanceTask> entry : tasks.entrySet()) {
                    DataMgr.Tasks.add(entry.getValue());
                }

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.example.joe.maintenancejournal.DATA_UPDATED");
                App.sharedInstance.sendBroadcast(broadcastIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Read failed: " + databaseError.getCode());

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.example.joe.maintenancejournal.DATA_UPDATED");
                App.sharedInstance.sendBroadcast(broadcastIntent);
            }
        });

        DataMgr.mEntryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataMgr.Entries.clear();

                GenericTypeIndicator<Map<String, TaskEntry>> entryList = new GenericTypeIndicator<Map<String, TaskEntry>>() {};

                Map<String, TaskEntry> entries = dataSnapshot.getValue(entryList);

                if(entries == null) return;
                for(Map.Entry<String, TaskEntry> entry : entries.entrySet()) {
                    DataMgr.Entries.add(entry.getValue());
                }

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.example.joe.maintenancejournal.DATA_UPDATED");
                App.sharedInstance.sendBroadcast(broadcastIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Read failed: " + databaseError.getCode());

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.example.joe.maintenancejournal.DATA_UPDATED");
                App.sharedInstance.sendBroadcast(broadcastIntent);
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return new DataBinder();
    }

    public class DataBinder extends Binder {
        DataService getService() {
            // Return this instance of LocalService so clients can call public methods
            return DataService.this;
        }
    }
}
