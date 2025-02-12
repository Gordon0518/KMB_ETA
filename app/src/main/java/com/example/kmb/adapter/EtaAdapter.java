package com.example.kmb.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kmb.R;
import com.example.kmb.model.stopETA;

import java.util.List;

public class EtaAdapter extends RecyclerView.Adapter<EtaAdapter.ViewHolder> {
    private final List<stopETA> stopETAList;

    public EtaAdapter(List<stopETA> stopETAList) {
        this.stopETAList = stopETAList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stop_eta, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        stopETA stopETA = stopETAList.get(position);
        holder.tvSequence.setText(String.valueOf(position + 1));
        holder.tvStopName.setText(stopETA.getStopName());
        holder.tvETAs.setText(formatETAs(stopETA.getEtas()));
    }

    private String formatETAs(List<String> etas) {
        if(etas.isEmpty()) return "No ETAs available";
        return "Next buses: " + TextUtils.join(", ", etas);
    }

    @Override
    public int getItemCount() {
        return stopETAList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStopName, tvETAs, tvSequence;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStopName = itemView.findViewById(R.id.tvStopName);
            tvETAs = itemView.findViewById(R.id.tvETAs);
            tvSequence =itemView.findViewById(R.id.tvSequence);
        }
    }
}

