package com.crm.myapplication.ui.batch;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crm.myapplication.Adapters.BatchAdapter;
import com.crm.myapplication.DataList;
import com.crm.myapplication.Models.Batch;
import com.crm.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BatchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Nullable
    static Dialog loadingDialog;
    @Nullable
    private static BatchAdapter memberAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton floatingActionButton;
    private RecyclerView view;
    public BatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BatchFragment.
     */
    // TODO: Rename and change types and number of parameters
    @NonNull
    public static BatchFragment newInstance(String param1, String param2) {
        BatchFragment fragment = new BatchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static void changeStatus(@NonNull String id, @NonNull String status) {
        if (status.equals("enable")) {
            loadingDialog.show();
            FirebaseDatabase.getInstance().getReference()
                    .child("FITCRM")
                    .child("gyms")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("batch")
                    .child(id)
                    .child("status")
                    .setValue("disable").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        loadBatchData();
                        loadingDialog.dismiss();
                    } else {
                        loadingDialog.dismiss();
                    }
                }
            });
        } else {
            loadingDialog.show();
            FirebaseDatabase.getInstance().getReference()
                    .child("FITCRM")
                    .child("gyms")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("batch")
                    .child(id)
                    .child("status")
                    .setValue("enable").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        loadBatchData();
                        loadingDialog.dismiss();
                    } else {
                        loadingDialog.dismiss();
                    }
                }
            });
        }
    }

    public static void loadBatchData() {
        if (DataList.batchList.size() > 0) {
            DataList.batchList.clear();
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
                        for (DataSnapshot d : snapshot.getChildren()) {
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
                        memberAdapter.notifyDataSetChanged();
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loadingDialog.dismiss();
                    }
                });

    }

    public static void deletePlan(@NonNull String id, int pos, Context ctx) {
        if (Integer.parseInt(DataList.batchList.get(pos).getBatchtot()) == 0) {
            loadingDialog.show();
            FirebaseDatabase.getInstance().getReference()
                    .child("FITCRM")
                    .child("gyms")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("batch")
                    .child(id)
                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        loadBatchData();
                        loadingDialog.dismiss();
                        Toast.makeText(ctx, "Batch Deleted Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(ctx, "Error Occurred while deleting the Batch", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            loadingDialog.dismiss();
            Toast.makeText(ctx, "Batch has members allocated", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_batch, container, false);

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        view = v.findViewById(R.id.planrec);
        floatingActionButton = v.findViewById(R.id.floatingActionButton);

        LinearLayoutManager l = new LinearLayoutManager(getContext());
        l.setOrientation(RecyclerView.VERTICAL);
        view.setLayoutManager(l);
        view.setHasFixedSize(true);
        view.setNestedScrollingEnabled(false);

        if (DataList.batchList.size() > 0) {
            DataList.batchList.clear();
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

                        for (DataSnapshot d : snapshot.getChildren()) {
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
                        memberAdapter = new BatchAdapter(getContext(), DataList.batchList);
                        view.setAdapter(memberAdapter);
                        memberAdapter.notifyDataSetChanged();
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loadingDialog.dismiss();
                    }
                });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddBatchActivity.class);
                startActivity(i);
            }
        });

        return v;
    }
}