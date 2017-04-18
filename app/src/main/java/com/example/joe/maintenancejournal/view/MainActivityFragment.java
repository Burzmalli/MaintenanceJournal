package com.example.joe.maintenancejournal.view;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joe.maintenancejournal.R;
import com.example.joe.maintenancejournal.controller.DataMgr;
import com.example.joe.maintenancejournal.controller.DataUpdateReceiver;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView itemCardRecycler = null;
    private RecyclerView.Adapter<JournalCardAdapter.MaintenanceItemHolder> itemHolderAdapter;

    boolean Registered = false;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DataMgr.LoadItems();



        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Create and set adapter for cardview in recyclerview
        itemCardRecycler = (RecyclerView) rootView.findViewById(R.id.list_of_things);

        itemCardRecycler.setHasFixedSize(true);

        LinearLayoutManager mgr = new LinearLayoutManager(getActivity());
        mgr.setOrientation(LinearLayoutManager.VERTICAL);
        itemCardRecycler.setLayoutManager(mgr);

        itemHolderAdapter = new JournalCardAdapter(DataMgr.Items);

        itemCardRecycler.setAdapter(itemHolderAdapter);

        RegisterForUpdate();

        return rootView;
    }

    private DataUpdateReceiver onEvent=new DataUpdateReceiver() {
        public void onReceive(Context ctxt, Intent i) {

            UpdateList();
        }
    };

    private void RegisterForUpdate() {
        if(!Registered) {
            IntentFilter ifilter = new IntentFilter("com.example.joe.maintenancejournal.DATA_UPDATED");

            getActivity().registerReceiver(onEvent, ifilter);
            Registered = true;
        }
    }

    private void UnregisterForUpdate() {
        if(Registered) {
            getActivity().unregisterReceiver(onEvent);
            Registered = false;
        }
    }

    @Override
    public void onResume()
    {
        //Update the list of items when the user comes back to this screen from creating an item
        UpdateList();

        RegisterForUpdate();

        super.onResume();
    }

    @Override
    public void onStop() {

        UnregisterForUpdate();

        super.onStop();
    }

    @Override
    public void onDestroy() {

        UnregisterForUpdate();

        super.onDestroy();
    }

    public void UpdateList() {
        if(itemHolderAdapter != null) {
            itemHolderAdapter.notifyDataSetChanged();
        }
    }
}
