package com.example.joe.maintenancejournal.controller;

import android.content.Context;

import com.example.joe.maintenancejournal.App;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Joe on 10/5/2016.
 */

public class ConfigMgr implements IConfigMgr{

    public static Configuration Configuration = new Configuration();
    private final String configFile = "config.txt";

    @Override
    public void loadConfiguration()
    {
        FileInputStream fsr = null;
        ObjectInputStream osr = null;

        try {

            Context context = App.sharedInstance.getApplicationContext();

            //Clear the existing items
            Configuration = new Configuration();

            //Attempt to open the save file into an object input stream
            fsr = context.openFileInput(configFile);
            osr = new ObjectInputStream(fsr);

            Configuration.EnableNotifications = osr.readBoolean();

            //Close streams
            osr.close();
            fsr.close();
        }
        catch(Exception e)
        {
            try {
                if (osr != null)
                    osr.close();

                if (fsr != null)
                    fsr.close();
            }
            catch(Exception f)
            {
                return;
            }
        }
    }

    @Override
    public void saveConfiguration()
    {
        FileOutputStream fsw = null;
        ObjectOutputStream osw = null;

        try {
            Context context = App.sharedInstance.getApplicationContext();

            //Open a fileoutputstream for writing
            fsw = context.openFileOutput(configFile, Context.MODE_PRIVATE);
            //Open an objectoutputstream with the file stream
            osw = new ObjectOutputStream(fsw);
            //write each object to the file
            osw.writeBoolean(Configuration.EnableNotifications);

            //close the streams
            osw.close();
            fsw.close();
        } catch (FileNotFoundException e) {
            return;
        }
        catch(Exception e)
        {
            try {
                if (osw != null)
                    osw.close();

                if (fsw != null)
                    fsw.close();
            }
            catch(Exception f)
            {
                return;
            }
        }
    }
}
