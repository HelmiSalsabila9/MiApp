package com.hels.miapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hels.miapp.Model.Category;
import com.hels.miapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    // Inisialisasi
    private Context context;
    private Dialog dialog;
    //List Model
    private List<Category> listCategory;

    public interface Dialog {
        void onClick(int pos);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public CategoryAdapter(Context context, List<Category> list) {
        this.context = context;
        this.listCategory = list;
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_categories, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        holder.nm_kategori.setText(listCategory.get(position).getNm_kategori());
        Glide.with(context).load(listCategory.get(position).getImg_kategori()).into(holder.img_kategory);
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //Inisialisasi
        ImageView img_kategory;
        TextView nm_kategori;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nm_kategori = itemView.findViewById(R.id.nm_kategori);
            img_kategory = itemView.findViewById(R.id.img_kategori);
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
