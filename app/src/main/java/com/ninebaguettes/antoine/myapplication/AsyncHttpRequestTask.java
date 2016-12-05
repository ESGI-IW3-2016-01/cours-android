package com.ninebaguettes.antoine.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncHttpRequestTask extends AsyncTask<String, Void, JSONObject> {

    private IHTTPRequestListener ihttpRequestListener;

    public AsyncHttpRequestTask(IHTTPRequestListener ihttpRequestListener) {
        this.ihttpRequestListener = ihttpRequestListener;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        String file = getURL(params[0], null);
        String file2 = getURL(params[1], null);

        JSONObject jsonObject;
        List<HashMap<String, String>> schedulesList = new ArrayList<>();

        try {
            jsonObject = new JSONObject(file);
            JSONArray schedules = jsonObject.optJSONObject("response").optJSONArray("schedules");
            Schedule schedule;

            for (int i = 0; i < schedules.length(); i++) {
                JSONObject temp = (JSONObject) schedules.get(i);
                String destination = "Mairie du XV -> " + temp.optString("destination");
                String message = temp.optString("message");

                schedule = new Schedule(destination, message);
                schedulesList.add(schedule.toHashMap());
            }

            jsonObject = new JSONObject(file2);
            schedules = jsonObject.optJSONObject("response").optJSONArray("schedules");

            for (int i = 0; i < schedules.length(); i++) {
                JSONObject temp = (JSONObject) schedules.get(i);
                String destination = "Bucarest -> " + temp.optString("destination");
                String message = temp.optString("message");

                schedule = new Schedule(destination, message);
                schedulesList.add(schedule.toHashMap());
            }

        } catch (JSONException e) {
            Log.d("Error","JSON Error");
            jsonObject = null;
        }

        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        this.ihttpRequestListener.onSuccess(jsonObject);
    }

    private String getURL(String url, HashMap<String, String> queryParams)
    {
        String response = "";
        try {
            URL uri = new URL(url);

            HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setDoInput(true);
            urlConnection.connect();

            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            response = convertInputStreamToString(inputStream);

        } catch (Exception e) {
            response = e.getMessage();
            Log.d(e.getClass().toString(), e.getMessage());
        }
        return response;
    }

    private String convertInputStreamToString(InputStream inputStream)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
