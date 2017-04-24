package com.example.joe.maintenancejournal.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Comparator;

/**
 * Created by Joe on 9/08/2016.
 */
@IgnoreExtraProperties
public class MaintenanceItem implements java.io.Serializable, Comparable<MaintenanceItem>{
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

    @Override
    public int compareTo(@NonNull MaintenanceItem o) {
        return ItemName.toLowerCase().compareTo(o.ItemName.toLowerCase());
    }
}
