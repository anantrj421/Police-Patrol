package com.example.ebeat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {
    DatabaseReference dbref;
    checkinout chck;
    private LatLng ltlg1;
    private LocationListener ltr1;
    private LocationManager lmgr1;
    private final long mind1=0;
    private final long mint1=1000;
    DatabaseReference dreff;
    private GoogleMap mmap1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Bundle bundle = getIntent().getExtras();
        final String user = bundle.getString("username");
        dbref = FirebaseDatabase.getInstance().getReference();
        Button reportCrime = (Button)findViewById(R.id.reportCrime);
        final Button checkIn = (Button)findViewById(R.id.check_in);
        final Button checkOut = (Button)findViewById(R.id.check_out);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        dreff= FirebaseDatabase.getInstance().getReference().child(user);
        ltr1= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try{

                        ltlg1=new LatLng(location.getLatitude(),location.getLongitude());
                        dreff.child("lat").setValue(location.getLatitude());
                        dreff.child("lng").setValue(location.getLongitude());
                        String text = Double.toString(location.getLatitude())+" " + Double.toString(location.getLongitude())+"\n";
                        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = cm.getActiveNetworkInfo();
                        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                            //Toast.makeText(getApplicationContext(), "Online", Toast.LENGTH_LONG).show();

                        } else {
                            //Toast.makeText(getApplicationContext(), "Offline", Toast.LENGTH_LONG).show();
                            try {
                                FileOutputStream fos = openFileOutput("ex.txt", MODE_APPEND);
                                fos.write(text.getBytes());
                                //Toast.makeText(getApplicationContext(), "Saved to " + getFilesDir() + "/" + "ex.txt",Toast.LENGTH_LONG).show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
        lmgr1=(LocationManager)getSystemService(LOCATION_SERVICE);
        try{
            lmgr1.requestLocationUpdates(LocationManager.GPS_PROVIDER,mint1,mind1,ltr1);
        }
        catch (SecurityException e){
            e.printStackTrace();
        }
        reportCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Main2Activity.this,Main4Activity.class);
                startActivity(i);
            }
        });
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                String checkin = sdf.format(date);
                checkinout cio = new checkinout(user,checkin,"Patrollling");
                dbref.child("Check").child(user).child("name").setValue(user);
                dbref.child("Check").child(user).child("checkin").setValue(checkin);
                dbref.child("Check").child(user).child("checkout").setValue("Patrolling");
            }
        });
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                String checkout = sdf.format(date);
                //checkinout temp = new checkinout(user,chck.getTimein(),checkout);
                dbref.child("Check").child(user).child("checkout").setValue(checkout);
            }
        });
    }

}
