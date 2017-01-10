package com.sarath.flipmap;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class NewFormActivity extends AppCompatActivity {

    private SQLiteDatabase mDB;
    private EditText etUserName;
    private EditText etLand;
    private EditText etPhone;
    private EditText etEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        etUserName=(EditText)findViewById(R.id.etUserName);
        etLand=(EditText)findViewById(R.id.etLand);
        etPhone=(EditText)findViewById(R.id.etPhone);
        etEmail=(EditText)findViewById(R.id.etEmail);

        mDB = new DBHelper(this).getWritableDatabase();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_form_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.add_new_contact_menu:
                Toast.makeText(this, "save to contact", Toast.LENGTH_SHORT).show();
                 save();
                break;
        }
        return true;
    }


    private void save() {
        ContentValues values = new ContentValues();
        values.put(ContactContract.ContactEntry.Column_NAME, etUserName.getText().toString());
        values.put(ContactContract.ContactEntry.COLUMN_EMAIL, etEmail.getText().toString());
        values.put(ContactContract.ContactEntry.COLUMN_HOMENO,etLand.getText().toString());
        values.put(ContactContract.ContactEntry.COLUMN_PHONENO,etPhone.getText().toString());
        long check = mDB.insert(ContactContract.ContactEntry.TABLE_NAME, null,values);
        if (check!=-1){
            finish();
        }
    }
}
