package com.example.gabinet_psychologiczny.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabinet_psychologiczny.Classes.CalendarUtils;
import com.example.gabinet_psychologiczny.Classes.VisitsHistoryRecyclerViewAdapter;
import com.example.gabinet_psychologiczny.Database.Relations.PatientWithVisits;
import com.example.gabinet_psychologiczny.Database.Relations.VisitAndService;
import com.example.gabinet_psychologiczny.Model.Service;
import com.example.gabinet_psychologiczny.Model.Visit;
import com.example.gabinet_psychologiczny.R;
import com.example.gabinet_psychologiczny.ViewModel.PatientViewModel;
import com.example.gabinet_psychologiczny.ViewModel.VisitViewModel;
import com.example.gabinet_psychologiczny.databinding.FragmentCalendarBinding;
import com.example.gabinet_psychologiczny.databinding.FragmentVisitDetailsBinding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VisitDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisitDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "visit_id";
    private static final String ARG_PARAM2 ="patient_first_name";
    private static final String ARG_PARAM3 ="patient_last_name";

    // TODO: Rename and change types of parameters
    private int id;
    private String firstName;
    private String lastName;
    private FragmentVisitDetailsBinding binding;
    private VisitViewModel visitViewModel;
    RecyclerView recyclerView;
    Visit visit;
    Service service;

    public VisitDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param param3 Parameter 3.
     * @return A new instance of fragment VisitDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VisitDetailsFragment newInstance(int param1, String param2, String param3) {
        VisitDetailsFragment fragment = new VisitDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_PARAM1);
            firstName = getArguments().getString(ARG_PARAM2);
            lastName = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVisitDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        FloatingActionButton buttonMoreOptions = binding.moreOptions;


        recyclerView = binding.annotationsRecyclerview;
        //VisitsHistoryRecyclerViewAdapter adapter = new AnnotationsRecyclerViewAdapter();
        //recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        adapter.setOnItemClickListener(new AnnotationsRecyclerViewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(VisitAndService visit) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("visit_id", visit.visit.getId());
//                Fragment fragment = new VisitDetailsFragment();
//                fragment.setArguments(bundle);
//
//                replaceFragment(fragment);
//            }
//        });


        TextView description = binding.description;

        description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE) {
                    visit.setDescription(textView.getText().toString());
                    visitViewModel.update(visit);
                    Toast.makeText(getActivity(), "Opis zmieniono pomyślnie!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });


        visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);
        visitViewModel.getVisitAndServiceById(id).observe(getViewLifecycleOwner(), new Observer<VisitAndService>() {
            @Override
            public void onChanged(@Nullable VisitAndService v) {
                visit = v.visit;
                service = v.service;
                setUpVisitInformation();
            }
        });

    }

    private void setUpVisitInformation() {
        binding.service.setText(service.getName());
        binding.patientName.setText(firstName + " " + lastName);
        binding.visitDate.setText(CalendarUtils.formattedDate(visit.getDay()));
        binding.visitStartEndTime.setText(visit.getStartTime() + " - " + visit.getEndTime());
        binding.description.setText(visit.getDescription());

    }


}