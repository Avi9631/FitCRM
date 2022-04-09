package com.crm.myapplication.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.crm.myapplication.Models.Plan;
import com.crm.myapplication.R;
import com.crm.myapplication.ui.plans.PlansFragment;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.MyViewHolder> {

    private Context mContext;
    private List<Plan> mData ;

    public PlanAdapter(Context mContext, List<Plan> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.item_plan_layout, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String planname=  mData.get(position).getPlanname();
        String id=   mData.get(position).getPlanid();
        String planfee=  mData.get(position).getPlanfee();
        String planduration=   mData.get(position).getPlanduration();
        String plandurationType=  mData.get(position).getPlandurationtype();
        String plandesc=   mData.get(position).getPlandesc();
        String planStatus= mData.get(position).getStatus();

        holder.setData(planname, id, planfee, planduration, plandurationType, plandesc, planStatus, position);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView planname, fee, duration, desc, status;
        private Button deleteBtn, statusBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            planname = itemView.findViewById(R.id.textView6);
            fee = itemView.findViewById(R.id.textView8);
            duration = itemView.findViewById(R.id.textView10);
            desc = itemView.findViewById(R.id.textView12);
            statusBtn= itemView.findViewById(R.id.button3);
            deleteBtn = itemView.findViewById(R.id.button14);
            status= itemView.findViewById(R.id.textView28);

        }


        public void setData(String planname, String id, String planfee, String planduration, String plandurationType, String plandesc, String planStatus, int position) {
            this.planname.setText(planname);
            this.fee.setText(planfee);
            this.duration.setText(planduration+" "+plandurationType);
            this.desc.setText(plandesc);
            if(planStatus.equals("enable")){
                this.status.setText("ACTIVE");
                this.status.setBackgroundColor(Color.parseColor("#26580f"));
            }else if( planStatus.equals("disable")){
                this.status.setText("DISABLED");
                this.status.setBackgroundColor(Color.parseColor("#ff0000"));
            }

            if(planStatus.equals("enable")){
                statusBtn.setText("DISABLE");
            }else if( planStatus.equals("disable")){
                statusBtn.setText("ENABLE");
            }

            statusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                    builder1.setMessage("Are you sure?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                public void onClick(DialogInterface dialog, int mid) {
                                    PlansFragment.changeStatus(id, planStatus);
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
            });

//            deleteBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
//                    builder1.setMessage("Are you sure to delete a batch?");
//                    builder1.setCancelable(true);
//                    builder1.setPositiveButton(
//                            "Yes",
//                            new DialogInterface.OnClickListener() {
//                                @RequiresApi(api = Build.VERSION_CODES.O)
//                                public void onClick(DialogInterface dialog, int mid) {
//                                    PlansFragment.deletePlan(position);
//                                    dialog.cancel();
//                                }
//                            });
//
//                    builder1.setNegativeButton(
//                            "No",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//                }
//            });
        }
    }

}
