package com.dmsoft.keuanganapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dmsoft.keuanganapp.adapter.RekeningKoranAdapter;
import com.dmsoft.keuanganapp.api.ApiClient;
import com.dmsoft.keuanganapp.api.ApiService;
import com.dmsoft.keuanganapp.model.RekeningKoranItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RekeningKoranActivity extends AppCompatActivity {

    RecyclerView rvKoran;
    TextView tvTotalKoran;

    List<RekeningKoranItem> list;
    RekeningKoranAdapter adapter;
    private String bulan,tahun;
    NumberFormat format = NumberFormat.getInstance(new Locale("id", "ID"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekening_koran);

        setTitle("Peredaran Uang");

        rvKoran = findViewById(R.id.rvKoran);
        tvTotalKoran = findViewById(R.id.tvTotalKoran);

        list = new ArrayList<>();
        adapter = new RekeningKoranAdapter(list);

        rvKoran.setLayoutManager(new LinearLayoutManager(this));
        rvKoran.setAdapter(adapter);

        bulan = getIntent().getStringExtra("bulan");
        tahun = getIntent().getStringExtra("tahun");
        loadKoran(bulan, tahun);

    }

    private void loadKoran(String bulan, String tahun) {

        ApiService api = ApiClient.getService();
        api.getRekeningKoran(bulan, tahun).enqueue(new Callback<List<RekeningKoranItem>>() {
            @Override
            public void onResponse(Call<List<RekeningKoranItem>> call, Response<List<RekeningKoranItem>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    list.clear();
                    list.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    // Hitung total
                    int total = 0;
                    for (RekeningKoranItem item : list) {
                        total += item.penerimaan;
                        total -= item.pengeluaran;
                    }

                    tvTotalKoran.setText("Total Peredaran Uang: Rp " + format.format(total));

                } else {
                    Toast.makeText(RekeningKoranActivity.this,
                            "Gagal load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RekeningKoranItem>> call, Throwable t) {
                Toast.makeText(RekeningKoranActivity.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
