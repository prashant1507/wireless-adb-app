package com.wireless.adb;

import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import android.annotation.SuppressLint;
import androidx.annotation.Nullable;
import org.json.JSONObject;


public class WirelessADBService extends Service {


    @SuppressLint("ForegroundServiceType")
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Getting Wireless ADB info", Toast.LENGTH_SHORT).show();
        developerModeStatus();
        batteryOptimizationStatus();
        isWifiConnected();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            try {
                String port = DeviceUtils.enableWirelessADB(getApplicationContext());
                JSONObject deviceDetails = DeviceUtils.getDeviceDetails(port, getApplicationContext());
                new SendDataToServer().sendPostRequest(deviceDetails.toString(), getApplicationContext());
            } catch (Exception e) {
                Log.e("Wireless ADB App | On Start Command", "Error enabling ADB or sending raw", e);
            }
        }).start();
        return START_STICKY;
    }

    private void developerModeStatus() {
        if (Settings.Secure.getInt(getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) == 0) {
            Toast.makeText(this, "Please Enable Developer Mode", Toast.LENGTH_SHORT).show();
            Log.e("Wireless ADB App | Developer Mode Status", "Developer Mode is not enabled");
        } else {
            Toast.makeText(this, "Developer Mode Enabled", Toast.LENGTH_SHORT).show();
            Log.i("Wireless ADB App | Developer Mode Status", "Developer Mode is Already Enabled");
        }
    }

    private void batteryOptimizationStatus() {
        String packageName = getPackageName();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (powerManager != null && !powerManager.isIgnoringBatteryOptimizations(packageName)) {
            boolean IS_BATTERY_OPTIMIZATION_DISABLED = false;
            Log.d("Battery Optimization Status", "Battery optimization is not disabled. Prompting user to disable it");
            @SuppressLint("BatteryLife") Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse(String.format("package:%s", packageName)));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            while (!IS_BATTERY_OPTIMIZATION_DISABLED) {
                powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                IS_BATTERY_OPTIMIZATION_DISABLED = powerManager.isIgnoringBatteryOptimizations(packageName);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Log.e("Wireless ADB App | Battery Optimization Status", "Failed to enable Battery Optimization", e);
                }
            }
        }
         else {
            Log.i("Wireless ADB App | Battery Optimization Status", "Battery optimization is already disabled for this app");
        }
    }

    private void isWifiConnected() {
        try {
            Thread.sleep(2000);
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
            boolean isConnected = networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            if (!isConnected) {
                Toast.makeText(this, "Wi-Fi not Connected", Toast.LENGTH_SHORT).show();
                Log.d("Wireless ADB App | WiFi Connection", "Wi-Fi not Connected");
            } else {
                Toast.makeText(this, "Wi-Fi is Connected", Toast.LENGTH_SHORT).show();
                Log.i("Wireless ADB App | WiFi Connection", "Wi-Fi is Connected");
            }
        } catch (InterruptedException e) {
            Log.e("Wireless ADB App | WiFi Connection", "Error while checking WiFi status", e);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Wireless ADB App | On Destroy", "Service stopped");
    }
}
