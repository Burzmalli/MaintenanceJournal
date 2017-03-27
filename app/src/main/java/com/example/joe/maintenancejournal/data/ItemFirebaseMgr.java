package com.example.joe.maintenancejournal.data;

import android.os.AsyncTask;

import com.example.joe.maintenancejournal.data.entities.MaintenanceItem;
import com.example.joe.maintenancejournal.data.entities.MaintenanceTask;

import java.util.List;

/**
 * Created by Joe on 3/26/2017.
 */

public class ItemFirebaseMgr implements IItemSvc {

    private final String FIREBASEURL = "https://maintenance-journal.firebaseio.com/";

    @Override
    public List<MaintenanceItem> loadItems() {
        new DatabaseAsyncTask().execute(FIREBASEURL);

        return null;
    }

    @Override
    public void createItem(MaintenanceItem item) {

    }

    @Override
    public void updateItem(MaintenanceItem item) {

    }

    @Override
    public void deleteItem(MaintenanceItem item) {

    }

    @Override
    public void createTask(MaintenanceTask task) {

    }

    @Override
    public void updateTask(MaintenanceTask task) {

    }

    @Override
    public void deleteTask(MaintenanceTask task) {

    }
}
