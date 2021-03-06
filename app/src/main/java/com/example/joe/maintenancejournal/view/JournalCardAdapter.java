package com.example.joe.maintenancejournal.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joe.maintenancejournal.App;
import com.example.joe.maintenancejournal.Constants;
import com.example.joe.maintenancejournal.R;
import com.example.joe.maintenancejournal.controller.DataMgr;
import com.example.joe.maintenancejournal.controller.DataUpdateReceiver;
import com.example.joe.maintenancejournal.model.MaintenanceItem;
import com.example.joe.maintenancejournal.model.MaintenanceTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jowillia on 9/16/2016.
 */
public class JournalCardAdapter extends RecyclerView.Adapter<JournalCardAdapter.MaintenanceItemHolder> {

    private List<MaintenanceItem> itemList;
    private View itemView;
    private ArrayAdapter<MaintenanceTask> thingsArrayAdapter;
    private boolean Registered;
    private static int lastClickedPos = -1;

    public JournalCardAdapter(List<MaintenanceItem> itemList)
    {
        this.itemList = itemList;
    }

    @Override
    public MaintenanceItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_layout, parent, false);

        RegisterForUpdate();

        return new MaintenanceItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MaintenanceItemHolder holder, int position) {
        MaintenanceItem item = itemList.get(position);
        holder.mItemImg.setId(item.ImgId);
        holder.mItemName.setText(item.ItemName);
        holder.mItemSummary.setText(item.GetSummary());

        thingsArrayAdapter = new ArrayAdapter<>(itemView.getContext(),
                android.R.layout.simple_list_item_1, DataMgr.GetItemTasks(item.Key));

        holder.mTaskList.setAdapter(thingsArrayAdapter);

        RegisterForUpdate();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private void RegisterForUpdate() {
        if(!Registered) {
            IntentFilter ifilter = new IntentFilter("com.example.joe.maintenancejournal.DATA_UPDATED");

            App.sharedInstance.registerReceiver(onEvent, ifilter);
            Registered = true;
        }
    }

    private DataUpdateReceiver onEvent=new DataUpdateReceiver() {
        public void onReceive(Context ctxt, Intent i) {

            updateView();
        }
    };

    private void UnregisterForUpdate() {
        if(Registered) {
            App.sharedInstance.unregisterReceiver(onEvent);
            Registered = false;
        }
    }

    public void updateView()
    {
        if(MaintenanceItemHolder.editing)
        {
            thingsArrayAdapter.notifyDataSetChanged();
        }
        else
        {

            notifyDataSetChanged();
        }
    }

    public static class MaintenanceItemHolder extends RecyclerView.ViewHolder {
        private ImageView mItemImg;
        private TextView mItemName;
        private TextView mItemSummary;
        private ListView mTaskList;
        private EditText mEditName;
        private MaintenanceItem mHeldItem;

        private static int lastSelected = -1;

        private Button mAddTaskBtn;
        private Button mDeleteTaskBtn;
        private Button mDeleteItemBtn;
        private FloatingActionButton mEditItemBtn;
        private Button mSaveChangesBtn;
        private Button mCancelChangesBtn;
        private TextInputLayout mItemNameInput;
        //private Button mAddImgBtn;

        private boolean expanded = false;
        public static boolean editing = false;

        private MaintenanceTask selectedTask;

        public MaintenanceItemHolder(View itemView) {
            super(itemView);

            mItemImg = (ImageView) itemView.findViewById(R.id.item_image);
            mItemName = (TextView) itemView.findViewById(R.id.item_name);
            mItemSummary = (TextView) itemView.findViewById(R.id.item_summary);
            mTaskList = (ListView) itemView.findViewById(R.id.list_of_tasks);
            mEditName = (EditText) itemView.findViewById(R.id.text_item_name);
            mAddTaskBtn = (Button) itemView.findViewById(R.id.button_add_task);
            mDeleteTaskBtn = (Button) itemView.findViewById(R.id.button_delete_task);
            mEditItemBtn = (FloatingActionButton) itemView.findViewById(R.id.fab);
            mSaveChangesBtn = (Button) itemView.findViewById(R.id.button_save_changes);
            //mAddImgBtn = (Button) itemView.findViewById(R.id.button_add_image);
            mDeleteItemBtn = (Button) itemView.findViewById(R.id.button_delete_item);
            mCancelChangesBtn = (Button) itemView.findViewById(R.id.button_cancel_item);
            mItemNameInput = (TextInputLayout) itemView.findViewById(R.id.item_name_layout);

            mEditName.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!s.toString().matches("[a-zA-Z0-9 ]*")) mItemNameInput.setError(App.sharedInstance.getString(R.string.item_name_nospecial));
                    else mItemNameInput.setError(null);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            mTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    selectedTask = DataMgr.GetItemTasks(mHeldItem.Key).get(position);

                    if(editing) {
                        view.setSelected(true);
                        mDeleteTaskBtn.setVisibility(View.VISIBLE);
                    } else {
                        Intent performIntent = new Intent(view.getContext(), PerformMaintenanceActivity.class);
                        performIntent.putExtra(Constants.ITEM_KEY, mHeldItem.Key);
                        performIntent.putExtra(Constants.TASK_KEY, selectedTask.Key);
                        view.getContext().startActivity(performIntent);
                        ((Activity)view.getContext()).overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    if(editing) {
                        AlertDialog.Builder dg = new AlertDialog.Builder(v.getContext());
                        dg.setIcon(android.R.drawable.ic_dialog_alert);
                        dg.setTitle("Cancel Edit");
                        dg.setMessage("Selecting another item will discard any changes to the currently selected item. " +
                                "Are you sure you want to select another item?");
                        dg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ExpandCard();
                            }

                        });
                        dg.setNegativeButton("No", null);
                        dg.show();
                    }
                    else
                    {
                        if(expanded)
                            CollapseCard();
                        else
                            ExpandCard();
                    }
                }
            });

            mEditItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    StartEdit();
                }
            });

            mSaveChangesBtn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    if(mItemNameInput.getError() != null) {
                        return;
                    }

                    if(!DataMgr.isNameUnique(mEditName.getText().toString()))
                    {
                        new AlertDialog.Builder(v.getContext())
                                .setTitle(v.getContext().getString(R.string.alert_title_unique_name))
                                .setMessage(String.format(v.getContext().getString(R.string.error_unique_name), mEditName.getText().toString()))
                                .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })

                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                        return;
                    }

                    EndEdit(false);
                }
            });

            mAddTaskBtn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    //Create the intent to call the task creation screen
                    Intent intent = new Intent(v.getContext(), CreateTaskActivity.class);

                    if(mHeldItem == null)
                        mHeldItem = DataMgr.GetItemFromName(mItemName.getText().toString());

                    //Pass the index for the item that will get the new task
                    intent.putExtra(Constants.ITEM_KEY, mHeldItem.Key);

                    //Open the screen
                    v.getContext().startActivity(intent);
                    ((Activity)v.getContext()).overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
                }
            });

            /*mAddImgBtn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    ((MainActivity) DataMgr.mainActivity).selectImage();

                    Bitmap img = DataMgr.selectedImage;

                    if(img == null)
                        return;

                    mItemImg.setImageBitmap(img);
                }
            });*/

            mDeleteTaskBtn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    AlertDialog.Builder dg = new AlertDialog.Builder(v.getContext());
                    dg.setIcon(android.R.drawable.ic_dialog_alert);
                    dg.setTitle(v.getContext().getString(R.string.alert_delete_task_title));
                    dg.setMessage(String.format(v.getContext().getString(R.string.alert_delete_task_msg),
                            selectedTask.TaskName));
                    dg.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DataMgr.DeleteTask(selectedTask);
                        }

                    });
                    dg.setNegativeButton("No", null);
                    dg.show();
                }
            });

            mDeleteItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    AlertDialog.Builder dg = new AlertDialog.Builder(v.getContext());
                    dg.setIcon(android.R.drawable.ic_dialog_alert);
                    dg.setTitle(v.getContext().getString(R.string.alert_delete_item_title));
                    dg.setMessage(String.format(v.getContext().getString(R.string.alert_delete_item_msg),
                            mHeldItem.ItemName));
                    dg.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DataMgr.DeleteItem(mHeldItem);
                            mHeldItem = null;
                            CollapseCard();
                        }
                    });
                    dg.setNegativeButton("No", null);
                    dg.show();
                }
            });

            mCancelChangesBtn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    if(editing) {
                        AlertDialog.Builder dg = new AlertDialog.Builder(v.getContext());
                        dg.setIcon(android.R.drawable.ic_dialog_alert);
                        dg.setTitle("Cancel Edit");
                        dg.setMessage("Are you sure you want to discard changes?");
                        dg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CollapseCard();
                            }

                        });
                        dg.setNegativeButton("No", null);
                        dg.show();
                    }
                    else
                    {
                        if(expanded)
                            CollapseCard();
                        else
                            ExpandCard();
                    }
                }
            });
        }

        public void ExpandCard()
        {
            mHeldItem = DataMgr.GetItemFromName(mItemName.getText().toString());

            if(DataMgr.LastClicked != null && DataMgr.LastClicked != this)
                DataMgr.LastClicked.CollapseCard();

            mTaskList.setVisibility(View.VISIBLE);
            mEditItemBtn.setVisibility(View.VISIBLE);

            DataMgr.LastClicked = this;

            expanded = true;
        }

        public void CollapseCard()
        {
            EndEdit(true);
            mTaskList.setVisibility(View.GONE);
            mAddTaskBtn.setVisibility(View.GONE);
            mDeleteTaskBtn.setVisibility(View.GONE);
            mEditItemBtn.setVisibility(View.GONE);
            DataMgr.LastClicked = null;
            expanded = false;
        }

        public void StartEdit()
        {
            if(mHeldItem == null)
                mHeldItem = DataMgr.GetItemFromName(mItemName.getText().toString());

            if(mHeldItem == null)
                return;

            mSaveChangesBtn.setVisibility(itemView.VISIBLE);
            mEditItemBtn.setVisibility(itemView.GONE);
            mItemName.setVisibility(itemView.GONE);
            mItemNameInput.setVisibility(itemView.VISIBLE);
            mAddTaskBtn.setVisibility(itemView.VISIBLE);
            //mAddImgBtn.setVisibility(itemView.VISIBLE);
            mDeleteItemBtn.setVisibility(itemView.VISIBLE);
            mCancelChangesBtn.setVisibility(itemView.VISIBLE);

            if(mHeldItem.ItemName.isEmpty()) {
                mEditName.setTextColor(Color.GRAY);
                mEditName.selectAll();
            }
            else {
                mEditName.setText(mHeldItem.ItemName);
                mEditName.setTextColor(Color.BLACK);
                mEditName.requestFocus();
            }

            editing = true;
        }

        public void EndEdit(boolean isCanceled)
        {
            if(!isCanceled) {
                if(mHeldItem == null)
                    mHeldItem = DataMgr.GetItemFromName(mItemName.getText().toString());

                if(mHeldItem == null)
                    return;

                mHeldItem.ItemName = mEditName.getText().toString();
                mItemName.setText(mHeldItem.ItemName);
                DataMgr.UpdateItem(mHeldItem);

                List<MaintenanceTask> tasks = DataMgr.GetItemTasks(mHeldItem.Key);

                for(MaintenanceTask task : tasks) {
                    if(!task.Saved) {
                        task.Saved = true;
                        DataMgr.UpdateTask(task);
                    }
                }
            }
            else
            {
                if(mHeldItem != null) {
                    ArrayList<MaintenanceTask> removeTasks = new ArrayList<>();

                    for (MaintenanceTask task : DataMgr.GetItemTasks(mHeldItem.Key)) {
                        if (!task.Saved)
                            removeTasks.add(task);
                    }

                    for (MaintenanceTask task : removeTasks) {

                        DataMgr.DeleteTask(task);
                    }

                    removeTasks.clear();
                }
            }

            selectedTask = null;
            mTaskList.setSelected(false);
            mDeleteTaskBtn.setVisibility(View.GONE);

            mAddTaskBtn.setVisibility(itemView.GONE);
            mSaveChangesBtn.setVisibility(itemView.GONE);
            mItemNameInput.setVisibility(itemView.GONE);
            mItemName.setVisibility(itemView.VISIBLE);
            mEditItemBtn.setVisibility(itemView.VISIBLE);
            //mAddImgBtn.setVisibility(itemView.GONE);
            mDeleteItemBtn.setVisibility(itemView.GONE);
            mCancelChangesBtn.setVisibility(itemView.GONE);

            editing = false;
        }
    }
}
