package com.wireless.adb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SendDataToServer {
    public void sendPostRequest(String jsonObject, Context context) {
        new SendPostTask(jsonObject, context).execute();
    }

    private static class SendPostTask extends AsyncTask<Void, Void, String> {
        private final String jsonInputString;
        @SuppressLint("StaticFieldLeak")
        private final Context context;
        SendPostTask(String jsonInputString, Context context) {
            this.jsonInputString = jsonInputString;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String apiUrl = Helpers.getData("ENDPOINT", context, "data.properties");
            HttpURLConnection connection = null;
            try {
                Thread.sleep(5000);
                URL url = new URL(apiUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.i("Wireless ADB App | Send Data To Server", "Request Success");
                    return "Success";
                } else {
                    Log.d("Wireless ADB App | Send Data To Server", "Request Failed. Retrying!");
                    new SendDataToServer().sendPostRequest(jsonInputString, context);
                }
            } catch (Exception e) {
                Log.e("Wireless ADB App | Send Data To Server", "Error in sending request", e);
                return "Error";
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return "Error";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Success")) {
                Toast.makeText(context, "Request Send Successfully!", Toast.LENGTH_SHORT).show();
                Log.d("Wireless ADB App | POST Result", "Request Success");
            } else {
                Toast.makeText(context, "Request Un-successful. Retrying!", Toast.LENGTH_SHORT).show();
                Log.d("Wireless ADB App | POST Result", "Request Failed. Retrying!");
                new SendDataToServer().sendPostRequest(jsonInputString, context);
            }
        }
    }
}
