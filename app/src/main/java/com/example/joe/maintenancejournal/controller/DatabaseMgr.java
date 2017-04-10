package com.example.joe.maintenancejournal.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.joe.maintenancejournal.model.MaintenanceItem;
import com.example.joe.maintenancejournal.model.MaintenanceTask;
import com.example.joe.maintenancejournal.model.TaskEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseMgr extends SQLiteOpenHelper implements IItemSvc {

    private static final String DBNAME = "maintenance.db";
    private static final int DBVERSION = 1;

    public DatabaseMgr(Context context)
    {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createItemTable = "create table item (id integer primary key autoincrement, " +
                "name text not null, description text, synced integer, uuid text, onlineid integer)";

        String createTaskTable = "create table task (id integer primary key autoincrement, " +
                "name text not null, cost real, date real, recurring integer, synced integer, " +
                "itemId integer, frequencyType text, frequency integer, uuid text, onlineid integer, " +
                "foreign key(itemId) references item(id))";

        String createEntryTable = "create table entry (id integer primary key autoincrement, " +
                "name text, notes text, cost real, date real, itemId integer, taskId integer, " +
                "synced integer, uuid text, onlineid integer, foreign key(taskId) references task(id))";

        db.execSQL(createItemTable);
        db.execSQL(createTaskTable);
        db.execSQL(createEntryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists task");
        db.execSQL("drop table if exists item");
        db.execSQL("drop table if exists entry");
        onCreate(db);
    }

    @Override
    public void loadItems() {
        DataMgr.Items = getAllItems();
    }

    private MaintenanceItem getItem(Cursor cursor)
    {
        MaintenanceItem item = new MaintenanceItem();

        item.ItemId = cursor.getInt(0);
        item.ItemName = cursor.getString(1);
        item.ItemDescription = cursor.getString(2);
        item.Synced = cursor.getInt(3) == 1;
        item.Uuid = cursor.getString(4);
        item.OnlineId = cursor.getInt(5);
        item.Tasks.addAll(getTasksForItem(item.ItemId));
        item.inDb = true;

        return item;
    }

    private List<MaintenanceTask> getTasksForItem(int itemId)
    {
        List<MaintenanceTask> tasks = new ArrayList<MaintenanceTask>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("task",
                new String[] {"id", "name", "cost", "date", "recurring", "itemId", "synced",
                        "frequencyType", "frequency", "uuid", "onlineid"},
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
        task.StartDate = new Date(cursor.getLong(3));
        task.Recurring = cursor.getInt(4) == 1;
        task.ItemId = cursor.getInt(5);
        task.Synced = cursor.getInt(6) == 1;
        task.FrequencyType = cursor.getString(7);
        task.Frequency = cursor.getInt(8);
        task.Uuid = cursor.getString(9);
        task.OnlineId = cursor.getInt(10);
        task.Entries = getEntriesForTask(task.TaskId);
        task.inDb = true;

        return task;
    }

    private List<TaskEntry> getEntriesForTask(int taskId)
    {
        List<TaskEntry> entries = new ArrayList();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("entry",
                new String[] {"id", "name", "cost", "date", "notes", "itemId", "taskId", "synced", "uuid", "onlineid"},
                "taskId = ?", new String[] { String.valueOf(taskId)}, null, null, null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {
            TaskEntry entry = getEntry(cursor);
            entries.add(entry);
            cursor.moveToNext();
        }

        cursor.close();

        return entries;
    }

    private TaskEntry getEntry(Cursor cursor)
    {
        TaskEntry entry = new TaskEntry();

        entry.TaskId = cursor.getInt(0);
        entry.TaskName = cursor.getString(1);
        entry.EntryCost = cursor.getDouble(2);
        entry.EntryDate = new Date(cursor.getLong(3));
        entry.Notes = cursor.getString(4);
        entry.ItemId = cursor.getInt(5);
        entry.TaskId = cursor.getInt(6);
        entry.Synced = cursor.getInt(7) == 1;
        entry.Uuid = cursor.getString(8);
        entry.OnlineId = cursor.getInt(9);
        entry.inDb = true;

        return entry;
    }

    public void createItem(MaintenanceItem item)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", item.ItemName);
        values.put("description", item.ItemDescription);
        values.put("synced", item.Synced ? 1 : 0);
        values.put("uuid", item.Uuid);
        values.put("onlineid", item.OnlineId);

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

    public void updateItem(MaintenanceItem item) {

        SQLiteDatabase db = getReadableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", item.ItemName);
        values.put("description", item.ItemDescription);
        values.put("synced", item.Synced ? 1 : 0);
        values.put("uuid", item.Uuid);
        values.put("onlineid", item.OnlineId);

        db.update("item", values,
                "id = ?", new String[] {String.valueOf(item.ItemId)});

        for(MaintenanceTask task : item.Tasks)
        {
            if(task.TaskId < 0)
                createTask(task);
        }

        db.close();
    }

    public void deleteItem(MaintenanceItem item) {

        for(MaintenanceTask task : item.Tasks)
        {
            deleteTask(task);
        }

        SQLiteDatabase db = getReadableDatabase();

        db.delete("item", "id = ?", new String[] {String.valueOf(item.ItemId)});

        db.close();
    }

    public void createTask( MaintenanceTask task)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", task.TaskName);
        values.put("cost", task.TaskCost);
        values.put("date", task.StartDate.getTime());
        values.put("recurring", task.Recurring ? 1 : 0);
        values.put("itemId", task.ItemId);
        values.put("synced", task.Synced ? 1 : 0);
        values.put("uuid", task.Uuid);
        values.put("onlineid", task.OnlineId);

        task.TaskId = (int)db.insert("task", null, values);

        db.close();
    }

    public void updateTask(MaintenanceTask task) {

        SQLiteDatabase db = getReadableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", task.TaskName);
        values.put("cost", task.TaskCost);
        values.put("date", task.StartDate.getTime());
        values.put("recurring", task.Recurring ? 1 : 0);
        values.put("itemId", task.ItemId);
        values.put("synced", task.Synced ? 1 : 0);
        values.put("uuid", task.Uuid);
        values.put("onlineid", task.OnlineId);

        db.update("task", values,
                "id = ?", new String[] {String.valueOf(task.TaskId)});

        db.close();
    }

    public void deleteTask(MaintenanceTask task) {
        SQLiteDatabase db = getReadableDatabase();

        for(TaskEntry entry : task.Entries) {
            deleteEntry(entry);
        }

        db.delete("task", "id = ?", new String[] {String.valueOf(task.TaskId)});

        db.close();
    }

    public void createEntry( TaskEntry entry)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", entry.TaskName);
        values.put("cost", entry.EntryCost);
        values.put("date", entry.EntryDate.getTime());
        values.put("notes", entry.Notes);
        values.put("itemId", entry.ItemId);
        values.put("taskId", entry.TaskId);
        values.put("synced", entry.Synced ? 1 : 0);
        values.put("uuid", entry.Uuid);
        values.put("onlineid", entry.OnlineId);

        entry.EntryId = (int)db.insert("entry", null, values);

        db.close();
    }

    public void updateEntry(TaskEntry entry) {

        SQLiteDatabase db = getReadableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", entry.TaskName);
        values.put("cost", entry.EntryCost);
        values.put("date", entry.EntryDate.getTime());
        values.put("notes", entry.Notes);
        values.put("itemId", entry.ItemId);
        values.put("taskId", entry.TaskId);
        values.put("synced", entry.Synced ? 1 : 0);
        values.put("uuid", entry.Uuid);
        values.put("onlineid", entry.OnlineId);

        db.update("entry", values,
                "id = ?", new String[] {String.valueOf(entry.EntryId)});

        db.close();
    }

    public void deleteEntry(TaskEntry entry) {
        SQLiteDatabase db = getReadableDatabase();

        db.delete("entry", "id = ?", new String[] {String.valueOf(entry.EntryId)});

        db.close();
    }

    @Override
    public void SetContext(Context context) {

    }

    @Override
    public List<MaintenanceItem> getAllItems() {
        List<MaintenanceItem> items = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("item",
                new String[] {"id", "name", "description", "synced", "uuid", "onlineid"},
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

    @Override
    public List<MaintenanceTask> getAllTasks() {
        List<MaintenanceTask> tasks = new ArrayList<MaintenanceTask>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("task",
                new String[] {"id", "name", "cost", "date", "recurring", "itemId", "synced",
                        "frequencyType", "frequency", "uuid"},
                null, null, null, null, null);

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

    @Override
    public List<TaskEntry> getAllEntries() {
        List<TaskEntry> entries = new ArrayList();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("entry",
                new String[] {"id", "name", "cost", "date", "notes", "itemId", "taskId", "synced", "uuid"},
                null, null, null, null, null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {
            TaskEntry entry = getEntry(cursor);
            entries.add(entry);
            cursor.moveToNext();
        }

        cursor.close();

        return entries;
    }
}
