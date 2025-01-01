package com.wireless.adb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) ||
                Intent.ACTION_USER_PRESENT.equals(intent.getAction()) ||
                Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())) {

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent activityIntent = new Intent(context, WirelessADBService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(activityIntent);
                } else {
                    context.startService(activityIntent);
                }
                Log.i("Wireless ADB App | Boot Completed", "Starting Wireless ADB Service");
            }, 3000);
        }
    }
}
