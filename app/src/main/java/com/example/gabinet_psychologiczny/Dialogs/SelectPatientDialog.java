package com.example.gabinet_psychologiczny.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gabinet_psychologiczny.Activities.PatientProfileActivity;
import com.example.gabinet_psychologiczny.Model.Patient;
import com.example.gabinet_psychologiczny.Model.Visit;
import com.example.gabinet_psychologiczny.Other.PatientsRecyclerViewAdapter;
import com.example.gabinet_psychologiczny.R;
import com.example.gabinet_psychologiczny.ViewModel.PatientViewModel;
import com.example.gabinet_psychologiczny.databinding.FragmentPatientSearchBinding;

import java.util.List;

public class SelectPatientDialog extends DialogFragment {

    private SelectPatientDialog.SelectPatientDialogListener listener;
    private PatientViewModel patientViewModel;
    private RecyclerView recyclerView;
    private SearchView patientSearchView;
    private Patient selectedPatient = null;
    private View dialogView = null;

    public static SelectPatientDialog newInstance() {
        SelectPatientDialog frag = new SelectPatientDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return dialogView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        patientSearchView = view.findViewById(R.id.patientSearchView);
        recyclerView = view.findViewById(R.id.patientsListRecyclerView);

        PatientsRecyclerViewAdapter adapter = new PatientsRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setOnItemClickListener(new PatientsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Patient patient) {
                selectedPatient = patient;
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

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.fragment_select_patient_dialog, null);
        builder.setView(dialogView);

        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button selectButton = dialogView.findViewById(R.id.selectButton);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPatient == null) {
                    Toast.makeText(getActivity(), "Nie wybrano pacjenta!", Toast.LENGTH_SHORT).show();
                }
                else{
                    listener.onDialogSuccess(selectedPatient.getId(), selectedPatient.getFirstName(), selectedPatient.getLastName());
                    dismiss();
                }
            }
        });




        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (SelectPatientDialog.SelectPatientDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement TimesPickerDialogListener");
        }
    }

    public interface SelectPatientDialogListener{
        void onDialogSuccess(int id, String name, String lName);

    }
}
