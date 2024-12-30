package com.wireless.adb;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

public class Helpers {
    public static String executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return reader.readLine();
        } catch (IOException e) {
            Log.e("Wireless ADB App | Execute Command", String.format("Error executing command | %s", command), e);
        }
        return null;
    }

    public static String getData(String key, Context context, String fileName) {
        try {
            Properties properties = new Properties();;
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);
            properties.load(inputStream);
            return properties.getProperty(key);
        } catch (IOException e) {
            Log.e("Wireless ADB App | Get Data", "Error reading property file", e);
        }
        return null;
    }

    public static String[] getDateTime() {
        SimpleDateFormat sfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sfTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String currentDate = sfDate.format(date);
        String currentTime = sfTime.format(date);
        return new String[]{currentDate, currentTime};
    }
}
