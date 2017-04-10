package com.example.joe.maintenancejournal.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe on 9/08/2016.
 */
public class MaintenanceTask implements java.io.Serializable{
    public String TaskName = "";
    public Date StartDate;
    public double TaskCost;
    public boolean Recurring;
    public String FrequencyType = "";
    public int Frequency;
    public int ItemId;
    public int TaskId = -1;
    public boolean Synced = false;
    public String TaskDescription = "";
    public String Uuid;
    public List<TaskEntry> Entries = new ArrayList<>();
    public boolean inDb = false;
    public int OnlineId = -1;

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

    public int GetEntryGapIndex() {
        int gap = 0;

        for(TaskEntry entry : Entries) {
            if(entry.OnlineId == gap)
                gap++;
        }

        return gap;
    }
}
