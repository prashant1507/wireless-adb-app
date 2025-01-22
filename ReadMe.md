# Android App for Enabling Wireless ADB for Rooted Devices

---
## Overview

This Android application is designed to automate enabling Wireless ADB on a rooted device.
It also gathers device-specific details and sends them to a Flask server for storage.
The app is designed to provide easy use for developers who frequently use Wireless ADB, ensuring all prerequisites are met and required information is seamlessly collected and transmitted.

---

## Features

1. **Enable Wireless ADB**
   - Automatically enables Wireless ADB on port `5555`.
   - Starts the service on boot.

2. **Prerequisite Checks**
   - Checks and notifies if **Developer Mode** is disabled.
   - Checks and prompts to disable **Battery Optimization** for the app.
   - Checks Wi-Fi connection status and notifies if disconnected.

3. **Device Information Collection**  
   The app gathers the following details:
   ```json
   {
       "serial_number": "R3CR30GAH6L",
       "model_number": "SM-G991B",
       "brand": "Samsung",
       "device_name": "Galaxy S21 5G",
       "os_version": "14",
       "build_number": "G991BXXS9FXBG",
       "bootloader": "G991BXXS9FXBG",
       "baseband_version": "G991BXXS9FXBG",
       "security_patch": "2024-03-01",
       "imei_1": "358957935511914",
       "architecture": "arm64-v8a",
       "chipset": "exynos2100",
       "country": "UAE",
       "ip_address": "192.168.0.244",
       "adb_port": "5555",
       "date": "2024-12-28",
       "time": "10:16:42"
   }
   ```

4. **Data Transmission**
   - Sends the collected details to a Flask server using a **POST** API.
   - Stores the details in a JSON file on the server.
   - The app sends data only once upon successful request.
   - If **POST** request fails then the app will re-try by itself.

---

## Pre-requirements

1. **Rooted Device**
   - The app requires root access to enable Wireless ADB.
   - Refer for [Rooting Samsung Devices](app/src/main/assets/Root_Samsung_Devices.md)

2. **Developer Mode**
   - Ensure Developer Mode is enabled on the device.
   - Enable 'Disable adb authorization timeout' in Settings -> Developer options

3. **Authentication**
   - 1st Time authentication is required from the machine where the device will be connected with Wireless ADB
   - Don't forget to mark as Remember always

4. Change the **Endpoint** or **Port**, update the following files:
- [data.properties](app/src/main/assets/data.properties)
- [network_security_config.xml](app/src/main/res/xml/network_security_config.xml)

---
## Installation and Usage

1. Install the APK on a rooted Android device.
2. Grant necessary permissions (e.g., root access).
3. Disable battery optimization for the app.
4. Connect the device to a Wi-Fi network.
5. Open the app to enable Wireless ADB.
6. Reboot the device to ensure the app starts automatically.
7. Collected device details will be sent to the configured Flask server.

---

## Debugging

**Execute below to get logs of the app** `adb logcat | grep 'Wireless ADB App'`

---

## Flask Server Setup

1. Install Python and Flask:
   ```
   pip install flask
   ```
2. Create a Flask server script to receive POST data:
   ```
    from flask import Flask, request, jsonify
    import json
    import os
    
    app = Flask(__name__)
    
    @app.route('/data', methods=['POST'])
    def receive_data():
    data = request.get_json()
    os.makedirs('devices', exist_ok=True)
    
        # Save to a JSON file
        with open(f"devices/{data['serial_number']}.json", 'w') as file:
            json.dump(data, file, indent=4)
        print(json.dumps(data, indent=4))
        return jsonify({"status": "success", "data": data}), 200
    
    @app.route('/hello', methods=['GET'])
    def say_hello():
    return "Hello", 200
    
    if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001)

   ```
3. Start the server:
   ```
   python app.py
   ```
4. Also Refer [Flask-App ReadMe](app/src/main/assets/flask-app/ReadMe.md)
---

## Notes

- This app is intended for use on **rooted devices** only.
- It is recommended to use the app on secure networks to avoid potential security risks.
- Modify the Flask server endpoint if needed.
- Models are taken from https://storage.googleapis.com/play_public/supported_devices.html
