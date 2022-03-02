package com.hels.miapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CustomersActivity extends AppCompatActivity {

    // Inisialisasi
    private EditText NamaCustomers, EmailCustomers, AlamatCustomers;
    private Button btnSave;
    private ProgressDialog progressDialog;
    private String id = "";

    // Memanggil DB
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        // EditText
        NamaCustomers = findViewById(R.id.nama_customers);
        EmailCustomers = findViewById(R.id.email_customers);
        AlamatCustomers = findViewById(R.id.alamat_customers);

        // Button
        btnSave = findViewById(R.id.btn_save);

        // ProgressDialog
        progressDialog = new ProgressDialog(CustomersActivity.this);
        progressDialog.setTitle("Tunggu yak..");
        progressDialog.setMessage("Lagi di simpan nich..");

        // Menambah Data
        btnSave.setOnClickListener(v -> {
            if (NamaCustomers.getText().length()>0 && EmailCustomers.getText().length()>0 && AlamatCustomers.getText().length()>0) {
                saveData(NamaCustomers.getText().toString(), EmailCustomers.getText().toString(), AlamatCustomers.getText().toString());
            }else {
                Toast.makeText(getApplicationContext(), "Isi semua datanya gan..", Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
            NamaCustomers.setText(intent.getStringExtra("nama_customers"));
            EmailCustomers.setText(intent.getStringExtra("email_customers"));
            AlamatCustomers.setText(intent.getStringExtra("alamat_customers"));
        }
    }

    // Save Data (C= Customers)
    private void saveData(String nama_customers, String email_customers, String alamat_customers) {
        Map<String, Object> c = new HashMap<>();
        c.put("nama_customers", nama_customers);
        c.put("email_customers", email_customers);
        c.put("alamat_customers", alamat_customers);

        // ProgressDiaog
        progressDialog.show();
        if (id != null) {
            db.collection("customers").document(id)
                    .set(c)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Berhasil update bro..", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(),"Gagal update bro..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            // Simpan ke Database
            db.collection("customers")
                    .add(c)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(),"Berhasil menambahkan bro..", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
        }

    }
}