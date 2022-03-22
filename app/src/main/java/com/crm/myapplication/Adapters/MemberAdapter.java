package com.crm.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.crm.myapplication.Models.Member;
import com.crm.myapplication.R;
import com.crm.myapplication.ui.payfee.PayFeeActivity;
import com.crm.myapplication.ui.slideshow.MemberDetailsActivity;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder>  implements Filterable {

    private Context mContext;
    List<Member> mData ;
    List<Member> filterList;
    CustomFilter4 filter;

    public MemberAdapter(Context mContext, List<Member> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.filterList = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.item_member_layout1, parent, false);
        return new MyViewHolder(row);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name=  mData.get(position).getName();
        String id=   mData.get(position).getId();
        String joindate=  mData.get(position).getJoindate();
        String expdate=   mData.get(position).getExpdate();
        String mob=  mData.get(position).getMob();
        String email=   mData.get(position).getEmail();
        String address=   mData.get(position).getAddress();
        String gender=   mData.get(position).getGender();
        String dob=   mData.get(position).getDob();
        String status=   mData.get(position).getStatus();
        String batch= mData.get(position).getBatchname();
        holder.setData(name, id, joindate, expdate, mob, email, address, gender, dob, position, batch, status);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter4(filterList, this);
        }
        return filter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView serviceName, servicePrice, serviceBID, serviceProb1, serviceAddress, serviceStatus;
        Button accept, decline, details;

        private ImageView propic;
        private TextView name, id, batch, expdate, gender, mobile, status;
        private Button detailsBtn, payFeeBtn;


        public MyViewHolder(View itemView) {
            super(itemView);
            propic = itemView.findViewById(R.id.imageView6);
            name = itemView.findViewById(R.id.textView13);
            id = itemView.findViewById(R.id.textView2);
            mobile = itemView.findViewById(R.id.textView14);
            batch = itemView.findViewById(R.id.textView15);
            expdate = itemView.findViewById(R.id.textView16);
            status = itemView.findViewById(R.id.textView18);
            detailsBtn = itemView.findViewById(R.id.button7);
            payFeeBtn = itemView.findViewById(R.id.button15);
            gender = itemView.findViewById(R.id.textView3);
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        public void setData(String name, String id, String joindate,
                            String expdate, String mob, String email,
                            String address, String gender, String dob,
                            int position, String batch, String status) {

            this.name.setText("Name : "+name);
            this.id.setText("ID : "+id);
            this.mobile.setText("Mobile No. : "+mob);
            this.status.setText(status);
            this.batch.setText("Batch : " + batch);
            DateTimeFormatter ft= DateTimeFormatter.ofPattern("dd/MM/uuuu");
            ZonedDateTime z= ZonedDateTime.parse(expdate);
            String d=  ft.format(z).toString();
            this.expdate.setText("Exp Date : "+ d);
            this.gender.setText("Gender : "+gender);

            detailsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(mContext, MemberDetailsActivity.class);
                    i.putExtra("position", position);
                    i.putExtra("id", id);
                    mContext.startActivity(i);
                }
            });

            payFeeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(mContext, PayFeeActivity.class);
                    i.putExtra("position", position);
                    i.putExtra("id", id);
                    i.putExtra("member", mData.get(position));
                    mContext.startActivity(i);
                }
            });
        }
    }

}


class CustomFilter4 extends Filter {

    MemberAdapter adapter;
    List<Member> filterList;

    public CustomFilter4(List<Member> filterList, MemberAdapter userAdapter) {
        this.adapter=userAdapter;
        this.filterList= filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults=new FilterResults();
        if(constraint!=null || constraint.length()>0){
            constraint=constraint.toString().toLowerCase();
            List<Member> list= new ArrayList<>();
            for(int i=0; i<filterList.size(); i++){
                if(filterList.get(i).getName().toLowerCase().contains(constraint)){
                    list.add(filterList.get(i));
                }
                else if(filterList.get(i).getMob().toLowerCase().contains(constraint)) {
                    list.add(filterList.get(i));
                }
            }
            filterResults.count= list.size();
            filterResults.values= list;
        }else {
            filterResults.count= filterList.size();
            filterResults.values= filterList;
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.mData= (List<Member>) results.values;
        adapter.notifyDataSetChanged();
    }


}