package com.crm.myapplication.ui.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.crm.myapplication.DataList;
import com.crm.myapplication.MainActivity;
import com.crm.myapplication.Models.Batch;
import com.crm.myapplication.Models.Plan;
import com.crm.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    ImageView propic;
    EditText name, mobile, email, address, details;
    Button selectImage, submit;
    int i = -1;
    public static String gid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = ((EditText) findViewById(R.id.editTextTextPersonName));
        mobile = ((EditText) findViewById(R.id.editTextTextPersonName2));
        email = ((EditText) findViewById(R.id.editTextTextPersonName3));
        address = ((EditText) findViewById(R.id.editTextTextPersonName4));
        details = ((EditText) findViewById(R.id.editTextTextMultiLine));
        propic = findViewById(R.id.imageView);
        selectImage = findViewById(R.id.button);
        submit = findViewById(R.id.button2);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(ProfileActivity.this);
                builder1.setMessage("Select Image from");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Camera",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                i = 0;
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 0);
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Gallery",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                i = 1;
                                imageChooser();
                                dialog.cancel();
                            }
                        });

                builder1.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (!((name.getText().toString()).equals("")) &&
                        !((mobile.getText().toString()).equals("")) && mobile.length() == 10 &&
                        !((email.getText().toString()).equals(""))) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ProfileActivity.this);
                    builder1.setMessage("Are you sure to save the info?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    saveToDB();
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Invalid Data", Toast.LENGTH_SHORT).show();
                }
            }
        });


}

    private void saveToDB() {

        Map<String, String> map=new HashMap<>();
        map.put("gym_name", name.getText().toString());
        map.put("gym_mobile", mobile.getText().toString());
        map.put("gym_email", email.getText().toString());
        map.put("gym_address", address.getText().toString());
        map.put("gym_details", details.getText().toString());

        FirebaseDatabase.getInstance().getReference()
                .child("FITCRM")
                .child("gyms")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            loadFromDB();
                            Toast.makeText(ProfileActivity.this, "Profile Updated!", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(ProfileActivity.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public  void loadFromDB(){
        FirebaseDatabase.getInstance().getReference()
                .child("FITCRM")
                .child("gyms")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("batch")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot d: snapshot.getChildren()){
                            DataList.batchList.add(new Batch(
                                    String.valueOf(d.child("batchid").getValue()),
                                    String.valueOf(d.child("batchname").getValue()),
                                    String.valueOf(d.child("batchdesc").getValue()),
                                    String.valueOf(d.child("batchtot").getValue()),
                                    String.valueOf(d.child("batchMaxStrength").getValue()),
                                    String.valueOf(d.child("batchtimestamp").getValue()),
                                    String.valueOf(d.child("status").getValue())
                            ));
                        }

                        FirebaseDatabase.getInstance().getReference()
                                .child("FITCRM")
                                .child("gyms")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("plans")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for(DataSnapshot d: snapshot.getChildren()){
                                            DataList.planList.add(new Plan(
                                                    String.valueOf(d.child("planid").getValue()),
                                                    String.valueOf(d.child("planname").getValue()),
                                                    String.valueOf(d.child("planfee").getValue()),
                                                    String.valueOf(d.child("planduration").getValue()),
                                                    String.valueOf(d.child("plandurationtype").getValue()),
                                                    String.valueOf(d.child("plandesc").getValue()),
                                                    String.valueOf(d.child("status").getValue()),
                                                    String.valueOf(d.child("planTimestamp").getValue())
                                            ));
                                        }
                                        Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                                        startActivity(i);finish();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (i) {
            case 0:
                if (resultCode == RESULT_OK) {
                    // compare the resultCode with the
                    // SELECT_PICTURE constant
                    if (requestCode == 0) {
                        // Get the url of the image from data
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        propic.setImageBitmap(photo);
                    }
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    // compare the resultCode with the
                    // SELECT_PICTURE constant
                    if (requestCode == 200) {
                        // Get the url of the image from data
                        Uri selectedImageUri1 = data.getData();
                        if (null != selectedImageUri1) {
                            // update the preview image in the layout
                            propic.setImageURI(selectedImageUri1);
                        }
                    }
                }
                break;
        }

    }
}