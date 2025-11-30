package com.dmsoft.keuanganapp.model;

import java.util.List;

public class RekeningKoranResponse {
    public boolean status;
    public int total_masuk;
    public int total_keluar;
    public List<RekeningKoranItem> data;
}
