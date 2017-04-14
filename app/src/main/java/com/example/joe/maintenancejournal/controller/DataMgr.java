package com.example.joe.maintenancejournal.controller;

import android.app.Activity;

import com.example.joe.maintenancejournal.model.MaintenanceItem;
import com.example.joe.maintenancejournal.model.MaintenanceTask;
import com.example.joe.maintenancejournal.model.TaskEntry;
import com.example.joe.maintenancejournal.view.JournalCardAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe on 9/08/2016.
 * A global item master for storing the app's maintenance items
 */
public class DataMgr{
    public static List<MaintenanceItem> Items = new ArrayList<>();
    public static JournalCardAdapter.MaintenanceItemHolder lastClicked = null;
    public static Activity mainActivity;
    public static ConfigMgr ConfigMgr = new ConfigMgr();
    private static boolean initialized = false;
    private static MaintenanceDataProvider myProvider;

    static {
        myProvider = MaintenanceDataProvider.sharedInstance;
    }

    public static void CancelPendingRequests() {

    }

    public static int GetItemPosition(MaintenanceItem itm) {
        if(!Items.contains(itm))
            return -1;

        return Items.indexOf(itm);
    }

    public static void InitialLoad()
    {
        if(!initialized) {
            ConfigMgr.loadConfiguration();
            LoadItems();
            initialized = true;
        }
    }

    public static void LoadItems()
    {
        //Items = myManager.loadItems();
        myProvider.loadItems();
    }

    public static void saveItem(MaintenanceItem item)
    {
        myProvider.createItem(item, MaintenanceDataProvider.SyncType.BOTH);
    }

    public static int GetGapIndex() {
        int gap = 0;

        for(MaintenanceItem itm : Items)
            if(itm.ItemId == gap) gap++;

        return gap;
    }

    public static MaintenanceItem findTaskOwner(MaintenanceTask task) {

        if(task.ItemId < Items.size())
            return Items.get(task.ItemId);

        for(MaintenanceItem item : Items) {
            if(item.Tasks.contains(task))
                return item;
        }

        return null;
    }

    public static void updateItem(MaintenanceItem item)
    {
        myProvider.updateItem(item, MaintenanceDataProvider.SyncType.BOTH);
    }

    public static void deleteItem(MaintenanceItem item)
    {
        myProvider.deleteItem(item, MaintenanceDataProvider.SyncType.BOTH);
    }

    public static void saveTask(MaintenanceTask task)
    {
        myProvider.createTask(task, MaintenanceDataProvider.SyncType.BOTH);
    }

    public static void updateTask(MaintenanceTask task)
    {
        myProvider.updateTask(task, MaintenanceDataProvider.SyncType.BOTH);
    }

    public static void deleteTask(MaintenanceTask task)
    {
        myProvider.deleteTask(task, MaintenanceDataProvider.SyncType.BOTH);
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

    public static MaintenanceItem GetItemFromName( String testName )
    {
        for(MaintenanceItem itm : Items)
        {
            if(testName == itm.ItemName)
                return itm;
        }

        return null;
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

    public static MaintenanceTask getNextTask()
    {
        MaintenanceTask nextTask = null;

        Calendar calendar = Calendar.getInstance();

        for(MaintenanceItem itm : Items )
        {
            for(MaintenanceTask task : itm.Tasks)
            {
                if(nextTask == null)
                    nextTask = task;

                if(task.StartDate.before(nextTask.StartDate) && task.StartDate.after(calendar.getTime()))
                    nextTask = task;
            }
        }

        return nextTask;
    }
}
