package com.example.joe.maintenancejournal.data;

import com.example.joe.maintenancejournal.data.entities.MaintenanceItem;
import com.example.joe.maintenancejournal.data.entities.MaintenanceTask;

import java.util.List;

/**
 * Created by jowillia on 10/6/2016.
 */

public interface IItemSvc {
    public List<MaintenanceItem> loadItems();
    public void createItem(MaintenanceItem item);
    public void updateItem(MaintenanceItem item);
    public void deleteItem(MaintenanceItem item);
    public void createTask(MaintenanceTask task);
    public void updateTask(MaintenanceTask task);
    public void deleteTask(MaintenanceTask task);
}
