package com.crm.myapplication.ui.plans;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crm.myapplication.Adapters.PlanAdapter;
import com.crm.myapplication.DataList;
import com.crm.myapplication.Models.Plan;
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
 * Use the {@link PlansFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlansFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlansFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlansFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlansFragment newInstance(String param1, String param2) {
        PlansFragment fragment = new PlansFragment();
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

    private FloatingActionButton floatingActionButton;
    private RecyclerView view;
    private static PlanAdapter memberAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_plans, container, false);
        view= v.findViewById(R.id.planrec);
        floatingActionButton= v.findViewById(R.id.floatingActionButton);

        LinearLayoutManager l= new LinearLayoutManager(getContext());
        l.setOrientation(RecyclerView.VERTICAL);
        view.setLayoutManager(l);
        view.setHasFixedSize(true);
        view.setNestedScrollingEnabled(false);

        if(DataList.planList.size()>0){
            DataList.planList.clear();
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
                        memberAdapter = new PlanAdapter(getContext(), DataList.planList );
                        view.setAdapter(memberAdapter);
                        memberAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), PlanActivity.class);
                startActivity(i);
            }
        });

        return v;
    }

    public static void changeStatus(String id, String status){
        if(status.equals("enable")) {
            FirebaseDatabase.getInstance().getReference()
                    .child("FITCRM")
                    .child("gyms")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("plans")
                    .child(id)
                    .child("status")
                    .setValue("disable").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        loadPlanData();
                    }
                }
            });

        }else{
            FirebaseDatabase.getInstance().getReference()
                    .child("FITCRM")
                    .child("gyms")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("plans")
                    .child(id)
                    .child("status")
                    .setValue("enable").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        loadPlanData();
                    }
                }
            });
        }

    }

    public static void loadPlanData(){
        if(DataList.planList.size()>0){
            DataList.planList.clear();
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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

//    public  static  void deletePlan(int pos){
//        DataList.planList.remove(pos);
//        memberAdapter.notifyDataSetChanged();
//    }
}