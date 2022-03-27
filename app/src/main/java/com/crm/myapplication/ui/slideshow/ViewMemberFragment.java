package com.crm.myapplication.ui.slideshow;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crm.myapplication.Adapters.MemberAdapter;
import com.crm.myapplication.DataList;
import com.crm.myapplication.LoginActivity;
import com.crm.myapplication.Models.Member;
import com.crm.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewMemberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewMemberFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewMemberFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewMemberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewMemberFragment newInstance(String param1, String param2) {
        ViewMemberFragment fragment = new ViewMemberFragment();
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


    RecyclerView view;
    String[] courses = {"All","Active Members", "Unpaid Members", "Inactive Members", "Expired Members",
            "Expire Today", "Expire 1-5", "Expire 6-10", "Expire 11-15", "Blocked Members", "Deleted Members"
            };
    private MemberAdapter memberAdapter;
    private EditText search;

    Dialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_member, container, false);

        loadingDialog= new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        view = v.findViewById(R.id.rec1);
        Spinner spin = v.findViewById(R.id.spinner2);
        search= v.findViewById(R.id.searchView);

        LinearLayoutManager l = new LinearLayoutManager(getContext());
        l.setOrientation(RecyclerView.VERTICAL);
        view.setLayoutManager(l);
        view.setHasFixedSize(true);
        view.setNestedScrollingEnabled(false);

        if(DataList.memberList.size()>0){
            DataList.memberList.clear();
        }

        loadingDialog.dismiss();
        FirebaseDatabase.getInstance().getReference()
                .child("FITCRM")
                .child("gyms")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("members")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot d: snapshot.getChildren()){
                            if(!String.valueOf(d.child("status")).equals("trashed")) {
                                DataList.memberList.add(new Member(
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
                                ));
                            }
                        }
                        memberAdapter = new MemberAdapter(getContext(), DataList.memberList);
                        view.setAdapter(memberAdapter);
                        memberAdapter.notifyDataSetChanged();
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
loadingDialog.dismiss();
                    }
                });

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filter(courses[i]);
                Toast.makeText(getContext(),
                        courses[i],
                        Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter ad = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, courses);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        spin.setAdapter(ad);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                memberAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return v;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void filter(final String st){
//        "Unpaid Members", "Inactive Members", "Expired Members",
        switch(st) {

            case "All":
                Toast.makeText(getContext(), "all", Toast.LENGTH_SHORT).show();
                memberAdapter = new MemberAdapter(getContext(), DataList.memberList);
                view.setAdapter(memberAdapter);
                memberAdapter.notifyDataSetChanged();
                break;

            case "Active Members":
                Toast.makeText(getContext(), "activr mem kkk", Toast.LENGTH_SHORT).show();
            memberAdapter = new MemberAdapter(getContext(),
                    DataList.memberList
                            .stream()
                            .filter(m -> m.getStatus().equals("active"))
                            .collect(Collectors.toList()));
            view.setAdapter(memberAdapter);
            memberAdapter.notifyDataSetChanged();
            break;

            case "Blocked Members":
                memberAdapter = new MemberAdapter(getContext(),
                        DataList.memberList
                                .stream()
                                .filter(m -> m.getStatus().equals("blocked"))
                                .collect(Collectors.toList()));
                view.setAdapter(memberAdapter);
                memberAdapter.notifyDataSetChanged();
                break;

            case "Deleted Members":
                memberAdapter = new MemberAdapter(getContext(),
                        DataList.memberList
                                .stream()
                                .filter(m -> m.getStatus().equals("trashed"))
                                .collect(Collectors.toList()));
                view.setAdapter(memberAdapter);
                memberAdapter.notifyDataSetChanged();
                break;

            case "Expired Members":
                memberAdapter = new MemberAdapter(getContext(),
                        DataList.memberList
                                .stream()
                                .filter(m -> checkexp(m.getExpdate()))
                                .collect(Collectors.toList()));
                view.setAdapter(memberAdapter);
                memberAdapter.notifyDataSetChanged();
                break;

            case "Expire Today":
                memberAdapter = new MemberAdapter(getContext(),
                        DataList.memberList
                                .stream()
                                .filter(m -> checkToday(m.getExpdate()))
                                .collect(Collectors.toList()));
                view.setAdapter(memberAdapter);
                memberAdapter.notifyDataSetChanged();
                break;

            case "Expire 1-5":
                memberAdapter = new MemberAdapter(getContext(),
                        DataList.memberList
                                .stream()
                                .filter(m -> checka(m.getExpdate(), 1L, 5L))
                                .collect(Collectors.toList()));
                view.setAdapter(memberAdapter);
                memberAdapter.notifyDataSetChanged();
                break;

            case "Expire 6-10":
                memberAdapter = new MemberAdapter(getContext(),
                        DataList.memberList
                                .stream()
                                .filter(m -> checka(m.getExpdate(), 6L, 10L))
                                .collect(Collectors.toList()));
                view.setAdapter(memberAdapter);
                memberAdapter.notifyDataSetChanged();
                break;

            case "Expire 11-15":
                memberAdapter = new MemberAdapter(getContext(),
                        DataList.memberList
                                .stream()
                                .filter(m -> checka(m.getExpdate(), 11L, 15L))
                                .collect(Collectors.toList()));
                view.setAdapter(memberAdapter);
                memberAdapter.notifyDataSetChanged();
                break;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean checkToday(String expdate){
        ZonedDateTime t= ZonedDateTime.parse(expdate);
        Duration d= Duration.between(t, ZonedDateTime.now(ZoneId.of("Asia/Kolkata"))) ;
//        (t.getDayOfMonth() == ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).getDayOfMonth())
//                && t.getMonthValue() == ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).getMonthValue()
//                && t.getYear() == ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).getYear()
        if(d.toDays() == 0){
            Toast.makeText(getContext(), String.valueOf(d.toDays()), Toast.LENGTH_SHORT).show();
            return true;
        }else{
            Toast.makeText(getContext(), "not exp today", Toast.LENGTH_SHORT).show();
            return false;
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean checka(String expdate,long p, long q){
        ZonedDateTime t= ZonedDateTime.parse(expdate);
        Duration d= Duration.between(t, ZonedDateTime.now(ZoneId.of("Asia/Kolkata"))) ;
        Toast.makeText(getContext(), String.valueOf(d.toDays()), Toast.LENGTH_SHORT).show();
        if(d.toDays() <= q && d.toDays()>=p){
            return true;
        }else{
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean checkexp(String expdate){
        ZonedDateTime t= ZonedDateTime.parse(expdate);
        Duration d= Duration.between(t, ZonedDateTime.now(ZoneId.of("Asia/Kolkata"))) ;
        if(d.toDays()>=0){
            Toast.makeText(getContext(), String.valueOf(d.toDays()), Toast.LENGTH_SHORT).show();
            return true;
        }else{
            Toast.makeText(getContext(), "exp mem", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}