package com.example.joe.maintenancejournal;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Joe on 3/25/2017.
 */

public class TaskEntry {
    public MaintenanceTask Parent;
    public String TaskName;
    public Date TaskDate;
    public double TaskCost;
    public int ItemId;
    public int EntryId = -1;
    public String Notes;

    //Set the string value for display in lists
    public String toString()
    {
        return TaskName + " : " + getShortDate() + " : $" + TaskCost;
    }

    public String getShortDate()
    {
        //Format the date to something short
        SimpleDateFormat sd =  new SimpleDateFormat("MM/dd/yyyy");
        return sd.format(TaskDate);
    }
}
