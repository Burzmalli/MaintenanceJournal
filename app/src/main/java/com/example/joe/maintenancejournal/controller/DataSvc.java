package com.example.joe.maintenancejournal.controller;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joe.maintenancejournal.Constants;
import com.example.joe.maintenancejournal.Global;
import com.example.joe.maintenancejournal.model.MaintenanceItem;
import com.example.joe.maintenancejournal.model.MaintenanceTask;
import com.example.joe.maintenancejournal.model.TaskEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jowillia on 3/30/2017.
 */

public class DataSvc extends Service {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    @Override
    public void onCreate() {

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("items");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String response = dataSnapshot.getValue(String.class);

                DataMgr.Items.clear();

                DataMgr.Items.addAll(ParseStringToItems(response));

                //Send broadcast that data has been received and parsed
                Intent intent = new Intent();
                intent.setAction("com.example.joe.maintenancejournal.DATA_UPDATED");
                sendBroadcast(intent);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("", "Failed to read value.", error.toException());
            }
        });
    }

    private List<MaintenanceItem> ParseStringToItems(String response) {
        List<MaintenanceItem> items = new ArrayList<>();

        try {
            //Parse the jsonArray
            JSONArray jsonArray = new JSONArray(response);
            final int length = jsonArray.length();

            for (int i = 0; i < length; i++) {
                MaintenanceItem item = new MaintenanceItem();

                if (!jsonArray.isNull(i)) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    item.ItemId = obj.getInt("itemid");

                    item.ItemName = obj.getString("itemname");
                    item.ItemDescription = obj.getString("itemdescription");
                    item.Uuid = obj.getString("uuid");

                    try {
                        for (int j = 0; j < obj.getJSONArray("itemtasks").length(); j++) {
                            JSONObject task = obj.getJSONArray("itemtasks").getJSONObject(j);

                            MaintenanceTask tsk = new MaintenanceTask();
                            tsk.TaskName = task.getString("taskname");
                            tsk.TaskCost = task.getDouble("taskcost");
                            tsk.FrequencyType = task.getString("frequencytype");
                            tsk.Frequency = task.getInt("frequency");
                            tsk.StartDate = Global.StrToDate(task.getString("startdate"));
                            tsk.Recurring = task.getBoolean("recurring");
                            tsk.TaskDescription = task.getString("taskdescription");
                            tsk.Uuid = task.getString("uuid");
                            tsk.ItemId = item.ItemId;
                            tsk.TaskId = task.getInt("taskid");

                            try {
                                for (int k = 0; k < task.getJSONArray("taskentries").length(); k++) {
                                    JSONObject entry = task.getJSONArray("taskentries").getJSONObject(k);

                                    TaskEntry ent = new TaskEntry();
                                    ent.TaskName = tsk.TaskName;
                                    ent.EntryCost = entry.getDouble("cost");
                                    ent.EntryDate = Global.StrToDate(entry.getString("entrydate"));
                                    ent.Notes = entry.getString("note");
                                    ent.Uuid = entry.getString("uuid");
                                    ent.TaskId = tsk.TaskId;
                                    ent.ItemId = tsk.ItemId;
                                    ent.TaskId = entry.getInt("entryid");
                                }
                            } catch(Exception ex) {

                            }

                            item.Tasks.add(tsk);
                        }
                    } catch( Exception ex) {

                    }

                    items.add(item);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return items;
    }

    public List<MaintenanceItem> getItemData() {
        List<MaintenanceItem> items = new ArrayList<>();

        return items;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return new DataBinder();
    }

    public class DataBinder extends Binder {
        DataSvc getService() {
            // Return this instance of LocalService so clients can call public methods
            return DataSvc.this;
        }
    }
}
