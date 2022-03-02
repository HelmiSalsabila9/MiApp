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
import com.hels.miapp.Adapter.CustomersAdapter;
import com.hels.miapp.Model.Customers;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomersFragment extends Fragment {

    // Inisialisasi
    private RecyclerView recyclerView;
    private FloatingActionButton btnAddCustomers;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Customers> list = new ArrayList<>();
    private CustomersAdapter customersAdapter;
    private ProgressDialog progressDialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomersFragment newInstance(String param1, String param2) {
        CustomersFragment fragment = new CustomersFragment();
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

    // OnCreate Customers
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customers, container, false);
        recyclerView = v.findViewById(R.id.recycler_view_customers);
        btnAddCustomers = v.findViewById(R.id.btn_add_customers);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Tunggu yak..");
        progressDialog.setMessage("Lagi mengambil data nich..");

        customersAdapter = new CustomersAdapter(getContext().getApplicationContext(), list);
        customersAdapter.setDialog(new CustomersAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {"Edit", "Hapus"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                Intent in = new Intent(getContext().getApplicationContext(), CustomersActivity.class);
                                in.putExtra("id", list.get(pos).getId());
                                in.putExtra("nama_customers", list.get(pos).getNama_customers());
                                in.putExtra("email_customers", list.get(pos).getEmail_customer());
                                in.putExtra("alamt_customers", list.get(pos).getAlamat_customer());
                                startActivity(in);
                                break;
                            case 1:
                                delData(list.get(pos).getId());
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

        //Menambakan Adapter
        recyclerView.setAdapter(customersAdapter);

        btnAddCustomers.setOnClickListener(view -> {
            startActivity(new Intent(getContext().getApplicationContext(), CustomersActivity.class));
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

    //MENGAMBIL DATA DARI FIRESTORE
    private void getData() {
        progressDialog.show();
        db.collection("customers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Customers c = new Customers(document.getString("nama_customers"), document.getString("email_customers"), document.getString("alamat_customers"));
                                c.setId(document.getId());
                                list.add(c);
                            }
                            customersAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(getContext().getApplicationContext(), "Gagal diambil bro..", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void delData(String id) {
        progressDialog.show();
        db.collection("customers").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext().getApplicationContext(),"Gagal hapus bro..", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                        getData();
                    }
                });
    }
}