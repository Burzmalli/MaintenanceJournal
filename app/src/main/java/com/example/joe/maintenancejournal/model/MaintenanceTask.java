package com.example.joe.maintenancejournal.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Joe on 9/08/2016.
 */
@IgnoreExtraProperties
public class MaintenanceTask implements java.io.Serializable{
    public String TaskName = "";
    public Date StartDate;
    public double TaskCost;
    public boolean Recurring;
    public String FrequencyType = "";
    public int Frequency;
    public String ParentKey = "";
    public String TaskDescription = "";
    public String Key;
    public boolean Saved;

    public MaintenanceTask() {

    }

    public MaintenanceTask(String name, Date date, double cost, boolean recurring, String frequencyType,
                           int frequency, String parentKey, String description, String key, boolean saved) {
        TaskName = name;
        StartDate = date;
        TaskCost = cost;
        Recurring = recurring;
        FrequencyType = frequencyType;
        Frequency = frequency;
        TaskDescription = description;
        ParentKey = parentKey;
        Key = key;
        Saved = saved;
    }

    //Set the string value for display in lists
    public String toString()
    {
        return TaskName + " : " + getShortDate() + " : $" + TaskCost;
    }

    public String getShortDate()
    {
        //Format the date to something short
        SimpleDateFormat sd =  new SimpleDateFormat("MM/dd/yyyy");
        return sd.format(StartDate);
    }
}
