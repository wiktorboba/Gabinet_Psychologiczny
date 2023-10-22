package com.example.gabinet_psychologiczny.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.gabinet_psychologiczny.Classes.AddPatientDialog;
import com.example.gabinet_psychologiczny.Classes.AddVisitDialog;
import com.example.gabinet_psychologiczny.Database.Relations.PatientWithVisits;
import com.example.gabinet_psychologiczny.Database.Relations.VisitAndService;
import com.example.gabinet_psychologiczny.Fragments.PatientSearchFragment;
import com.example.gabinet_psychologiczny.Model.Patient;
import com.example.gabinet_psychologiczny.Model.Visit;
import com.example.gabinet_psychologiczny.Classes.VisitsHistoryRecyclerViewAdapter;
import com.example.gabinet_psychologiczny.ViewModel.PatientViewModel;
import com.example.gabinet_psychologiczny.ViewModel.VisitViewModel;
import com.example.gabinet_psychologiczny.databinding.ActivityPatientProfileBinding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PatientProfileActivity extends AppCompatActivity {
    private ActivityPatientProfileBinding binding;
    private VisitViewModel visitViewModel;

    Patient patient;
    List<VisitAndService> visitList;

    RecyclerView recyclerView;

    TextView edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        edit = binding.editProfile;
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        ExtendedFloatingActionButton buttonAddPatient = binding.addVisitButton;
        buttonAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });


        recyclerView = binding.visitsHistoryRecyclerView;
        VisitsHistoryRecyclerViewAdapter adapter = new VisitsHistoryRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new VisitsHistoryRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(VisitAndService visit) {

            }
        });

        Intent i = getIntent();
        int id = i.getIntExtra("id", 0);

        visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);
        visitViewModel.getPatientWithVisitsById(id).observe(this, new Observer<PatientWithVisits>() {
            @Override
            public void onChanged(@Nullable PatientWithVisits p) {
                patient = p.patient;
                visitList = p.visitList;
                adapter.setVisitsList(p.visitList);
                setUpPersonalInformation();
            }
        });

    }


    private void setUpPersonalInformation() {
        binding.name.setText(patient.getFirstName());
        binding.lastName.setText(patient.getLastName());
        binding.age.setText(Integer.toString(patient.getAge()));
        binding.phoneNumber.setText(patient.getPhoneNumber());
        binding.numberOfVisits.setText(Integer.toString(visitList.size())); //TODO
    }

    private void addVisit() {
        Visit visit = new Visit(1, patient.getId(), "opis", "19/10/2023", "16:00", "17:30", true, true);
        visitViewModel.insert(visit);
    }

    private void openDialog(){
        AddVisitDialog addVisitDialog = new AddVisitDialog(patient.getId(), patient.getFirstName(), patient.getLastName());
        //addVisitDialog.setTargetFragment(PatientSearchFragment.this, 1);
        addVisitDialog.show(getSupportFragmentManager(), "Dodaj pacjenta");
    }
}