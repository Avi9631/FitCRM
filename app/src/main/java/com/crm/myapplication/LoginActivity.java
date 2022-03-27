package com.crm.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.crm.myapplication.Models.Batch;
import com.crm.myapplication.Models.Plan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout l1, l2;
    private EditText email, pass;
    private Button btn1, btn2;
    private TextView forgotPass;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth= FirebaseAuth.getInstance();
        l1= findViewById(R.id.loginlinlay);
        email= findViewById(R.id.email);
        pass= findViewById(R.id.pass);
        btn1= findViewById(R.id.btn1);
        btn2= findViewById(R.id.btn2);
        forgotPass= findViewById(R.id.textView24);

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!email.getText().toString().equals("")) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                                        builder1.setMessage("We have send you a mail for password reset?");
                                        builder1.setCancelable(true);

                                        builder1.setPositiveButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        FirebaseAuth.getInstance().sendPasswordResetEmail("user@example.com")
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(LoginActivity.this, "Please check your registered email for password reset", Toast.LENGTH_SHORT).show();;
                                                                        }
                                                                    }
                                                                });
                                                        dialog.cancel();
                                                    }
                                                });

                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Email not registered", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(LoginActivity.this, "Please input your email to reset the password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(email.getText().toString())) {
                    if(!TextUtils.isEmpty(pass.getText().toString())){
//                        checkLogin(email.getText().toString(), pass.getText().toString());
                        login();
                    }else{
                        Toast.makeText(LoginActivity.this, "Please enter a valid password.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    public void login(){
        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent i=new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            loadFromDB();
        }
    }

    public void loadFromDB(){
        if(com.crm.myapplication.DataList.batchList.size()>0){
            com.crm.myapplication.DataList.batchList.clear();
        }
        if(com.crm.myapplication.DataList.planList.size()>0){
            com.crm.myapplication.DataList.planList.clear();
        }
        FirebaseDatabase.getInstance().getReference()
                .child("FITCRM")
                .child("gyms")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("batch")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot d: snapshot.getChildren()){
                            com.crm.myapplication.DataList.batchList.add(new Batch(
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
                                            com.crm.myapplication.DataList.planList.add(new Plan(
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
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
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

}