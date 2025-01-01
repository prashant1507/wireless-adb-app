package com.wireless.adb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent serviceIntent = new Intent(this, WirelessADBService.class);
        startService(serviceIntent);

        // ToDo: Add a button on the Main Activity which will send details on demand
        // Close the activity immediately
//        finish();
    }


}
