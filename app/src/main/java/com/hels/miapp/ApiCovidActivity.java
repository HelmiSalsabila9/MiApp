package com.hels.miapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiCovidActivity extends AppCompatActivity {

    TextView jml_sembuh, jml_positif, jml_mati, jml_dirawat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_covid);

        jml_sembuh = findViewById(R.id.jml_sembuh);
        jml_positif = findViewById(R.id.jml_positif);
        jml_mati = findViewById(R.id.jml_mati);
        jml_dirawat = findViewById(R.id.jml_dirawat);

        tampilData();
    }

    private void tampilData() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://apicovid19indonesia-v2.vercel.app/api/indonesia";

        JSONObject jsonObject = new JSONObject();
        final String requestBody = jsonObject.toString();

        // METHOD GET
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response.toString());
                    String sembuh = jo.getString("sembuh");
                    String positif = jo.getString("positif");
                    String dirawat = jo.getString("dirawat");
                    String mati = jo.getString("meninggal");

                    jml_sembuh.setText(sembuh);
                    jml_positif.setText(positif);
                    jml_dirawat.setText(dirawat);
                    jml_mati.setText(mati);

                    Toast.makeText(ApiCovidActivity.this, "Berhasil ngambil bro..", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ApiCovidActivity.this,"Gagal ngambil bro.." + error, Toast.LENGTH_SHORT).show();
            }
        }
        );

        queue.add(stringRequest);
    }
}