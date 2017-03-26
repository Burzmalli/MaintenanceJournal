package com.example.joe.maintenancejournal.data.entities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe on 9/08/2016.
 */
public class MaintenanceTask implements java.io.Serializable{
    public String TaskName;
    public Date StartDate;
    public double TaskCost;
    public boolean Recurring;
    public String FrequencyType;
    public String Frequency;
    public int ItemId;
    public int TaskId = -1;
    public List<TaskEntry> Entries = new ArrayList<>();

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
