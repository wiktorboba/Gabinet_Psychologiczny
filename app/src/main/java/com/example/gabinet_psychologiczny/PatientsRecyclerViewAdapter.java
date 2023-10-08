package com.example.gabinet_psychologiczny;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PatientsRecyclerViewAdapter extends RecyclerView.Adapter<PatientsRecyclerViewAdapter.MyViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<Patient> patientsList;

    public PatientsRecyclerViewAdapter(Context context, ArrayList<Patient> patientsList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.patientsList = patientsList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public PatientsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patients_recycler_view_item, parent, false);
        return new PatientsRecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientsRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.firstName.setText(patientsList.get(position).getName());
        holder.lastName.setText(patientsList.get(position).getLastName());
        holder.age.setText(String.valueOf(patientsList.get(position).getAge()));
    }

    @Override
    public int getItemCount() {
        return patientsList.size();
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
