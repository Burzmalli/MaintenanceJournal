package com.example.joe.maintenancejournal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Joe on 4/1/2017.
 */

public class Global {
    public static Date StrToDate(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(str);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return convertedDate;
    }
}
