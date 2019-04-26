package com.example.ebeat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class CrimeReportClass extends AppCompatActivity {
    ListView lv;
    List<crimed> crimelist;
    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_report_class);
        crimelist = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lv);
        db = FirebaseDatabase.getInstance().getReference("crime");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                crimelist.clear();
                for (DataSnapshot crimesnapshot : dataSnapshot.getChildren()) {
                    crimed c = crimesnapshot.getValue(crimed.class);
                    crimelist.add(c);
                }
                customAdapted adapter = new customAdapted(CrimeReportClass.this, crimelist);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
