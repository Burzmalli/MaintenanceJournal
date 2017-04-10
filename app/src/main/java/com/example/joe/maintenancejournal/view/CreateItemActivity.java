package com.example.joe.maintenancejournal.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.joe.maintenancejournal.controller.DataMgr;
import com.example.joe.maintenancejournal.model.MaintenanceItem;
import com.example.joe.maintenancejournal.R;

import java.util.UUID;

public class CreateItemActivity extends BaseActivity {

    protected MaintenanceItem myItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Create a new item when screen is created and add it to the item master
        myItem = new MaintenanceItem();

        myItem.ItemName = "temp";
        myItem.Uuid = UUID.randomUUID().toString();

        myItem.OnlineId = DataMgr.GetGapIndex();

        DataMgr.Items.add(myItem);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        //Remove temporary new item if creation is canceled by pressing Back
        if(DataMgr.Items.contains(myItem))
            DataMgr.Items.remove(myItem);

        super.onBackPressed();
    }
}
