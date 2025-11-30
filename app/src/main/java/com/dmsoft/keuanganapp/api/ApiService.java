package com.dmsoft.keuanganapp.api;



import com.dmsoft.keuanganapp.model.PenerimaanItem;
import com.dmsoft.keuanganapp.model.PenerimaanResponse;
import com.dmsoft.keuanganapp.model.PengeluaranItem;
import com.dmsoft.keuanganapp.model.PengeluaranResponse;
import com.dmsoft.keuanganapp.model.RekeningKoranItem;
import com.dmsoft.keuanganapp.model.RekeningKoranResponse;
import com.dmsoft.keuanganapp.model.RekeningKoranTotalResponse;
import com.dmsoft.keuanganapp.model.SimpleResponse;
import com.dmsoft.keuanganapp.model.TotalResponse;
import com.dmsoft.keuanganapp.model.TransaksiRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("add_transaksi.php?action=add")
    Call<SimpleResponse> addTransaksi(@Body TransaksiRequest request);
    @GET("get_penerimaan.php")
    Call<List<PenerimaanItem>> getPenerimaan(
            @Query("bulan") String bulan,
            @Query("tahun") String tahun
    );
    @GET("get_pengeluaran.php")
    Call<List<PengeluaranItem>> getPengeluaran(
            @Query("bulan") String bulan,
            @Query("tahun") String tahun
    );
    @GET("get_koran.php")
    Call<List<RekeningKoranItem>> getRekeningKoran(
            @Query("bulan") String bulan,
            @Query("tahun") String tahun
    );
    @GET("get_total_penerimaan.php")
    Call<TotalResponse> getTotalPenerimaan(
            @Query("bulan") String bulan,
            @Query("tahun") String tahun
    );
    @GET("get_total_pengeluaran.php")
    Call<TotalResponse> getTotalPengeluaran(
            @Query("bulan") String bulan,
            @Query("tahun") String tahun
    );
    @GET("get_total_rekening_koran.php")
    Call<RekeningKoranTotalResponse> getTotalRekeningKoran(
            @Query("bulan") String bulan,
            @Query("tahun") String tahun
    );





}
