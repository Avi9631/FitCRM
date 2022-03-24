package com.crm.myapplication.ui.slideshow;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.crm.myapplication.DataList;
import com.crm.myapplication.Models.Batch;
import com.crm.myapplication.Models.Member;
import com.crm.myapplication.R;
import com.crm.myapplication.ui.gallery.AdmissionActivity;
import com.crm.myapplication.ui.plans.PlansFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EditMemberActivity extends AppCompatActivity {

    ImageView propic, docs;
    EditText name, mobile, email, joindate, address, dob, details;
    Button selectImage, submit, selectDoc;
    int i = -1;
    private String arr[]=new String[(int) DataList.batchList
            .stream().filter(m -> m.getStatus().equals("enable")).count()];
    Batch p;
    Spinner spin;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);

        int k=0;
        for(Batch b: DataList.batchList){
            if(b.getStatus().equals("enable")) {
                arr[k] = b.getBatchname(); k++;
            }
        }


        String id = getIntent().getStringExtra("id");
        Member m = DataList.memberList.stream().filter(c -> c.getId().equals(id)).findAny().orElse(null);


        name = ((EditText) findViewById(R.id.editTextTextPersonName));
        mobile = ((EditText) findViewById(R.id.editTextTextPersonName2));
        email = ((EditText) findViewById(R.id.editTextTextPersonName3));
        joindate = ((EditText) findViewById(R.id.editTextDate));
        address = ((EditText) findViewById(R.id.editTextTextPersonName4));
        details = ((EditText) findViewById(R.id.editTextTextMultiLine));
        RadioButton male = (RadioButton) findViewById(R.id.radioButton);
        RadioButton female = (RadioButton) findViewById(R.id.radioButton2);
        spin= findViewById(R.id.spinner);


        dob = ((EditText) findViewById(R.id.editTextDate2));
        propic = findViewById(R.id.imageView);
        docs = findViewById(R.id.imageView5);
        selectImage = findViewById(R.id.button);
        selectDoc = findViewById(R.id.button13);
        submit = findViewById(R.id.button2);


        name.setText(m.getName());
        mobile.setText(m.getMob());
        email.setText(m.getEmail());

        ZonedDateTime l= ZonedDateTime.parse(m.getJoindate());
        DateTimeFormatter ft= DateTimeFormatter.ofPattern("dd/MM/yyyy");
        joindate.setText(ft.format(l).toString());
        joindate.setEnabled(false);

        address.setText(m.getAddress());
        details.setText(m.getDetails());
        if(m.getGender().equals("Male")){
            male.setChecked(true);
        }else if(m.getGender().equals("Female")){
            female.setChecked(true);
        }
        dob.setEnabled(false);

        if(!m.getDob().trim().equals("")) {
            ZonedDateTime l1 = ZonedDateTime.parse(m.getDob());
            DateTimeFormatter ft1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dob.setText(ft1.format(l1).toString());
        }else{
            dob.setText(m.getDob());
        }

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(EditMemberActivity.this);
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

        selectDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(EditMemberActivity.this);
                builder1.setMessage("Select Image from");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Camera",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                i = 2;
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 2);
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Gallery",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                i = 3;
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

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(EditMemberActivity.this,
                        arr[i],
                        Toast.LENGTH_LONG)
                        .show();
                p= DataList.batchList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter ad = new ArrayAdapter(EditMemberActivity.this,
                android.R.layout.simple_spinner_item, arr);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        spin.setAdapter(ad);

        ArrayAdapter myAdap = (ArrayAdapter) spin.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = myAdap.getPosition(m.getBatchname());
        spin.setSelection(spinnerPosition);

        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String gender = "";
                if (male.isChecked()) {
                    gender = "Male";
                } else if (female.isChecked()) {
                    gender = "Female";
                }
                if(p != null) {
                    if (gender.equals("Male") || gender.equals("Female")) {
                        if (!((name.getText().toString()).equals("")) &&
                                !((mobile.getText().toString()).equals("")) && mobile.length() == 10) {
                            if (joindate.getText().toString().equals("") ||
                                    verifyDate(joindate.getText().toString())) {
                                if (dob.getText().toString().equals("") ||
                                        verifyDate(dob.getText().toString())) {
                                    if (Integer.parseInt(p.getBatchtot()) < Integer.parseInt(p.getBatchMaxStrength())) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(EditMemberActivity.this);
                                        builder1.setMessage("Are you sure to add a member?");
                                        builder1.setCancelable(true);

                                        String finalGender = gender;
                                        builder1.setPositiveButton(
                                                "Yes",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        Member mem= new Member(name.getText().toString(),
                                                                m.getId(),
                                                                m.getPicurl(),
                                                                m.getDocurl(),
                                                                m.getJoindate(),
                                                                m.getFeepaydate(),
                                                                m.getExpdate(),
                                                                mobile.getText().toString(),
                                                                email.getText().toString(),
                                                                address.getText().toString(),
                                                                finalGender, dob.getText().toString(),
                                                                details.getText().toString(),
                                                                m.getStatus(),
                                                                ((p==null)?"":p.getBatchid()),
                                                                ((p==null)?"":p.getBatchname()));

                                                        FirebaseDatabase.getInstance().getReference()
                                                                .child("FITCRM")
                                                                .child("gyms")
                                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                .child("members")
                                                                .child(mem.getId())
                                                                .setValue(mem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(EditMemberActivity.this, "Valid data updated", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }
                                                        });
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
                                        Toast.makeText(EditMemberActivity.this, "This Batch is full", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(EditMemberActivity.this, "Invalid DOB", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(EditMemberActivity.this, "Invalid Join Date", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EditMemberActivity.this, "Invalid Data", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditMemberActivity.this, "Invalid Gender", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(EditMemberActivity.this, "Please select a batch", Toast.LENGTH_SHORT).show();
                }
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
            case 2:
                if (resultCode == RESULT_OK) {
                    // compare the resultCode with the
                    // SELECT_PICTURE constant
                    if (requestCode == 2) {
                        // Get the url of the image from data
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        docs.setImageBitmap(photo);
                    }
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    // compare the resultCode with the
                    // SELECT_PICTURE constant
                    if (requestCode == 200) {
                        // Get the url of the image from data
                        Uri selectedImageUri2 = data.getData();
                        if (null != selectedImageUri2) {
                            // update the preview image in the layout
                            docs.setImageURI(selectedImageUri2);
                        }
                    }
                }
                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean verifyDate(String date) {
        if (date.length() == 10 && date.charAt(2) == '/'
                && date.charAt(5) == '/') {
            int dd = Integer.parseInt(date.substring(0, 2));
            int mm = Integer.parseInt(date.substring(3, 5));
            int yr = Integer.parseInt(date.substring(6, 10));
            return dd <= 31 && mm <= 12 && yr <= (LocalDate.now().getYear());
        } else {
            return false;
        }
    }

}