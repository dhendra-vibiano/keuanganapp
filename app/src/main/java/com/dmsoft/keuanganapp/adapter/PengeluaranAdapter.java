package com.dmsoft.keuanganapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dmsoft.keuanganapp.R;
import com.dmsoft.keuanganapp.model.PengeluaranItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PengeluaranAdapter extends RecyclerView.Adapter<PengeluaranAdapter.ViewHolder> {

    private List<PengeluaranItem> list;
    private NumberFormat format = NumberFormat.getInstance(new Locale("id", "ID"));

    public PengeluaranAdapter(List<PengeluaranItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pengeluaran, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        PengeluaranItem item = list.get(i);

        holder.tvTanggal.setText(item.tanggal);
        holder.tvDeskripsi.setText(item.deskripsi);
        holder.tvNominal.setText("Rp " + format.format(item.nominal));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTanggal, tvDeskripsi, tvNominal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvNominal = itemView.findViewById(R.id.tvNominal);
        }
    }
}
