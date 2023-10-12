package com.example.gabinet_psychologiczny.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.gabinet_psychologiczny.Models.Patient;
import com.example.gabinet_psychologiczny.Models.Service;
import com.example.gabinet_psychologiczny.Models.Visit;
import com.example.gabinet_psychologiczny.Classes.VisitsHistoryRecyclerViewAdapter;
import com.example.gabinet_psychologiczny.databinding.ActivityPatientProfileBinding;

import java.util.ArrayList;

public class PatientProfileActivity extends AppCompatActivity {
    private ActivityPatientProfileBinding binding;

    Patient patient;
    Service service = new Service(1, "Terapia", 100);
    ArrayList<Visit> visitsHistory = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent i = getIntent();
        patient = i.getParcelableExtra("patient");

        setUpVisitsHistory();
        setUpPersonalInformation();
        setUpRecyclerView();
    }

    private void setUpVisitsHistory() {
        visitsHistory.add(new Visit(1, service, patient, "", "5/10/2023 15:30", "5/10/2023 16:00", true, true));
        visitsHistory.add(new Visit(2, service, patient, "", "6/10/2023 13:30", "5/10/2023 14:00", true, true));
        visitsHistory.add(new Visit(3, service, patient, "", "7/10/2023 10:00", "5/10/2023 11:00", true, true));
        visitsHistory.add(new Visit(4, service, patient, "", "7/10/2023 10:00", "5/10/2023 11:00", true, true));
        visitsHistory.add(new Visit(5, service, patient, "", "7/10/2023 10:00", "5/10/2023 11:00", true, true));
        visitsHistory.add(new Visit(6, service, patient, "", "7/10/2023 10:00", "5/10/2023 11:00", true, true));
        visitsHistory.add(new Visit(7, service, patient, "", "7/10/2023 10:00", "5/10/2023 11:00", true, true));
        visitsHistory.add(new Visit(8, service, patient, "", "7/10/2023 10:00", "5/10/2023 11:00", true, true));
        visitsHistory.add(new Visit(9, service, patient, "", "7/10/2023 10:00", "5/10/2023 11:00", true, true));
        visitsHistory.add(new Visit(10, service, patient, "", "7/10/2023 10:00", "5/10/2023 11:00", true, true));
        visitsHistory.add(new Visit(11, service, patient, "", "7/10/2023 10:00", "5/10/2023 11:00", true, true));
        visitsHistory.add(new Visit(12, service, patient, "", "7/10/2023 10:00", "5/10/2023 11:00", true, true));
    }

    private void setUpRecyclerView() {
        recyclerView = binding.visitsHistoryRecyclerView;
        VisitsHistoryRecyclerViewAdapter adapter = new VisitsHistoryRecyclerViewAdapter(this, visitsHistory);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpPersonalInformation() {
        binding.name.setText(patient.getFirstName());
        binding.lastName.setText(patient.getLastName());
        binding.age.setText(Integer.toString(patient.getAge()));
        binding.phoneNumber.setText(patient.getPhoneNumber());
        binding.numberOfVisits.setText(Integer.toString(visitsHistory.size()));
    }
}