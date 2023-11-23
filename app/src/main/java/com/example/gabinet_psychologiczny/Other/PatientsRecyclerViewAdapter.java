package com.example.gabinet_psychologiczny.Other;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gabinet_psychologiczny.Model.Patient;
import com.example.gabinet_psychologiczny.R;

import java.util.ArrayList;
import java.util.List;

public class PatientsRecyclerViewAdapter extends RecyclerView.Adapter<PatientsRecyclerViewAdapter.MyViewHolder>{
    private OnItemClickListener listener;
    List<Patient> patientsList = new ArrayList<>();
    private int selectedItemIndex = -1;
    @NonNull
    @Override
    public PatientsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patients_recycler_view_item, parent, false);
        return new PatientsRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientsRecyclerViewAdapter.MyViewHolder holder, int position) {
        Patient currentPatient = patientsList.get(position);
        String name = currentPatient.getFirstName() + " " + currentPatient.getLastName();
        holder.fullName.setText(name);
        holder.age.setText(String.valueOf(currentPatient.getAge()) + " lat");
        if(selectedItemIndex==position)
            holder.itemView.setSelected(true);
        else
            holder.itemView.setSelected(false);
    }

    @Override
    public int getItemCount() {
        return patientsList.size();
    }

    public void setPatientsList(List<Patient> patientsList){
        this.patientsList = patientsList;
        notifyDataSetChanged();
    }

    public void setSelectedItemIndex(int index){
        selectedItemIndex = index;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fullName, age;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.patientFullName);
            age = itemView.findViewById(R.id.patientAge);;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    setSelectedItemIndex(position);

                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(patientsList.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Patient patient);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
