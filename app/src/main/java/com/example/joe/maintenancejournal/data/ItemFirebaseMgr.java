package com.example.joe.maintenancejournal.data;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.joe.maintenancejournal.Constants;
import com.example.joe.maintenancejournal.data.entities.MaintenanceItem;
import com.example.joe.maintenancejournal.data.entities.MaintenanceTask;

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
        String createUrl = FIREBASEURL + "items/" + item.ItemId + "/.json";
        //String createUrl = FIREBASEURL + "items/.json";
        //String createUrl = FIREBASEURL + "items.json";
        new DatabaseAsyncTask().execute(createUrl, "PUT", item.GetJSON());
    }

    @Override
    public void updateItem(MaintenanceItem item) {
        String updateUrl = FIREBASEURL + "items/" + (DataMgr.Items.indexOf(item)) + "/.json";
        //String createUrl = FIREBASEURL + "items.json";
        new DatabaseAsyncTask().execute(updateUrl, "PATCH", item.GetJSON());
    }

    @Override
    public void deleteItem(MaintenanceItem item) {
        String updateUrl = FIREBASEURL + "items/" + (DataMgr.Items.indexOf(item)) + "/.json";
        //String createUrl = FIREBASEURL + "items.json";
        new DatabaseAsyncTask().execute(updateUrl, "DELETE", item.GetJSON());
    }

    @Override
    public void createTask(MaintenanceTask task) {
        MaintenanceItem item = DataMgr.findTaskOwner(task);
        if (item != null) {
        String updateUrl = FIREBASEURL + "items/" + (DataMgr.Items.indexOf(item)) + "/.json";
        //String createUrl = FIREBASEURL + "items.json";

        new DatabaseAsyncTask().execute(updateUrl, "PATCH", item.GetJSON());
        }
    }

    @Override
    public void updateTask(MaintenanceTask task) {
        MaintenanceItem item = DataMgr.findTaskOwner(task);
        if(item != null){
            String updateUrl = FIREBASEURL + "items/" + (DataMgr.Items.indexOf(item)) + "/.json";
            //String createUrl = FIREBASEURL + "items.json";

            new DatabaseAsyncTask().execute(updateUrl, "PATCH", item.GetJSON());
        }
    }

    @Override
    public void deleteTask(MaintenanceTask task) {
        MaintenanceItem item = DataMgr.findTaskOwner(task);
        if(item != null) {
            String updateUrl = FIREBASEURL + "items/" + (DataMgr.Items.indexOf(item)) + "/.json";
            //String createUrl = FIREBASEURL + "items.json";

            item.Tasks.remove(task);
            new DatabaseAsyncTask().execute(updateUrl, "PATCH", item.GetJSON());
        }
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
