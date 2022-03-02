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
import com.hels.miapp.Adapter.CategoryAdapter;
import com.hels.miapp.Model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    // Inisialisasi
    private RecyclerView recyclerView;
    private FloatingActionButton btnAddCategory;
    private List<Category> listCategory = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private ProgressDialog progressDialog;

    // Database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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
        View v = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = v.findViewById(R.id.recycler_view_categories);
        btnAddCategory = v.findViewById(R.id.btn_add_categories);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Tunggu yak..");
        progressDialog.setMessage("Lagi ambil data nich..");

        categoryAdapter = new CategoryAdapter(getContext().getApplicationContext(), listCategory);
        categoryAdapter.setDialog(new CategoryAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {"Edit Dong", "Hapus"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                Intent in = new Intent(getContext().getApplicationContext(), CategoryActivity.class);
                                in.putExtra("id", listCategory.get(pos).getId());
                                in.putExtra("nm_kategori", listCategory.get(pos).getNm_kategori());
                                in.putExtra("img_kategori", listCategory.get(pos).getImg_kategori());
                                startActivity(in);
                                break;
                            case 1:
                                delData(listCategory.get(pos).getId(), listCategory.get(pos).getImg_kategori());
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

        //Nambah adapter
        recyclerView.setAdapter(categoryAdapter);

        btnAddCategory.setOnClickListener(view -> {
            startActivity(new Intent(getContext().getApplicationContext(), CategoryActivity.class));
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

    // Mengambil data dari firestore
    private void getData() {
        progressDialog.show();
        db.collection("category")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                listCategory.clear();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Category c = new Category(doc.getString("nm_kategori"), doc.getString("img_kategori"));
                        c.setId(doc.getId());
                        listCategory.add(c);
                    }
                    categoryAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getContext().getApplicationContext(), "Gagal di ambil bro", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private void delData(String id, String img_kategori) {
        progressDialog.show();
        db.collection("category").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext().getApplicationContext(), "Berhasil dihapus bro..", Toast.LENGTH_SHORT).show();
                            getData();
                        }else {
                            FirebaseStorage.getInstance().getReferenceFromUrl(img_kategori).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    getData();
                                }
                            });
                        }
                    }
                });
    }
}