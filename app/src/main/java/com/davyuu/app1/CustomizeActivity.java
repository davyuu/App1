package com.davyuu.app1;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomizeActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    EditText editName, editSurname, editMark;
    Button btnAddData;
    Button btnViewAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);

        dbHelper = new DatabaseHelper(this);

        editName = (EditText) findViewById(R.id.editName);
        editSurname = (EditText) findViewById(R.id.editSurname);
        editMark = (EditText) findViewById(R.id.editMark);
        btnAddData = (Button) findViewById(R.id.button_add);
        btnViewAll = (Button) findViewById(R.id.button_view);

        addData();
        viewAllData();
    }

    public void addData(){
        btnAddData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean isInserted = dbHelper.insertData(editName.getText().toString(),
                        editSurname.getText().toString(),
                        editMark.getText().toString());
                if(isInserted){
                    Toast.makeText(CustomizeActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(CustomizeActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void viewAllData(){
        btnViewAll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Cursor results = dbHelper.getAllData();
                if(results.getCount() == 0){
                    // Show msg
                    showMessage("Error", "No data found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while(results.moveToNext()){
                    buffer.append("Id: "+results.getString(0)+"\n");
                    buffer.append("Name: "+results.getString(1)+"\n");
                    buffer.append("Surname: "+results.getString(2)+"\n");
                    buffer.append("Mark: "+results.getString(3)+"\n\n");
                }

                // Show all data
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
