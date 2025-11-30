package com.dmsoft.keuanganapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dmsoft.keuanganapp.adapter.PenerimaanAdapter;
import com.dmsoft.keuanganapp.api.ApiClient;
import com.dmsoft.keuanganapp.api.ApiService;
import com.dmsoft.keuanganapp.model.PenerimaanItem;
import com.dmsoft.keuanganapp.model.PenerimaanResponse;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenerimaanActivity extends AppCompatActivity {

    private RecyclerView rvPenerimaan;
    private TextView tvTotalPenerimaan;

    private List<PenerimaanItem> listPenerimaan;
    private PenerimaanAdapter penerimaanAdapter;

    private String bulan,tahun;
    ImageButton btnAdd;

    NumberFormat format = NumberFormat.getInstance(new Locale("id", "ID"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penerimaan);

        setTitle("Penerimaan");

        rvPenerimaan = findViewById(R.id.rvPenerimaan);
        tvTotalPenerimaan = findViewById(R.id.tvTotalPenerimaan);

        // === FIX WAJIB ===
        listPenerimaan = new ArrayList<>();
        penerimaanAdapter = new PenerimaanAdapter(listPenerimaan);

        rvPenerimaan.setLayoutManager(new LinearLayoutManager(this));
        rvPenerimaan.setAdapter(penerimaanAdapter);
        bulan = getIntent().getStringExtra("bulan");
        tahun = getIntent().getStringExtra("tahun");
        loadData(bulan, tahun);

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(PenerimaanActivity.this, AddTransaksiActivity.class);
            intent.putExtra("jenis", "penerimaan");
            startActivity(intent);
        });

    }

    private void loadData(String bulan, String tahun) {
        Log.d("API","tes");
        ApiService api = ApiClient.getService();
        Log.d("API",api.toString());
        api.getPenerimaan(bulan, tahun).enqueue(new Callback<List<PenerimaanItem>>() {
            @Override
            public void onResponse(Call<List<PenerimaanItem>> call, Response<List<PenerimaanItem>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<PenerimaanItem> res = response.body();

                    // HITUNG TOTAL SENDIRI
                    int total = 0;
                    for (PenerimaanItem item : res) {
                        total += item.nominal;
                    }

                    tvTotalPenerimaan.setText("Total: Rp " + format.format(total));

                    listPenerimaan.clear();
                    listPenerimaan.addAll(res);
                    penerimaanAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(PenerimaanActivity.this,
                            "Data tidak valid", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PenerimaanItem>> call, Throwable t) {
                Toast.makeText(PenerimaanActivity.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
