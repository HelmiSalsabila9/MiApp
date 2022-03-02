package com.hels.miapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    // Inisialisasi pemanggilan
    private EditText editName, editEmail, editPassword, editKonfirmasiPassword;
    private Button btnRegister;
    private TextView btnLogin;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FullScreen
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);

        // Menjabarkan inisialisasi pemanggilan
        editName = findViewById(R.id.name);
        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);
        editKonfirmasiPassword = findViewById(R.id.password_conf);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin = findViewById(R.id.btn_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Proses loading
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silahkan tunggu");
        progressDialog.setCancelable(false);

        // Fungsi button login
        btnLogin.setOnClickListener(v -> {
//            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });

        // Fungsi dan penggondisian button register
        btnRegister.setOnClickListener(v ->{
            if (editName.getText().length()>0 && editEmail.getText().length()>0 && editPassword.getText().length()>0 && editKonfirmasiPassword.getText().length()>0){
                if (editPassword.getText().toString().equals(editKonfirmasiPassword.getText().toString())){
                    register(editName.getText().toString(), editEmail.getText().toString(), editPassword.getText().toString());
                }else {
                    Toast.makeText(getApplicationContext(), "Password harus sama!", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(), "Silahkan isi semua data!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void register(String name, String email, String password){
        // Pemanggilan loading
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult()!=null) {
                    // User yg terdaftar
                    FirebaseUser FirebaseUser = task.getResult().getUser();

                    if (FirebaseUser!=null) {
                        // Memperbaharui profile pengguna
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

                        //Update profile pengguna
                        FirebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // pemanggilan reload
                                reload();
                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(), "Register gagal!",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    // Apabila user sudah login maka langsung masuk tidak perlu login lagi
    private void reload(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

}