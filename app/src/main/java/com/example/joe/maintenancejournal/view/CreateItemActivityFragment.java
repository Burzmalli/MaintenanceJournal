package com.example.joe.maintenancejournal.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joe.maintenancejournal.controller.DataMgr;
import com.example.joe.maintenancejournal.model.MaintenanceItem;
import com.example.joe.maintenancejournal.model.MaintenanceTask;
import com.example.joe.maintenancejournal.R;

import java.util.List;
import java.util.UUID;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateItemActivityFragment extends Fragment {

    private View myView;

    private MaintenanceItem myItem = null;

    private List<MaintenanceTask> thingsList = null;
    private ListView thingsListView = null;
    private ArrayAdapter<MaintenanceTask> thingsArrayAdapter;

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
        thingsList = myItem.Tasks;

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
                intent.putExtra("itemIndex", DataMgr.Items.indexOf(myItem));

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

                if(myItem != null)
                    myItem.ItemName = tv.getText().toString();

                if(myItem.Uuid == null || myItem.Uuid.isEmpty())
                    myItem.Uuid = UUID.randomUUID().toString();

                DataMgr.saveItem(myItem);

                //Close the screen to return to the item list
                getActivity().finish();
            }
        });

        return myView;
    }

    @Override
    public void onResume()
    {
        //Update the list of tasks when the user comes back to this screen
        if(thingsArrayAdapter != null)
            thingsArrayAdapter.notifyDataSetChanged();

        super.onResume();
    }
}
