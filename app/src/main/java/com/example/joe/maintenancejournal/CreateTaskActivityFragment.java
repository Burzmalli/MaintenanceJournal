package com.example.joe.maintenancejournal;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateTaskActivityFragment extends Fragment {

    private View myView;

    private MaintenanceItem myItem = null;

    public CreateTaskActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get the index, from the intent, of the item that will be getting the new task
        Intent intent = getActivity().getIntent();

        int itemIndex = intent.getIntExtra("itemIndex", 0);

        //Get the item based on the index
        myItem = GlobalMgr.Items.get(itemIndex);

        //Get the view for later use
        myView = inflater.inflate(R.layout.fragment_create_task, container, false);

        final EditText costText = (EditText) myView.findViewById(R.id.text_task_cost);

        //Set the button functionality when saving the task
        Button saveBtn = (Button) myView.findViewById(R.id.button_save_task);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get the textview with the task name and set the name to it
                TextView tv = (TextView)myView.findViewById(R.id.text_task_name);

                if(tv.getText().toString().isEmpty() || costText.getText().toString().isEmpty())
                    return;

                MaintenanceTask task = new MaintenanceTask();
                task.TaskName = tv.getText().toString();

                String cleanString = costText.getText().toString().replaceAll("[$,]", "");

                double parsed = Double.parseDouble(cleanString);

                //Set a default task cost (functionality coming next time)
                task.TaskCost = parsed;

                //Get the date from the date picker and set the task date to it
                DatePicker picker = (DatePicker) myView.findViewById(R.id.picker_task_date);

                Calendar cal = Calendar.getInstance();
                cal.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());

                task.TaskDate = cal.getTime();
                task.ItemId = myItem.ItemId;

                CheckedTextView ctv = (CheckedTextView) myView.findViewById(R.id.checkbox_recurring);
                task.Recurring = ctv.isChecked();

                //Add the task to the selected item's task list
                myItem.Tasks.add(task);

                //Close the screen
                getActivity().finish();
            }
        });

        final CheckedTextView ctv = (CheckedTextView) myView.findViewById(R.id.checkbox_recurring);
        ctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctv.isChecked())
                    ctv.setChecked(false);
                else
                    ctv.setChecked(true);

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
