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
import android.widget.TextView;

import com.example.gabinet_psychologiczny.Classes.AddVisitDialog;
import com.example.gabinet_psychologiczny.Classes.VisitsHistoryRecyclerViewAdapter;
import com.example.gabinet_psychologiczny.Database.Relations.PatientWithVisits;
import com.example.gabinet_psychologiczny.Database.Relations.VisitAndService;
import com.example.gabinet_psychologiczny.Model.Patient;
import com.example.gabinet_psychologiczny.Model.Visit;
import com.example.gabinet_psychologiczny.R;
import com.example.gabinet_psychologiczny.ViewModel.VisitViewModel;
import com.example.gabinet_psychologiczny.databinding.FragmentPatientDetailsBinding;
import com.example.gabinet_psychologiczny.databinding.FragmentVisitDetailsBinding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "patient_id";

    // TODO: Rename and change types of parameters
    private int id;

    private FragmentPatientDetailsBinding binding;
    private VisitViewModel visitViewModel;

    Patient patient;
    List<VisitAndService> visitList;

    RecyclerView recyclerView;

    TextView edit;

    public PatientDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment PatientDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientDetailsFragment newInstance(int param1) {
        PatientDetailsFragment fragment = new PatientDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPatientDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setOnItemClickListener(new VisitsHistoryRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(VisitAndService visit) {
                Bundle bundle = new Bundle();
                bundle.putInt("visit_id", visit.visit.getId());
                bundle.putString("patient_first_name", patient.getFirstName());
                bundle.putString("patient_last_name", patient.getLastName());
                Fragment fragment = new VisitDetailsFragment();
                fragment.setArguments(bundle);

                replaceFragment(fragment);
            }
        });


        visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);
        visitViewModel.getPatientWithVisitsById(id).observe(getViewLifecycleOwner(), new Observer<PatientWithVisits>() {
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


    private void openDialog(){
        AddVisitDialog addVisitDialog = new AddVisitDialog(patient.getId(), patient.getFirstName(), patient.getLastName());
        //addVisitDialog.setTargetFragment(PatientSearchFragment.this, 1);
        addVisitDialog.show(getChildFragmentManager(), "Dodaj pacjenta");
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout2, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}