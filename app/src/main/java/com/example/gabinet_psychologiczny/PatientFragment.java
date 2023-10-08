package com.example.gabinet_psychologiczny;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gabinet_psychologiczny.databinding.FragmentPatientBinding;
import com.example.gabinet_psychologiczny.databinding.FragmentPatientSearchBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentPatientBinding binding;

    Patient patient = new Patient(1, "Jan", "Kowalski", 27, "111222333");
    Service service = new Service(1, "Terapia", 100);
    ArrayList<Visit> visitsHistory = new ArrayList<>();
    RecyclerView recyclerView;




    public PatientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientFragment newInstance(String param1, String param2) {
        PatientFragment fragment = new PatientFragment();
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
        binding = FragmentPatientBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpVisitsHistory();
        setUpRecyclerView(view);
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

    private void setUpRecyclerView(View view) {
        recyclerView = binding.visitsHistoryRecyclerView;
        VisitsHistoryRecyclerViewAdapter adapter = new VisitsHistoryRecyclerViewAdapter(getContext(), visitsHistory);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}