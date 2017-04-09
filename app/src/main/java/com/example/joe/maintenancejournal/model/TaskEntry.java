package com.example.joe.maintenancejournal.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Joe on 3/25/2017.
 */

public class TaskEntry {
    public String TaskName;
    public Date EntryDate;
    public double EntryCost;
    public int TaskId;
    public int ItemId;
    public int EntryId = -1;
    public String Notes;

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
