package com.example.joe.maintenancejournal.ux;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joe.maintenancejournal.data.DataMgr;
import com.example.joe.maintenancejournal.data.entities.MaintenanceItem;
import com.example.joe.maintenancejournal.data.entities.MaintenanceTask;
import com.example.joe.maintenancejournal.R;
import com.example.joe.maintenancejournal.data.entities.TaskEntry;

import java.text.NumberFormat;
import java.util.Calendar;

/**
 * Created by Joe on 3/26/2017.
 */

public class PerformMaintenanceActivityFragment extends Fragment {
    private View myView;

    private MaintenanceItem myItem = null;
    private MaintenanceTask myTask = null;

    public PerformMaintenanceActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get the index, from the intent, of the item that will be getting the new task
        Intent intent = getActivity().getIntent();

        int itemIndex = intent.getIntExtra("itemIndex", 0);
        int taskIndex = intent.getIntExtra("taskIndex", 0);

        //Get the item based on the index
        myItem = DataMgr.Items.get(itemIndex);
        myTask = myItem.Tasks.get(taskIndex);

        //Get the view for later use
        myView = inflater.inflate(R.layout.fragment_perform_maintenance, container, false);

        final EditText costText = (EditText) myView.findViewById(R.id.text_entry_cost);
        final EditText notesText = (EditText) myView.findViewById(R.id.entry_notes);

        //Set the button functionality when saving the task
        Button saveBtn = (Button) myView.findViewById(R.id.button_save_entry);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get the textview with the task name and set the name to it
                TextView tv = (TextView)myView.findViewById(R.id.text_entry_name);

                if(tv.getText().toString().isEmpty() || costText.getText().toString().isEmpty())
                    return;

                TaskEntry entry = new TaskEntry();
                entry.TaskName = myTask.TaskName;

                String cleanString = costText.getText().toString().replaceAll("[$,]", "");

                double parsed = Double.parseDouble(cleanString);

                //Set a default task cost (functionality coming next time)
                entry.EntryCost = parsed;

                //Get the date from the date picker and set the task date to it
                DatePicker picker = (DatePicker) myView.findViewById(R.id.picker_task_date);

                Calendar cal = Calendar.getInstance();
                cal.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());

                entry.EntryDate = cal.getTime();
                entry.Notes = notesText.getText().toString();
                entry.ItemId = myItem.ItemId;
                entry.TaskId = myTask.TaskId;

                //Add the task to the selected item's task list
                myTask.Entries.add(entry);

                //Close the screen
                getActivity().finish();
            }
        });


        costText.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){

                    try {
                        costText.removeTextChangedListener(this);

                        String cleanString = s.toString().replaceAll("[$,.]", "");

                        double parsed = Double.parseDouble(cleanString);
                        String formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));

                        current = formatted;
                        costText.setText(formatted);
                        costText.setSelection(formatted.length());

                        costText.addTextChangedListener(this);
                    }
                    catch(Exception ex)
                    {
                        Toast.makeText(getContext(),
                                ex.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }


            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });

        return myView;
    }
}
