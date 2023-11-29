package com.example.gabinet_psychologiczny.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabinet_psychologiczny.Dialogs.AddVisitDialog;
import com.example.gabinet_psychologiczny.Dialogs.EditVisitDialog;
import com.example.gabinet_psychologiczny.Dialogs.SelectPatientDialog;
import com.example.gabinet_psychologiczny.Other.CalendarUtils;
import com.example.gabinet_psychologiczny.Database.Relations.VisitWithPatientAndService;
import com.example.gabinet_psychologiczny.Model.Patient;
import com.example.gabinet_psychologiczny.Model.Service;
import com.example.gabinet_psychologiczny.Model.Visit;
import com.example.gabinet_psychologiczny.R;
import com.example.gabinet_psychologiczny.ViewModel.VisitViewModel;
import com.example.gabinet_psychologiczny.databinding.FragmentVisitDetailsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VisitDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisitDetailsFragment extends Fragment implements EditVisitDialog.EditVisitDialogListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "visit_id";
    private static final String ARG_PARAM2 ="patient_first_name";
    private static final String ARG_PARAM3 ="patient_last_name";

    // TODO: Rename and change types of parameters
    private int id;
    private FragmentVisitDetailsBinding binding;
    private VisitViewModel visitViewModel;
    RecyclerView recyclerView;
    Visit visit;
    Service service;
    Patient patient;

    String[] visitStatusItems;
    String[] paymentStatusItems;

    boolean optionsClicked = false;
    FloatingActionButton optionsButton;
    FloatingActionButton addAnnotationButton;
    FloatingActionButton editVisitStatus;

    Animation showButtonAnimation;
    Animation hideButtonAnimation;

    public VisitDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment VisitDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VisitDetailsFragment newInstance(int param1) {
        VisitDetailsFragment fragment = new VisitDetailsFragment();
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
        binding = FragmentVisitDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton buttonMoreOptions = binding.moreOptions;
        recyclerView = binding.annotationsRecyclerview;
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

        optionsButton = binding.moreOptions;
        addAnnotationButton = binding.addAnnotationButton;
        editVisitStatus = binding.editStatusButton;
        showButtonAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.expand_buttons_from_bottom);
        hideButtonAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.hide_butons_to_bottom);

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionsClicked = !optionsClicked;
                setVisibility(optionsClicked);
                setAnimation(optionsClicked);
                addAnnotationButton.setClickable(optionsClicked);
                editVisitStatus.setClickable(optionsClicked);
            }
        });

        editVisitStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditVisitDialog();
            }
        });


        visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);
        visitViewModel.getVisitAndServiceById(id).observe(getViewLifecycleOwner(), new Observer<VisitWithPatientAndService>() {
            @Override
            public void onChanged(@Nullable VisitWithPatientAndService v) {
                visit = v.visit;
                service = v.service;
                patient = v.patient;
                setUpVisitInformation();
                setStatus();
            }
        });

    }

    private void setUpVisitInformation() {
        binding.service.setText(service.getName());
        binding.patientName.setText(patient.getFirstName() + " " + patient.getLastName());
        binding.visitDate.setText(CalendarUtils.formattedDate(visit.getDay()));
        binding.visitStartEndTime.setText(visit.getStartTime() + " - " + visit.getEndTime());
        binding.description.setText(visit.getDescription());

    }

    private void setStatus(){
        visitStatusItems = getResources().getStringArray(R.array.visit_status_items);
        paymentStatusItems = getResources().getStringArray(R.array.payment_status_items);

        binding.visitStatusText.setText(visitStatusItems[visit.getVisitStatus()]);
        binding.paymentStatusText.setText(paymentStatusItems[visit.getPaymentStatus()]);
        setStatusIcons();
    }

    private void setStatusIcons(){
        //if(visit.getVisitStatus())
    }

    private void setVisibility(boolean clicked){
        if(!clicked){
            addAnnotationButton.setVisibility(View.INVISIBLE);
            editVisitStatus.setVisibility(View.INVISIBLE);
        }
        else {
            addAnnotationButton.setVisibility(View.VISIBLE);
            editVisitStatus.setVisibility(View.VISIBLE);
        }
    }

    private void setAnimation(boolean clicked){
        if(!clicked){
            addAnnotationButton.startAnimation(hideButtonAnimation);
            editVisitStatus.startAnimation(hideButtonAnimation);
        }
        else {
            addAnnotationButton.startAnimation(showButtonAnimation);
            editVisitStatus.startAnimation(showButtonAnimation);
        }
    }


    private void openEditVisitDialog(){
        EditVisitDialog editVisitDialog = new EditVisitDialog();

        Bundle args = new Bundle(); // min max times
        args.putInt("visitStatus", visit.getVisitStatus());
        args.putInt("paymentStatus", visit.getPaymentStatus());
        editVisitDialog.setArguments(args);

        editVisitDialog.setTargetFragment(VisitDetailsFragment.this, 1);
        editVisitDialog.show(getFragmentManager(), "Wybierz pacjenta");
    }

    @Override
    public void onDialogSuccess(int newVisitStatus, int newPaymentStatus) {
        visit.setVisitStatus(newVisitStatus);
        visit.setPaymentStatus(newPaymentStatus);
        visitViewModel.update(visit);
    }
}