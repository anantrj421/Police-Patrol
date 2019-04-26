package com.example.ebeat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class checkInData extends AppCompatActivity {
    ListView lv;
    List <checkinout>l;
    DatabaseReference dbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_data);
        lv = findViewById(R.id.lvcio);
        l = new ArrayList<>();
        dbref = FirebaseDatabase.getInstance().getReference("Check");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                l.clear();
                for(DataSnapshot t:dataSnapshot.getChildren()){
                    String name = (String) t.child("name").getValue();
                    String checkin =(String)t.child("checkin").getValue();
                    String checkout =(String)t.child("checkout").getValue();
                    checkinout temp = new checkinout(name,checkin,checkout);
                    l.add(temp);
                }
                customcheckadapter cca = new customcheckadapter(checkInData.this,l);
                lv.setAdapter(cca);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
