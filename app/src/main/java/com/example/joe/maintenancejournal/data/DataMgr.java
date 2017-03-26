package com.example.joe.maintenancejournal.data;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.joe.maintenancejournal.R;
import com.example.joe.maintenancejournal.data.entities.MaintenanceItem;
import com.example.joe.maintenancejournal.data.entities.MaintenanceTask;
import com.example.joe.maintenancejournal.ux.JournalCardAdapter;
import com.example.joe.maintenancejournal.ux.MainActivity;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
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
    public static Bitmap selectedImage;
    private static String saveFilename = "items.txt";
    public static ConfigMgr ConfigMgr = new ConfigMgr();

    private static int mId;

    private static IItemSvc myManager;

    static {
        //myManager = new ItemFirebaseMgr();
        myManager = new ItemDatabaseMgr(mainActivity.getBaseContext());
    }

    public static void GlobalLoad()
    {
        ConfigMgr.loadConfiguration();
        LoadItems();
    }

    //Item reading method that uses the passed context for file IO
    public static void ReadSerializedItems(Context context) {
        FileInputStream fsr = null;
        ObjectInputStream osr = null;

        try {
            //Clear the existing items
            Items.clear();

            //Attempt to open the save file into an object input stream
            fsr = context.openFileInput(saveFilename);
            osr = new ObjectInputStream(fsr);

            //read the first object
            MaintenanceItem item = (MaintenanceItem) osr.readObject();

            //Read items until null
            while(item != null)
            {
                Items.add(item);
                item = (MaintenanceItem) osr.readObject();
            }

            //Close streams
            osr.close();
            fsr.close();
        }
        catch(Exception e)
        {
            try {
                if (osr != null)
                    osr.close();

                if (fsr != null)
                    fsr.close();
            }
            catch(Exception f)
            {
                return;
            }
        }
    }

    public static void LoadItems()
    {
        if(mainActivity == null)
            return;

        Items = myManager.loadItems();
    }

    public static void addItem(MaintenanceItem item)
    {
        Items.add(item);

        saveItem(item);
    }

    public static void saveItem(MaintenanceItem item)
    {
        if(mainActivity == null)
            return;

        myManager.createItem(item);
    }

    public static void updateItem(MaintenanceItem item)
    {
        myManager.updateItem(item);
    }

    public static void deleteItem(MaintenanceItem item)
    {
        Items.remove(item);

        myManager.deleteItem(item);
    }

    public static void saveTask(MaintenanceTask task)
    {
        if(mainActivity == null)
            return;

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
