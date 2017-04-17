package com.example.joe.maintenancejournal.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.joe.maintenancejournal.Constants;
import com.example.joe.maintenancejournal.R;
import com.example.joe.maintenancejournal.controller.DataMgr;
import com.example.joe.maintenancejournal.model.MaintenanceItem;
import com.example.joe.maintenancejournal.model.MaintenanceTask;
import com.example.joe.maintenancejournal.model.TaskEntry;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.UUID;

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

        String itemKey = intent.getStringExtra(Constants.ITEM_KEY);
        String taskKey = intent.getStringExtra(Constants.TASK_KEY);

        //Get the item based on the index
        myItem = DataMgr.GetItemFromKey(itemKey);
        myTask = DataMgr.GetTaskFromKey(taskKey);

        //Get the view for later use
        myView = inflater.inflate(R.layout.fragment_perform_maintenance, container, false);

        final EditText costText = (EditText) myView.findViewById(R.id.text_entry_cost);
        final EditText notesText = (EditText) myView.findViewById(R.id.entry_notes);

        //Set the button functionality when saving the task
        Button saveBtn = (Button) myView.findViewById(R.id.button_save_entry);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(costText.getText().toString().isEmpty())
                    return;

                TaskEntry entry = new TaskEntry();
                entry.TaskName = myTask.TaskName;
                entry.Key = UUID.randomUUID().toString();

                String cleanString = costText.getText().toString().replaceAll("[$,]", "");

                double parsed = Double.parseDouble(cleanString);

                //Set a default task cost (functionality coming next time)
                entry.EntryCost = parsed;

                //Get the date from the date picker and set the task date to it
                DatePicker picker = (DatePicker) myView.findViewById(R.id.picker_entry_date);

                Calendar cal = Calendar.getInstance();
                cal.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());

                entry.EntryDate = cal.getTime();
                entry.Notes = notesText.getText().toString();

                DataMgr.CreateEntry(entry);

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
