package com.example.joe.maintenancejournal.ux;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.joe.maintenancejournal.data.DataMgr;
import com.example.joe.maintenancejournal.data.entities.MaintenanceItem;
import com.example.joe.maintenancejournal.R;

public class CreateItemActivity extends BaseActivity {

    protected MaintenanceItem myItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Create a new item when screen is created and add it to the item master
        myItem = new MaintenanceItem();

        myItem.ItemName = "temp";

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
