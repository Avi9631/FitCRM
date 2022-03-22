package com.crm.myapplication.Adapters;

import android.content.Context;
import android.content.DialogInterface;
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

import com.crm.myapplication.Models.Batch;
import com.crm.myapplication.R;
import com.crm.myapplication.ui.batch.BatchFragment;

import java.util.List;

public class BatchAdapter extends RecyclerView.Adapter<BatchAdapter.MyViewHolder> {

    private Context mContext;
    private List<Batch> mData ;

    public BatchAdapter(Context mContext, List<Batch> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.item_batch_layout, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String batch=  mData.get(position).getBatchname();
        String id=   mData.get(position).getBatchid();
        String batchTot=  mData.get(position).getBatchtot();
        String batchStrength=   mData.get(position).getBatchMaxStrength();
        String batchdesc=   mData.get(position).getBatchdesc();
        String batchStatus= mData.get(position).getStatus();

        holder.setData(batch, id, batchTot, batchStrength, batchdesc, batchStatus, position);

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
            status= itemView.findViewById(R.id.textView33);

        }

        public void setData(String batch, String id, String batchTot, String batchStrength, String batchdesc, String batchStatus, int position) {
            this.planname.setText(batch);
            this.fee.setText(batchTot);
            this.duration.setText(batchTot+" / "+batchStrength);
            this.desc.setText(batchdesc);
            if(batchStatus.equals("enable")){
                this.status.setText("ACTIVE");
            }else if( batchStatus.equals("disable")){
                this.status.setText("DISABLED");
            }

            if(batchStatus.equals("enable")){
                statusBtn.setText("DISABLE");
            }else if( batchStatus.equals("disable")){
                statusBtn.setText("ENABLE");
            }

            statusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                    builder1.setMessage("Are you sure to add a batch?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                public void onClick(DialogInterface dialog, int mid) {
                                    BatchFragment.changeStatus(id, batchStatus);
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

            deleteBtn.setOnClickListener(new View.OnClickListener() {
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
                                    BatchFragment.deletePlan(id, position, mContext);
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
        }
    }

}
