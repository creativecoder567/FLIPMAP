package com.sarath.flipmap;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.sarath.flipmap.ContactContract.ContactEntry;

import java.util.ArrayList;


public class FormActivity extends AppCompatActivity {
    private SQLiteDatabase mDB;
    private long idRow=-1;
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


        Intent intent = getIntent();
        if (intent!=null){
             idRow = intent.getExtras().getLong(ContactContract.ContactEntry._ID);
            Toast.makeText(this, "activity_form "+idRow, Toast.LENGTH_SHORT).show();
            String [] SelectionID=new String[]{String.valueOf(idRow)};

            String[] projection=new String[]{
                    ContactEntry.Column_NAME,
                    ContactEntry.COLUMN_EMAIL,
                            ContactEntry.COLUMN_HOMENO ,
                            ContactEntry.COLUMN_PHONENO
            };

            Cursor cursor=mDB.query(ContactContract.ContactEntry.TABLE_NAME,projection, ContactContract.ContactEntry._ID+" = ?",SelectionID,null,null,null);

            if(cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactEntry.Column_NAME));
                String email = cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_EMAIL));
                String homeNO = cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_HOMENO));
                String phoneNo = cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_PHONENO));
                etUserName.setText(name);
                etEmail.setText(email);
                etLand.setText(homeNO);
                etPhone.setText(phoneNo);
            }



        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_Tick:
                Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();
                save();
                break;
            case R.id.delete_menu:
                delete();
                break;
            case R.id.add_contact_menu:
                Toast.makeText(this, "save to contact", Toast.LENGTH_SHORT).show();
                addContactToApp();
                break;
        }
        return true;
    }

    private void addContactToApp() {


        String DisplayName = etUserName.getText().toString();
        String MobileNumber = etPhone.getText().toString();
        String HomeNumber = etLand.getText().toString();
        String emailID = etEmail.getText().toString();
        //String company =null;
        //String jobTitle = null;
        //String WorkNumber=null;

        ArrayList < ContentProviderOperation > ops = new ArrayList < ContentProviderOperation > ();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        if (DisplayName != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            DisplayName).build());
        }

        //------------------------------------------------------ Mobile Number
        if (MobileNumber != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        //------------------------------------------------------ Home Numbers
        if (HomeNumber != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
        }
        //------------------------------------------------------ Email
        if (emailID != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Work Numbers

       /* if (WorkNumber != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, WorkNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }



        //------------------------------------------------------ Organization
        if (!company.equals("") && !jobTitle.equals("")) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());
        } */

        // Asking the Contact provider to create a new contact
        try {
            ContentProviderResult[] check = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            if (check!=null){
                Toast.makeText(this, " Contact Add  successful" , Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void delete() {
        String [] SelectionID=new String[]{String.valueOf(idRow)};
        int check = mDB.delete(ContactEntry.TABLE_NAME, ContactEntry._ID + " =? ", SelectionID);
        if (check!=-1){
            finish();
        }
    }

    private void save() {
        ContentValues values = new ContentValues();
        values.put(ContactEntry.Column_NAME, etUserName.getText().toString());
        values.put(ContactEntry.COLUMN_EMAIL, etEmail.getText().toString());
        values.put(ContactEntry.COLUMN_HOMENO,etLand.getText().toString());
        values.put(ContactEntry.COLUMN_PHONENO,etPhone.getText().toString());
        String [] SelectionID=new String[]{String.valueOf(idRow)};
        int check = mDB.update(ContactEntry.TABLE_NAME, values, ContactEntry._ID + " =? ", SelectionID);
        if (check!=-1){
            finish();
        }
    }
}
