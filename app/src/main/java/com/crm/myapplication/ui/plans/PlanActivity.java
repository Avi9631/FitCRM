package com.crm.myapplication.ui.plans;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.crm.myapplication.Models.Plan;
import com.crm.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class PlanActivity extends AppCompatActivity {

    EditText planname, fee, duration, desc;
    String durationType;

    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Plans");

        planname = findViewById(R.id.editTextTextPersonName);
        fee = findViewById(R.id.editTextTextPersonName2);
        duration = findViewById(R.id.editTextDate);
        desc = findViewById(R.id.editTextDate2);
        RadioButton day = (RadioButton) findViewById(R.id.radioButton);
        RadioButton month = (RadioButton) findViewById(R.id.radioButton2);

        loadingDialog = new Dialog(PlanActivity.this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        Button btn = findViewById(R.id.button2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String durationType = "";
                if (day.isChecked()) {
                    durationType = "Day";
                } else if (month.isChecked()) {
                    durationType = "Month";
                }
                if ((durationType.equals("Day") || durationType.equals("Month"))
                        && !planname.getText().toString().equals("") &&
                        !fee.getText().toString().equals("") &&
                        !duration.getText().toString().equals("")) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(PlanActivity.this);
                    builder1.setMessage("Are you sure to add a plan?");
                    builder1.setCancelable(true);

                    String finalDurationType = durationType;
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                public void onClick(@NonNull DialogInterface dialog, int id) {
                                    loadingDialog.show();
                                    String pid = UUID.randomUUID().toString();
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("FITCRM")
                                            .child("gyms")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("plans")
                                            .child(pid)
                                            .setValue(new Plan(pid,
                                                    planname.getText().toString(),
                                                    fee.getText().toString(),
                                                    duration.getText().toString(),
                                                    finalDurationType,
                                                    desc.getText().toString(), "enable",
                                                    ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).toString()))
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    finish();
                                                    loadingDialog.dismiss();
                                                    dialog.cancel();
                                                }
                                            });
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(@NonNull DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
            }
        });

    }

}