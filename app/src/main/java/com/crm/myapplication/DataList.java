package com.crm.myapplication;

import androidx.annotation.NonNull;

import com.crm.myapplication.Models.Batch;
import com.crm.myapplication.Models.Member;
import com.crm.myapplication.Models.MemberFee;
import com.crm.myapplication.Models.Plan;

import java.util.ArrayList;
import java.util.List;

public class DataList {

    @NonNull
    public static List<Plan> planList = new ArrayList<>();
    @NonNull
    public static List<Batch> batchList = new ArrayList<>();
    @NonNull
    public static List<Member> memberList = new ArrayList<>();
    @NonNull
    public static List<MemberFee> feeList = new ArrayList<>();
    public static String name, mobile, email, address, details;

    public static void LoadMembersFromFirebase() {

    }


}
