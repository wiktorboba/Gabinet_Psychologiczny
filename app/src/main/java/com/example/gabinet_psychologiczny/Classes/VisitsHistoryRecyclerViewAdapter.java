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
        View view = inflater.inflate(R.layout.visits_history_recycler_view_item_v2, parent, false);
        return new VisitsHistoryRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitsHistoryRecyclerViewAdapter.MyViewHolder holder, int position) {
        VisitAndService currentVisit = visitsList.get(position);
        String date = currentVisit.visit.getDay();
        String dayMonth = date.substring(0, date.length()-1-4);
        String year = date.substring(date.length()-4, date.length());

        String startEndTime = currentVisit.visit.getStartTime() + " - " + currentVisit.visit.getEndTime();
        holder.dayAndMonth.setText(dayMonth);
        holder.year.setText(year);
        holder.visitTime.setText(startEndTime);
        holder.serviceName.setText(currentVisit.service.getName());
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
        TextView dayAndMonth, year, serviceName, visitTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            dayAndMonth = itemView.findViewById(R.id.dayAndMonth);
            year = itemView.findViewById(R.id.year);
            serviceName = itemView.findViewById(R.id.serviceName);
            visitTime = itemView.findViewById(R.id.visitTime);

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
