package com.example.joe.maintenancejournal.controller;

import com.example.joe.maintenancejournal.model.MaintenanceItem;
import com.example.joe.maintenancejournal.model.MaintenanceTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jowillia on 4/14/2017.
 */

public class DataMgr {
    public static List<MaintenanceItem> Items = new ArrayList<>();
    public static ConfigMgr ConfigMgr = new ConfigMgr();
    private static DatabaseReference mDatabase;
    private static FirebaseDatabase mInstance;

    static {
        mInstance = FirebaseDatabase.getInstance();
        mInstance.setPersistenceEnabled(true);
        mDatabase = mInstance.getReference();
        mDatabase.keepSynced(true);
    }

    public static void LoadItems() {
        DatabaseReference itemRef = mDatabase.child("items");

        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Items.clear();

                GenericTypeIndicator<Map<String, MaintenanceItem>> itemList = new GenericTypeIndicator<Map<String, MaintenanceItem>>() {};

                Map<String, MaintenanceItem> items = dataSnapshot.getValue(itemList);

                if(items == null) return;
                //MaintenanceItem item = dataSnapshot.getValue(MaintenanceItem.class);
                for(Map.Entry<String, MaintenanceItem> entry : items.entrySet()) {
                    Items.add(entry.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public static MaintenanceItem GetItemFromName( String testName )
    {
        for(MaintenanceItem itm : Items)
        {
            if(testName == itm.ItemName)
                return itm;
        }

        return null;
    }

    public static boolean isNameUnique( String testName)
    {
        for(MaintenanceItem itm : Items)
        {
            if(testName.contentEquals(itm.ItemName))
                return false;
        }

        return true;
    }

    //Returns all tasks
    public static ArrayList<String> GetTasks()
    {
        ArrayList<String> tasks = new ArrayList<String>();
        for(MaintenanceItem itm : Items )
        {
            for(MaintenanceTask task : itm.Tasks)
            {
                tasks.add(itm.ItemName + " : " + task.TaskName + " : " + task.getShortDate() + " : $" + task.TaskCost);
            }
        }

        return tasks;
    }

    //Returns a list of tasks after the from date
    public static ArrayList<String> GetFilteredTasks( Date from )
    {
        ArrayList<String> tasks = new ArrayList<String>();
        for(MaintenanceItem itm : Items )
        {
            for(MaintenanceTask task : itm.Tasks)
            {
                if(task.StartDate.equals(from) || task.StartDate.after(from))
                    tasks.add(itm.ItemName + " : " + task.TaskName + " : " + task.getShortDate() + " : $" + task.TaskCost);
            }
        }

        return tasks;
    }

    //Returns a list of tasks between the from and to dates
    public static ArrayList<String> GetFilteredTasks( Date from, Date to )
    {
        ArrayList<String> tasks = new ArrayList<String>();
        for(MaintenanceItem itm : Items )
        {
            for(MaintenanceTask task : itm.Tasks)
            {
                if((task.StartDate.equals(from) || task.StartDate.after(from))
                        && (task.StartDate.equals(to) || task.StartDate.before(to)))
                    tasks.add(itm.ItemName + " : " + task.TaskName + " : " + task.getShortDate() + " : $" + task.TaskCost);
            }
        }

        return tasks;
    }
}
