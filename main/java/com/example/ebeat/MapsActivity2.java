package com.example.ebeat;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationListener ltr;
    private LocationManager lmgr;
    private LatLng ltlg;
    private LatLng cltlg;
    private LatLng cltlg1;
    private GoogleMap mMap2;
    int f=0;
    private LatLngBounds bound;
    private final long mind=0;
    private final long mint=40;
    private Marker cmarker=null;
    private LatLng ltlg1;
    DatabaseReference reff;
    private double clt, cln;
    private double lat1, lng1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent i = getIntent();
        final String user = i.getStringExtra("username");
        reff= FirebaseDatabase.getInstance().getReference().child(user);
        clt=13.030;
        cln=77.564;

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 lat1 = (double)dataSnapshot.child("lat").getValue();
//                    Toast.makeText(getApplicationContext(),Double.toString(lat1),Toast.LENGTH_LONG).show();
                 lng1 = (double)dataSnapshot.child("lng").getValue();
                 ltlg=new LatLng(lat1,lng1);
                 cltlg = new LatLng(clt-.001,cln-.001);
                 cltlg1 = new LatLng(clt+.001,cln+.001);
                 bound=new LatLngBounds(cltlg,cltlg1);
                 if ((bound.contains(ltlg))&&f==0){
                     mMap.addMarker(new MarkerOptions().position(ltlg).title("Beat point"));
                     f=1;
                 }

                 //Toast.makeText(getApplicationContext(),Double.toString(lat1),Toast.LENGTH_LONG).show();
                try{
                    //if(cmarker==null){
                        ltlg=new LatLng(lat1,lng1);
                        mMap.addMarker(new MarkerOptions().position(ltlg).title("location").icon(BitmapDescriptorFactory.fromResource(R.drawable.path)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ltlg,20));
                   // }
                    //else {
                    //    cmarker.remove();
                    //    cmarker=null;
                    //}
                }
                catch (SecurityException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

        ltr = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                try{
//                    //if(cmarker==null){
//                        //ltlg=new LatLng(lat1,lng1);
//                        mMap.addMarker(new MarkerOptions().position(ltlg).title("location"));
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ltlg,20));
//                   // }
//                    //else {
//                    //    cmarker.remove();
//                   //     cmarker=null;
//                   // }
//                }
//                catch (SecurityException e){
//                    e.printStackTrace();
//                }
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
        lmgr=(LocationManager) getSystemService(LOCATION_SERVICE);
        try{
            lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,mint,mind,ltr);
        }
        catch (SecurityException e){
            e.printStackTrace();
        }
    }
}
