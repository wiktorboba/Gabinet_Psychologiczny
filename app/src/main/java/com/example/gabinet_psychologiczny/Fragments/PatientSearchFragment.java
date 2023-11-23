package com.example.gabinet_psychologiczny.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.gabinet_psychologiczny.Activities.PatientProfileActivity;
import com.example.gabinet_psychologiczny.Dialogs.AddPatientDialog;
import com.example.gabinet_psychologiczny.ViewModel.PatientViewModel;
import com.example.gabinet_psychologiczny.Model.Patient;
import com.example.gabinet_psychologiczny.Other.PatientsRecyclerViewAdapter;
import com.example.gabinet_psychologiczny.R;
import com.example.gabinet_psychologiczny.databinding.FragmentPatientSearchBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientSearchFragment extends Fragment implements AddPatientDialog.AddPatientDialogListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentPatientSearchBinding binding;

    private PatientViewModel patientViewModel;

    RecyclerView recyclerView;
    SearchView patientSearchView;


    public PatientSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientSearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientSearchFragment newInstance(String param1, String param2) {
        PatientSearchFragment fragment = new PatientSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPatientSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        patientSearchView = binding.patientSearchView;

        recyclerView = binding.patientsListRecyclerView;
        PatientsRecyclerViewAdapter adapter = new PatientsRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setOnItemClickListener(new PatientsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Patient patient) {
                Intent i = new Intent(getActivity(), PatientProfileActivity.class);
                int id = patient.getId();
                i.putExtra("id", id);
                startActivity(i);
            }
        });



        FloatingActionButton buttonAddPatient = binding.buttonAddPatient;
        buttonAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });



        patientViewModel = new ViewModelProvider(this).get(PatientViewModel .class);
        patientViewModel.getAllPatients().observe(getViewLifecycleOwner(), new Observer<List<Patient>>() {
            @Override
            public void onChanged(@Nullable List<Patient> patients) {
                adapter.setPatientsList(patients);
            }
        });


        patientSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if(s != null && !s.isEmpty()){
                    String searchQuery = "%"+s+"%";
                    patientViewModel.searchPatient(searchQuery).observe(getViewLifecycleOwner(), new Observer<List<Patient>>() {
                        @Override
                        public void onChanged(List<Patient> patients) {
                            adapter.setPatientsList(patients);
                        }
                    });
                }
                return true;
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void openDialog(){
        AddPatientDialog addPatientDialog = new AddPatientDialog();
        addPatientDialog.setTargetFragment(PatientSearchFragment.this, 1);
        addPatientDialog.show(getFragmentManager(), "Dodaj pacjenta");
    }

    @Override
    public void onDialogSuccess(String firstname, String lastname, String age, String phonenumber) {

        Patient patient = new Patient(firstname, lastname, Integer.parseInt(age), phonenumber);
        patientViewModel.insert(patient);
        Toast.makeText(getActivity(), "Pacjent dodany pomyślnie!", Toast.LENGTH_SHORT).show();
    }


    private void searchDatabase(String searchQuery) {

    }

    @Override
    public void onResume() {
        super.onResume();
        PatientsRecyclerViewAdapter adapter = (PatientsRecyclerViewAdapter)recyclerView.getAdapter();
        adapter.setSelectedItemIndex(-1);
    }
}