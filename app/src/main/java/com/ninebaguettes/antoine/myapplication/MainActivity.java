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

public class MainActivity extends Activity implements IHTTPRequestListener {

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
        String url = "http://api-ratp.pierre-grimaud.fr/v2/bus/80/stations/mairie+du+15+e?destination=mairie+du+18eme";
        String url2 = "http://api-ratp.pierre-grimaud.fr/v2/bus/80/stations/bucarest?destination=porte+de+versailles";
        new AsyncHttpRequestTask(this).execute(url,url2);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        Log.d("Success", jsonObject.toString());
    }

    @Override
    public void onFailure(JSONObject jsonObject) {
        Log.d("Error", jsonObject.toString());
    }
}