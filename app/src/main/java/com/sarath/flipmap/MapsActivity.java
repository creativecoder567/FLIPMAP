package com.sarath.flipmap;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        DBHelper dbHelper=new DBHelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng india = new LatLng(20.5937, 78.9629);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(india));
        addMarkers();
    }
    private void addMarkers(){
        int j=1;
        Cursor cursor=readDataFromSQlite();

       while (cursor.moveToNext()){

           String name = cursor.getString(cursor.getColumnIndex(ContactContract.ContactEntry.Column_NAME));
           String email = cursor.getString(cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_EMAIL));
           String homeNO = cursor.getString(cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_HOMENO));
           String phoneNo = cursor.getString(cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_PHONENO));

            float iconMarker = BitmapDescriptorFactory.HUE_AZURE;

            switch (j){
                case 1:iconMarker = BitmapDescriptorFactory.HUE_BLUE;
                    break;
                case 2:iconMarker = BitmapDescriptorFactory.HUE_CYAN;
                    break;
                case 3:iconMarker = BitmapDescriptorFactory.HUE_MAGENTA;
                    break;
                case 4:iconMarker = BitmapDescriptorFactory.HUE_RED;
                    break;
                case 5:iconMarker = BitmapDescriptorFactory.HUE_ROSE;
                    break;
                case 6:iconMarker = BitmapDescriptorFactory.HUE_VIOLET;
                    break;
                default:j=0;
                    iconMarker = BitmapDescriptorFactory.HUE_YELLOW;
            }

            double lat=Double.parseDouble(homeNO)/100000000;
            double lng=Double.parseDouble(phoneNo)/100000000;
            LatLng latLng = new LatLng(lat,lng);

            mMap.addMarker(new MarkerOptions().position(latLng).title(name)).setIcon(BitmapDescriptorFactory.defaultMarker(iconMarker));
            j++;
        }

    }

    private Cursor readDataFromSQlite(){
        return sqLiteDatabase.query(ContactContract.ContactEntry.TABLE_NAME,null,null,null,null,null, ContactContract.ContactEntry.Column_NAME+" ASC");
    }



}


