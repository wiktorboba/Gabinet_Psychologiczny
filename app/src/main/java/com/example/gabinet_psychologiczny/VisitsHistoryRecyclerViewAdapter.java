package com.example.gabinet_psychologiczny;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VisitsHistoryRecyclerViewAdapter extends RecyclerView.Adapter<VisitsHistoryRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<Visit> visitsList;

    public VisitsHistoryRecyclerViewAdapter(Context context, ArrayList<Visit> visitsList) {
        this.context = context;
        this.visitsList = visitsList;
    }

    @NonNull
    @Override
    public VisitsHistoryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.visits_history_recycler_view_item, parent, false);
        return new VisitsHistoryRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitsHistoryRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.date.setText(visitsList.get(position).getStartDate());
        holder.time.setText(visitsList.get(position).getEndDate());
        holder.service.setText(visitsList.get(position).getService().getName());
    }

    @Override
    public int getItemCount() {
        return visitsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date, time, service;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.visitDate);
            time = itemView.findViewById(R.id.visitTime);
            service = itemView.findViewById(R.id.visitService);
        }
    }
}
