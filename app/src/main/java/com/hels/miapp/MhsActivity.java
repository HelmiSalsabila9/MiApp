package com.hels.miapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MhsActivity extends AppCompatActivity {

    public static final String URL = "http://192.168.100.15:8080/api_uas/";

    private RadioButton radioSexButton;
    private ProgressDialog progress;
    private Button lihatData;
    @BindView(R.id.radioSesi) RadioGroup radioGroup;
    @BindView(R.id.editTextNPM) EditText editTextNPM;
    @BindView(R.id.editTextNama) EditText editTextNama;
    @BindView(R.id.editTextKelas) EditText editTextKelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhs);
        ButterKnife.bind(this);

        lihatData = findViewById(R.id.buttonLihat);

        //Tambah data
        lihatData.setOnClickListener(view -> {
            startActivity(new Intent(MhsActivity.this, ViewActivity.class));
        });
    }


    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.buttonDaftar) void daftar() {
        //membuat progress dialog
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        //mengambil data dari edittext
        String npm = editTextNPM.getText().toString();
        String nama = editTextNama.getText().toString();
        String kelas = editTextKelas.getText().toString();
        int selectedId = radioGroup.getCheckedRadioButtonId();

        // mencari id radio button
        radioSexButton = (RadioButton) findViewById(selectedId);
        String sesi = radioSexButton.getText().toString();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<Value> call = api.daftar(npm, nama, kelas, sesi);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                progress.dismiss();
                if (value.equals("1")) {
                    Toast.makeText(MhsActivity.this, message,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MhsActivity.this, message,
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(MhsActivity.this, "Jaringan Error euy!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}