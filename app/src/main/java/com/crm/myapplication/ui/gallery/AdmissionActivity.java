package com.crm.myapplication.ui.gallery;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.crm.myapplication.DataList;
import com.crm.myapplication.LoginActivity;
import com.crm.myapplication.MainActivity;
import com.crm.myapplication.Models.Member;
import com.crm.myapplication.Models.MemberFee;
import com.crm.myapplication.Models.Plan;
import com.crm.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AdmissionActivity extends AppCompatActivity {

    Spinner spin;
    double total;
    double a, b, c, d;
    private TextView name, payScale, admsnFeeTxt, feeTxt, taxTxt, tot, discText;
    private Button submit;
    private EditText admsnFee, discFee, tax;

    private List<Plan> enableList= DataList.planList
            .stream().filter(m -> m.getStatus().equals("enable")).collect(Collectors.toList());
    private Plan p=null;
    private String arr[]=new String[enableList.size()];
    Uri uripro, uridoc;
    FirebaseStorage storage;
    StorageReference storageReference;

    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission);

        int k=0;
        for(Plan b: enableList){
            if(b.getStatus().equals("enable")) {
                arr[k] = b.getPlanname(); k++;
            }
        }

        loadingDialog= new Dialog(AdmissionActivity.this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        Member m = (Member) getIntent().getSerializableExtra("member");
        if(AddMemberFragment.uripro !=null)
        uripro = Uri.parse(getIntent().getStringExtra("uripro"));
        if(AddMemberFragment.uridoc !=null)
        uridoc = Uri.parse(getIntent().getStringExtra("uridoc"));

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        name = findViewById(R.id.textView29);
        payScale = findViewById(R.id.textView30);
        admsnFeeTxt = findViewById(R.id.textView20);
        feeTxt = findViewById(R.id.textView21);
        taxTxt = findViewById(R.id.textView22);
        tax = findViewById(R.id.edtTxt19);
        tot = findViewById(R.id.textView31);
        submit = findViewById(R.id.button17);
        name = findViewById(R.id.textView29);
        admsnFee = findViewById(R.id.editTextTextPersonName8);
        discFee = findViewById(R.id.editTextTextPersonName12);
        spin = findViewById(R.id.spinner4);
        discText = findViewById(R.id.textView23);

        name.setText("Name : " + m.getName());

        discFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")) {
                    d = Double.parseDouble(charSequence.toString());
                } else {
                    d = 0;
                }
                setData(m);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        admsnFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")) {
                    a = Double.parseDouble(charSequence.toString());
                } else {
                    a = 0;
                }
                setData(m);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")) {
                    b = Double.parseDouble(charSequence.toString());
                } else {
                    b = 0;
                }
                setData(m);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(AdmissionActivity.this,
                        arr[i],
                        Toast.LENGTH_LONG)
                        .show();
                p= enableList.get(i);
                c = Double.parseDouble(enableList.get(i).getPlanfee());
                setData(m);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter ad = new ArrayAdapter(AdmissionActivity.this,
                android.R.layout.simple_spinner_item, arr);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        spin.setAdapter(ad);


        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(total>0  && p!=null){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AdmissionActivity.this);
                    builder1.setMessage("Are you sure to add a member?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    ZonedDateTime l= ZonedDateTime.parse(m.getJoindate());
                                    ZonedDateTime l1 = null;
                                    if(p.getPlandurationtype().equals("Month")){
                                       l1= l.plusMonths(Long.parseLong(p.getPlanduration()));
                                    }else if(p.getPlandurationtype().equals("Day")){
                                        l1= l.plusDays(Long.parseLong(p.getPlanduration()));
                                    }
                                    m.setFeepaydate(ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).toString());
                                    m.setExpdate(l1.toString());
//                                    DataList.memberList.add(m);

                                    String mid= UUID.randomUUID().toString();
                                    m.setId(mid);
                                   uploadImage(m);

