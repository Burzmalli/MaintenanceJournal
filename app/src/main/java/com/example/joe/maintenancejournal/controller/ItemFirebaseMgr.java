package com.example.joe.maintenancejournal.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.example.joe.maintenancejournal.App;
import com.example.joe.maintenancejournal.Constants;
import com.example.joe.maintenancejournal.model.MaintenanceItem;
import com.example.joe.maintenancejournal.model.MaintenanceTask;
import com.example.joe.maintenancejournal.model.TaskEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Joe on 3/26/2017.
 */

public class ItemFirebaseMgr implements IItemSvc {

    private final String FIREBASEURL = "https://maintenance-journal2.firebaseio.com/";
    private Context mContext;

    private DataSvc mService;
    private boolean mBound = false;
    private FirebaseDatabase mDatabase;
    private DatabaseReference ref;

    @Override
    public void loadItems() {

        /*if(mContext == null) mContext = App.sharedInstance.getApplicationContext();

        //Build URL for GET request
        String getUrl = Constants.FIREBASEURL + Constants.BASE_ARRAY_FILE;

        //Bind to service if not yet bound;
        if(!mBound) {

            //Create service intent, put GET type and target URL, and bind service
            Intent svcIntent = new Intent(mContext, DataSvc.class);
            svcIntent.putExtra(Constants.INT_VOLLEY_REQ_TYPE, Request.Method.GET);
            svcIntent.putExtra(Constants.INT_VOLLEY_REQ_URL, getUrl);
            mContext.bindService(svcIntent, mConnection, Context.BIND_AUTO_CREATE);

            return;
        }

        //Otherwise, kick off rest request
        mService.SendRest(Request.Method.GET, getUrl, null);*/

        DatabaseReference ref = mDatabase.getReference("items");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void createItem(MaintenanceItem item) {

        if(mContext == null) mContext = App.sharedInstance.getApplicationContext();

        //Build URL for PUT request
        String putUrl = "";// = Constants.FIREBASEURL + "items/" + (item.OnlineId) + "/.json";

        //Bind to service if not yet bound
        if(!mBound) {
            //Create service intent, put PUT type and target URL, and bind service
            Intent svcIntent = new Intent(mContext, DataSvc.class);
            svcIntent.putExtra(Constants.INT_VOLLEY_REQ_TYPE, Request.Method.PUT);
            svcIntent.putExtra(Constants.INT_VOLLEY_REQ_URL, putUrl);
            mContext.bindService(svcIntent, mConnection, Context.BIND_AUTO_CREATE);
            return;
        }

        //Otherwise, kick off rest request
        //mService.SendRest(Request.Method.PUT, putUrl, item.GetAsJSONObject());
    }

    @Override
    public void updateItem(MaintenanceItem item) {
        if(mContext == null) mContext = App.sharedInstance.getApplicationContext();

        //Build URL for PATCH request
        String patchUrl = "";// = FIREBASEURL + "items/" + item.OnlineId + "/.json";

        //Bind to service if not yet bound
        if(!mBound) {
            //Create service intent, put PATCH type and target URL, and bind service
            Intent svcIntent = new Intent(mContext, DataSvc.class);
            svcIntent.putExtra(Constants.INT_VOLLEY_REQ_TYPE, Request.Method.PATCH);
            svcIntent.putExtra(Constants.INT_VOLLEY_REQ_URL, patchUrl);
            mContext.bindService(svcIntent, mConnection, Context.BIND_AUTO_CREATE);
            return;
        }

        //Otherwise, kick off rest request
        //mService.SendRest(Request.Method.PATCH, patchUrl, item.GetAsJSONObject());
    }

    @Override
    public void deleteItem(MaintenanceItem item) {

        if(mContext == null) mContext = App.sharedInstance.getApplicationContext();

        //Build URL for delete request
        String deleteUrl = "";// = FIREBASEURL + "items/" + item.OnlineId + "/.json";

        if(!mBound) {
            //Create service intent, put DELETE type and target URL, and bind service
            Intent svcIntent = new Intent(mContext, DataSvc.class);
            svcIntent.putExtra(Constants.INT_VOLLEY_REQ_TYPE, Request.Method.DELETE);
            svcIntent.putExtra(Constants.INT_VOLLEY_REQ_URL, deleteUrl);
            mContext.bindService(svcIntent, mConnection, Context.BIND_AUTO_CREATE);
            return;
        }

        //Otherwise, kick off rest request
        //mService.SendRest(Request.Method.DELETE, deleteUrl, null);
    }

    @Override
    public void createTask(MaintenanceTask task) {

        if(mContext == null) mContext = App.sharedInstance.getApplicationContext();

        MaintenanceItem item = DataMgr.findTaskOwner(task);
        if (item == null) return;

        //Build URL for PATCH request
        String updateUrl = "";// = FIREBASEURL + "items/" + item.OnlineId + "/.json";

        //Bind to service if not yet bound
        if(!mBound) {
            //Create service intent, put PATCH type and target URL, and bind service
            Intent svcIntent = new Intent(mContext, DataSvc.class);
            svcIntent.putExtra(Constants.INT_VOLLEY_REQ_TYPE, Request.Method.PATCH);
            svcIntent.putExtra(Constants.INT_VOLLEY_REQ_URL, updateUrl);
            mContext.bindService(svcIntent, mConnection, Context.BIND_AUTO_CREATE);
            return;
        }

        //Otherwise, kick off rest request
        //mService.SendRest(Request.Method.PATCH, updateUrl, item.GetAsJSONObject());
    }

    @Override
    public void updateTask(MaintenanceTask task) {

        if(mContext == null) mContext = App.sharedInstance.getApplicationContext();

        MaintenanceItem item = DataMgr.findTaskOwner(task);
        if (item == null) return;

        //Build URL for PATCH request
        String updateUrl = "";// = FIREBASEURL + "items/" + item.OnlineId + "/.json";

        //Bind to service if not yet bound
        if(!mBound) {
            //Create service intent, put PATCH type and target URL, and bind service
            Intent svcIntent = new Intent(mContext, DataSvc.class);
            svcIntent.putExtra(Constants.INT_VOLLEY_REQ_TYPE, Request.Method.PATCH);
            svcIntent.putExtra(Constants.INT_VOLLEY_REQ_URL, updateUrl);
            mContext.bindService(svcIntent, mConnection, Context.BIND_AUTO_CREATE);
            return;
        }

        //Otherwise, kick off rest request
        //mService.SendRest(Request.Method.PATCH, updateUrl, item.GetAsJSONObject());
    }

    @Override
    public void deleteTask(MaintenanceTask task) {

        if(mContext == null) mContext = App.sharedInstance.getApplicationContext();

        MaintenanceItem item = DataMgr.findTaskOwner(task);
        if (item == null) return;

        //Build URL for PATCH request
        String updateUrl = "";// = FIREBASEURL + "items/" + item.OnlineId + "/.json";

        //Bind to service if not yet bound
        if(!mBound) {
            //Create service intent, put PATCH type and target URL, and bind service
            Intent svcIntent = new Intent(mContext, DataSvc.class);
            svcIntent.putExtra(Constants.INT_VOLLEY_REQ_TYPE, Request.Method.PATCH);
            svcIntent.putExtra(Constants.INT_VOLLEY_REQ_URL, updateUrl);
            mContext.bindService(svcIntent, mConnection, Context.BIND_AUTO_CREATE);
            return;
        }

        //Otherwise, kick off rest request
        //mService.SendRest(Request.Method.PATCH, updateUrl, item.GetAsJSONObject());
    }

    @Override
    public void createEntry(TaskEntry entry) {

    }

    @Override
    public void updateEntry(TaskEntry entry) {

    }

    @Override
    public void deleteEntry(TaskEntry entry) {

    }

    @Override
    public void SetContext(Context context) { mContext = context; }

    @Override
    public List<MaintenanceItem> getAllItems() {
        return null;
    }

    @Override
    public List<MaintenanceTask> getAllTasks() {
        return null;
    }

    @Override
    public List<TaskEntry> getAllEntries() {
        return null;
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

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
