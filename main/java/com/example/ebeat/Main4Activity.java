package com.example.ebeat;

import android.content.ContentResolver;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Main4Activity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri mImageUri;
    ImageView mImageView;
    DatabaseReference dbref;
    StorageReference stref,fileReference;
    StorageTask uploadTask;
    private GoogleMap mMap2;
    private LocationListener ltr;
    private LocationManager lmgr;
    private LatLng ltlg;
    private final long mind=0;
    private final long mint=1000;
    private Marker cmarker=null;
    double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        final EditText crimedescription = (EditText) findViewById(R.id.crime);
        mImageView = (ImageView) findViewById(R.id.img);
        Button submit = (Button) findViewById(R.id.submit);
        dbref = FirebaseDatabase.getInstance().getReference("crime");
        stref = FirebaseStorage.getInstance().getReference("crime");
        Button choose = (Button) findViewById(R.id.choose);
        ltr=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try{
                    if(cmarker==null){
                        ltlg=new LatLng(location.getLatitude(),location.getLongitude());
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

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
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_LONG).show();
                if (mImageUri != null) {
                    fileReference = stref.child(System.currentTimeMillis()
                            + "." + getFileExtension(mImageUri));

                    uploadTask = fileReference.putFile(mImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    //Toast.makeText(Main4Activity.this, "Upload successful", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Main4Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "No file selected", Toast.LENGTH_SHORT).show();
                }
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {

                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        // Continue with the task to get the download URL
                        //Toast.makeText(getApplicationContext(),fileReference.getDownloadUrl().toString(),Toast.LENGTH_LONG).show();
                        return fileReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = (Uri) task.getResult();
                            crimed newcrime = new crimed(crimedescription.getText().toString(),downloadUri.toString(),latitude,longitude);
                            String uploadId = dbref.push().getKey();
                            dbref.child(uploadId).setValue(newcrime);
                            Toast.makeText(getApplicationContext(),"Final Upload done",Toast.LENGTH_LONG).show();
                        } else {
                            // Handle failures
                            // ...
                        }

                    }
                });
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(mImageView);
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}
