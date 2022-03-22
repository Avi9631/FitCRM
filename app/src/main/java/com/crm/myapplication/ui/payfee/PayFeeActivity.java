package com.crm.myapplication.ui.payfee;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.crm.myapplication.DataList;
import com.crm.myapplication.Models.Member;
import com.crm.myapplication.Models.MemberFee;
import com.crm.myapplication.Models.Plan;
import com.crm.myapplication.R;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class PayFeeActivity extends AppCompatActivity {

    Spinner spin;
    double total;
    double a, b, c, d, f;
    private TextView name, payScale, admsnFeeTxt, feeTxt, taxTxt, tot, discText, fineTxt;
    private Button submit;
    private EditText admsnFee, discFee, tax, fine;
    private String arr[]=new String[DataList.planList.size()];
    private Plan p=null;
    Member m=null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_fee);
         m = (Member) getIntent().getSerializableExtra("member");

        for(int i=0;i< DataList.planList.size(); i++){
            arr[i] = DataList.planList.get(i).getPlanname();
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
                p= DataList.planList.get(i);
                c = Double.parseDouble(DataList.planList.get(i).getPlanfee());
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

                                    DataList.feeList.add(new MemberFee(UUID.randomUUID().toString(),
                                            m.getId(),
                                            p.getPlanname(),
                                            p.getPlanfee(),
                                            String.valueOf(d),
                                            String.valueOf(a),
                                            String.valueOf(b),
                                            String.valueOf(ZonedDateTime.now(ZoneId.of("Asia/Kolkata")))));
                                    ZonedDateTime l= ZonedDateTime.parse(m.getExpdate());
                                    ZonedDateTime l1 = null;
                                    if(p.getPlandurationtype().equals("Month")){
                                        l1= l.plusMonths(Long.parseLong(p.getPlanduration()));
                                        DataList.memberList.get(getIntent().getIntExtra("position", -1)).setExpdate(l1.toString());
                                    }else if(p.getPlandurationtype().equals("Day")){
                                        l1= l.plusDays(Long.parseLong(p.getPlanduration()));
                                        DataList.memberList.get(getIntent().getIntExtra("position", -1)).setExpdate(l1.toString());
                                    }
                                    DataList.memberList.get(getIntent().getIntExtra("position", -1)).setFeepaydate(ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).toString());
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
            String now= ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).getDayOfMonth()+"/"
                    +ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).getMonthValue()+"/"
                    +ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).getYear();
            payScale.setText("Pay Scale: "+now+" To "+l1.getDayOfMonth()+"/"+l1.getMonthValue()+"/"+l1.getYear());        }

    }
}


//get vcs
//url
//install git
//locate folder and git bash