package com.hels.miapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    //Inisialisasi
    private TextView nama_lengkap;
    private FirebaseUser firebaseUser;

    // Inisialisasi Card CRUD
    private CardView mhaRetrofit;
    private CardView customersHome;
    private CardView categoryHome;
    private CardView productHome;

    // Inisialisasi card API
    private CardView cardapi1;

    //About
    private CardView cardapi2;

    // Covid
    private CardView cardapi3;
    // Daging
    private CardView cardapi4;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //Deklarasi
        nama_lengkap = v.findViewById(R.id.nama_lengkap);
        mhaRetrofit = v.findViewById(R.id.mhs_retrofit);
        customersHome = v.findViewById(R.id.customer_home);
        productHome = v.findViewById(R.id.product_home);
        categoryHome = v.findViewById(R.id.category_home);

        // Deklarasi api
        cardapi3 = v.findViewById(R.id.cardapi3);
        cardapi4 = v.findViewById(R.id.cardapi4);
        cardapi2 = v.findViewById(R.id.cardapi2);

        // Pemanggilan
        mhaRetrofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext().getApplicationContext(), MhsActivity.class));
            }
        });

        customersHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext().getApplicationContext(), CustomersActivity.class));
            }
        });

        productHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext().getApplicationContext(), ProductActivity.class));
            }
        });

        categoryHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext().getApplicationContext(), CategoryActivity.class));
            }
        });

        // API
        // Covid
        cardapi3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext().getApplicationContext(), ApiCovidActivity.class));
            }
        });

        cardapi4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext().getApplicationContext(), ApiDagingActivity.class));
            }
        });

        cardapi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext().getApplicationContext(), AboutActivity.class));
            }
        });
        // Nampilin

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser.getDisplayName() != null) {
            nama_lengkap.setText(firebaseUser.getDisplayName());
        }else {
            nama_lengkap.setText("Gagal bro..");
        }
        return v;
    }
}