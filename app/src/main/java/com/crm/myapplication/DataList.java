package com.crm.myapplication;

import com.crm.myapplication.Models.Batch;
import com.crm.myapplication.Models.Member;
import com.crm.myapplication.Models.MemberFee;
import com.crm.myapplication.Models.Plan;

import java.util.ArrayList;
import java.util.List;

public class DataList {

    public static List<Plan> planList = new ArrayList<>();
    public static List<Batch> batchList = new ArrayList<>();
    public static List<Member> memberList = new ArrayList<>();
    public static List<MemberFee> feeList = new ArrayList<>();
    public static String name, mobile, email, address, details;

    public static void LoadMembersFromFirebase(){

    }


}