//                                    Intent i= new Intent(AdmissionActivity.this, MainActivity.class);
//                                    startActivity(i);
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
                }else {
                    Toast.makeText(AdmissionActivity.this, "Invalid Data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setData(Member m) {
        admsnFeeTxt.setText("Admission : Rs." + a + "/-");
        feeTxt.setText("Fee : Rs." + c + "/-");
        taxTxt.setText("Tax : Rs." + b + "/-");
        discText.setText("Disc : Rs." + d + "/-");
        total = (a + b + c) - d;
        tot.setText("Tot Amt to be Paid : Rs." + total + "/-");
        if(p==null){
            payScale.setText("Pay Scale: ");
        }else{
            ZonedDateTime l= ZonedDateTime.parse(m.getJoindate());
            ZonedDateTime l1 = null;
            if(p.getPlandurationtype().equals("Month")){
                l1= l.plusMonths(Long.parseLong(p.getPlanduration()));
            }else if(p.getPlandurationtype().equals("Day")){
                l1= l.plusDays(Long.parseLong(p.getPlanduration()));
            }

            DateTimeFormatter ft= DateTimeFormatter.ofPattern("dd/MM/uuuu");

            payScale.setText("Pay Scale: "+ft.format(l).toString()+" To "+ft.format(l1).toString());
        }
    }


    private void uploadData(Member m){
        loadingDialog.show();
        FirebaseDatabase.getInstance().getReference()
                .child("FITCRM")
                .child("gyms")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("members")
                .child(m.getId())
                .setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    FirebaseDatabase.getInstance().getReference()
                            .child("FITCRM")
                            .child("gyms")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("members")
                            .child(m.getId())
                            .child("fees")
                            .child(UUID.randomUUID().toString())
                            .setValue(new MemberFee(m.getId(),
                                    m.getId(),
                                    p.getPlanname(),
                                    p.getPlanfee(),
                                    String.valueOf(d),
                                    String.valueOf(a),
                                    String.valueOf(b),
                                    String.valueOf(0),
                                    String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Kolkata")))))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        FirebaseDatabase.getInstance().getReference()
                                                .child("FITCRM")
                                                .child("gyms")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("batch")
                                                .child(m.getBatch())
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                                                    int max= Integer.parseInt(snapshot.child("batchMaxStrength").getValue().toString());
                                                        int curr= Integer.parseInt(snapshot.child("batchtot").getValue().toString());
                                                        curr+=1;
                                                        snapshot.getRef().child("batchtot").setValue(curr);
                                                        Toast.makeText(AdmissionActivity.this, "Member Added", Toast.LENGTH_SHORT).show();
                                                        Intent i=new Intent(AdmissionActivity.this, MainActivity.class);
                                                        startActivity(i);
                                                        finish();
                                                        loadingDialog.dismiss();

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
loadingDialog.dismiss();
                                                    }
                                                });


                                    }
                                }
                            });
                }
            }
        });
    }

    private void uploadImage(Member m)
    {
        if (uripro != null) {
            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/propics/"
                                    + m.getId());
            StorageReference ref1
                    = storageReference
                    .child(
                            "/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/docpics/"
                                    + m.getId());


            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uripro);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //here you can choose quality factor in third parameter(ex. i choosen 25)
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] fileInBytes = baos.toByteArray();

            // adding listeners on upload
            // or failure of image
            loadingDialog.show();
            ref.putBytes(fileInBytes)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                             Toast.makeText(AdmissionActivity.this,
                                            "Image Uploaded!!",
                                            Toast.LENGTH_SHORT).show();

                                    if(uridoc != null){

                                        Bitmap bmp = null;
                                        try {
                                            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uridoc);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                        //here you can choose quality factor in third parameter(ex. i choosen 25)
                                        bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                                        byte[] fileInBytes1 = baos.toByteArray();

                                        ref1.putBytes(fileInBytes1)
                                                .addOnSuccessListener(
                                                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(
                                                                    UploadTask.TaskSnapshot taskSnapshot)
                                                            {

                                                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        m.setPicurl(uri.toString());
                                                                        ref1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                            @Override
                                                                            public void onSuccess(Uri uri) {
                                                                                m.setDocurl(uri.toString());
                                                                                uploadData(m);
                                                                                loadingDialog.dismiss();
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e)
                                                    {

                                                        // Error, Image not uploaded
                                                        Toast.makeText(AdmissionActivity.this,
                                                                "Failed " + e.getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                        loadingDialog.dismiss();
                                                    }
                                                });
                                    }else{
                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                m.setPicurl(uri.toString());uploadData(m);
                                                loadingDialog.dismiss();
                                            }
                                        });
                                    }
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            Toast
                                    .makeText(AdmissionActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                            loadingDialog.dismiss();
                        }
                    });
        }else {
            m.setPicurl("");
            m.setDocurl("");
            uploadData(m);
            loadingDialog.dismiss();
        }
    }

}