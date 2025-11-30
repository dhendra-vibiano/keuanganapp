package com.dmsoft.keuanganapp.model;

public class DetailItem {
    public String item;
    public int qty;
    public int harga;
    public int subtotal;

    public DetailItem(String item, int qty, int harga, int subtotal) {
        this.item = item;
        this.qty = qty;
        this.harga = harga;
        this.subtotal = subtotal;
    }
}