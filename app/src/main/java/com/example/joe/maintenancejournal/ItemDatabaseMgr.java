package com.example.joe.maintenancejournal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jowillia on 10/6/2016.
 */

public class ItemDatabaseMgr extends SQLiteOpenHelper implements IItemSvc{

    private static final String DBNAME = "maintenance.db";
    private static final int DBVERSION = 1;

    public ItemDatabaseMgr(Context context)
    {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createItemTable = "create table item (id integer primary key autoincrement, " +
                "name text not null)";

        String createTaskTable = "create table task (id integer primary key autoincrement, " +
                "name text not null, cost real, date real, recurring integer, " +
                "itemId integer, foreign key(itemId) references item(id))";

        db.execSQL(createItemTable);
        db.execSQL(createTaskTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists task");
        db.execSQL("drop table if exists item");
        onCreate(db);
    }

    @Override
    public List<MaintenanceItem> loadItems() {
        List<MaintenanceItem> items = new ArrayList<MaintenanceItem>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("item",
                new String[] {"id", "name"},
                null, null, null, null, null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {
            MaintenanceItem item = getItem(cursor);
            items.add(item);
            cursor.moveToNext();
        }

        cursor.close();

        return items;
    }

    private MaintenanceItem getItem(Cursor cursor)
    {
        MaintenanceItem item = new MaintenanceItem();

        item.ItemId = cursor.getInt(0);
        item.ItemName = cursor.getString(1);
        item.Tasks.addAll(getTasksForItem(item.ItemId));

        return item;
    }

    private List<MaintenanceTask> getTasksForItem(int itemId)
    {
        List<MaintenanceTask> tasks = new ArrayList<MaintenanceTask>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("task",
                new String[] {"id", "name", "cost", "date", "recurring", "itemId"},
                "itemId = ?", new String[] { String.valueOf(itemId)}, null, null, null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {
            MaintenanceTask task = getTask(cursor);
            tasks.add(task);
            cursor.moveToNext();
        }

        cursor.close();

        return tasks;
    }

    private MaintenanceTask getTask(Cursor cursor)
    {
        MaintenanceTask task = new MaintenanceTask();

        task.TaskId = cursor.getInt(0);
        task.TaskName = cursor.getString(1);
        task.TaskCost = cursor.getDouble(2);
        task.TaskDate = new Date(cursor.getLong(3));
        task.Recurring = cursor.getInt(4) == 1;
        task.ItemId = cursor.getInt(5);

        return task;
    }

    @Override
    public void createItem(MaintenanceItem item)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", item.ItemName);

        item.ItemId = (int)db.insert("item", null, values);

        db.close();

        if(item.Tasks.size() > 0)
        {
            for( MaintenanceTask tsk : item.Tasks)
            {
                tsk.ItemId = item.ItemId;

                createTask( tsk );
            }
        }
    }

    @Override
    public void updateItem(MaintenanceItem item) {

        SQLiteDatabase db = getReadableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", item.ItemName);

        db.update("item", values,
                "id = ?", new String[] {String.valueOf(item.ItemId)});

        for(MaintenanceTask task : item.Tasks)
        {
            if(task.TaskId < 0)
                createTask(task);
        }

        db.close();
    }

    @Override
    public void deleteItem(MaintenanceItem item) {

        for(MaintenanceTask task : item.Tasks)
        {
            deleteTask(task);
        }

        SQLiteDatabase db = getReadableDatabase();

        db.delete("item", "id = ?", new String[] {String.valueOf(item.ItemId)});

        db.close();
    }

    @Override
    public void createTask( MaintenanceTask task)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", task.TaskName);
        values.put("cost", task.TaskCost);
        values.put("date", task.TaskDate.getTime());
        values.put("recurring", task.Recurring ? 1 : 0);
        values.put("itemId", task.ItemId);

        task.TaskId = (int)db.insert("task", null, values);

        db.close();
    }

    @Override
    public void updateTask(MaintenanceTask task) {

        SQLiteDatabase db = getReadableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", task.TaskName);
        values.put("cost", task.TaskCost);
        values.put("date", task.TaskDate.getTime());
        values.put("recurring", task.Recurring ? 1 : 0);
        values.put("itemId", task.ItemId);

        db.update("task", values,
                "id = ?", new String[] {String.valueOf(task.TaskId)});

        db.close();
    }

    @Override
    public void deleteTask(MaintenanceTask task) {
        SQLiteDatabase db = getReadableDatabase();

        db.delete("task", "id = ?", new String[] {String.valueOf(task.TaskId)});

        db.close();
    }
}
