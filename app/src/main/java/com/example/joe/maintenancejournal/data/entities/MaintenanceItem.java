package com.example.joe.maintenancejournal.data.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 9/08/2016.
 */
public class MaintenanceItem implements java.io.Serializable{
    public String ItemName;
    public String ItemDescription;
    public List<MaintenanceTask> Tasks = new ArrayList<MaintenanceTask>();
    public int ImgId = 0;
    public int ItemId = -1; //Value for ID in database. -1 indicates unsaved item.

    //Set the string value for display in lists
    public String toString()
    {
        return ItemName;
    }

    //Gets a summary for future functionality
    public String GetSummary()
    {
        return "";
    }
}
