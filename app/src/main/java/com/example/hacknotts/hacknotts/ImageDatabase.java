package com.example.hacknotts.hacknotts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class ImageDatabase extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "imageDB";
    private static final String TABLENAME_NAME = "pictureList";
    private static final int ORIG_LAT = 51;
    private static final int ORIG_LONG = -1;
    private static final int BOUNDING_DIST = 100;
    String queryResults = "nothing";

    public ImageDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
    }

    public void onCreate() {
        //creating Database
        this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLENAME_NAME + ";");
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + TABLENAME_NAME + " (" +
                "id VARCHAR(1000) NOT NULL," +
                "lat DOUBLE NOT NULL," +
                "long DOUBLE NOT NULL" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME_NAME + ";");
        onCreate(db);
    }

    public void insertLocation(File file, double latitude, double longitude) {
        System.out.println(file.getAbsolutePath());
        Log.d("g54mdp", "DbHelper" + file.getAbsolutePath());
        this.getWritableDatabase().execSQL("INSERT INTO " + TABLENAME_NAME + "(id,lat,long) " + "VALUES " + "('" + file.getAbsolutePath() + "','" + latitude + "','" + longitude + "');");
    }

    public String getSongAtIndex(int index) {
        StringBuilder sb = new StringBuilder();
        Cursor c = this.getWritableDatabase().query(TABLENAME_NAME, new String[]{"id"}, null, null, null, null, null);
        if (c.moveToFirst()) // if there is at least 1 row
        {
            //do

            //{// the column at index 0, id
            String name = c.getString(c.getColumnIndex("id")); // the column at index 1, name
            //if(id == index) {
            sb.append(name);
            //}
            //}
            //while(c.moveToNext()); // while there are still rows remaining
        }
        Log.d("g54mdp", "DbHelper" + sb.toString());
        return sb.toString();
    }

    public ArrayList<String> locationQuery() {
        ArrayList<String> result = new ArrayList<String>();
        Cursor c = getWritableDatabase().rawQuery("SELECT * from "+ TABLENAME_NAME + " WHERE lat BETWEEN "+ ORIG_LAT + "- 5 AND "+ ORIG_LAT +
                "+ 5 AND long BETWEEN "+ORIG_LONG+" -5 AND "+ ORIG_LONG + " + 5 ORDER BY ABS(ABS()+ABS())",null);
             /*   "select *, ((ACOS(SIN(" + ORIG_LAT + "* PI() / 180) * SIN(lat * PI() / 180) + COS(" + ORIG_LAT +
                " * PI() / 180) * COS(lat * PI() / 180) * COS((" + ORIG_LONG + " - lon) * PI() / 180)) * 180 / PI()) * 60 * 1.1515)as distance from "
                + TABLENAME_NAME + " where lat between (" + ORIG_LAT + " - " + BOUNDING_DIST + ") and (" + ORIG_LAT + " + " + BOUNDING_DIST + ") and lon between ("
                + ORIG_LONG + " - " + BOUNDING_DIST + ") and (" + ORIG_LONG + " + " + BOUNDING_DIST + ") order by distance asc limit 5", null);
                */
        Double lat;
        Double lon;
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    queryResults = c.getString(c.getColumnIndex("id"));
                    lat = c.getDouble(c.getColumnIndex("lat"));
                    lon = c.getDouble(c.getColumnIndex("long"));
                    double resLat = Math.abs(lat - ORIG_LAT);
                    double resLon = lon - ORIG_LONG;


                    result.add(queryResults);
                } while (c.moveToNext());
            }
        }

        return result;
    }

    public String getPicName(int index) {
        String songNameFull;
        songNameFull = getSongAtIndex(index);
        String[] songNameSplit = songNameFull.split("/");
        Log.d("DEBUG1", "DbHelper" + songNameSplit[songNameSplit.length - 1]);
        String[] extensionNameSplit = songNameSplit[songNameSplit.length - 1].split("\\.");
        Log.d("DEBUG2", "DbHelper" + extensionNameSplit[0]);
        return extensionNameSplit[0];
    }
}