package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    // we need a loation manager and a location listener
    LocationManager locationManager;
    LocationListener locationListener;
    TextView location1;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //check if request code is 1 -> it will be but to make sure
        if(requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        location1 = (TextView)findViewById(R.id.location);
        //setting the location manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //setting the location listener
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //to get the address from the location of the user
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (listAddress != null && listAddress.size() > 0) {
                        Log.i("PLACE INFO", listAddress.get(0).toString());
                        //now that we have got the address we need to parse it an print it in the text view
                        String address = "";
                        if(listAddress.get(0).getLatitude() != 0)
                        {
                            address += "Latitude : " +  String.valueOf(listAddress.get(0).getLatitude()) + "\r\n";
                        }
                        if(listAddress.get(0).getLongitude() != 0)
                        {
                            address += "Longitude : " +  String.valueOf(listAddress.get(0).getLongitude()) + "\r\n";
                        }
                        if(listAddress.get(0).getLocality() != null)
                        {
                            address += "Locality : " + listAddress.get(0).getLocality() + "\r\n";
                        }
                        if(listAddress.get(0).getCountryName() != null)
                        {
                            address += "Country : " + listAddress.get(0).getCountryName();
                        }
                        location1.setText(address);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        //checking the version
        if (Build.VERSION.SDK_INT < 23) {
            // update in 0s and 0 m
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
        else {
            //we need to ask for permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else {
                //we have the permiission

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        }
    }
}