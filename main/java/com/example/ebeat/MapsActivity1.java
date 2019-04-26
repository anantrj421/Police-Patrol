package com.example.ebeat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity1 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationListener ltr;
    private LocationManager lmgr;
    private LatLng ltlg;
    private final long mind=0;
    private final long mint=1000;
    private Marker cmarker=null;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps1);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        reff= FirebaseDatabase.getInstance().getReference().child("ley");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ltr=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try{
                    if(cmarker==null){
                        ltlg=new LatLng(location.getLatitude(),location.getLongitude());
                        cmarker=mMap.addMarker(new MarkerOptions().position(ltlg).title("My location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ltlg,20));
                        reff.child("lat").setValue(location.getLatitude());
                        reff.child("lng").setValue(location.getLongitude());
                    }
                    else{
                        cmarker.remove();
                        cmarker=null;
                    }

                }
                catch (SecurityException e){
                    e.printStackTrace();
                }
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
        lmgr=(LocationManager)getSystemService(LOCATION_SERVICE);
        try{
            lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,mint,mind,ltr);
        }
        catch (SecurityException e){
            e.printStackTrace();
        }

        // Add a marker in Sydney and move the camera

    }
}
