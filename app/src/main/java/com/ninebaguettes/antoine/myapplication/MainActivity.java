package com.ninebaguettes.antoine.myapplication;

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

        // Créer 10 users
        // Créer une List des 10 users
        // Créer une HashMap de cette List
        List<HashMap<String, String>> userList = new ArrayList<>();
        User user;
        for (int i = 0; i < 10; i++) {
            user = new User("username-" + i, "lastname-" + i, "email-" + i);
            userList.add(user.toHashMap());
        }

        setContentView(R.layout.activity_list);

        demoListView = (ListView) findViewById(R.id.listView);
        Button button = (Button) findViewById(R.id.button);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice);
//        adapter.add("Jolan");
//        adapter.add("Antoine");
//        adapter.add("Alexandre");
//        adapter.add("aime");
//        adapter.add("déteste");
//        adapter.add("les");;
//        adapter.add("bites");
//        adapter.add("poils");

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                userList,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{NAME_KEY, NAME_EMAIL},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

//        demoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String val = (String) demoListView.getItemAtPosition(position);
//                Toast toast = Toast.makeText(getApplicationContext(), val, Toast.LENGTH_SHORT);
//                toast.show();
//                adapter.remove(val);
//                adapter.notifyDataSetChanged();
//            }
//        });

        button.setText("Supprimer");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray array = demoListView.getCheckedItemPositions();
                String val = "";
                for (int i = 0; i <= array.size(); i++) {
                    if (array.get(i)) {
                        val += " " + demoListView.getItemAtPosition(i);
                    }
                }
                if (val.length() > 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), val, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        });
        demoListView.setAdapter(simpleAdapter);
    }
}