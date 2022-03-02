package com.hels.miapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    // Inisialisasikan
    private ImageView gambar_produk;
    private EditText nama_produk, harga, deskripsi, nama_toko;
    private Button btnSaveProduct;
    private ProgressDialog progressDialog;
    private String id = "" ;

    // Memanggil DB
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        //Deklarasiin
        gambar_produk = findViewById(R.id.gambar_produk);
        nama_produk = findViewById(R.id.nama_produk);
        harga = findViewById(R.id.harga);
        deskripsi = findViewById(R.id.deskripsi);
        nama_toko = findViewById(R.id.nama_toko);

        // Deklarasiin Button
        btnSaveProduct = findViewById(R.id.btn_save_produk);

        progressDialog = new ProgressDialog(ProductActivity.this);
        progressDialog.setTitle("Tunggu yak..");
        progressDialog.setMessage("Lagi disimpan nich..");

        //Gambar Produk
        gambar_produk.setOnClickListener(view -> {
            selectImage();
        });

        // Nambah data
        btnSaveProduct.setOnClickListener(view -> {
            if (nama_produk.getText().length()>0 && harga.getText().length()>0 && deskripsi.getText().length()>0 && nama_toko.getText().length()>0) {
                upload(nama_produk.getText().toString(), harga.getText().toString(), deskripsi.getText().toString(), nama_toko.getText().toString());
            }else {
                Toast.makeText(getApplicationContext(), "Isi semua datanya bro..", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
            nama_produk.setText(intent.getStringExtra("nama_produk"));
            harga.setText(intent.getStringExtra("harga"));
            deskripsi.setText(intent.getStringExtra("deskripsi"));
            nama_toko.setText(intent.getStringExtra("nama_toko"));
            // Gambar
            Glide.with(getApplicationContext()).load(intent.getStringExtra("gambar_produk")).into(gambar_produk);
        }
    }

    // memilih menggunakan take camera atau pilih dari gallery
    private void selectImage() {
        final CharSequence[] itemImg = {"Foto Langsung", "Pilih dari Gallery", "Cencel"};
        AlertDialog.Builder b = new AlertDialog.Builder(ProductActivity.this);
        b.setTitle(getString(R.string.app_name));
        b.setIcon(R.mipmap.ic_launcher);
        b.setItems(itemImg, ((dialog, item) -> {
            if (itemImg[item].equals("Foto Langsung")){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,10);

            }else if (itemImg[item].equals("Pilih dari Gallery")){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 20);

            }else if(itemImg[item].equals("Cancel")){
                dialog.dismiss();
            }
        }));
        b.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 && resultCode == RESULT_OK && data != null){
            final Uri path = data.getData();
            Thread thread = new Thread(() -> {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(path);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    gambar_produk.post(() -> {
                        gambar_produk.setImageBitmap(bitmap);
                    });
                }catch (IOException e){
                    e.printStackTrace();
                }
            });
            thread.start();
        }
        if (requestCode == 10 && resultCode == RESULT_OK){
            final Bundle extras = data.getExtras();
            Thread thread = new Thread(() -> {
                Bitmap bitmap = (Bitmap) extras.get("data");
                gambar_produk.post(() -> {
                    gambar_produk.setImageBitmap(bitmap);
                });
            });
            thread.start();
        }
    }

    private void upload(String nama_produk, String harga, String deskripsi, String nama_toko){
        progressDialog.show();

        gambar_produk.setDrawingCacheEnabled(true);
        gambar_produk.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) gambar_produk.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        //Upload Gambar Product
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference("images_product").child("IMG"+new Date().getTime()+".jpeg");
        UploadTask uploadTask = reference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata()!=null){
                    if (taskSnapshot.getMetadata().getReference() != null){
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.getResult()!=null) {
                                    saveDataProduk(nama_produk, harga, deskripsi, nama_toko, task.getResult().toString());
                                }else{
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Gagal Menambahkan Gambar bro..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Gagal Menambahkan Gambar bro..", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Gagal Menambahkan Gambar bro..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveDataProduk(String gambar_produk, String nama_produk, String harga, String deskripsi, String nama_toko){
        Map<String, Object> p = new HashMap<>();
        p.put("gambar_produk", gambar_produk);
        p.put("nama_produk", nama_produk);
        p.put("harga", harga);
        p.put("deskripsi", deskripsi);
        p.put("nama_toko", nama_toko);

        progressDialog.show();
        if (id != null){
            db.collection("product").document(id)
                    .set(p)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Successful!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else {
            db.collection("product")
                    .add(p)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(), "Successful!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
        }
    }
}