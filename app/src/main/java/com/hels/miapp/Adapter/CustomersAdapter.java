package com.hels.miapp.Adapter;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hels.miapp.Model.Customers;
import com.hels.miapp.R;

import java.util.List;

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.MyViewHolder> {

    //Inisialisasi
    private Context context;
    // List Model
    private List<Customers> list;
    private Dialog dialog;

    public interface Dialog{
        void onClick(int pos);
    }

    public void setDialog(Dialog dialog){
        this.dialog = dialog;
    }

    public CustomersAdapter(Context context, List<Customers> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_customers, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nama_customers.setText(list.get(position).getNama_customers());
        holder.email_customers.setText(list.get(position).getEmail_customer());
        holder.alamat_customers.setText(list.get(position).getAlamat_customer());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // Inisialisasi
        TextView nama_customers, email_customers, alamat_customers;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_customers = itemView.findViewById(R.id.nama_customers);
            email_customers = itemView.findViewById(R.id.email_customers);
            alamat_customers = itemView.findViewById(R.id.alamat_customers);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog!=null) {
                        dialog.onClick(getLayoutPosition());
                    }
                }
            });
        }
    }
}