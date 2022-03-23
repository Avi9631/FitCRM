package com.crm.myapplication.ui.slideshow;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.crm.myapplication.DataList;
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

public class MemberDetailsActivity extends AppCompatActivity {

    TextView name;
    TextView id;
    TextView picurl;
    TextView docurl;
    TextView joindate;
    TextView expdate;
    TextView mob;
    TextView email;
    TextView address;
    TextView gender;
    TextView dob;
    Member member=null;

    Button call, payFee, msg, idCard, delete, block, edit , addDays;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);
        FirebaseDatabase.getInstance().getReference()
                .child("FITCRM")
                .child("gyms")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("members")
                .child(getIntent().getStringExtra("id"))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot d) {
                         member =new Member(
                                String.valueOf(d.child("name").getValue()),
                                String.valueOf(d.child("id").getValue()),
                                String.valueOf(d.child("picurl").getValue()),
                                String.valueOf(d.child("docurl").getValue()),
                                String.valueOf(d.child("joindate").getValue()),
                                String.valueOf(d.child("feepaydate").getValue()),
                                String.valueOf(d.child("expdate").getValue()),
                                String.valueOf(d.child("mob").getValue()),
                                String.valueOf(d.child("email").getValue()),
                                String.valueOf(d.child("address").getValue()),
                                String.valueOf(d.child("gender").getValue()),
                                String.valueOf(d.child("dob").getValue()),
                                String.valueOf(d.child("details").getValue()),
                                String.valueOf(d.child("status").getValue()),
                                String.valueOf(d.child("batch").getValue()),
                                 String.valueOf(d.child("batchname").getValue())
                        );
                         loaded();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


//
//        name= findViewById(R.id.textView4);
//        id= findViewById(R.id.textView5);
//        picurl= findViewById(R.id.textView6);
//        docurl= findViewById(R.id.textView7);
//        joindate= findViewById(R.id.textView8);
//        expdate= findViewById(R.id.textView9);
//        mob= findViewById(R.id.textView10);
//        email= findViewById(R.id.textView11);
//        address= findViewById(R.id.textView12);
//        gender= findViewById(R.id.textView13);
//        dob= findViewById(R.id.textView14);

        call= findViewById(R.id.button3);
        payFee= findViewById(R.id.button5);
        msg= findViewById(R.id.button4);
        msg.setVisibility(View.GONE);
        idCard= findViewById(R.id.button6);
        edit= findViewById(R.id.button9);
        block= findViewById(R.id.button10);
        delete= findViewById(R.id.button11);
        addDays= findViewById(R.id.button12);


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
//
//        name.setText("Name : \n"+
//                member.getName());
//        id.setText("ID :  \n"+
//                member.getId());
//        ZonedDateTime z1= ZonedDateTime.parse(member.getJoindate());
//        joindate.setText("Join Date :  \n"+ z1.getDayOfMonth() +"/"+z1.getMonthValue()+"/"+z1.getYear());
//        ZonedDateTime z= ZonedDateTime.parse(DataList.memberList.get(getIntent().getIntExtra("position", -1)).getExpdate());
//        expdate.setText("Exp Date :  \n"+ z.getDayOfMonth() +"/"+z.getMonthValue()+"/"+z.getYear());
//        mob.setText("Mob :  \n"+
//                member.getMob());
//        email.setText("Email :  \n"+
//                member.getEmail());
//        address.setText("Address :  \n"+
//                member.getAddress());
//        gender.setText("Gender :  \n"+
//                member.getGender());
//        dob.setText("DOB :  \n"+
//                member.getDob());

        String content = "<p>Name : " +
                member.getName() +
                "</p>"+
                "<p>ID : " +
                member.getId() +
                "</p>"+
                "<p>JoinDate : " +
                member.getJoindate() +
                "</p>"
                ;
        WebView wb= findViewById(R.id.webview);
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
                                i= new Intent(Intent.ACTION_CALL);
                                i.setData(Uri.parse("tel:"+mob.getText().toString()));
                                startActivity(i);
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
                                    }
                                });
                                break;
                            case "active":
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
                                    }
                                });
                                break;
                            case "edit":
                                i = new Intent(MemberDetailsActivity.this, EditMemberActivity.class);
                                i.putExtra("position", getIntent().getIntExtra("position", -1));
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
}