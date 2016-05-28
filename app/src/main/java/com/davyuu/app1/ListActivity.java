package com.davyuu.app1;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private List<String> nameList;
    private ListView nameListView;
    private ArrayAdapter nameArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        nameListView = (ListView) findViewById(R.id.listView);

        final DatabaseHelper dbHelper = new DatabaseHelper(this);
        nameList = dbHelper.getName();
        nameArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nameList);
        nameListView.setAdapter(nameArrayAdapter);

        nameListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                String name = ((TextView) view).getText().toString();
                String surname = dbHelper.getSurname(name);
                String mark = dbHelper.getMark(name);
                String studentId = dbHelper.getId(name);

                StringBuffer buffer = new StringBuffer();
                buffer.append("Id: "+studentId+"\n");
                buffer.append("Name: "+name+"\n");
                buffer.append("Surname: "+surname+"\n");
                buffer.append("Mark: "+mark);

                showMessage("Data", buffer.toString());
            }
        });
    }

    public void showMessage(String title, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.show();
    }
}
