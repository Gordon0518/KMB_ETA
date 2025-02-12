package com.example.kmb.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kmb.R;
import com.example.kmb.model.busList;

import java.util.ArrayList;
import java.util.List;

public class busAdapter extends RecyclerView.Adapter<busAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public List<busList> busList;
    private List<busList> busListFull;
    private final OnItemClickListener listener;

    public busAdapter(List<busList> busList, OnItemClickListener listener) {
        this.busList = busList;
        this.busListFull = new ArrayList<>(busList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buslist_view, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        busList bus = busList.get(position);
        holder.busNum.setText(bus.getBusNum());
        holder.start.setText(bus.getStart());
        holder.dest.setText(bus.getDest());
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView busNum, start, dest;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            busNum = itemView.findViewById(R.id.busNum);
            start = itemView.findViewById(R.id.start);
            dest = itemView.findViewById(R.id.dest);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(position);
                }
            });
        }
    }

    public Filter getFilter() {
        return busFilter;
    }

    private final Filter busFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<busList> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(busListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (busList bus : busListFull) {
                    if (bus.getBusNum().toLowerCase().contains(filterPattern)) {
                        filteredList.add(bus);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            busList.clear();
            busList.addAll((List<busList>) results.values);
            notifyDataSetChanged();
        }
    };

    public void updateFullList(List<busList> newList) {
        busListFull.clear();
        busListFull.addAll(newList);
        busList.clear();
        busList.addAll(newList);
        notifyDataSetChanged();
    }
}







