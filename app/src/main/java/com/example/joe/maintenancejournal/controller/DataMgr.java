package com.example.joe.maintenancejournal.controller;

import android.content.Intent;

import com.example.joe.maintenancejournal.App;
import com.example.joe.maintenancejournal.model.MaintenanceItem;
import com.example.joe.maintenancejournal.model.MaintenanceTask;
import com.example.joe.maintenancejournal.model.TaskEntry;
import com.example.joe.maintenancejournal.view.JournalCardAdapter;
import com.example.joe.maintenancejournal.view.SyncSpinner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jowillia on 4/14/2017.
 */

public class DataMgr {
    public static List<MaintenanceItem> Items = new ArrayList<>();
    public static List<MaintenanceTask> Tasks = new ArrayList<>();
    public static List<TaskEntry> Entries = new ArrayList<>();
    public static ConfigMgr ConfigMgr = new ConfigMgr();
    private static DatabaseReference mDatabase;
    private static FirebaseDatabase mInstance;
    private static DatabaseReference mItemRef;
    private static DatabaseReference mTaskRef;
    private static DatabaseReference mEntryRef;
    public static JournalCardAdapter.MaintenanceItemHolder LastClicked;
    public static boolean mSyncing;

    private static MaintenanceItem tempItem;

    static {
        mInstance = FirebaseDatabase.getInstance();
        mDatabase = mInstance.getReference();
        mDatabase.keepSynced(true);
        mItemRef = mDatabase.child("items");
        mTaskRef = mDatabase.child("tasks");
        mEntryRef = mDatabase.child("entries");

        LoadItems();
    }

