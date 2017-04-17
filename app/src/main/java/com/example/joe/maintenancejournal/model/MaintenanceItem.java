package com.example.joe.maintenancejournal.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Joe on 9/08/2016.
 */
@IgnoreExtraProperties
public class MaintenanceItem implements java.io.Serializable{
    public String ItemName = "";
    public String ItemDescription = "";
    public int ImgId = 0;
    public String Key;
    public boolean Saved;

    public MaintenanceItem() {

    }

    public MaintenanceItem(String itemname, String itemdescription, String key, boolean saved) {
        ItemName = itemname;
        ItemDescription = itemdescription;
        Key = key;
        Saved = saved;
    }

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
