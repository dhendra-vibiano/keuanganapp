package com.dmsoft.keuanganapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dmsoft.keuanganapp.R;
import com.dmsoft.keuanganapp.model.RekeningKoranItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RekeningKoranAdapter extends RecyclerView.Adapter<RekeningKoranAdapter.ViewHolder> {

    private List<RekeningKoranItem> list;
    NumberFormat format = NumberFormat.getInstance(new Locale("id", "ID"));

    public RekeningKoranAdapter(List<RekeningKoranItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rekening_koran, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        RekeningKoranItem item = list.get(position);

        h.tvTanggal.setText(item.tanggal);
        h.tvDeskripsi.setText(item.deskripsi);
        h.tvPenerimaan.setText("Rp " + format.format(item.penerimaan));
        h.tvPengeluaran.setText("Rp " + format.format(item.pengeluaran));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTanggal, tvDeskripsi, tvPenerimaan, tvPengeluaran;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvPenerimaan = itemView.findViewById(R.id.tvPenerimaan);
            tvPengeluaran = itemView.findViewById(R.id.tvPengeluaran);
        }
    }
}
