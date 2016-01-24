package com.locomotor.mydatabasehelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button insert = (Button) findViewById(R.id.button);
        Button delete = (Button) findViewById(R.id.button2);
        Button deleteall = (Button) findViewById(R.id.button3);


        final DatabaseHelper database = DatabaseHelper.getInstance(MainActivity.this);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                database.createTableIfNotExists();

                SQLiteDatabase db = database.getReadableDatabase();

                // test values...
                Cursor cur = db.rawQuery("SELECT COUNT(*) FROM "+ DatabaseHelper.TABLE_NAME, null);
                if (cur != null){
                    cur.moveToFirst();
                    if (cur.getInt(0) == 0) {
                        // error condition insert that Request (one data) into database..
                        database.insertData(11, "9:13", "9:18", "orcun");
                        database.insertData(55, "8:13", "8:18", "orcun");
                        database.insertData(66, "7:13", "7:18", "orcun");
                        database.insertData(44, "6:13", "6:18", "orcun");
                    }
                    cur.close();
                    db.close();
                }
            }


        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Set<RequestItem> set =  database.getAllData();
                List<RequestItem> requestList = new ArrayList<>(set);

                for (RequestItem items : requestList) {
                    //todo: send request using list then delete that request from table
                    Log.e("send REQUEST",
                            items._id + " id " +
                                    items.requestSmilePercentage + " smile " +
                                    items.requestDateStart + " dateStart " +
                                    items.requestDateEnd +" dateEnd "+
                                    items.requestUserName+" userNAme");

                    // delete request
                    database.deleteRequest(items._id);
                }


            }
        });


        deleteall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // todo: delete all table when different user entered!
                String loginedUserNameDynamic = "kola";

                // when different user login the system, erase previous user's all unsent requests
                if (!database.getUserNameFromDatabase().equals(loginedUserNameDynamic)) {
                    database.deleteAllRequests();
                    Log.e("DBTEST", "TABLE DELETED");
                }


            }
        });



    }
}
