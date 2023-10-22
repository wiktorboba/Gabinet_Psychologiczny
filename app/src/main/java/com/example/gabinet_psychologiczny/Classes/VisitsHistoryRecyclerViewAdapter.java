package com.example.gabinet_psychologiczny.Classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gabinet_psychologiczny.Database.Relations.VisitAndService;
import com.example.gabinet_psychologiczny.Model.Visit;
import com.example.gabinet_psychologiczny.R;

import java.util.ArrayList;
import java.util.List;

public class VisitsHistoryRecyclerViewAdapter extends RecyclerView.Adapter<VisitsHistoryRecyclerViewAdapter.MyViewHolder> {

    List<VisitAndService> visitsList = new ArrayList<>();

    private OnItemClickListener listener;

    @NonNull
    @Override
    public VisitsHistoryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.visits_history_recycler_view_item, parent, false);
        return new VisitsHistoryRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitsHistoryRecyclerViewAdapter.MyViewHolder holder, int position) {
        VisitAndService currentVisit = visitsList.get(position);

        String startEndTime = currentVisit.visit.getStartTime() + " - " + currentVisit.visit.getEndTime();
        holder.day.setText(currentVisit.visit.getDay());
        holder.time.setText(startEndTime);
        holder.service.setText(currentVisit.service.getName());
    }

    @Override
    public int getItemCount() {
        return visitsList.size();
    }

    public void setVisitsList(List<VisitAndService> visitsList){
        this.visitsList = visitsList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView day, time, service;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.visitDay);
            time = itemView.findViewById(R.id.visitTime);
            service = itemView.findViewById(R.id.visitService);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(visitsList.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(VisitAndService visit);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
