package com.example.joe.maintenancejournal.data;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joe.maintenancejournal.App;
import com.example.joe.maintenancejournal.data.entities.MaintenanceItem;
import com.example.joe.maintenancejournal.data.entities.MaintenanceTask;
import com.example.joe.maintenancejournal.ux.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Joe on 3/26/2017.
 */

public class DatabaseAsyncTask extends AsyncTask {



    @Override
    protected Object doInBackground(Object[] params) {
        // Instantiate the RequestQueue.
        /*RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.google.com";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);*/

        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        try {
            //JSONObject itemsObj = new JSONObject((String) result);
            //JSONArray jsonArray = itemsObj.getJSONArray("items");
            JSONArray jsonArray = new JSONArray((String) result);
            final int length = jsonArray.length();

            DataMgr.Items = new ArrayList<>();

            for( int i = 0; i < length; i++ ) {
                MaintenanceItem item = new MaintenanceItem();

                if(!jsonArray.isNull(i)) {
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
                        tsk.StartDate = StrToDate(task.getString("startdate"));
                        tsk.Recurring = task.getBoolean("recurring");
                        tsk.TaskDescription = task.getString("taskdescription");
                        tsk.ItemId = item.ItemId;
                        tsk.TaskId = j + item.ItemId;

                        item.Tasks.add(tsk);
                    }

                    DataMgr.Items.add(item);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Date StrToDate(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(str);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return convertedDate;
    }
}
