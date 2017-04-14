package com.example.joe.maintenancejournal.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public MaintenanceItem ParentItem;
    public String TaskDescription = "";
    public String Uuid;
    public List<TaskEntry> Entries = new ArrayList<>();

    public MaintenanceTask() {

    }

    public MaintenanceTask(String name, Date date, double cost, boolean recurring, String frequencyType,
                           int frequency, String parentId, String description, String uuid) {
        TaskName = name;
        StartDate = date;
        TaskCost = cost;
        Recurring = recurring;
        FrequencyType = frequencyType;
        Frequency = frequency;
        //TODO: Assign ParentItem based on parentId
        TaskDescription = description;
        Uuid = uuid;
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
