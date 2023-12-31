package com.example.myapplication.shelvedClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.db.DBContract;
import com.example.myapplication.db.DbHelper;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent = getIntent();
        String loggedInUID = intent.getStringExtra("UID");
        TextView textView = findViewById(R.id.TextView);

        if (intent!=null){

            textView.setText("Logged in UID: " + loggedInUID);
        }

        Button myButton = findViewById(R.id.button2);


        myButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                textView.setText( getAllItemDescriptionsAsString());
                Toast.makeText(SecondActivity.this, "going back to first activity", Toast.LENGTH_SHORT).show();
                getAllItemDescriptionsAsString();
                //Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                //startActivity(intent);
            }
        });





    }
    private void createMockItemEntries() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (int i = 0; i < 5; i++) {
            ContentValues values = new ContentValues();
            values.put(DBContract.ItemsDB.COLUMN_ITEM_DESC, "Item " + i);
            values.put(DBContract.ItemsDB.COLUMN_EAN, 100000000000L + i); // Example EAN values, adjust as needed
            values.put(DBContract.ItemsDB.COLUMN_BELONGS_TO, "User " + i);
            values.put(DBContract.ItemsDB.COLUMN_QTY, i * 10); // Example quantity values, adjust as needed

            long newRowId = db.insert(DBContract.ItemsDB.TABLE_NAME, null, values);
        }

        System.out.println("Mock entries added to ITEM DB");
        dbHelper.close();
    }
    public String getAllItemDescriptionsAsString() {
        StringBuilder itemDescriptionsString = new StringBuilder();
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] projection = {DBContract.ItemsDB.COLUMN_ITEM_DESC};

        Cursor cursor = db.query(
                DBContract.ItemsDB.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int descIndex = cursor.getColumnIndex(DBContract.ItemsDB.COLUMN_ITEM_DESC);
                String itemDescription = cursor.getString(descIndex);
                itemDescriptionsString.append(itemDescription).append("\n");
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return itemDescriptionsString.toString();
    }


}