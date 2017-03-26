package com.example.joe.maintenancejournal.ux;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joe.maintenancejournal.data.DataMgr;
import com.example.joe.maintenancejournal.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView thingsListView = null;
    private RecyclerView.Adapter<JournalCardAdapter.MaintenanceItemHolder> thingsArrayAdapter;

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

        return rootView;
    }

    @Override
    public void onResume()
    {
        //Update the list of items when the user comes back to this screen from creating an item
        if(thingsArrayAdapter != null) {
            //thingsArrayAdapter.notifyDataSetChanged();
            ((JournalCardAdapter) thingsArrayAdapter).updateView();
        }

        super.onResume();
    }
}
