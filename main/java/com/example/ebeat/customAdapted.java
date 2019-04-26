package com.example.ebeat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class customAdapted extends ArrayAdapter {
    Context context;
    List<crimed> crimelist;
    public customAdapted(Context context, List<crimed> crimelist) {
        super(context,R.layout.listitem,crimelist);
        this.context = context;
        this.crimelist = crimelist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.listitem,parent,false);
        TextView tv = convertView.findViewById(R.id.textView);
        ImageView imgView = convertView.findViewById(R.id.imgview);
        final crimed c = crimelist.get(position);
        tv.setText(c.getDesc());
        Picasso.get().load(c.getImageurl()).into(imgView);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strUri = "http://maps.google.com/maps?q=loc:" + Double.toString(c.getLatitude()) + "," + Double.toString(c.getLongitude()) + " (" + "Crimes Reported" + ")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
