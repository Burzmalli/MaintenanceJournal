package com.example.joe.maintenancejournal.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joe.maintenancejournal.controller.DataMgr;
import com.example.joe.maintenancejournal.R;
import com.example.joe.maintenancejournal.controller.DataUpdateReceiver;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView thingsListView = null;
    private RecyclerView.Adapter<JournalCardAdapter.MaintenanceItemHolder> thingsArrayAdapter;
    private BroadcastReceiver br = new DataUpdateReceiver();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Create and set adapter for cardview in recyclerview
        thingsListView = (RecyclerView) rootView.findViewById(R.id.list_of_things);

        thingsListView.setHasFixedSize(true);

        LinearLayoutManager mgr = new LinearLayoutManager(getActivity());
        mgr.setOrientation(LinearLayoutManager.VERTICAL);
        thingsListView.setLayoutManager(mgr);

        thingsArrayAdapter = new JournalCardAdapter(DataMgr.Items);

        thingsListView.setAdapter(thingsArrayAdapter);

        IntentFilter ifilter = new IntentFilter("com.example.joe.maintenancejournal.DATA_UPDATED");

        getActivity().registerReceiver(onEvent, ifilter);

        return rootView;
    }

    private DataUpdateReceiver onEvent=new DataUpdateReceiver() {
        public void onReceive(Context ctxt, Intent i) {

            UpdateList();
        }
    };

    @Override
    public void onResume()
    {
        //Update the list of items when the user comes back to this screen from creating an item
        UpdateList();

        super.onResume();
    }

    @Override
    public void onStop() {
        //LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(onEvent);
        getActivity().unregisterReceiver(onEvent);
        super.onStop();
    }

    public void UpdateList() {
        if(thingsArrayAdapter != null) {
            thingsArrayAdapter.notifyDataSetChanged();
        }
    }
}
