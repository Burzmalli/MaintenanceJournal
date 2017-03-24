package com.example.joe.maintenancejournal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class CreateItemActivity extends AppCompatActivity {

    protected MaintenanceItem myItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Create a new item when screen is created and add it to the item master
        myItem = new MaintenanceItem();

        myItem.ItemName = "temp";

        GlobalMgr.Items.add(myItem);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        //Remove temporary new item if creation is canceled by pressing Back
        if(GlobalMgr.Items.contains(myItem))
            GlobalMgr.Items.remove(myItem);

        super.onBackPressed();
    }
}
