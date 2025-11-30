package com.dmsoft.keuanganapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dmsoft.keuanganapp.adapter.PengeluaranAdapter;
import com.dmsoft.keuanganapp.api.ApiClient;
import com.dmsoft.keuanganapp.api.ApiService;
import com.dmsoft.keuanganapp.model.PengeluaranItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PengeluaranActivity extends AppCompatActivity {

    private RecyclerView rvPengeluaran;
    private TextView tvTotalPengeluaran;

    private List<PengeluaranItem> listPengeluaran;
    private PengeluaranAdapter pengeluaranAdapter;
    private String bulan,tahun;
    NumberFormat format = NumberFormat.getInstance(new Locale("id", "ID"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengeluaran);

        setTitle("Pengeluaran");

        tvTotalPengeluaran = findViewById(R.id.tvTotalPengeluaran);
        rvPengeluaran = findViewById(R.id.rvPengeluaran);

        // SETUP LIST + ADAPTER KOSONG
        listPengeluaran = new ArrayList<>();
        pengeluaranAdapter = new PengeluaranAdapter(listPengeluaran);

        rvPengeluaran.setLayoutManager(new LinearLayoutManager(this));
        rvPengeluaran.setAdapter(pengeluaranAdapter);

        bulan = getIntent().getStringExtra("bulan");
        tahun = getIntent().getStringExtra("tahun");
        loadData(bulan, tahun);
    }

    private void loadData(String bulan, String tahun) {

        ApiService api = ApiClient.getService();

        api.getPengeluaran(bulan, tahun).enqueue(new Callback<List<PengeluaranItem>>() {
            @Override
            public void onResponse(Call<List<PengeluaranItem>> call, Response<List<PengeluaranItem>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<PengeluaranItem> res = response.body();

                    // HITUNG TOTAL SENDIRI
                    int total = 0;
                    for (PengeluaranItem item : res) {
                        total += item.nominal;
                    }

                    tvTotalPengeluaran.setText("Total Pengeluaran: Rp " + format.format(total));

                    listPengeluaran.clear();
                    listPengeluaran.addAll(res);
                    pengeluaranAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(PengeluaranActivity.this,
                            "Data tidak valid", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PengeluaranItem>> call, Throwable t) {
                Toast.makeText(PengeluaranActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
