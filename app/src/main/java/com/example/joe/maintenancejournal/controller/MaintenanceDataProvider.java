package com.example.joe.maintenancejournal.controller;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.joe.maintenancejournal.App;
import com.example.joe.maintenancejournal.model.MaintenanceItem;
import com.example.joe.maintenancejournal.model.MaintenanceTask;
import com.example.joe.maintenancejournal.model.TaskEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 4/9/2017.
 */

public class MaintenanceDataProvider extends ContentProvider {

    enum SyncType {
        ONLINE,
        LOCAL,
        BOTH
    }

    private IItemSvc myOnlineSource;
    private IItemSvc myLocalCache;
    public static MaintenanceDataProvider sharedInstance;

    static {
        sharedInstance = new MaintenanceDataProvider();
        sharedInstance.onCreate();
    }

    @Override
    public boolean onCreate() {

        if(myOnlineSource == null)
            myOnlineSource = new ItemFirebaseMgr();

        if(myLocalCache == null)
            myLocalCache = new DatabaseMgr(App.sharedInstance.getApplicationContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    public void loadItems() {
        getOnlineSource().loadItems();
    }

    public void createItem(MaintenanceItem item, SyncType type) {

        if(type == SyncType.LOCAL || type == SyncType.BOTH) {
            getLocalCache().createItem(item);
        }

        if(type == SyncType.ONLINE || type == SyncType.BOTH) {
            getOnlineSource().createItem(item);
        }
    }

    public void updateItem(MaintenanceItem item, SyncType type) {
        if(type == SyncType.ONLINE || type == SyncType.BOTH) {
            getOnlineSource().updateItem(item);
        }

        if(type == SyncType.LOCAL || type == SyncType.BOTH) {
            getLocalCache().updateItem(item);
        }
    }

    public void deleteItem(MaintenanceItem item, SyncType type) {

        if(type == SyncType.ONLINE || type == SyncType.BOTH) {
            getOnlineSource().deleteItem(item);
        }

        if(type == SyncType.LOCAL || type == SyncType.BOTH) {
            getLocalCache().deleteItem(item);
        }
    }

    public void createTask(MaintenanceTask task, SyncType type) {

        if(type == SyncType.ONLINE || type == SyncType.BOTH) {
            getOnlineSource().createTask(task);
        }

        if(type == SyncType.LOCAL || type == SyncType.BOTH) {
            getLocalCache().createTask(task);
        }
    }

    public void updateTask(MaintenanceTask task, SyncType type) {
        if(type == SyncType.ONLINE || type == SyncType.BOTH) {
            getOnlineSource().updateTask(task);
        }

        if(type == SyncType.LOCAL || type == SyncType.BOTH) {
            getLocalCache().updateTask(task);
        }
    }

    public void deleteTask(MaintenanceTask task, SyncType type) {
        if(type == SyncType.ONLINE || type == SyncType.BOTH) {
            getOnlineSource().deleteTask(task);
        }

        if(type == SyncType.LOCAL || type == SyncType.BOTH) {
            getLocalCache().deleteTask(task);
        }
    }

    public void createEntry(TaskEntry entry, SyncType type) {
        if(type == SyncType.ONLINE || type == SyncType.BOTH) {
            getOnlineSource().createEntry(entry);
        }

        if(type == SyncType.LOCAL || type == SyncType.BOTH) {
            getLocalCache().createEntry(entry);
        }
    }

    public void updateEntry(TaskEntry entry, SyncType type) {
        if(type == SyncType.ONLINE || type == SyncType.BOTH) {
            getOnlineSource().updateEntry(entry);
        }

        if(type == SyncType.LOCAL || type == SyncType.BOTH) {
            getLocalCache().updateEntry(entry);
        }
    }

    public void deleteEntry(TaskEntry entry, SyncType type) {
        if(type == SyncType.ONLINE || type == SyncType.BOTH) {
            getOnlineSource().deleteEntry(entry);
        }

        if(type == SyncType.LOCAL || type == SyncType.BOTH) {
            getLocalCache().deleteEntry(entry);
        }
    }

    public List<MaintenanceItem> getLocalItems() {
        List<MaintenanceItem> itms = getLocalCache().getAllItems();

        return itms;
    }

    public List<MaintenanceTask> getLocalTasks() {
        List<MaintenanceTask> tasks = getLocalCache().getAllTasks();

        return tasks;
    }

    public List<TaskEntry> getLocalEntries() {
        List<TaskEntry> entries = getLocalCache().getAllEntries();

        return entries;
    }

    private IItemSvc getLocalCache() {
        if(myLocalCache == null) {
            myLocalCache = new DatabaseMgr(App.sharedInstance.getApplicationContext());
        }

        return myLocalCache;
    }

    private IItemSvc getOnlineSource() {
        if(myOnlineSource == null)
            myOnlineSource = new ItemFirebaseMgr();

        return myOnlineSource;
    }
}
