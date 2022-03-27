package com.crm.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.crm.myapplication.Models.Batch;
import com.crm.myapplication.Models.Plan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreenActivity extends AppCompatActivity {

    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        loadingDialog= new Dialog(SplashScreenActivity.this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            loadFromDB();
        }else{
            Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(i);finish();
        }

    }

    public void loadFromDB(){
        if(DataList.batchList.size()>0){
            DataList.batchList.clear();
        }
        if(DataList.planList.size()>0){
            DataList.planList.clear();
        }
        loadingDialog.show();
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
                                        Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                                        startActivity(i);finish();
                                        loadingDialog.dismiss();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
loadingDialog.dismiss();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
loadingDialog.dismiss();
                    }
                });
    }

}