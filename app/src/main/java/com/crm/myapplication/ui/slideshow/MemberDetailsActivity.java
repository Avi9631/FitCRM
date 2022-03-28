package com.crm.myapplication.ui.slideshow;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.crm.myapplication.DataList;
import com.crm.myapplication.LoginActivity;
import com.crm.myapplication.Models.Member;
import com.crm.myapplication.R;
import com.crm.myapplication.ui.payfee.PayFeeActivity;
import com.crm.myapplication.Models.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MemberDetailsActivity extends AppCompatActivity {

    Member member=null;
    ImageView v1, v2;
String dobstr;
    Button call, payFee, msg, idCard, delete, block, edit , addDays;

    Dialog loadingDialog;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Member Details");

        call= findViewById(R.id.button3);
        payFee= findViewById(R.id.button5);
        msg= findViewById(R.id.button4);
        msg.setVisibility(View.GONE);
        idCard= findViewById(R.id.button6);
        edit= findViewById(R.id.button9);
        block= findViewById(R.id.button10);
        delete= findViewById(R.id.button11);
        addDays= findViewById(R.id.button12);

        loadingDialog= new Dialog(MemberDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        v1= findViewById(R.id.imageView2);
        v2= findViewById(R.id.imageView3);

         member= DataList.memberList.stream()
                .filter(c-> c.getId().equals(getIntent().getStringExtra("id")))
                .findAny()
                .orElse(null);

                loaded();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loaded(){
        if(member
                .getStatus()
                .equals("blocked")){
            block.setText("UNBLOCK");
        }else{
            block.setText("BLOCK");
        }

        Glide.with(this).load(member.getPicurl()).into(v1);
        Glide.with(this).load(member.getDocurl()).into(v2);

        DateTimeFormatter ft= DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if(!member.getDob().trim().equals("")) {
            ZonedDateTime l1 = ZonedDateTime.parse(member.getDob());
            DateTimeFormatter ft1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dobstr= (ft1.format(l1).toString());
        }else{
            dobstr= member.getDob();
        }

        String content = "<p><b>Name : </b>" +
                member.getName() +
                "</p>"+
                "<p><b>ID : </b>" +
                member.getId() +
                "</p>"+
                "<p><b>Join Date : </b>" +
                ft.format(ZonedDateTime.parse(member.getJoindate())).toString() +
                "</p>"+
                "</p>"+
                "<p><b>Exp Date : </b>" +
                ft.format(ZonedDateTime.parse(member.getExpdate())).toString() +
                "</p>"+
                "</p>"+
                "<p><b>Mobile : </b>" +
                member.getMob() +
                "</p>"+
                "</p>"+
                "<p><b>Email : </b>" +
                member.getEmail() +
                "</p>"+
                "</p>"+
                "<p><b>Address : </b>" +
                member.getAddress() +
                "</p>"+
                "</p>"+
                "<p><b>Gender : </b>" +
                member.getGender() +
                "</p>"+
                "<p><b>DOB : </b>" +
                dobstr +
                "</p>"+
                "<p><b>Batch : </b>" +
                member.getBatchname()+
                "</p>"+
                "<p><b>Gender : </b>" +
                member.getGender() +
                "</p>"+
                "<p><b>Last Fee Pay Date : </b>" +
                ft.format(ZonedDateTime.parse(member.getFeepaydate())).toString() +
                "</p>"+
                "<p><b>Member Details : </b>" +
                member.getDetails() +
                "</p>"+
                "<p><b>Member Status : </b>" +
                member.getStatus() +
                "</p>"+
        "<p><b>Member Image : </b>" +
                "<a href="+member.getPicurl()+">"+member.getPicurl() +
                "</a></p>"+
        "<p><b>Member Document Image : </b>" +
                "<a href="+member.getDocurl()+">"+member.getDocurl() +
                "</a></p>"
                ;
        WebView wb= findViewById(R.id.webview);

        // disable scroll on touch
        wb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        wb.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("Are you sure you want to call", "call");
            }
        });

        idCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("Are you sure you want to generate ID", "id");
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("Are you sure you want to edit details?", "edit");
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("Are you sure you want to delete member?", "delete");
            }
        });

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(member
                        .getStatus()
                        .equals("blocked"))
                    showAlert("Are you sure you want to block member?", "active");
                else{
                    showAlert("Are you sure you want to unblock member?", "block");
                }
            }
        });

        payFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("Are you sure you want to Pay Fee?", "payfee");
            }
        });

        addDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void showAlert( String s, String m){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MemberDetailsActivity.this);
        builder1.setMessage(s);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i;
                        switch(m){
                            case "call":
//                                i= new Intent(Intent.ACTION_CALL);
//                                i.setData(Uri.parse("tel:"+member.getMob()));
//                                startActivity(i);
                                callAtRuntime();
                                break;
                            case "payfee":
                                i=new Intent(MemberDetailsActivity.this, PayFeeActivity.class);
                                i.putExtra("member", member);
                                i.putExtra("position", getIntent().getIntExtra("position", -1));
                                i.putExtra("id", id);
                                startActivity(i);
                                break;
                            case "msg":
                                Toast.makeText(MemberDetailsActivity.this, "Sending SMS", Toast.LENGTH_SHORT).show();
                                break;
                            case "id":
                                Toast.makeText(MemberDetailsActivity.this, "Generating ID card", Toast.LENGTH_SHORT).show();
                                break;
                            case "delete":
                                FirebaseDatabase.getInstance().getReference()
                                        .child("FITCRM")
                                        .child("gyms")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("members")
                                        .child(member.getId())
                                        .child("status").setValue("trashed").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(MemberDetailsActivity.this, "Member Moved to Trash", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                break;
                            case "block":
                                loadingDialog.show();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("FITCRM")
                                        .child("gyms")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("members")
                                        .child(member.getId())
                                        .child("status").setValue("blocked").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(MemberDetailsActivity.this, "Member Moved to Blocked List", Toast.LENGTH_SHORT).show();
                                        }
                                        loadingDialog.dismiss();
                                    }
                                });
                                break;
                            case "active":
                                loadingDialog.show();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("FITCRM")
                                        .child("gyms")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("members")
                                        .child(member.getId())
                                        .child("status").setValue("active").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(MemberDetailsActivity.this, "Member Moved to Active List", Toast.LENGTH_SHORT).show();
                                        }
                                        loadingDialog.dismiss();
                                    }
                                });
                                break;
                            case "edit":
                                i = new Intent(MemberDetailsActivity.this, EditMemberActivity.class);
                                i.putExtra("id", member.getId());
                                startActivity(i);
                                break;
//                            case "adddays":
//                                ZonedDateTime l= ZonedDateTime.parse(DataList.memberList.get(getIntent().getIntExtra("position", -1)).getExpdate());
//                                ZonedDateTime l1 = l.plusDays(9);
//
//                                Toast.makeText(MemberDetailsActivity.this, "Added days", Toast.LENGTH_SHORT).show();
//                                break;
                        }
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
    }

    public void callAtRuntime(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
        else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+member.getMob()));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callAtRuntime();
            } else {
                Toast.makeText(this, "Oh No !!!Permission Denied.Try Again !",Toast.LENGTH_SHORT).show();
            }
        }
    }
}