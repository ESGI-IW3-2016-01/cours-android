package com.ninebaguettes.antoine.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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

public class MainActivity extends Activity {

    private ListView demoListView;
    private static final String NAME_KEY = "name";
    private static final String NAME_EMAIL = "email";
    private static final String DESTINATION_KEY = "destination";
    private static final String DESTINATION_MESSAGE = "message";
    private static final String MY_KEY = "cle";
    private SimpleAdapter simpleAdapter;
    private List<HashMap<String, String>> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            String message = intent.getStringExtra(Intent.EXTRA_TEXT);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MY_KEY, "HELLO WORLD");

        setContentView(R.layout.activity_list);

        demoListView = (ListView) findViewById(R.id.listView);
        Button button = (Button) findViewById(R.id.button);

        simpleAdapter = new SimpleAdapter(this,
                data,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{DESTINATION_KEY, DESTINATION_MESSAGE},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

        demoListView.setAdapter(simpleAdapter);
        button.setText("Refresh");
    }

    public void clickHandler(View view)
    {
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "Text to share");
//        sendIntent.setType("text/plain");
//        startActivity(sendIntent);
        String url = "http://api-ratp.pierre-grimaud.fr/v2/bus/80/stations/mairie+du+15+e?destination=mairie+du+18eme";
        String url2 = "http://api-ratp.pierre-grimaud.fr/v2/bus/80/stations/bucarest?destination=porte+de+versailles";
        new ApiRequestTask().execute(url,url2);
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

    public class ApiRequestTask extends AsyncTask<String, Void, List<HashMap<String, String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(String... params) {
            String file = downloadUrl(params[0]);
            String file2 = downloadUrl(params[1]);

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
            }
            return schedulesList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> scheduleList) {
            data.clear();
            data.addAll(scheduleList);
            simpleAdapter.notifyDataSetChanged();
        }

        private String downloadUrl(String uri)
        {
            String file = "Error";
            try {
                URL url = new URL(uri);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                file = convertInputStreamToString(inputStream);
            } catch (IOException e) {
                Log.d("IOError", "Erreur à l'ouverture du fichier");
            } catch (Exception e) {
                Log.d("Exception", "Erreure Fatale");
            }
            return file;
        }
    }

}