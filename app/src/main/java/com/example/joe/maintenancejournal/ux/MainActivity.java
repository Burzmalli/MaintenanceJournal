package com.example.joe.maintenancejournal.ux;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joe.maintenancejournal.data.DataMgr;
import com.example.joe.maintenancejournal.data.entities.MaintenanceItem;
import com.example.joe.maintenancejournal.data.entities.MaintenanceTask;
import com.example.joe.maintenancejournal.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends BaseActivity {

    String userChoosenTask;
    int REQUEST_CAMERA = 0;
    int SELECT_FILE = 1;

    private String[] drawerListViewItems;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataMgr.mainActivity = this;

        DataMgr.InitialLoad();

        /*if(DataMgr.Items.size() < 1) {
            //Get previously saved items
            DataMgr.ReadSerializedItems(getBaseContext());

            //Create dummy item data if no data was read
            if (DataMgr.Items.size() < 1) {
                CreateDummyData();
            }
        }*/

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

        //drawerLayout.setDrawerListener(actionBarDrawerToggle);


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

    private void CreateDummyData()
    {
        MaintenanceItem firstItem = new MaintenanceItem();
        MaintenanceTask firstTask = new MaintenanceTask();

        firstItem.ItemName = "ExampleItem1";
        firstTask.TaskName = "ExampleTask1-1";
        firstTask.TaskCost = 3.55;
        firstTask.StartDate = Calendar.getInstance().getTime();

        firstItem.Tasks.add(firstTask);

        firstTask = new MaintenanceTask();
        firstTask.TaskName = "ExampleTask1-2";
        firstTask.TaskCost = 10.38;
        firstTask.StartDate = Calendar.getInstance().getTime();

        firstItem.Tasks.add(firstTask);

        DataMgr.addItem(firstItem);

        firstItem = new MaintenanceItem();
        firstTask = new MaintenanceTask();

        firstItem.ItemName = "ExampleItem2";
        firstTask.TaskName = "ExampleTask2-1";
        firstTask.TaskCost = 3.55;
        firstTask.StartDate = Calendar.getInstance().getTime();

        firstItem.Tasks.add(firstTask);

        firstTask = new MaintenanceTask();
        firstTask.TaskName = "ExampleTask2-2";
        firstTask.TaskCost = 10.38;
        firstTask.StartDate = Calendar.getInstance().getTime();

        firstItem.Tasks.add(firstTask);

        DataMgr.addItem(firstItem);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
//code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private Bitmap onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        DataMgr.selectedImage = bm;

        return bm;
    }

    private Bitmap onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        if(thumbnail == null)
            return thumbnail;

        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataMgr.selectedImage = thumbnail;

        return thumbnail;
    }

    public void selectImage() {
        final CharSequence[] items = { getString(R.string.source_from_camera), getString(R.string.source_from_gallery),
                getString(R.string.source_cancel) };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.title_add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(MainActivity.this);
                if (items[item].equals(getString(R.string.source_from_camera))) {
                    userChoosenTask=getString(R.string.source_from_camera);
                    if(result)
                        cameraIntent();
                } else if (items[item].equals(getString(R.string.source_from_gallery))) {
                    userChoosenTask=getString(R.string.source_from_gallery);
                    if(result)
                        galleryIntent();
                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context)
        {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
            {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }
}
