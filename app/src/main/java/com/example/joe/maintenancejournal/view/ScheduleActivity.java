package com.example.joe.maintenancejournal.view;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.example.joe.maintenancejournal.R;
import com.example.joe.maintenancejournal.controller.DataMgr;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleActivity extends BaseActivity {

    private List<String> thingsList = null;
    private ListView thingsListView = null;
    private ArrayAdapter<String> thingsArrayAdapter;

    private String[] drawerListViewItems;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private EditText fromText;
    private EditText toText;
    private SimpleDateFormat sdf;
    private Date fromSelected = null;
    private Date toSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //Get list of items for navigation drawer menu
        drawerListViewItems = getResources().getStringArray(R.array.screens);

        //Get the listview in the navigation drawer
        drawerListView = (ListView) findViewById(R.id.left_drawer);

        //Set the adapter for the navigation drawer's list
        drawerListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerListViewItems));

        //Get the drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);

        //Create the drawer open/close toggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        );

        //Create the listener for the items in the navigation drawer
        drawerListView.setOnItemClickListener(new ScheduleActivity.DrawerItemClickListener());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Set the list source to the item's tasks
        thingsList = DataMgr.GetTasks();

        //Update the list view with a list adapter
        thingsListView = (ListView) findViewById(R.id.list_of_tasks);

        thingsArrayAdapter = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_list_item_1, thingsList);

        thingsListView.setAdapter(thingsArrayAdapter);

        sdf = new SimpleDateFormat("MM/dd/yyyy");

        fromText = (EditText) findViewById(R.id.date_from);
        toText = (EditText) findViewById(R.id.date_to);

        fromText.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();

                DatePickerDialog myDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        newDate.set(Calendar.HOUR, 0);
                        fromText.setText(sdf.format(newDate.getTime()));
                        fromSelected = newDate.getTime();
                        setSchedule();
                    }

                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                if(toSelected != null)
                    myDialog.getDatePicker().setMaxDate(toSelected.getTime());

                myDialog.show();
            }
        });

        toText.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();

                DatePickerDialog myDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        newDate.set(Calendar.HOUR, 23);
                        newDate.set(Calendar.MINUTE, 59);
                        newDate.set(Calendar.SECOND, 59);
                        toText.setText(sdf.format(newDate.getTime()));
                        toSelected = newDate.getTime();
                        setSchedule();
                    }

                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                if(fromSelected != null)
                    myDialog.getDatePicker().setMinDate(fromSelected.getTime());

                myDialog.show();
            }
        });

        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Schedule Viewed"));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setSchedule()
    {
        thingsList.clear();

        if(fromSelected == null)
        {
            thingsList.addAll(DataMgr.GetTasks());
        }
        else if(toSelected == null)
        {
            thingsList.addAll(DataMgr.GetFilteredTasks(fromSelected));
        }
        else
        {
            thingsList.addAll(DataMgr.GetFilteredTasks(fromSelected, toSelected));
        }

        if(thingsArrayAdapter != null) {
            thingsArrayAdapter.notifyDataSetChanged();
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id)
        {
            String selectedText = (String)((TextView) view).getText();

            if(selectedText == drawerListViewItems[0])
            {
                Intent intent = new Intent(view.getContext(), MainActivity.class);

                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ScheduleActivity.this).toBundle());
            }
            else if (selectedText == drawerListViewItems[2]){

                Intent intent = new Intent(view.getContext(), OptionsActivity.class);

                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ScheduleActivity.this).toBundle());
            }

            drawerLayout.closeDrawer(drawerListView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
