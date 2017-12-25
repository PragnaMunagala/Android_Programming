package com.example.karthik.healthmonitor;

/**
 * Created by karthik on 9/26/17.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.sql.Timestamp;

public class myDbHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "data.db";

    private static final String TABLE_NAME = "Name_ID_Age_Sex";

    public static final String COLUMN_X = "_xValue";
    public static final String COLUMN_Y = "_yValue";
    public static final String COLUMN_Z = "_zValue";
    public static final String COLUMN_TS = "timestamp";
    public static final String FILE_DIR = "Android/Data/CSE535_ASSIGNMENT2/";
    public myDbHandler(Context context) {

        //super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        super(context, Environment.getExternalStorageDirectory()
                + File.separator + FILE_DIR
                + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
        //SQLiteDatabase db = this.getReadableDatabase();
        //super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()+"/databaseFolder/myDB", null);
    }
//Create a table to store Accelerometer values and timestamp
    @Override
    public void onCreate(SQLiteDatabase database) {
        String VALUES_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_TS + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY," + COLUMN_X + " REAL," + COLUMN_Y + " REAL," + COLUMN_Z + " REAL" + ")";
        database.execSQL(VALUES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
//Inserting values into the table
    public void addValues(Values value,String table) {
            try{
                SQLiteDatabase db=this.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put(COLUMN_TS,value.getTimestamp());
                values.put(COLUMN_X,value.getxValue());
                values.put(COLUMN_Y,value.getyValue());
                values.put(COLUMN_Z,value.getzValue());
                db.insert(table,null,values);}
            catch (Exception e){
                System.out.println("Exception is"+e);
            }
    }
//Retrieving values from the table
    public void getValues() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT _xValue,_yValue,_zValue FROM Name_ID_Age_Sex ORDER BY TIMESTAMP DESC LIMIT 10 ", null);
        if(cur.moveToFirst()){
            do{
                String column1 = cur.getString(0);
                String column2 = cur.getString(1);
                String column3 = cur.getString(2);
                System.out.println("x value is" + column1);
                System.out.println("y value is" + column2);
                System.out.println("z value is" + column3);
            }while(cur.moveToNext());
        }
        cur.close();
        db.close();
    }
}
