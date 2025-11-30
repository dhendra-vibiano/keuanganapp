package com.dmsoft.keuanganapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dmsoft.keuanganapp.api.ApiClient;
import com.dmsoft.keuanganapp.api.ApiService;
import com.dmsoft.keuanganapp.model.SimpleResponse;
import com.dmsoft.keuanganapp.model.TransaksiRequest;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTransaksiActivity extends AppCompatActivity {

    private EditText etTanggal, etNominal, etMetode, etSumber, etDeskripsi;
    private Button btnSimpan;

    private String jenis; // penerimaan / pengeluaran

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaksi);

        jenis = getIntent().getStringExtra("jenis");
        if (jenis == null) jenis = "penerimaan";

        setTitle("Tambah " + jenis);

        etTanggal = findViewById(R.id.etTanggal);
        etNominal = findViewById(R.id.etNominal);
        etMetode = findViewById(R.id.etMetode);
        etSumber = findViewById(R.id.etSumber);
        etDeskripsi = findViewById(R.id.etDeskripsi);
        btnSimpan = findViewById(R.id.btnSimpan);

        // ==== DATEPICKER ====
        etTanggal.setFocusable(false);
        etTanggal.setOnClickListener(v -> showDatePicker());

        btnSimpan.setOnClickListener(v -> simpanTransaksi());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                (DatePicker view, int y, int m, int d) -> {
                    // Format: YYYY-MM-DD
                    String tgl = y + "-" + String.format("%02d", (m + 1)) + "-" + String.format("%02d", d);
                    etTanggal.setText(tgl);
                },
                year, month, day
        );

        datePicker.show();
    }

    private void simpanTransaksi() {
        String tanggal = etTanggal.getText().toString().trim();
        String nominalStr = etNominal.getText().toString().trim();
        String metode = etMetode.getText().toString().trim();
        String sumber = etSumber.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();

        if (TextUtils.isEmpty(tanggal) || TextUtils.isEmpty(nominalStr)) {
            Toast.makeText(this, "Tanggal dan nominal wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        int nominal = Integer.parseInt(nominalStr);

        TransaksiRequest request = new TransaksiRequest(
                jenis,
                tanggal,
                nominal,
                metode,
                sumber,
                deskripsi
        );

        ApiService api = ApiClient.getService();

        api.addTransaksi(request).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    SimpleResponse res = response.body();

                    if (res.status) {
                        Toast.makeText(AddTransaksiActivity.this,
                                "Berhasil disimpan!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddTransaksiActivity.this,
                                "Gagal: " + res.message, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(AddTransaksiActivity.this,
                            "Response tidak valid dari server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Toast.makeText(AddTransaksiActivity.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
