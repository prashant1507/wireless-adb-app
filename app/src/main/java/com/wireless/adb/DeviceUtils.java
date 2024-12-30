package com.wireless.adb;

import static android.content.Context.WIFI_SERVICE;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import org.json.JSONObject;

public class DeviceUtils {

    public static String enableWirelessADB(Context context) {
        boolean isPortSet = false;
        String port = Helpers.getData("PORT", context, "data.properties");
        while (!isPortSet) {
            Helpers.executeCommand(String.format("su -c setprop service.adb.tcp.port %s", port));
            Helpers.executeCommand("su -c stop adbd");
            Helpers.executeCommand("su -c start adbd");
            String getPort = Helpers.executeCommand("getprop service.adb.tcp.port");
            assert getPort != null;
            assert port != null;
            if (port.equals(getPort)) {
                isPortSet = true;
            }
        }
        Log.i("Wireless ADB App | Enable Wireless ADB", String.format("Wireless ADB enabled on port %s", port));
        return port;
    }

    public static JSONObject getDeviceDetails(String adbPort, Context context) {
        JSONObject deviceDetails = new JSONObject();
        try {
            String serialNumber = Helpers.executeCommand("su -c getprop ro.serialno");
            String modelNumber = Build.MODEL;

            String brand = Build.BRAND.substring(0, 1).toUpperCase() + Build.BRAND.substring(1);
            String deviceName = Helpers.getData(modelNumber, context.getApplicationContext(), "devices.properties");
            assert deviceName != null;
            if (deviceName.isEmpty()) {
                deviceName = Settings.Global.getString(context.getContentResolver(), Settings.Global.DEVICE_NAME);
            }
            String version = Build.VERSION.RELEASE;
            String buildNumber = Build.DISPLAY.split("\\.")[3];
            String bootloader = Build.BOOTLOADER;
            String baseBandVersion = Build.getRadioVersion().split(",")[0];
            String securityPatch = Build.VERSION.SECURITY_PATCH;
            String imei1 = Helpers.executeCommand("su -c service call iphonesubinfo 1 s16 com.android.shell | cut -d \"'\" -f2 | grep -Eo '[0-9]' | xargs | sed 's/\\ //g'");
            String architecture = Build.SUPPORTED_ABIS[0];
            String chipset = Build.HARDWARE;
            String countryCode = Helpers.executeCommand("getprop ro.csc.country_code");
            String ipAddress = getWifiIpAddress(context);

            deviceDetails.put("serial_number", serialNumber);
            deviceDetails.put("model_number", modelNumber);
            deviceDetails.put("brand", brand);
            deviceDetails.put("device_name", deviceName);
            deviceDetails.put("os_version", version);
            deviceDetails.put("build_number", buildNumber);
            deviceDetails.put("bootloader", bootloader);
            deviceDetails.put("baseband_version", baseBandVersion);
            deviceDetails.put("security_patch", securityPatch);
            deviceDetails.put("imei_1", imei1);
            deviceDetails.put("architecture", architecture);
            deviceDetails.put("chipset", chipset);
            deviceDetails.put("country", countryCode);
            deviceDetails.put("ip_address", ipAddress);
            deviceDetails.put("adb_port", adbPort);
            deviceDetails.put("date", Helpers.getDateTime()[0]);
            deviceDetails.put("time", Helpers.getDateTime()[1]);

        } catch (Exception e) {
            Log.e("Wireless ADB App | Get Device Details", "Error retrieving device details", e);
        }
        return deviceDetails;
    }

    private static String getWifiIpAddress(Context context) {
        String ipAddress = null;
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            ipAddress = (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
        }
        return ipAddress;
    }
}
