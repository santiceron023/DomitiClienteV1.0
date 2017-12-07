package com.example.danni.domiti;

import android.*;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    double latitude = 0, longitude = 0;
    String clienteId = "vacio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }

        };
        location();
    }


    private void location() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        android.Manifest.permission.INTERNET, android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
                return;
            }
        } else {
            //location();
        }

        //if(!bandera)
        locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //LatLng Udea = new LatLng(6.266953, -75.569111);
        //mMap.addMarker(new MarkerOptions().position(Udea).title("Univer antioquia").snippet("clase de android").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Udea, 17));


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                while (latitude==0 && longitude == 0){

                }

                String ubicacion = "Latitud: "+ String.valueOf(latitude)+" longitud : " + String.valueOf(longitude);
                Toast.makeText(getApplicationContext(),ubicacion,Toast.LENGTH_SHORT).show();


                //shared preferences para db
                SharedPreferences sharedPrefs = getSharedPreferences("DomitiPreferences",MODE_PRIVATE);
                clienteId = sharedPrefs.getString("Celular","no cargo celular");

                //database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Usuarios").child(clienteId).
                        child("ubicacion");
                myRef.setValue(ubicacion);

                Toast.makeText(getApplicationContext(),"Ubicacion Guardada",Toast.LENGTH_SHORT).show();

                //LatLng Udea = new LatLng(6.266953, -75.569111);
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Udea, 17));





                return false;
            }
        });



        //mMap.getUiSettings().setMyLocationButtonEnabled(true);

    }



}