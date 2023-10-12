package com.example.gabinet_psychologiczny.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gabinet_psychologiczny.Models.Patient;
import com.example.gabinet_psychologiczny.R;

import java.util.ArrayList;
import java.util.List;

public class PatientsRecyclerViewAdapter extends RecyclerView.Adapter<PatientsRecyclerViewAdapter.MyViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;
    List<Patient> patientsList = new ArrayList<>();

    public PatientsRecyclerViewAdapter(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public PatientsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patients_recycler_view_item, parent, false);
        return new PatientsRecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientsRecyclerViewAdapter.MyViewHolder holder, int position) {
        Patient currentPatient = patientsList.get(position);
        holder.firstName.setText(currentPatient.getFirstName());
        holder.lastName.setText(currentPatient.getLastName());
        holder.age.setText(String.valueOf(currentPatient.getAge()));
    }

    @Override
    public int getItemCount() {
        return patientsList.size();
    }

    public void setPatientsList(List<Patient> patientsList){
        this.patientsList = patientsList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView firstName, lastName, age;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            firstName = itemView.findViewById(R.id.patientFirstName);
            lastName = itemView.findViewById(R.id.patientLastName);
            age = itemView.findViewById(R.id.patientAge);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
