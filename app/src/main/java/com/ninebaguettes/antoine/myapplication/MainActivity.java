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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView demoListView;
    private static final String NAME_KEY = "name";
    private static final String NAME_EMAIL = "email";

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

        Fragment fragment = new MyFirstFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.relative_fragment, fragment)
                .addToBackStack(null)
                .commit();

        setContentView(R.layout.activity_list);

        demoListView = (ListView) findViewById(R.id.listView);
        Button button = (Button) findViewById(R.id.button);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                userList,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{NAME_KEY, NAME_EMAIL},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
        button.setText("Partager");
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
}