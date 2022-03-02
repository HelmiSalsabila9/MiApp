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

public class CategoryActivity extends AppCompatActivity {

    // Inisialisasi
    private String id = "";
    private EditText nm_kategori;
    private ImageView img_kategori;
    private Button btnSaveKategori;
    private ProgressDialog progressDialog;

    // Memanggila DB
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Deklarasiin
        nm_kategori = findViewById(R.id.nm_kategori);
        img_kategori = findViewById(R.id.img_kategori);

        // Button
        btnSaveKategori = findViewById(R.id.btn_save_kategori);

        // Progress dialog
        progressDialog = new ProgressDialog(CategoryActivity.this);
        progressDialog.setTitle("Tunggu yak..");
        progressDialog.setTitle("Lagi disimpan nich..");

        // Gambar nih
        img_kategori.setOnClickListener(view -> {
            selectImg();
        });

        //Tambah data
        btnSaveKategori.setOnClickListener(view -> {
            if (nm_kategori.getText().length()>0){
                upload(nm_kategori.getText().toString());
            }else {
                Toast.makeText(getApplicationContext(), "Isi semua data bro..", Toast.LENGTH_SHORT).show();
            }
        });
        Intent in = getIntent();
        if (in != null) {
            id = in.getStringExtra("id");
            nm_kategori.setText(in.getStringExtra("nm_kategori"));
            //Gambar
            Glide.with(getApplicationContext()).load(in.getStringExtra("img_kategori")).into(img_kategori);

        }
    }

    private void selectImg() {
        final CharSequence[] itemImg = {"Foto Langsung", "Ambil Gallery", "Cencel"};
        AlertDialog.Builder build = new AlertDialog.Builder(CategoryActivity.this);
        build.setTitle(getString(R.string.app_name));
        build.setIcon(R.mipmap.ic_launcher);
        build.setItems(itemImg, ((dialog, item) ->{
            if (itemImg[item].equals("Foto Langsung")){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,10);

            }else if (itemImg[item].equals("Ambil Gallery")){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 20);

            }else if(itemImg[item].equals("Cancel")){
                dialog.dismiss();
            }
        }));
        build.show();
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
                    img_kategori.post(() -> {
                        img_kategori.setImageBitmap(bitmap);
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
                img_kategori.post(() -> {
                    img_kategori.setImageBitmap(bitmap);
                });
            });
            thread.start();
        }
    }

    private void upload(String nm_kategori){
        progressDialog.show();

        img_kategori.setDrawingCacheEnabled(true);
        img_kategori.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) img_kategori.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        //UPLOAD IMage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference("images_category").child("IMG"+new Date().getTime()+".jpeg");
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
                    if (taskSnapshot.getMetadata().getReference()!=null){
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.getResult()!=null) {
                                    saveData(nm_kategori, task.getResult().toString());
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
                    Toast.makeText(getApplicationContext(), "Gagal Menambahkan Gambar..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveData(String nm_kategori, String img_kategori){
        Map<String, Object> kategori = new HashMap<>();
        kategori.put("nm_kategori", nm_kategori);
        kategori.put("img_kategori", img_kategori);

        progressDialog.show();
        if (id != null){
            db.collection("category").document(id)
                    .set(kategori)
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
            db.collection("category")
                    .add(kategori)
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