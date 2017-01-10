package com.sarath.flipmap;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sarath.flipmap.ContactContract.ContactEntry;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static final int PICKFILE_RESULT_CODE = 100;
    private SQLiteDatabase sqLiteDatabase;
    private ContactCursorAdaptor mCursorAdaptor;
    private TextView detailTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHelper dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ListView listView = (ListView) findViewById(R.id.listview);
        detailTV = (TextView) findViewById(R.id.detailTV);
        mCursorAdaptor = new ContactCursorAdaptor(this, null);
        listView.setAdapter(mCursorAdaptor);
        Cursor cursor = readDataFromSQlite();
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            mCursorAdaptor.swapCursor(cursor);
            detailTV.setVisibility(View.GONE);

        } else {
            Toast.makeText(this, "load the data", Toast.LENGTH_SHORT).show();
            detailTV.setVisibility(View.VISIBLE);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "id" + id, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplication(), FormActivity.class);
                intent.putExtra(ContactEntry._ID, id);
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_file:
                addfileIntent();
                break;
            case R.id.delete:
                sqLiteDatabase.delete(ContactEntry.TABLE_NAME, null, null);
                mCursorAdaptor.swapCursor(readDataFromSQlite());
                detailTV.setVisibility(View.VISIBLE);
                break;
            case R.id.show_map:
                Intent intent = new Intent(getApplication(), MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.save_all:
                save_all();
                break;
            case R.id.new_contact:
                Intent intentForm = new Intent(getApplication(), NewFormActivity.class);
                startActivity(intentForm);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void save_all() {
        Cursor cursor = readDataFromSQlite();
        while (cursor.moveToNext()) {
            String DisplayName = cursor.getString(cursor.getColumnIndex(ContactContract.ContactEntry.Column_NAME));
            String emailID = cursor.getString(cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_EMAIL));
            String HomeNumber = cursor.getString(cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_HOMENO));
            String MobileNumber = cursor.getString(cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_PHONENO));

            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

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


            // Asking the Contact provider to create a new contact
            try {
                ContentProviderResult[] check = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                if (check != null) {
                    Toast.makeText(this, " Contact Add  successful", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
        cursor.close();

    }

    private void showAllDetail(List<Contact> contactList) {
        for (Contact contact : contactList) {
            Log.i(TAG, "showAllDetail: " + contact.toString());
        }
    }

    private void writeSQLite(List<Contact> contactList) {
        for (Contact contact : contactList) {
            ContentValues values = new ContentValues();
            values.put(ContactEntry.Column_NAME, contact.getName());
            values.put(ContactEntry.COLUMN_EMAIL, contact.getEmail());
            values.put(ContactEntry.COLUMN_HOMENO, contact.getLandLine());
            values.put(ContactEntry.COLUMN_PHONENO, contact.getPhoneNo());
            sqLiteDatabase.insert(ContactEntry.TABLE_NAME, null, values);
        }
    }

    private Cursor readDataFromSQlite() {
        return sqLiteDatabase.query(ContactEntry.TABLE_NAME, null, null, null, null, null, ContactEntry.Column_NAME + " ASC");
    }

    void addfileIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        startActivityForResult(intent, PICKFILE_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKFILE_RESULT_CODE) {
            if (data != null) {
                Uri uri = data.getData();

                List<Contact> contactList = readTextFileFromUri(uri);
                writeSQLite(contactList);
                mCursorAdaptor.swapCursor(readDataFromSQlite());
            }
        }
    }

    public List<Contact> readTextFileFromUri(Uri uri) {
        List<Contact> contactList = new ArrayList<Contact>();
        BufferedReader br;
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            br = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] personDetail = line.split(",");
                String name = personDetail[0];
                String email = personDetail[1];
                String homeNumber = personDetail[2].trim();
                String phoneNo = personDetail[3].trim();
                Contact contact = new Contact(name, email, homeNumber, phoneNo);
                contactList.add(contact);
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        detailTV.setVisibility(View.INVISIBLE);
        return contactList;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCursorAdaptor.swapCursor(readDataFromSQlite());
    }
}