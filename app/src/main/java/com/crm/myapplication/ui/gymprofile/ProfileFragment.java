package com.crm.myapplication.ui.gymprofile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.crm.myapplication.DataList;
import com.crm.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    EditText name, mobile, email, address, details;
    Button  submit;
    int i = -1;
    public static String gid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile, container, false);


        name = (v.findViewById(R.id.editTextTextPersonName));
        mobile = (v.findViewById(R.id.editTextTextPersonName2));
        email = ((EditText) v.findViewById(R.id.editTextTextPersonName3));
        address = ((EditText) v.findViewById(R.id.editTextTextPersonName4));
        details = ((EditText) v.findViewById(R.id.editTextTextMultiLine));
        submit = v.findViewById(R.id.button2);

        email.setEnabled(false);

        FirebaseDatabase.getInstance().getReference()
                .child("FITCRM")
                .child("gyms")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DataList.name =String.valueOf(snapshot.child("gym_name").getValue());
                        DataList.mobile =String.valueOf(snapshot.child("gym_mobile").getValue());
                        DataList.email =String.valueOf(snapshot.child("gym_email").getValue());
                        DataList.address =String.valueOf(snapshot.child("gym_address").getValue());
                        DataList.details =String.valueOf(snapshot.child("gym_details").getValue());
                        name.setText(DataList.name);
                        mobile.setText(DataList.mobile);
                        email.setText(DataList.email);
                        address.setText(DataList.address);
                        details.setText(DataList.details);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (!((name.getText().toString()).equals("")) &&
                        !((mobile.getText().toString()).equals("")) && mobile.length() == 10 &&
                        !((email.getText().toString()).equals(""))) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Are you sure to save the info?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    saveToDB();
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
                } else {
                    Toast.makeText(getContext(), "Invalid Data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }



    private void saveToDB() {

        Map<String, String> map=new HashMap<>();
        map.put("gym_name", name.getText().toString());
        map.put("gym_mobile", mobile.getText().toString());
        map.put("gym_email", email.getText().toString());
        map.put("gym_address", address.getText().toString());
        map.put("gym_details", details.getText().toString());

        FirebaseDatabase.getInstance().getReference()
                .child("FITCRM")
                .child("gyms")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(getContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(getContext(), "Error Occured!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}



