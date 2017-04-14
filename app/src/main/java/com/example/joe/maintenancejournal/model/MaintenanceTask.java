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
    public int ItemId;
    public int TaskId = -1;
    public String TaskDescription = "";
    public String Uuid;
    public List<TaskEntry> Entries = new ArrayList<>();

    public MaintenanceTask() {

    }

    public MaintenanceTask(String name, Date date, double cost, boolean recurring, String frequencyType,
                           int frequency, int itemId, int id, String description, String uuid) {
        TaskName = name;
        StartDate = date;
        TaskCost = cost;
        Recurring = recurring;
        FrequencyType = frequencyType;
        Frequency = frequency;
        ItemId = itemId;
        TaskId = id;
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
