package com.davyuu.app1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = new DatabaseHelper(this);

        /*try {
            dbHelper.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void openList(View view){
        Intent intent = new Intent(getApplicationContext(), ListActivity.class);
        startActivity(intent);
    }

    public void openSearch(View view){
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(intent);
    }

    public void openCustomize(View view){
        Intent intent = new Intent(getApplicationContext(), CustomizeActivity.class);
        startActivity(intent);
    }

    public void openListener(View view){
        Intent intent = new Intent(getApplicationContext(), ListenerActivity.class);
        startActivity(intent);
    }

    public void openDetector(View view){
        Intent intent = new Intent(getApplicationContext(), DetectorActivity.class);
        startActivity(intent);
    }
}
