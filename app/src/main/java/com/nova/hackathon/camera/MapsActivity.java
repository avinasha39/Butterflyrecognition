package com.nova.hackathon.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9002;
    private static final float DEFAULTZOOM = 15;
    GoogleMap mMap;

    private void gotoLocation(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.moveCamera(update);
        LatLng a = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(a).title(""));

    }

    public void geoLocate(View v) throws IOException {
        hideSoftKeyboard(v);
        EditText et = (EditText) findViewById(R.id.editText1);

        InputStream inputStream = getResources().openRawResource(R.raw.location);
        CsvReader csvFile = new CsvReader(inputStream);
        mMap.clear();
        String stname = et.getText().toString();
        //String stname = "Polyommatus ariana";
        String[] location = csvFile.read(stname);
        for (String ind : location) {
            try {
                System.out.println(ind);
                Geocoder gc = new Geocoder(this);
                List<Address> list = gc.getFromLocationName(ind, 1);
                Address add = list.get(0);
                String locality = add.getLocality();
                Toast.makeText(this, locality, Toast.LENGTH_LONG).show();
                double lat = add.getLatitude();
                double lng = add.getLongitude();
                gotoLocation(lat, lng, DEFAULTZOOM);
            } catch (Exception ex) {
            }

        }
    }

    private void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //LatLng mumbai = new LatLng(-32, 141);
        // mMap.addMarker(new MarkerOptions().position(mumbai).title("Marker in mumbai"));


        mMap.setMyLocationEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

    }
}