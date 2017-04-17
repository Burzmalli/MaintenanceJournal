package com.example.joe.maintenancejournal.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joe.maintenancejournal.Constants;
import com.example.joe.maintenancejournal.R;
import com.example.joe.maintenancejournal.controller.DataMgr;
import com.example.joe.maintenancejournal.controller.DataUpdateReceiver;
import com.example.joe.maintenancejournal.model.MaintenanceItem;
import com.example.joe.maintenancejournal.model.MaintenanceTask;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateItemActivityFragment extends Fragment {

    private View myView;

    private MaintenanceItem myItem = null;

    private List<MaintenanceTask> thingsList = null;
    private ListView thingsListView = null;
    private ArrayAdapter<MaintenanceTask> thingsArrayAdapter;

    private boolean Registered = false;

    public CreateItemActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Store the view for later access
        myView = inflater.inflate(R.layout.fragment_create_item, container, false);

        //Get the item created by the parent activity
        myItem = ((CreateItemActivity)getActivity()).myItem;

        //Set the list source to the item's tasks
        thingsList = DataMgr.GetItemTasks(myItem.Key);

        //Update the list view with a list adapter
        thingsListView = (ListView) myView.findViewById(R.id.list_of_tasks);

        thingsArrayAdapter = new ArrayAdapter<MaintenanceTask>(myView.getContext(),
                android.R.layout.simple_list_item_1, thingsList);

        thingsListView.setAdapter(thingsArrayAdapter);

        //Set the button functionality for adding tasks
        Button btn = (Button) myView.findViewById(R.id.button_add_task);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create the intent to call the task creation screen
                Intent intent = new Intent(view.getContext(), CreateTaskActivity.class);

                //Pass the index for the item that will get the new task
                intent.putExtra(Constants.ITEM_KEY, myItem.Key);

                //Open the screen
                startActivity(intent);
            }
        });

        //Set the button functionality for the save button
        Button saveBtn = (Button) myView.findViewById(R.id.button_save_item);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Find the name text view and assign the text to the created item
                TextView tv = (TextView)myView.findViewById(R.id.text_item_name);

                if(!DataMgr.isNameUnique(tv.getText().toString()))
                {
                    new AlertDialog.Builder(getContext())
                            .setTitle(getString(R.string.alert_title_unique_name))
                            .setMessage(String.format(getString(R.string.error_unique_name), tv.getText().toString()))
                            .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    return;
                }

                if(myItem != null) {
                    myItem.ItemName = tv.getText().toString();
                    myItem.Saved = true;
                }

                for(MaintenanceTask task : DataMgr.GetItemTasks(myItem.Key)) {
                    if(!task.Saved) {
                        task.Saved = true;
                        DataMgr.UpdateTask(task);
                    }
                }

                DataMgr.UpdateItem(myItem);

                //Close the screen to return to the item list
                getActivity().finish();
            }
        });

        RegisterForUpdate();

        return myView;
    }

    private void RegisterForUpdate() {
        if(!Registered) {
            IntentFilter ifilter = new IntentFilter("com.example.joe.maintenancejournal.DATA_UPDATED");

            getActivity().registerReceiver(onEvent, ifilter);
            Registered = true;
        }
    }

    private DataUpdateReceiver onEvent=new DataUpdateReceiver() {
        public void onReceive(Context ctxt, Intent i) {

            UpdateTaskList();
        }
    };

    private void UnregisterForUpdate() {
        if(Registered) {
            getActivity().unregisterReceiver(onEvent);
            Registered = false;
        }
    }

    private void UpdateTaskList() {

        if(thingsList != null) {
            thingsList.clear();
            thingsList.addAll(DataMgr.GetItemTasks(myItem.Key));
        }

        if(thingsArrayAdapter != null)
            thingsArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        RegisterForUpdate();

        //Update the list of tasks when the user comes back to this screen
        UpdateTaskList();
    }

    @Override
    public void onStop() {
        UnregisterForUpdate();

        super.onStop();
    }

    @Override
    public void onDestroy() {
        UnregisterForUpdate();

        super.onDestroy();
    }
}
