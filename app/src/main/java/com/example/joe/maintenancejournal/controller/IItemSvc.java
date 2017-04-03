package com.example.joe.maintenancejournal.controller;

import android.content.Context;

import com.example.joe.maintenancejournal.model.MaintenanceItem;
import com.example.joe.maintenancejournal.model.MaintenanceTask;

import java.util.List;

/**
 * Created by jowillia on 10/6/2016.
 */

public interface IItemSvc {

    void loadItems();
    void createItem(MaintenanceItem item);
    void updateItem(MaintenanceItem item);
    void deleteItem(MaintenanceItem item);
    void createTask(MaintenanceTask task);
    void updateTask(MaintenanceTask task);
    void deleteTask(MaintenanceTask task);
    void SetContext(Context context);
}
