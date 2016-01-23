package com.locomotor.mydatabasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by MiloRambaldi on 23-Jan-16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "smileRequestDatabase.db";
    public static final String TABLE_NAME = "unsentRequests";
    public static final int VERSION = 1;

    public static final String _id = "_id";
    public static final String dateStart = "dateStart";
    public static final String dateEnd = "dateEnd";
    public static final String userName = "userName";
    public static final String smilePercentage = "smilePercentage";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + _id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + smilePercentage + " INTEGER NOT NULL, "
                + dateStart + " TEXT NOT NULL, "
                + dateEnd + " TEXT NOT NULL, "
                + userName + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertData(int smilepercentage, String datestart, String dateend, String username){
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();

            values.put(smilePercentage, smilepercentage);
            values.put(dateStart, datestart);
            values.put(dateEnd, dateend);
            values.put(userName, username);

            if (db != null) {
                db.insert(DatabaseHelper.TABLE_NAME, null, values);
            }
            Log.e("DBTEST", "KAYIT EKLENDI");

        } catch (Exception e) {
            Log.e("DBTEST", e.toString());
        }
    }

    public Set<RequestItem> getAllData() {

        RequestItem requestItem;
        Set<RequestItem> result = new HashSet<>();
        Set<String> set = new HashSet<>();
        String selectQuery = "select * from " + DatabaseHelper.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                requestItem = new RequestItem();
                requestItem._id = cursor.getString(0);
                requestItem.requestSmilePercentage = cursor.getInt(1);
                requestItem.requestDateStart = cursor.getString(2);
                requestItem.requestDateEnd = cursor.getString(3);
                requestItem.requestUserName = cursor.getString(4);

                result.add(requestItem);

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return result;
    }

    public void deleteRequest(String requestID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE _id = '" + requestID + "'");

    }
}
