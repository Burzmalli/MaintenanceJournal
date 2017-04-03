package com.example.joe.maintenancejournal.controller;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.joe.maintenancejournal.R;
import com.example.joe.maintenancejournal.model.MaintenanceItem;
import com.example.joe.maintenancejournal.model.MaintenanceTask;
import com.example.joe.maintenancejournal.view.JournalCardAdapter;
import com.example.joe.maintenancejournal.view.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe on 9/08/2016.
 * A global item master for storing the app's maintenance items
 */
public class DataMgr {
    public static List<MaintenanceItem> Items = new ArrayList<>();
    public static JournalCardAdapter.MaintenanceItemHolder lastClicked = null;
    public static Activity mainActivity;
    public static ConfigMgr ConfigMgr = new ConfigMgr();
    private static boolean initialized = false;
    public static String DATA_UPDATE_COMPLETE = "data_updated";

    private static int mId;
    private Service dataSvc;

    private static IItemSvc myManager;

    static {
        myManager = new ItemFirebaseMgr();
    }

    public static void SetContext( Context context ) {
        myManager.SetContext(context);
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
        myManager.loadItems();
    }

    public static void saveItem(MaintenanceItem item)
    {
        myManager.createItem(item);
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
        myManager.updateItem(item);
    }

    public static void deleteItem(MaintenanceItem item)
    {
        myManager.deleteItem(item);
    }

    public static void saveTask(MaintenanceTask task)
    {
        myManager.createTask(task);
    }

    public static void updateTask(MaintenanceTask task)
    {
        myManager.updateTask(task);
    }

    public static void deleteTask(MaintenanceTask task)
    {
        myManager.deleteTask(task);
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

    //Test code from developer.android.com. Not yet customized and implemented.
    public static void SetNotification(String title, String text, Date date)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mainActivity)
                        .setSmallIcon(R.drawable.icon128)
                        .setContentTitle(title)
                        .setContentText(text);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(mainActivity, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mainActivity);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.

        AlarmManager mgr = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);

        mNotificationManager.notify(mId, mBuilder.build());
    }
}
