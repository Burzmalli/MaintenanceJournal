package com.example.joe.maintenancejournal.data;

import android.os.AsyncTask;
import android.util.Log;

import com.example.joe.maintenancejournal.App;
import com.example.joe.maintenancejournal.data.entities.MaintenanceItem;
import com.example.joe.maintenancejournal.data.entities.MaintenanceTask;

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
        String result = "";

        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(params[0].toString());
            String requestMethod = params[1].toString();


            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Accept", "application/json");
            //urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.setRequestMethod(requestMethod);

            if(params.length > 2) {
                String requestJson = params[2].toString();

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                DataOutputStream outstream = new DataOutputStream(urlConnection.getOutputStream());
                outstream.writeBytes(requestJson);
                outstream.flush();
                outstream.close();
            }

            InputStream is = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String str = "";

            while((str = br.readLine()) != null) {
                result += str;
            }
        } catch (IOException ex){
            Log.i("DatabaseAsyncTask", "EXCEPTION: " + ex.getMessage());
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }

        return result;
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

            App.sharedInstance.RefereshCurrentActivity();
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
