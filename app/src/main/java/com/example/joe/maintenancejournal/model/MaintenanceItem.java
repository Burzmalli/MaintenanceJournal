package com.example.joe.maintenancejournal.model;

import com.google.firebase.database.IgnoreExtraProperties;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 9/08/2016.
 */
@IgnoreExtraProperties
public class MaintenanceItem implements java.io.Serializable{
    public String ItemName = "";
    public String ItemDescription = "";
    public List<MaintenanceTask> Tasks = new ArrayList<>();
    public int ImgId = 0;
    public String Uuid;
    public int ItemId = -1; //Value for ID in database. -1 indicates unsaved item.

    public MaintenanceItem() {

    }

    public MaintenanceItem(String name, String description, int imgId, String uuid, int id) {
        ItemName = name;
        ItemDescription = description;
        ImgId = imgId;
        Uuid = uuid;
        ItemId = id;
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

    public String GetJSON() {
        String str = "";

        try {
            JSONObject obj = GetAsJSONObject();

            if(obj != null)
                str = obj.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return str;
    }

    public int GetTaskGapIndex() {
        int gap = 0;

        for(MaintenanceTask task : Tasks) {
            if(task.TaskId == gap)
                gap++;
        }

        return gap;
    }

    public JSONObject GetAsJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("itemname", ItemName);
            obj.put("itemdescription", ItemDescription);
            obj.put("uuid", Uuid);
            obj.put("itemid", ItemId);

            if(Tasks.size() > 0) {
                JSONArray tskArray = new JSONArray();

                int idStart = 0;

                for(MaintenanceTask task : Tasks) {
                    JSONObject tskObj = new JSONObject();
                    tskObj.put("taskname", task.TaskName);
                    tskObj.put("taskcost", task.TaskCost);
                    tskObj.put("taskdescription", task.TaskDescription);
                    tskObj.put("startdate", task.getShortDate());
                    tskObj.put("frequencytype", task.FrequencyType);
                    tskObj.put("frequency", task.Frequency);
                    tskObj.put("recurring", task.Recurring);
                    tskObj.put("uuid", task.Uuid);
                    tskObj.put("taskid", task.TaskId);

                    task.TaskId = idStart;
                    idStart++;

                    if(task.Entries.size() > 0) {
                        JSONArray entryArray = new JSONArray(task.Entries.size());

                        for(TaskEntry entry : task.Entries) {
                            JSONObject entryObj = new JSONObject();
                            entryObj.put("cost", entry.EntryCost);
                            entryObj.put("entrydate", entry.getShortDate());
                            entryObj.put("note", entry.Notes);
                            entryObj.put("uuid", entry.Uuid);
                            entryObj.put("entryid", entry.EntryId);

                            entryArray.put(entryObj);
                        }

                        tskObj.put("entries", entryArray);
                    }

                    tskArray.put(tskObj);
                }

                obj.put("itemtasks", tskArray);
            }

            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
