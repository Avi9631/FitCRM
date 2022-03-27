package com.crm.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.crm.myapplication.ui.profile.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {


    private LinearLayout l1, l2;
    private EditText email, pass, confirmpass;
    private Button btn1;
    FirebaseAuth mAuth;

    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth= FirebaseAuth.getInstance();
        l1= findViewById(R.id.loginlinlay);
        email= findViewById(R.id.email);
        pass= findViewById(R.id.pass);
        confirmpass= findViewById(R.id.confirmpass);
        btn1= findViewById(R.id.btn1);

        loadingDialog= new Dialog(RegisterActivity.this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(email.getText().toString())) {
                    if(!TextUtils.isEmpty(pass.getText().toString())){
                        if(pass.getText().toString().equals(confirmpass.getText().toString())){
                            logup();
                        }else {
                            Toast.makeText(RegisterActivity.this, "Password do not match", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this, "Please enter a valid password.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void logup(){
        mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i=new Intent(RegisterActivity.this, ProfileActivity.class);
                            i.putExtra("email", email.getText().toString());
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Cannot be registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}