package com.crm.myapplication.ui.payfee;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PayFeeActivity extends AppCompatActivity {

    Spinner spin;
    double total;
    double a, b, c, d, f;
    private TextView name, payScale, admsnFeeTxt, feeTxt, taxTxt, tot, discText, fineTxt;
    private Button submit;
    private EditText admsnFee, discFee, tax, fine;

    private List<Plan> enableList= DataList.planList
            .stream().filter(m -> m.getStatus().equals("enable")).collect(Collectors.toList());
    private String arr[]=new String[enableList.size()];
    private Plan p=null;
    Member m=null;

    Dialog loadingDialog;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_fee);
         m = (Member) getIntent().getSerializableExtra("member");

        loadingDialog= new Dialog(PayFeeActivity.this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        int k=0;
        for(Plan b: enableList){
            if(b.getStatus().equals("enable")) {
                arr[k] = b.getPlanname(); k++;
            }
        }

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
        fine= findViewById(R.id.editTextTextPersonName5);
        fineTxt= findViewById(R.id.textView26);

        name.setText("Name : " + m.getName());

        fine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")) {
                    f = Double.parseDouble(charSequence.toString());
                } else {
                    f = 0;
                }
                setData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        discFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")) {
                    d = Double.parseDouble(charSequence.toString());
                } else {
                    d = 0;
                }
                setData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        admsnFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")) {
                    a = Double.parseDouble(charSequence.toString());
                } else {
                    a = 0;
                }
                setData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")) {
                    b = Double.parseDouble(charSequence.toString());
                } else {
                    b = 0;
                }
                setData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(PayFeeActivity.this,
                        arr[i],
                        Toast.LENGTH_LONG)
                        .show();
                p= enableList.get(i);
                c = Double.parseDouble(enableList.get(i).getPlanfee());
                setData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter ad = new ArrayAdapter(PayFeeActivity.this,
                android.R.layout.simple_spinner_item, arr);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        spin.setAdapter(ad);


        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(total>0){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(PayFeeActivity.this);
                    builder1.setMessage("Are you sure to pay the fee?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
loadingDialog.show();
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("FITCRM")
                                            .child("gyms")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("members")
                                            .child(m.getId())
                                            .child("fees")
                                            .child(UUID.randomUUID().toString())
                                            .setValue(new MemberFee(UUID.randomUUID().toString(),
                                                    m.getId(),
                                                    p.getPlanname(),
                                                    p.getPlanfee(),
                                                    String.valueOf(d),
                                                    String.valueOf(a),
                                                    String.valueOf(b),
                                                    String.valueOf(f),
                                                    String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Kolkata")))))
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    ZonedDateTime l= ZonedDateTime.parse(m.getExpdate());
                                                    ZonedDateTime l1 = null;
                                                    if(p.getPlandurationtype().equals("Month")){
                                                        l1= l.plusMonths(Long.parseLong(p.getPlanduration()));
                                                    }else if(p.getPlandurationtype().equals("Day")){
                                                        l1= l.plusDays(Long.parseLong(p.getPlanduration()));
                                                    }

                                                    ZonedDateTime finalL = l1;
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("FITCRM")
                                                            .child("gyms")
                                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .child("members")
                                                            .child(m.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            snapshot.getRef().child("expdate").setValue(finalL.toString());
                                                            snapshot.getRef().child("feepaydate").setValue(ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).toString());
                                                            loadingDialog.dismiss();
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
loadingDialog.dismiss();
                                                        }
                                                    });
                                                }
                                            });

                                    Toast.makeText(PayFeeActivity.this, "Fee Paid "+m.getExpdate(), Toast.LENGTH_SHORT).show();
                                    finish();
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
                    Toast.makeText(PayFeeActivity.this, "Invalid Data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setData() {
        admsnFeeTxt.setText("Admission : Rs." + a + "/-");
        feeTxt.setText("Fee : Rs." + c + "/-");
        taxTxt.setText("Tax : Rs." + b + "/-");
        discText.setText("Disc : Rs." + d + "/-");
        fineTxt.setText("Fine : Rs."+f+"/-");
        total = (a + b + c + f) - d;
        tot.setText("Tot Amt to be Paid : Rs." + total + "/-");

        if(p==null){
            payScale.setText("Pay Scale: ");
        }else{
            ZonedDateTime l= ZonedDateTime.parse(m.getExpdate());
            ZonedDateTime l1 = null;
            if(p.getPlandurationtype().equals("Month")){
                l1= l.plusMonths(Long.parseLong(p.getPlanduration()));
            }else if(p.getPlandurationtype().equals("Day")){
                l1= l.plusDays(Long.parseLong(p.getPlanduration()));
            }
            DateTimeFormatter ft= DateTimeFormatter.ofPattern("dd/MM/uuuu");

            payScale.setText("Pay Scale: "+ft.format(l).toString()+" To "+ft.format(l1).toString());        }

    }
}

