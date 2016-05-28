package com.davyuu.app1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_PATH = "/data/data/com.davyuu.app1/databases/";
    private static final String DB_NAME = "students.db";
    private static final String TABLE_NAME = "student_table";

    public static final String COL_0 = "ID";
    public static final String COL_1 = "NAME";
    public static final String COL_2 = "SURNAME";
    public static final String COL_3 = "MARK";

    private SQLiteDatabase myDatabase;
    private final Context myContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder("CREATE TABLE ")
                .append(TABLE_NAME)
                .append(" (")
                .append(COL_0)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(COL_1)
                .append(" TEXT,")
                .append(COL_2)
                .append(" TEXT,")
                .append(COL_3)
                .append(" INTEGER")
                .append(")");
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor results = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return results;
    }

    public List<String> getName(){
        Cursor cursor = getAllData();

        List<String> list = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex(COL_1)));
            cursor.moveToNext();
        }
        return list;
    }

    public String getSurname(String name){
        Cursor results = getAllData();

        results.moveToFirst();
        while (!results.isAfterLast()) {
            if(results.getString(results.getColumnIndex(COL_1)).equalsIgnoreCase(name)){
                return results.getString(results.getColumnIndex(COL_2));
            }
            results.moveToNext();
        }
        return "";
    }

    public String getMark(String name){
        Cursor results = getAllData();

        results.moveToFirst();
        while (!results.isAfterLast()) {
            if(results.getString(results.getColumnIndex(COL_1)).equalsIgnoreCase(name)){
                return results.getString(results.getColumnIndex(COL_3));
            }
            results.moveToNext();
        }
        return "";
    }

    public String getId(String name){
        Cursor results = getAllData();

        results.moveToFirst();
        while (!results.isAfterLast()) {
            if(results.getString(results.getColumnIndex(COL_1)).equalsIgnoreCase(name)){
                return results.getString(results.getColumnIndex(COL_0));
            }
            results.moveToNext();
        }
        return "";
    }

    public boolean insertData(String name, String surname, String mark){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, name);
        contentValues.put(COL_2, surname);
        contentValues.put(COL_3, mark);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    /*
    private boolean checkDatabase(){
        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //database does't exist yet.
        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    private void copyDatabase() throws IOException{
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDatabase() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {

        if(myDatabase != null)
            myDatabase.close();

        super.close();

    }
    */

}