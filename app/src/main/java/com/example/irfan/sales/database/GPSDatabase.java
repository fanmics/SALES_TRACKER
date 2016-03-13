package com.example.irfan.sales.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Lenovo on 2/7/2016.
 */
public class GPSDatabase {
    private Context context;
    private DbHelper dbHelper;
    public final String DBNAME="GPSLOGGERDB";
    //public final String DBNAME="gps1";

    public final int DBVERSION=3;
    public SQLiteDatabase db;
    public final String COLUMN1="locationId";
    public final String COLUMN2="latitude";
    public final String COLUMN3="longitude";
    public final String COLUMN4="created_date";
    //public final String TABLENAME="location";
    public final String POINTS_TABLE_NAME = "LOCATION_POINTS";

    public final String CREATERDB="CREATE TABLE IF NOT EXISTS " +
            POINTS_TABLE_NAME + " (LOCATION_ID integer primary key autoincrement,CREATED_DATE VARCHAR, LATITUDE REAL, LONGITUDE REAL," +
            "ALTITUDE REAL, ACCURACY REAL, SPEED REAL, BEARING REAL);";

    //const
    public GPSDatabase(Context context){
        this.context=context;
        dbHelper=new DbHelper(context);
    }
    public class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context context){
            super(context,DBNAME,null,DBVERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(CREATERDB);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
        }
    }
    public void deleteAll()
    {
        db.execSQL("delete table" + POINTS_TABLE_NAME);
        db.close();
    }

    public long insertRows(String column2, String column3){
        ContentValues value=new ContentValues();
        value.put(COLUMN2, column2);
        value.put(COLUMN3, column3);
        return db.insert(POINTS_TABLE_NAME,null,value);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public Cursor getAllRows(){
//        String whereClause = "created_date like '%?%' ";
        String[] whereArgs = new String[] {
                getDateTime()
        };
        db.execSQL(CREATERDB);
        //Cursor cursor=db.query(TABLENAME, new String[]{COLUMN1,COLUMN2,COLUMN3}, whereClause,whereArgs, null, null, null);
        Cursor cursor=db.rawQuery("SELECT * FROM "+ POINTS_TABLE_NAME +" WHERE strftime('%Y-%m-%d', created_date)=strftime('%Y-%m-%d', ?)",whereArgs);
        return cursor;
    }

    public Cursor getLastRows(){
        String[] whereArgs = new String[] {
                getDateTime()
        };
        //Cursor cursor=db.query(TABLENAME, new String[]{COLUMN1,COLUMN2,COLUMN3}, whereClause,whereArgs, null, null, null);
        Cursor cursor=db.rawQuery("SELECT max(LOCATION_ID) as LOCATION_ID,LATITUDE, LONGITUDE FROM "+ POINTS_TABLE_NAME +" WHERE strftime('%Y-%m-%d', created_date)=strftime('%Y-%m-%d', ?) GROUP BY LATITUDE, LONGITUDE",whereArgs);
        return cursor;
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        //return true;
    }
    public void close(){
        dbHelper.close();
        //return true;
    }
}
