package com.example.gabinet_psychologiczny;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.gabinet_psychologiczny.databinding.FragmentPatientSearchBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientSearchFragment extends Fragment implements RecyclerViewInterface{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentPatientSearchBinding binding;


    ArrayList<Patient> patientsList = new ArrayList<>();
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
        setUpPatientsList();
        setUpRecyclerView(view);
    }

    private void setUpPatientsList() {
        patientsList.add(new Patient(1, "Jan", "Kowalski", 27, "111222333"));
        patientsList.add(new Patient(2, "Jan", "Kowalski", 27, "111222333"));
        patientsList.add(new Patient(3, "Jan", "Kowalski", 27, "111222333"));
        patientsList.add(new Patient(4, "Jan", "Kowalski", 27, "111222333"));
        patientsList.add(new Patient(5, "Jan", "Kowalski", 27, "111222333"));
        patientsList.add(new Patient(6, "Jan", "Kowalski", 27, "111222333"));
        patientsList.add(new Patient(7, "Jan", "Kowalski", 27, "111222333"));
        patientsList.add(new Patient(8, "Jan", "Kowalski", 27, "111222333"));
        patientsList.add(new Patient(9, "Jan", "Kowalski", 27, "111222333"));
        patientsList.add(new Patient(10, "Jan", "Kowalski", 27, "111222333"));
        patientsList.add(new Patient(11, "Jan", "Kowalski", 27, "111222333"));
        patientsList.add(new Patient(12, "Jan", "Kowalski", 27, "111222333"));
    }

    private void setUpRecyclerView(View view) {
        recyclerView = binding.patientsListRecyclerView;
        PatientsRecyclerViewAdapter adapter = new PatientsRecyclerViewAdapter(getContext(), patientsList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(getActivity(), PatientProfileActivity.class);
        startActivity(i);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}