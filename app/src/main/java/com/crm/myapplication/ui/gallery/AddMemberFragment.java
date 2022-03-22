package com.crm.myapplication.ui.gallery;

import static android.app.Activity.RESULT_OK;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.crm.myapplication.DataList;
import com.crm.myapplication.Models.Batch;
import com.crm.myapplication.Models.Member;
import com.crm.myapplication.R;
import com.crm.myapplication.ui.plans.PlansFragment;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMemberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMemberFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ImageView propic, docs;
    EditText name, mobile, email, joindate, address, dob, details;
    Button selectImage, submit, selectDoc;
    int i = -1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public AddMemberFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddMemberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMemberFragment newInstance(String param1, String param2) {
        AddMemberFragment fragment = new AddMemberFragment();
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
    private String arr[]=new String[(int) DataList.batchList
            .stream().filter(m -> m.getStatus().equals("enable")).count()];
    Batch p;
    Spinner spin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_member, container, false);
        int k=0;
        for(Batch b: DataList.batchList){
            if(b.getStatus().equals("enable")) {
                arr[k] = b.getBatchname(); k++;
            }
        }
        name = ((EditText) view.findViewById(R.id.editTextTextPersonName));
        mobile = ((EditText) view.findViewById(R.id.editTextTextPersonName2));
        email = ((EditText) view.findViewById(R.id.editTextTextPersonName3));
        joindate = ((EditText) view.findViewById(R.id.editTextDate));
        address = ((EditText) view.findViewById(R.id.editTextTextPersonName4));
        details = ((EditText) view.findViewById(R.id.editTextTextMultiLine));
        RadioButton male = (RadioButton) view.findViewById(R.id.radioButton);
        RadioButton female = (RadioButton) view.findViewById(R.id.radioButton2);
        spin= view.findViewById(R.id.spinner);

        dob = ((EditText) view.findViewById(R.id.editTextDate2));
        propic = view.findViewById(R.id.imageView);
        docs = view.findViewById(R.id.imageView5);
        selectImage = view.findViewById(R.id.button);
        selectDoc = view.findViewById(R.id.button13);
        submit = view.findViewById(R.id.button2);


        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(requireContext());
                builder1.setMessage("Select Image from");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Camera",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                i = 0;
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 0);
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Gallery",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                i = 1;
                                imageChooser();
                                dialog.cancel();
                            }
                        });

                builder1.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });

        selectDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(requireContext());
                builder1.setMessage("Select Image from");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Camera",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                i = 2;
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 2);
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Gallery",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                i = 3;
                                imageChooser();
                                dialog.cancel();
                            }
                        });

                builder1.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String gender = "";
                if (male.isChecked()) {
                    gender = "Male";
                } else if (female.isChecked()) {
                    gender = "Female";
                }
                if (gender.equals("Male") || gender.equals("Female")) {
                    if (!((name.getText().toString()).equals("")) &&
                            !((mobile.getText().toString()).equals("")) && mobile.length() == 10) {
                        if (joindate.getText().toString().equals("") ||
                                verifyDate(joindate.getText().toString())) {
                            if (dob.getText().toString().equals("") ||
                                    verifyDate(dob.getText().toString())) {
                                if(Integer.parseInt(p.getBatchtot()) < Integer.parseInt(p.getBatchMaxStrength())) {
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                    builder1.setMessage("Are you sure to add a member?");
                                    builder1.setCancelable(true);

                                    String finalGender = gender;
                                    builder1.setPositiveButton(
                                            "Yes",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                    Member m = new Member(name.getText().toString(),
                                                            "",
                                                            "", "",
                                                            (joindate.getText().toString()).equals("") ? ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).toString() : joindate.getText().toString(),
                                                            "", "", mobile.getText().toString(),
                                                            email.getText().toString(),
                                                            address.getText().toString(),
                                                            finalGender, dob.getText().toString(),
                                                            details.getText().toString(),
                                                            "active",
                                                            ((p == null) ? "" : p.getBatchid()),
                                                            ((p == null) ? "" : p.getBatchname()));
                                                    PlansFragment.loadPlanData();
                                                    Intent i = new Intent(getContext(), AdmissionActivity.class);
                                                    i.putExtra("member", m);
                                                    startActivity(i);
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
                                }else {
                                    Toast.makeText(getContext(), "This Batch is full", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Invalid DOB", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Invalid Join Date", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Invalid Data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Invalid Gender", Toast.LENGTH_SHORT).show();
                }
            }
        });


        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),
                        arr[i],
                        Toast.LENGTH_LONG)
                        .show();
                p= DataList.batchList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter ad = new ArrayAdapter(getContext(),
                android.R.layout.simple_spinner_item, arr);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        spin.setAdapter(ad);

        return view;
    }

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (i) {
            case 0:
                if (resultCode == RESULT_OK) {
                    // compare the resultCode with the
                    // SELECT_PICTURE constant
                    if (requestCode == 0) {
                        // Get the url of the image from data
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        propic.setImageBitmap(photo);
                    }
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    // compare the resultCode with the
                    // SELECT_PICTURE constant
                    if (requestCode == 200) {
                        // Get the url of the image from data
                        Uri selectedImageUri1 = data.getData();
                        if (null != selectedImageUri1) {
                            // update the preview image in the layout
                            propic.setImageURI(selectedImageUri1);
                        }
                    }
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    // compare the resultCode with the
                    // SELECT_PICTURE constant
                    if (requestCode == 2) {
                        // Get the url of the image from data
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        docs.setImageBitmap(photo);
                    }
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    // compare the resultCode with the
                    // SELECT_PICTURE constant
                    if (requestCode == 200) {
                        // Get the url of the image from data
                        Uri selectedImageUri2 = data.getData();
                        if (null != selectedImageUri2) {
                            // update the preview image in the layout
                            docs.setImageURI(selectedImageUri2);
                        }
                    }
                }
                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean verifyDate(String date) {
        if (date.length() == 10 && date.charAt(2) == '/'
                && date.charAt(5) == '/') {
            int dd = Integer.parseInt(date.substring(0, 2));
            int mm = Integer.parseInt(date.substring(3, 5));
            int yr = Integer.parseInt(date.substring(6, 10));
            return dd <= 31 && mm <= 12 && yr <= (LocalDate.now().getYear());
        } else {
            return false;
        }
    }
}