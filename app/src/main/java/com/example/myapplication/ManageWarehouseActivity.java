package com.example.myapplication;
import java.util.List;
import java.util.Random;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.db.DBContract;
import com.example.myapplication.db.DbHelper;
import com.example.myapplication.rvListManageWarehouse.rvList.ListItemManageWarehouse;
import com.example.myapplication.rvListManageWarehouse.rvList.ListViewAdapterManageWarehouse;

public class ManageWarehouseActivity extends AppCompatActivity {
    String  currentWarehouse = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DbHelper dbHelper = new DbHelper(ManageWarehouseActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_warehouse);
        //get logged in UID / Username
        Intent intent = getIntent();
        String loggedInUID = intent.getStringExtra("UID");

        //link Layout Items to vars
        EditText warehouseName = findViewById(R.id.WarehouseNameField);
        EditText addUserField = findViewById(R.id.AddUsersField);
        Button addWarehouseButton = findViewById(R.id.ConfirmWarehouseName);
        Button addUserButton = findViewById(R.id.AddUserButton);


        if (intent!=null){
            Log.d(loggedInUID, loggedInUID);

        }
        addWarehouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!dbHelper.doesWarehouseExist(warehouseName.getText().toString())) {
                    setCurrentWarehouse(dbHelper.createWarehouseAndReturnCurrentWarehouse(warehouseName.getText().toString(), loggedInUID));
                    Toast.makeText(ManageWarehouseActivity.this, "warehouse created successfully", Toast.LENGTH_SHORT).show();

                    addWarehouseButton.setEnabled(false);

                }
                else {
                    Toast.makeText(ManageWarehouseActivity.this, "warehouse name taken", Toast.LENGTH_SHORT).show();

                }
            }
        });

        addUserButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v ){

                String enteredUNAME = addUserField.getText().toString();
                String matchingUID = null;
                try {
                   matchingUID = dbHelper.getUserIdByUsername(enteredUNAME);
                }
                catch (Exception e){
                    Toast.makeText(ManageWarehouseActivity.this, "User not registered", Toast.LENGTH_SHORT).show();

                }


                if (currentWarehouse!=null){
                    if(matchingUID!= null){
                        if(!dbHelper.doesUserHaveAccess(matchingUID.toString(),currentWarehouse)){
                        dbHelper.allowUserAccess(matchingUID.toString(),currentWarehouse);}
                        else{
                            Toast.makeText(ManageWarehouseActivity.this, "User already has access", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else{
                        Toast.makeText(ManageWarehouseActivity.this, "User not registered1", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ManageWarehouseActivity.this, "Create Warehouse first!", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    public void setCurrentWarehouse(String currentWarehouse){
       this.currentWarehouse = currentWarehouse;
    }














}