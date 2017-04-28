package com.example.joe.maintenancejournal.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.example.joe.maintenancejournal.R;
import com.example.joe.maintenancejournal.controller.DataMgr;
import com.example.joe.maintenancejournal.model.MaintenanceItem;

public class CreateItemActivity extends BaseActivity {

    protected MaintenanceItem myItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Create a new item when screen is created and add it to the item master
        myItem = new MaintenanceItem();

        myItem.ItemName = "temp";

        DataMgr.CreateItem(myItem);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Create Item Activity Displayed"));
    }

    @Override
    public void onBackPressed() {
        //Remove temporary new item if creation is canceled by pressing Back
        if(!myItem.Saved) {

            DataMgr.DeleteItem(myItem);
        }

        super.onBackPressed();
    }
}
