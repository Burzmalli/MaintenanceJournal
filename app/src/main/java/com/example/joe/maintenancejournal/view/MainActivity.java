package com.example.joe.maintenancejournal.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.joe.maintenancejournal.R;

public class MainActivity extends BaseActivity {

    private String[] drawerListViewItems;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private MainActivityFragment mainActivityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Get list of items for navigation drawer menu
        drawerListViewItems = getResources().getStringArray(R.array.screens);

        //Get the listview in the navigation drawer
        drawerListView = (ListView) findViewById(R.id.left_drawer);

        //Set the adapter for the navigation drawer's list
        drawerListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drawerListViewItems));

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
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Set button functionality for adding items
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreateItemActivity.class);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
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

    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id)
        {
            String selectedText = (String)((TextView) view).getText();

            if (selectedText == drawerListViewItems[1]){

                Intent intent = new Intent(view.getContext(), ScheduleActivity.class);

                startActivity(intent);
            }
            else if(selectedText == drawerListViewItems[2])
            {
                Intent intent = new Intent(view.getContext(), OptionsActivity.class);

                startActivity(intent);
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
