package com.example.joe.maintenancejournal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * Created by Joe on 9/08/2016.
 */
public class MaintenanceTask implements java.io.Serializable{
    public String TaskName;
    public Date TaskDate;
    public double TaskCost;
    public boolean Recurring;
    public int ItemId;
    public int TaskId = -1;

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
