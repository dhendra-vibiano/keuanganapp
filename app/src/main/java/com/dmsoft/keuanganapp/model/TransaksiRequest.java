package com.dmsoft.keuanganapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TransaksiRequest {

    @SerializedName("jenis_transaksi")
    private String jenisTransaksi;

    private String tanggal;
    private int nominal;
    private String metode;
    private String sumber;
    private String deskripsi;
    private String raw;

    private List<DetailItem> detail; // optional

    public TransaksiRequest(String jenisTransaksi, String tanggal, int nominal,
                            String metode, String sumber, String deskripsi) {
        this.jenisTransaksi = jenisTransaksi;
        this.tanggal = tanggal;
        this.nominal = nominal;
        this.metode = metode;
        this.sumber = sumber;
        this.deskripsi = deskripsi;
        this.raw = ""; // bisa isi json mentah kalau mau
        this.detail = null; // atau isi list jika pakai detail
    }

    // getter & setter kalau perlu
}
