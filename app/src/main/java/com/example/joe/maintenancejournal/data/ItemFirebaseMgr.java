package com.example.joe.maintenancejournal.data;

import android.content.Context;
import android.os.AsyncTask;

import com.example.joe.maintenancejournal.data.entities.MaintenanceItem;
import com.example.joe.maintenancejournal.data.entities.MaintenanceTask;

import java.util.List;

/**
 * Created by Joe on 3/26/2017.
 */

public class ItemFirebaseMgr implements IItemSvc {

    private final String FIREBASEURL = "https://maintenance-journal.firebaseio.com/";
    private Context myContext;

    public ItemFirebaseMgr(Context context) {
        myContext = context;
    }

    @Override
    public List<MaintenanceItem> loadItems() {
        String getUrl = FIREBASEURL + "items.json";

        new DatabaseAsyncTask(myContext).execute(getUrl, "GET");

        return null;
    }

    @Override
    public void createItem(MaintenanceItem item) {
        String createUrl = FIREBASEURL + "items/" + item.ItemId + "/.json";
        //String createUrl = FIREBASEURL + "items/.json";
        //String createUrl = FIREBASEURL + "items.json";
        new DatabaseAsyncTask(myContext).execute(createUrl, "PUT", item.GetJSON());
    }

    @Override
    public void updateItem(MaintenanceItem item) {
        String updateUrl = FIREBASEURL + "items/" + (DataMgr.Items.indexOf(item)) + "/.json";
        //String createUrl = FIREBASEURL + "items.json";
        new DatabaseAsyncTask(myContext).execute(updateUrl, "PATCH", item.GetJSON());
    }

    @Override
    public void deleteItem(MaintenanceItem item) {
        String updateUrl = FIREBASEURL + "items/" + (DataMgr.Items.indexOf(item)) + "/.json";
        //String createUrl = FIREBASEURL + "items.json";
        new DatabaseAsyncTask(myContext).execute(updateUrl, "DELETE", item.GetJSON());
    }

    @Override
    public void createTask(MaintenanceTask task) {
        MaintenanceItem item = DataMgr.findTaskOwner(task);
        if (item != null) {
        String updateUrl = FIREBASEURL + "items/" + (DataMgr.Items.indexOf(item)) + "/.json";
        //String createUrl = FIREBASEURL + "items.json";

        new DatabaseAsyncTask(myContext).execute(updateUrl, "PATCH", item.GetJSON());
        }
    }

    @Override
    public void updateTask(MaintenanceTask task) {
        MaintenanceItem item = DataMgr.findTaskOwner(task);
        if(item != null){
            String updateUrl = FIREBASEURL + "items/" + (DataMgr.Items.indexOf(item)) + "/.json";
            //String createUrl = FIREBASEURL + "items.json";

            new DatabaseAsyncTask(myContext).execute(updateUrl, "PATCH", item.GetJSON());
        }
    }

    @Override
    public void deleteTask(MaintenanceTask task) {
        MaintenanceItem item = DataMgr.findTaskOwner(task);
        if(item != null) {
            String updateUrl = FIREBASEURL + "items/" + (DataMgr.Items.indexOf(item)) + "/.json";
            //String createUrl = FIREBASEURL + "items.json";

            item.Tasks.remove(task);
            new DatabaseAsyncTask(myContext).execute(updateUrl, "PATCH", item.GetJSON());
        }
    }
}
