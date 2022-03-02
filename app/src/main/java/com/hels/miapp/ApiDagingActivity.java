package com.hels.miapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ApiDagingActivity extends AppCompatActivity {

    TextView nm_daging1, harga_daging1;
    private ListView listView;



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_daging);

        // Nama
        nm_daging1 = findViewById(R.id.nm_daging);
        // Harga
        harga_daging1 = findViewById(R.id.harga_daging);
        listView = findViewById(R.id.list_daging);

        tampilData();
    }

    private void tampilData() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://jibs.my.id/api/harga_komoditas";

        JSONObject jsonObject = new JSONObject();
        final String requestBody = jsonObject.toString();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONObject("national_commodity_price").getJSONArray("Daging Ayam");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject dagingAyam = jsonArray.getJSONObject(i);

                        String name = dagingAyam.getString("name");
                        Integer harga = dagingAyam.getInt("display");

                        nm_daging1.setText(name);
                        harga_daging1.setText(harga);

                        Toast.makeText(ApiDagingActivity.this, "Berhasil ngambil bro..", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ApiDagingActivity.this,"Gagal ngambil bro.." + error, Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(request);
    }
}