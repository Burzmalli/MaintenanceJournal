package com.example.joe.maintenancejournal.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Joe on 3/25/2017.
 */
@IgnoreExtraProperties
public class TaskEntry {
    public String TaskName;
    public Date EntryDate;
    public double EntryCost;
    public MaintenanceTask ParentTask;
    public String Uuid;
    public String Notes;

    public TaskEntry() {

    }

    public TaskEntry(String name, Date date, double cost, String taskId, String uuid,
                     String notes) {
        TaskName = name;
        EntryDate = date;
        EntryCost = cost;
        Uuid = uuid;
        Notes = notes;
        //TODO: Assign ParentTask based on taskId
    }

    //Set the string value for display in lists
    public String toString()
    {
        return TaskName + " : " + getShortDate() + " : $" + EntryCost;
    }

    public String getShortDate()
    {
        //Format the date to something short
        SimpleDateFormat sd =  new SimpleDateFormat("MM/dd/yyyy");
        return sd.format(EntryDate);
    }
}
