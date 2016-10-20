package com.ninebaguettes.antoine.myapplication;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView demoListView;
    private static final String NAME_KEY = "name";
    private static final String NAME_EMAIL = "email";
    private static final String MY_KEY = "cle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            String message = intent.getStringExtra(Intent.EXTRA_TEXT);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

        List<HashMap<String, String>> userList = new ArrayList<>();
        User user;
        for (int i = 0; i < 10; i++) {
            user = new User("username-" + i, "lastname-" + i, "email-" + i);
            userList.add(user.toHashMap());
        }

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MY_KEY, "HELLO WORLD");

        setContentView(R.layout.activity_list);

        demoListView = (ListView) findViewById(R.id.listView);
        Button button = (Button) findViewById(R.id.button);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                userList,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{NAME_KEY, NAME_EMAIL},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

        try {
            InputStream inputStream = getAssets().open("ratp.json");
            String file = convertInputStreamToString(inputStream);
            JSONObject jsonObject = new JSONObject(file);
            String destination = jsonObject.optJSONObject("response").optJSONObject("informations").optJSONObject("destination").optString("name");
            editor.putString("destination",destination);
            editor.commit();
            Log.d("OKAY",destination);
        } catch (IOException e) {
            Log.d("IOError", "Erreur à l'ouverture du fichier");
        } catch (JSONException e) {
            Log.d("JSONError", "Erreur au parsing du fichier");
        }
        Log.d("OKAY", "Fichier ouvert et parsé");

        button.setText(sharedPreferences.getString("destination", "default"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Text to share");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        demoListView.setAdapter(simpleAdapter);
    }

    private String convertInputStreamToString(InputStream inputStream) {
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