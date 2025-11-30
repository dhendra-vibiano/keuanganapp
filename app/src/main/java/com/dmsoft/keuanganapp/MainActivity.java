package com.dmsoft.keuanganapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.dmsoft.keuanganapp.api.ApiClient;
import com.dmsoft.keuanganapp.api.ApiService;
import com.dmsoft.keuanganapp.model.RekeningKoranTotalResponse;
import com.dmsoft.keuanganapp.model.TotalResponse;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private CardView cardPenerimaan, cardPengeluaran, cardRekeningKoran, cardInfo;
    TextView tvTotalPenerimaan, tvTotalPengeluaran, tvRekkoran;
    NumberFormat format = NumberFormat.getInstance(new Locale("id", "ID"));
    Spinner spinnerBulan, spinnerTahun;
    Button btnTerapkan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardPenerimaan = findViewById(R.id.cardPenerimaan);
        cardPengeluaran = findViewById(R.id.cardPengeluaran);
        cardRekeningKoran = findViewById(R.id.cardRekeningKoran);
        cardInfo = findViewById(R.id.cardInfo);

        tvTotalPenerimaan = findViewById(R.id.tvTotalPenerimaan);
        tvTotalPengeluaran = findViewById(R.id.tvTotalPengeluaran);
        tvRekkoran = findViewById(R.id.tvRekkoran);

        spinnerBulan = findViewById(R.id.spinnerBulan);
        spinnerTahun = findViewById(R.id.spinnerTahun);
        btnTerapkan = findViewById(R.id.btnTerapkan);

        String[] bulanList = {"01","02","03","04","05","06","07","08","09","10","11","12"};
        ArrayAdapter<String> bulanAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, bulanList);
        spinnerBulan.setAdapter(bulanAdapter);

        List<String> tahunList = new ArrayList<>();
        for (int i = 2020; i <= 2030; i++) tahunList.add(String.valueOf(i));

        ArrayAdapter<String> tahunAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, tahunList);
        spinnerTahun.setAdapter(tahunAdapter);

        Calendar c = Calendar.getInstance();
        // Bulan sekarang (January=0 → Tambahkan +1 dan buat jadi dua digit)
        int currentMonth = c.get(Calendar.MONTH) + 1;
        String bulanDefault = String.format("%02d", currentMonth);

        // Tahun sekarang
        int currentYear = c.get(Calendar.YEAR);
        String tahunDefault = String.valueOf(currentYear);

        // Set spinner default
        spinnerBulan.setSelection(currentMonth - 1);
        spinnerTahun.setSelection(tahunList.indexOf(tahunDefault));

//         Load dashboard pertama kali
        loadTotals(bulanDefault, tahunDefault);

        btnTerapkan.setOnClickListener(v -> {
            String bulan = spinnerBulan.getSelectedItem().toString();
            String tahun = spinnerTahun.getSelectedItem().toString();
            loadTotals(bulan, tahun);    // panggil ulang data dashboard
        });

        // transaksi penerimaan
        cardPenerimaan.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(MainActivity.this, PenerimaanActivity.class);
              intent.putExtra("bulan", spinnerBulan.getSelectedItem().toString());
              intent.putExtra("tahun", spinnerTahun.getSelectedItem().toString());
              startActivity(intent);
          }

        });

        // transaksi pengeluaran
        cardPengeluaran.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PengeluaranActivity.class);
                intent.putExtra("bulan", spinnerBulan.getSelectedItem().toString());
                intent.putExtra("tahun", spinnerTahun.getSelectedItem().toString());
                startActivity(intent);
            }
        });

        cardRekeningKoran.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RekeningKoranActivity.class);
                intent.putExtra("bulan", spinnerBulan.getSelectedItem().toString());
                intent.putExtra("tahun", spinnerTahun.getSelectedItem().toString());
                startActivity(intent);
            }
        });


        cardInfo.setOnClickListener(v ->
                        // bisa tampilkan dialog info aplikasi
                {}
        );
    }

    private void loadTotals(String bulan,String tahun) {
        ApiService api = ApiClient.getService();

        // TOTAL PENERIMAAN
        api.getTotalPenerimaan(bulan, tahun).enqueue(new Callback<TotalResponse>() {
            @Override
            public void onResponse(Call<TotalResponse> call, Response<TotalResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int total = response.body().total;
                    tvTotalPenerimaan.setText("Total bulan ini: Rp " + format.format(total));
                }
            }

            @Override
            public void onFailure(Call<TotalResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this,
                        "Gagal load total penerimaan: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // TOTAL PENGELUARAN
        api.getTotalPengeluaran(bulan, tahun).enqueue(new Callback<TotalResponse>() {
            @Override
            public void onResponse(Call<TotalResponse> call, Response<TotalResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int total = response.body().total;
                    tvTotalPengeluaran.setText("Total bulan ini: Rp " + format.format(total));
                }
            }

            @Override
            public void onFailure(Call<TotalResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this,
                        "Gagal load total pengeluaran: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // TOTAL REKENING KORAN (PEREDARAN UANG)
        api.getTotalRekeningKoran(bulan, tahun).enqueue(new Callback<RekeningKoranTotalResponse>() {
            @Override
            public void onResponse(Call<RekeningKoranTotalResponse> call, Response<RekeningKoranTotalResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    int totalPeredaran = response.body().total_peredaran;

                    tvRekkoran.setText(
                            "Total peredaran uang: Rp " + format.format(totalPeredaran)
                    );
                }
            }

            @Override
            public void onFailure(Call<RekeningKoranTotalResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this,
                        "Gagal load rekening koran: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
