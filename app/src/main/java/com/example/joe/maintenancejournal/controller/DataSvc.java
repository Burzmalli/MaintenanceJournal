package com.example.joe.maintenancejournal.controller;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jowillia on 3/30/2017.
 */

public class DataSvc extends Service {

    private RequestQueue mQueue;

    @Override
    public void onCreate() {

        mQueue = Volley.newRequestQueue(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        //Get the request type and URL from the intent
        int requestType = intent.getIntExtra(Constants.INT_VOLLEY_REQ_TYPE, Request.Method.GET);
        String requestUrl = intent.getStringExtra(Constants.INT_VOLLEY_REQ_URL);

        SendRest(requestType, requestUrl, null);

        return new DataBinder();
    }

    public void SendRest(int type, String url, JSONObject item) {

        //Get the request type and URL from the intent
        final int requestType = type;
        String requestUrl = url;

        //If JSONObject not null
        if(item != null ) {

            //Post item to database and toast success
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestType, requestUrl, item,
                    new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    String toastTxt = "";

                    if(requestType == Request.Method.PUT) {
                        toastTxt = "Item successfully put.";
                    } else if (requestType == Request.Method.PATCH ) {
                        toastTxt = "Item successfully updated";
                    }

                    Toast.makeText(getApplicationContext(),
                            toastTxt,
                            Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),
                            "Data Operation Failed: " + error.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

            mQueue.add(jsonObjectRequest);
        } else {

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(requestType, requestUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //Parse the jsonArray
                                JSONArray jsonArray = new JSONArray(response);
                                final int length = jsonArray.length();

                                DataMgr.Items.clear();

                                for (int i = 0; i < length; i++) {
                                    MaintenanceItem item = new MaintenanceItem();

                                    if (!jsonArray.isNull(i)) {
                                        JSONObject obj = jsonArray.getJSONObject(i);

                                        item.ItemId = i;

                                        item.ItemName = obj.getString("itemname");
                                        item.ItemDescription = obj.getString("itemdescription");

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
                                            tsk.ItemId = item.ItemId;
                                            tsk.TaskId = j + item.ItemId;

                                            item.Tasks.add(tsk);
                                        }

                                        DataMgr.Items.add(item);
                                    }
                                }

                                //Send broadcast that data has been received and parsed
                                Intent intent = new Intent();
                                intent.setAction("com.example.joe.maintenancejournal.DATA_UPDATED");
                                sendBroadcast(intent);
                                //intent.setAction(DataMgr.DATA_UPDATE_COMPLETE);
                                //LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),
                            "Data Operation Failed: " + error.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

            mQueue.add(stringRequest);
        }
    }

    public class DataBinder extends Binder {
        DataSvc getService() {
            // Return this instance of LocalService so clients can call public methods
            return DataSvc.this;
        }
    }
}
