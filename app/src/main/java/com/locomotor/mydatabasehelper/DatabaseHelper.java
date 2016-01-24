package com.locomotor.mydatabasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by MiloRambaldi on 23-Jan-16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static DatabaseHelper mInstance = null;

    public static final String DB_NAME = "smileRequestDatabase.db";
    public static final String TABLE_NAME = "unsentRequests";
    public static final int VERSION = 1;

    public static final String _id = "_id";
    public static final String dateStart = "dateStart";
    public static final String dateEnd = "dateEnd";
    public static final String userName = "userName";
    public static final String smilePercentage = "smilePercentage";


    public static DatabaseHelper getInstance(Context ctx) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
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

    /**
     * create table if does not exists
     */
    public void createTableIfNotExists() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + smilePercentage + " INTEGER NOT NULL, "
                + dateStart + " TEXT NOT NULL, "
                + dateEnd + " TEXT NOT NULL, "
                + userName + " TEXT NOT NULL);");
        db.close();

    }

    /**
     * Insert requests into the Database
     * @param smilepercentage percentage of Smile Value
     * @param datestart Report Start Date
     * @param dateend Report End Date
     * @param username username
     */
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


    /**
     * GET All Data From Database
     * @return all Stored Data
     */
    public Set<RequestItem> getAllData() {

        RequestItem requestItem;
        Set<RequestItem> result = new HashSet<>();
        String selectQuery = "select * from " + DatabaseHelper.TABLE_NAME + " ORDER BY _id ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        try {
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

                }while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * Delete Requests by id
     * @param requestID  row will be deleted from table
     */
    public void deleteRequest(String requestID) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE _id = '" + requestID + "'");
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * DELETE all TABLE from Database
     */
    public void deleteAllRequests() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME, null, null);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * get active userName that saved in Database
     * @return String value
     */
    public String getUserNameFromDatabase() {
        String result = "null";
        String selectQuery = "select * from " + DatabaseHelper.TABLE_NAME + " ORDER BY _id ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst())
                result = cursor.getString(4);

            cursor.close();
            db.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("DBTEST", e.toString());
        }
        return result;
    }
}
