package com.example.gabinet_psychologiczny.Other;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gabinet_psychologiczny.Database.Relations.VisitWithAnnotationsAndPatientAndService;
import com.example.gabinet_psychologiczny.R;

import java.util.ArrayList;
import java.util.List;

public class VisitsHistoryRecyclerViewAdapter extends RecyclerView.Adapter<VisitsHistoryRecyclerViewAdapter.MyViewHolder> {

    List<VisitWithAnnotationsAndPatientAndService> visitsList = new ArrayList<>();

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
        VisitWithAnnotationsAndPatientAndService currentVisit = visitsList.get(position);
        String day = Integer.toString(currentVisit.visit.getDay().getDayOfMonth());
        String month = Integer.toString(currentVisit.visit.getDay().getMonthValue());
        String year = Integer.toString(currentVisit.visit.getDay().getYear());
        String startEndTime = CalendarUtils.formattedTime(currentVisit.visit.getStartTime()) + " - " + CalendarUtils.formattedTime(currentVisit.visit.getEndTime());

        holder.dayAndMonth.setText(day + "." + month);
        holder.year.setText(year);
        holder.visitTime.setText(startEndTime);
        holder.serviceName.setText(currentVisit.service.getName());

        boolean annotationStatus = !currentVisit.annotations.isEmpty();

        setStatusIcons(currentVisit.visit.getVisitStatus(), currentVisit.visit.getPaymentStatus(), annotationStatus, holder.visitIcon, holder.paymentIcon, holder.hasAnnotationIcon, holder.resources);
    }

    @Override
    public int getItemCount() {
        return visitsList.size();
    }

    public void setVisitsList(List<VisitWithAnnotationsAndPatientAndService> visitsList){
        this.visitsList = visitsList;
        notifyDataSetChanged();
    }

    private void setStatusIcons(int visitStatus, int paymentStatus, boolean hasAnnotationStatus, ImageView visitStatusIcon, ImageView paymentStatusIcon, ImageView hasAnnotationIcon, Resources resources){

        switch (visitStatus){
            case 0:
                visitStatusIcon.setImageResource(R.drawable.baseline_pending_actions_24);
                visitStatusIcon.setColorFilter(resources.getColor(R.color.yellow, null));
                break;
            case 1:
                visitStatusIcon.setImageResource(R.drawable.baseline_content_paste_off_24);
                visitStatusIcon.setColorFilter(resources.getColor(R.color.red, null));
                break;
            case 2:
            case 3:
                visitStatusIcon.setImageResource(R.drawable.baseline_content_paste_off_24);
                visitStatusIcon.setColorFilter(resources.getColor(R.color.gray, null));
                break;
            case 4:
                visitStatusIcon.setImageResource(R.drawable.outline_assignment_turned_in_24);
                visitStatusIcon.setColorFilter(resources.getColor(R.color.green, null));
                break;
        }

        if(paymentStatus==0){
            paymentStatusIcon.setImageResource(R.drawable.baseline_money_off_24);
            paymentStatusIcon.setColorFilter(resources.getColor(R.color.red, null));
        }
        else {
            paymentStatusIcon.setImageResource(R.drawable.baseline_attach_money_24);
            paymentStatusIcon.setColorFilter(resources.getColor(R.color.green, null));
        }

        if(hasAnnotationStatus)
            hasAnnotationIcon.setVisibility(View.VISIBLE);
        else
            hasAnnotationIcon.setVisibility(View.INVISIBLE);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dayAndMonth, year, serviceName, visitTime;
        ImageView visitIcon, paymentIcon, hasAnnotationIcon;
        Resources resources;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            resources = itemView.getResources();

            dayAndMonth = itemView.findViewById(R.id.dayAndMonth);
            year = itemView.findViewById(R.id.year);
            serviceName = itemView.findViewById(R.id.serviceName);
            visitTime = itemView.findViewById(R.id.visitTime);

            visitIcon = itemView.findViewById(R.id.visitIcon);
            paymentIcon = itemView.findViewById(R.id.paymentIcon);
            hasAnnotationIcon = itemView.findViewById(R.id.hasAnnotationIcon);

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
        void onItemClick(VisitWithAnnotationsAndPatientAndService visit);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
