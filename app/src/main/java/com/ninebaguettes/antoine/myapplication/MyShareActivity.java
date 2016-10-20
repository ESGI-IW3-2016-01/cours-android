package com.ninebaguettes.antoine.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Antoine on 19/10/2016.
 */

public class MyShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String string = intent.getStringExtra(Intent.EXTRA_TEXT);
        Log.d("log", "SHARING: " + string);

        Intent intent1 = new Intent(this, MainActivity.class);
        intent1.putExtra(Intent.EXTRA_TEXT, "Sharing OK");
        startActivity(intent1);
    }

    private void finishWithResult()
    {
        Bundle conData = new Bundle();
        conData.putString("name", "Bala");
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
    }
}
