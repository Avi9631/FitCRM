package com.crm.myapplication.ui.batch;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.crm.myapplication.Models.Batch;
import com.crm.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class AddBatchActivity extends AppCompatActivity {

    EditText batchname, batchstrength, batchdesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_batch);

        batchname = findViewById(R.id.editTextTextPersonName);
        batchstrength = findViewById(R.id.editTextTextPersonName2);
        batchdesc = findViewById(R.id.editTextDate2);

        Button btn = findViewById(R.id.button2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!batchname.getText().toString().equals("") &&
                        !batchdesc.getText().toString().equals("")) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AddBatchActivity.this);
                    builder1.setMessage("Are you sure to add a batch?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                public void onClick(DialogInterface dialog, int id) {
                                    String bid=UUID.randomUUID().toString();
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("FITCRM")
                                            .child("gyms")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("batch")
                                            .child(bid)
                                            .setValue(new Batch(bid,
                                                    batchname.getText().toString(),
                                                    batchdesc.getText().toString(), "0",
                                                    batchstrength.getText().toString(),
                                                    ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).toString(),
                                                    "enable"));


//                                    DataList.batchList.add(new Batch(UUID.randomUUID().toString(),
//                                            batchname.getText().toString(),
//                                            batchdesc.getText().toString(), "0",
//                                            batchstrength.getText().toString(),
//                                            ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).toString(),
//                                            "enable"));
//                                    Toast.makeText(AddBatchActivity.this,
//                                            String.valueOf(DataList.batchList.size()),
//                                            Toast.LENGTH_SHORT).show();
                                    finish();
                                    dialog.cancel();
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
        });

    }

}