package com.example.joe.maintenancejournal.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Joe on 3/25/2017.
 */
@IgnoreExtraProperties
public class TaskEntry {
    public String TaskName;
    public Date EntryDate;
    public double EntryCost;
    public String ParentKey;
    public String Key;
    public String Notes;
    public boolean Saved;

    public TaskEntry() {

    }

    public TaskEntry(String name, Date date, double cost, String taskKey, String key,
                     String notes, boolean saved) {
        TaskName = name;
        EntryDate = date;
        EntryCost = cost;
        Key = key;
        Notes = notes;
        ParentKey = taskKey;
        Saved = saved;
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
