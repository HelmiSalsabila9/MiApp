package com.hels.miapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.hels.miapp.Adapter.ProductAdapter;
import com.hels.miapp.Model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {

    //Inisialisasi
    private RecyclerView recyclerView;
    private FloatingActionButton btnAddProduct;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Product> listProduct = new ArrayList<>();
    private ProductAdapter productAdapter;
    private ProgressDialog progressDialog;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product, container, false);
        recyclerView = v.findViewById(R.id.recycler_view_product);
        btnAddProduct = v.findViewById(R.id.btn_add_product);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Tunggu yak..");
        progressDialog.setMessage("Lagi ngambil data nich..");

        productAdapter = new ProductAdapter(getContext().getApplicationContext(), listProduct);
        productAdapter.setDialog(new ProductAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {"Edit", "Hapus"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                Intent in = new Intent(getContext().getApplicationContext(), ProductActivity.class);
                                in.putExtra("id", listProduct.get(pos).getId());
                                in.putExtra("gambar_produk", listProduct.get(pos).getGambar_produk());
                                in.putExtra("nama_produk", listProduct.get(pos).getNama_produk());
                                in.putExtra("harga", listProduct.get(pos).getHarga());
                                in.putExtra("deskrisi", listProduct.get(pos).getDeskripsi());
                                in.putExtra("nama_toko", listProduct.get(pos).getNama_toko());
                                startActivity(in);
                                break;
                            case 1:
                                delDataProduct(listProduct.get(pos).getId(), listProduct.get(pos).getGambar_produk());
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getContext().getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
        //Menambahkan Adapter
        recyclerView.setAdapter(productAdapter);

        // Button add
        btnAddProduct.setOnClickListener(view -> {
            startActivity(new Intent(getContext().getApplicationContext(), ProductActivity.class));
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDataProduct();
    }

    private void getDataProduct() {
        progressDialog.show();
        db.collection("product")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        listProduct.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product p = new Product(document.getString("nama_toko"), document.getString("deskripsi"), document.getString("gambar_produk"), document.getString("harga"), document.getString("nama_produk"));
                                p.setId(document.getId());
                                listProduct.add(p);
                            }
                            productAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(getContext().getApplicationContext(), "Gagal diambil bro..", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void delDataProduct(String id, String gambar_product) {
        progressDialog.show();
        db.collection("product").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext().getApplicationContext(), "Berhasil dihapus bro..", Toast.LENGTH_SHORT).show();
                            getDataProduct();
                        }else {
                            FirebaseStorage.getInstance().getReferenceFromUrl(gambar_product).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    getDataProduct();
                                }
                            });
                        }
                    }
                });
    }
}