package com.example.ebeat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class customcheckadapter extends ArrayAdapter {
    Context context;
    List<checkinout> list;
    public customcheckadapter(Context context, List<checkinout> list) {
        super(context,R.layout.chekinoutlistitem, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.chekinoutlistitem,parent,false);
        final TextView name = (TextView)convertView.findViewById(R.id.name);
        TextView cin = (TextView)convertView.findViewById(R.id.checkintime);
        TextView cout = (TextView)convertView.findViewById(R.id.checkouttime);
        checkinout temp = list.get(position);
        name.setText(temp.getName());
        cin.setText(temp.getTimein());
        cout.setText(temp.getTimeout());
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,MapsActivity2.class);
                i.putExtra("username",name.getText());
                context.startActivity(i);
            }
        });
        return convertView;
    }
}