    public static void LoadItems() {

        Intent spinnerIntent = new Intent(App.sharedInstance.getApplicationContext(), SyncSpinner.class);
        App.sharedInstance.startActivity(spinnerIntent);

        mItemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSyncing = true;

                Items.clear();

                GenericTypeIndicator<Map<String, MaintenanceItem>> itemList = new GenericTypeIndicator<Map<String, MaintenanceItem>>() {};

                Map<String, MaintenanceItem> items = dataSnapshot.getValue(itemList);

                if(items == null) return;
                for(Map.Entry<String, MaintenanceItem> entry : items.entrySet()) {
                    Items.add(entry.getValue());
                }

                Collections.sort(Items);

                mSyncing = false;

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.example.joe.maintenancejournal.DATA_UPDATED");
                App.sharedInstance.sendBroadcast(broadcastIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Read failed: " + databaseError.getCode());

                mSyncing = false;

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.example.joe.maintenancejournal.DATA_UPDATED");
                App.sharedInstance.sendBroadcast(broadcastIntent);
            }
        });

        mTaskRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSyncing = true;

                Tasks.clear();

                GenericTypeIndicator<Map<String, MaintenanceTask>> taskList = new GenericTypeIndicator<Map<String, MaintenanceTask>>() {};

                Map<String, MaintenanceTask> tasks = dataSnapshot.getValue(taskList);

                if(tasks == null) return;
                for(Map.Entry<String, MaintenanceTask> entry : tasks.entrySet()) {
                    Tasks.add(entry.getValue());
                }

                mSyncing = false;

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.example.joe.maintenancejournal.DATA_UPDATED");
                App.sharedInstance.sendBroadcast(broadcastIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Read failed: " + databaseError.getCode());

                mSyncing = false;

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.example.joe.maintenancejournal.DATA_UPDATED");
                App.sharedInstance.sendBroadcast(broadcastIntent);
            }
        });

        mEntryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSyncing = true;

                Entries.clear();

                GenericTypeIndicator<Map<String, TaskEntry>> entryList = new GenericTypeIndicator<Map<String, TaskEntry>>() {};

                Map<String, TaskEntry> entries = dataSnapshot.getValue(entryList);

                if(entries == null) return;
                for(Map.Entry<String, TaskEntry> entry : entries.entrySet()) {
                    Entries.add(entry.getValue());
                }

                mSyncing = false;

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.example.joe.maintenancejournal.DATA_UPDATED");
                App.sharedInstance.sendBroadcast(broadcastIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Read failed: " + databaseError.getCode());

                mSyncing = false;

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.example.joe.maintenancejournal.DATA_UPDATED");
                App.sharedInstance.sendBroadcast(broadcastIntent);
            }
        });
    }

    public static MaintenanceItem GetItemFromKey(String Key) {
        for(MaintenanceItem item : Items) {
            if(item.Key.equals(Key)) return item;
        }

        return null;
    }

    public static MaintenanceTask GetTaskFromKey(String Key) {
        for(MaintenanceTask task : Tasks) {
            if(task.Key.equals(Key)) return task;
        }

        return null;
    }

    public static List<MaintenanceTask> GetItemTasks(String itemUuid) {
        List<MaintenanceTask> tasks = new ArrayList<>();

        for(MaintenanceTask task : Tasks) {
            if(task.ParentKey.equals(itemUuid)) {
                tasks.add(task);
            }
        }

        return tasks;
    }

    public static List<TaskEntry> GetTaskEntries(String taskUuid) {
        List<TaskEntry> entries = new ArrayList<>();

        for(TaskEntry entry : Entries) {
            if(entry.ParentKey.equals(taskUuid)) {
                entries.add(entry);
            }
        }

        return entries;
    }

    public static void ClearTempData() {
        if(tempItem != null) {
            if(tempItem.ItemName.equals("temp")) DeleteItem(tempItem);
        }
    }

    public static void CreateItem(MaintenanceItem item) {
        String key = mItemRef.push().getKey();
        item.Key = key;
        mItemRef.child(item.Key).setValue(item);
        if(item.ItemName.equals("temp")) tempItem = item;
    }

    public static void CreateTask(MaintenanceTask task) {
        String key = mTaskRef.push().getKey();
        task.Key = key;
        mTaskRef.child(task.Key).setValue(task);
    }

    public static void CreateEntry(TaskEntry entry) {
        String key = mEntryRef.push().getKey();
        entry.Key = key;
        mEntryRef.child(entry.Key).setValue(entry);
    }

    public static void UpdateItem(MaintenanceItem item) {
        mItemRef.child(item.Key).setValue(item);
    }

    public static void UpdateTask(MaintenanceTask task) {
        mTaskRef.child(task.Key).setValue(task);
    }

    public static void UpdateEntry(TaskEntry entry) {
        mEntryRef.child(entry.Key).setValue(entry);
    }

    public static void DeleteItem(MaintenanceItem item) {

        List<MaintenanceTask> tasks = GetItemTasks(item.Key);

        for(MaintenanceTask task : tasks) {
            DeleteTask(task);
        }

        mItemRef.child(item.Key).removeValue();
    }

    public static void DeleteTask(MaintenanceTask task) {

        List<TaskEntry> entries = GetTaskEntries(task.Key);

        for(TaskEntry entry : entries) {
            DeleteEntry(entry);
        }

        mTaskRef.child(task.Key).removeValue();
    }

    public static void DeleteEntry(TaskEntry entry) {
        mEntryRef.child(entry.Key).removeValue();
    }

    public static MaintenanceItem GetItemFromName( String testName )
    {
        for(MaintenanceItem itm : Items)
        {
            if(testName.equals(itm.ItemName))
                return itm;
        }

        return null;
    }

    public static boolean isNameUnique( String testName)
    {
        for(MaintenanceItem itm : Items)
        {
            if(testName.contentEquals(itm.ItemName))
                return false;
        }

        return true;
    }

    //Returns all tasks
    public static ArrayList<String> GetTasks()
    {
        ArrayList<String> tasks = new ArrayList<>();
        for(MaintenanceItem itm : Items )
        {
            for(MaintenanceTask task : GetItemTasks(itm.Key))
            {
                tasks.add(itm.ItemName + " : " + task.TaskName + " : " + task.getShortDate() + " : $" + task.TaskCost);
            }
        }

        return tasks;
    }

    //Returns a list of tasks after the from date
    public static ArrayList<String> GetFilteredTasks( Date from )
    {
        ArrayList<String> tasks = new ArrayList<>();
        for(MaintenanceItem itm : Items )
        {
            for(MaintenanceTask task : GetItemTasks(itm.Key))
            {
                if(task.StartDate.equals(from) || task.StartDate.after(from))
                    tasks.add(itm.ItemName + " : " + task.TaskName + " : " + task.getShortDate() + " : $" + task.TaskCost);
            }
        }

        return tasks;
    }

    //Returns a list of tasks between the from and to dates
    public static ArrayList<String> GetFilteredTasks( Date from, Date to )
    {
        ArrayList<String> tasks = new ArrayList<>();
        for(MaintenanceItem itm : Items )
        {
            for(MaintenanceTask task : GetItemTasks(itm.Key))
            {
                if((task.StartDate.equals(from) || task.StartDate.after(from))
                        && (task.StartDate.equals(to) || task.StartDate.before(to)))
                    tasks.add(itm.ItemName + " : " + task.TaskName + " : " + task.getShortDate() + " : $" + task.TaskCost);
            }
        }

        return tasks;
    }
}
