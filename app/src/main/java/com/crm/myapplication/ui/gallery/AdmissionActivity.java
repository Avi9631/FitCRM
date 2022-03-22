package com.crm.myapplication.ui.gallery;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.crm.myapplication.DataList;
import com.crm.myapplication.MainActivity;
import com.crm.myapplication.Models.Member;
import com.crm.myapplication.Models.MemberFee;
import com.crm.myapplication.Models.Plan;
import com.crm.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class AdmissionActivity extends AppCompatActivity {

    Spinner spin;
    double total;
    double a, b, c, d;
    private TextView name, payScale, admsnFeeTxt, feeTxt, taxTxt, tot, discText;
    private Button submit;
    private EditText admsnFee, discFee, tax;

    private String arr[]=new String[(int) DataList.planList
            .stream().filter(m -> m.getStatus().equals("enable")).count()];
    private Plan p=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission);


        int k=0;
        for(Plan b: DataList.planList){
            if(b.getStatus().equals("enable")) {
                arr[k] = b.getPlanname(); k++;
            }
        }

        Member m = (Member) getIntent().getSerializableExtra("member");

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
                p= DataList.planList.get(i);
                c = Double.parseDouble(DataList.planList.get(i).getPlanfee());
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
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("FITCRM")
                                            .child("gyms")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("members")
                                            .child(mid)
                                            .setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("FITCRM")
                                                        .child("gyms")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .child("members")
                                                        .child(mid)
                                                        .child("fees")
                                                        .child(UUID.randomUUID().toString())
                                                        .setValue(new MemberFee(mid,
                                                                m.getId(),
                                                                p.getPlanname(),
                                                                p.getPlanfee(),
                                                                String.valueOf(d),
                                                                String.valueOf(a),
                                                                String.valueOf(b),
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
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                }
                                                                            });

                                                                    Toast.makeText(AdmissionActivity.this, "Member Added", Toast.LENGTH_SHORT).show();
                                                                    Intent i=new Intent(AdmissionActivity.this, MainActivity.class);
                                                                    startActivity(i);
                                                                    finish();
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });

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
            String now= ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).getDayOfMonth()+"/"
                    +ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).getMonthValue()+"/"
                    +ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).getYear();
            payScale.setText("Pay Scale: "+now+" To "+l1.getDayOfMonth()+"/"+l1.getMonthValue()+"/"+l1.getYear());
        }
    }

}