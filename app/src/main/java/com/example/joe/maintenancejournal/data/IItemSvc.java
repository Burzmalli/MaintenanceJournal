package com.example.joe.maintenancejournal.data;

import android.content.Context;

import com.example.joe.maintenancejournal.data.entities.MaintenanceItem;
import com.example.joe.maintenancejournal.data.entities.MaintenanceTask;

import java.util.List;

/**
 * Created by jowillia on 10/6/2016.
 */

public interface IItemSvc {

    List<MaintenanceItem> loadItems();
    void createItem(MaintenanceItem item);
    void updateItem(MaintenanceItem item);
    void deleteItem(MaintenanceItem item);
    void createTask(MaintenanceTask task);
    void updateTask(MaintenanceTask task);
    void deleteTask(MaintenanceTask task);
    void SetContext(Context context);
}
