package com.hels.miapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hels.miapp.Model.Product;
import com.hels.miapp.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    // Inisiallisasi
    private Context contextProduct;
    //List Model
    private List<Product> listProduct;
    private Dialog dialog;

    public interface Dialog {
        void onClick(int pos);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public ProductAdapter(Context contextProduct, List<Product> listProduct) {
        this.contextProduct = contextProduct;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produk, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nama_produk.setText(listProduct.get(position).getNama_produk());
        holder.harga.setText(listProduct.get(position).getHarga());
        holder.deskripsi.setText(listProduct.get(position).getDeskripsi());
        holder.nama_toko.setText(listProduct.get(position).getNama_toko());
        Glide.with(contextProduct).load(listProduct.get(position).getGambar_produk()).into(holder.gambar_produk);
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView gambar_produk;
        TextView nama_produk, harga, deskripsi, nama_toko;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            gambar_produk = itemView.findViewById(R.id.gambar_produk);
            nama_produk = itemView.findViewById(R.id.nama_produk);
            harga = itemView.findViewById(R.id.harga);
            deskripsi = itemView.findViewById(R.id.deskripsi);
            nama_toko = itemView.findViewById(R.id.nama_toko);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog != null) {
                        dialog.onClick(getLayoutPosition());
                    }
                }
            });
        }
    }
}
