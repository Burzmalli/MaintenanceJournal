package com.example.joe.maintenancejournal.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.android.volley.Request;
import com.example.joe.maintenancejournal.Constants;
import com.example.joe.maintenancejournal.model.MaintenanceItem;
import com.example.joe.maintenancejournal.model.MaintenanceTask;

import java.util.List;

/**
 * Created by Joe on 3/26/2017.
 */

public class ItemFirebaseMgr implements IItemSvc {

    private final String FIREBASEURL = "https://maintenance-journal.firebaseio.com/";
    private Context mContext;

    private DataSvc mService;
    private boolean mBound = false;

    @Override
    public List<MaintenanceItem> loadItems() {

        if(mContext == null) return null;

        String getUrl = Constants.FIREBASEURL + Constants.BASE_ARRAY_FILE;

        Intent svcIntent = new Intent(mContext, DataSvc.class);
        svcIntent.putExtra(Constants.INT_VOLLEY_REQ_TYPE, Request.Method.GET);
        svcIntent.putExtra(Constants.INT_VOLLEY_REQ_URL, getUrl);
        mContext.bindService(svcIntent, mConnection, Context.BIND_AUTO_CREATE);

        return null;
    }

    @Override
    public void createItem(MaintenanceItem item) {

        if(mContext == null);

        String putUrl = Constants.FIREBASEURL + "items/" + item.ItemId + "/.json";

        Intent svcIntent = new Intent(mContext, DataSvc.class);
        svcIntent.putExtra(Constants.INT_VOLLEY_REQ_TYPE, Request.Method.PUT);
        svcIntent.putExtra(Constants.INT_VOLLEY_REQ_URL, putUrl);
        mContext.bindService(svcIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void updateItem(MaintenanceItem item) {
        String patchUrl = FIREBASEURL + "items/" + (DataMgr.Items.indexOf(item)) + "/.json";
        Intent svcIntent = new Intent(mContext, DataSvc.class);
        svcIntent.putExtra(Constants.INT_VOLLEY_REQ_TYPE, Request.Method.PATCH);
        svcIntent.putExtra(Constants.INT_VOLLEY_REQ_URL, patchUrl);
        mContext.bindService(svcIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void deleteItem(MaintenanceItem item) {
        String deleteUrl = FIREBASEURL + "items/" + (DataMgr.Items.indexOf(item)) + "/.json";
        Intent svcIntent = new Intent(mContext, DataSvc.class);
        svcIntent.putExtra(Constants.INT_VOLLEY_REQ_TYPE, Request.Method.DELETE);
        svcIntent.putExtra(Constants.INT_VOLLEY_REQ_URL, deleteUrl);
        mContext.bindService(svcIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void createTask(MaintenanceTask task) {
        MaintenanceItem item = DataMgr.findTaskOwner(task);
        if (item == null) return;
        String updateUrl = FIREBASEURL + "items/" + (DataMgr.Items.indexOf(item)) + "/.json";
        Intent svcIntent = new Intent(mContext, DataSvc.class);
        svcIntent.putExtra(Constants.INT_VOLLEY_REQ_TYPE, Request.Method.PATCH);
        svcIntent.putExtra(Constants.INT_VOLLEY_REQ_URL, updateUrl);
        mContext.bindService(svcIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void updateTask(MaintenanceTask task) {
        MaintenanceItem item = DataMgr.findTaskOwner(task);
        if (item == null) return;
        String updateUrl = FIREBASEURL + "items/" + (DataMgr.Items.indexOf(item)) + "/.json";
        Intent svcIntent = new Intent(mContext, DataSvc.class);
        svcIntent.putExtra(Constants.INT_VOLLEY_REQ_TYPE, Request.Method.PATCH);
        svcIntent.putExtra(Constants.INT_VOLLEY_REQ_URL, updateUrl);
        mContext.bindService(svcIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void deleteTask(MaintenanceTask task) {
        MaintenanceItem item = DataMgr.findTaskOwner(task);
        if (item == null) return;
        String updateUrl = FIREBASEURL + "items/" + (DataMgr.Items.indexOf(item)) + "/.json";
        Intent svcIntent = new Intent(mContext, DataSvc.class);
        svcIntent.putExtra(Constants.INT_VOLLEY_REQ_TYPE, Request.Method.PATCH);
        svcIntent.putExtra(Constants.INT_VOLLEY_REQ_URL, updateUrl);
        mContext.bindService(svcIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void SetContext(Context context) { mContext = context; }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DataSvc.DataBinder binder = (DataSvc.DataBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
