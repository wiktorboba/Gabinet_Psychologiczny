package com.example.gabinet_psychologiczny.Classes;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gabinet_psychologiczny.Model.Service;
import com.example.gabinet_psychologiczny.Model.Visit;
import com.example.gabinet_psychologiczny.R;
import com.example.gabinet_psychologiczny.ViewModel.ServiceViewModel;
import com.example.gabinet_psychologiczny.ViewModel.VisitViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddVisitDialog extends AppCompatDialogFragment {

    AutoCompleteTextView serviceTextView;
    AutoCompleteTextView patientTextView;
    AutoCompleteTextView dateTextView;
    ImageView addServiceImageView;

    ArrayList<String> serviceItems = new ArrayList<>();
    List<Service> serviceList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;


    Integer patientId = null;
    String patientFirstName;
    String patientLastName;

    int selectedServiceInArrayAdapter;
    String visitDay = "19/10/2023";
    String visitStartTime = "15:00";
    String visitEndTime = "16:30";


    private VisitViewModel visitViewModel;
    private ServiceViewModel serviceViewModel;

    public AddVisitDialog() {

    }
    public AddVisitDialog(Integer patientId, String patientFirstName, String patientLastName) {
        this.patientId = patientId;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_visit_dialog, null);

        visitViewModel = new ViewModelProvider(getActivity()).get(VisitViewModel.class);
        serviceViewModel = new ViewModelProvider(getActivity()).get(ServiceViewModel.class);


        builder.setView(view)
                .setTitle("Dodaj wizytę")
                .setNegativeButton("anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(patientId == null || visitDay.isEmpty() || visitStartTime.isEmpty() || visitEndTime.isEmpty()) {
                            Toast.makeText(getActivity(), "Podano niepoprawne dane", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Visit visit = new Visit(serviceList.get(selectedServiceInArrayAdapter).getId(), patientId, "opis wizyty",
                                    visitDay, visitStartTime, visitEndTime, false, false);

                            visitViewModel.insert(visit);
                            Toast.makeText(getActivity(), "Dodano nową wizytę", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

        serviceTextView = view.findViewById(R.id.autoCompleteTextViewServiceSelection);
        patientTextView = view.findViewById(R.id.autoCompleteTextViewPatientSelection);
        dateTextView = view.findViewById(R.id.autoCompleteTextViewDateSelection);

        serviceViewModel.getAllServices().observe(getActivity(), new Observer<List<Service>>() {
            @Override
            public void onChanged(List<Service> services) {
                serviceList = services;
                setServiceItems();
            }
        });

        addServiceImageView = view.findViewById(R.id.imageViewAddService);
        addServiceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddServiceDialog();
            }
        });


        serviceTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedServiceInArrayAdapter = i;
            }
        });


        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTextView.setText(visitDay + " " + visitStartTime + "-" + visitEndTime);
            }
        });

        if(patientId != null) {
            patientTextView.setText(patientFirstName + " " + patientLastName);
        }
        else {
            patientTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //wyswietl liste pacjentow
                }
            });
        }

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        setServiceItems();
    }

    private void setServiceItems(){

        serviceItems = new ArrayList<>();
        for(Service s : serviceList) {
            serviceItems.add(s.getName());
        }

        arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, serviceItems);
        serviceTextView.setAdapter(arrayAdapter);
    }

    private void openAddServiceDialog(){
        AddServiceDialog addServiceDialog = new AddServiceDialog();
        //addVisitDialog.setTargetFragment(PatientSearchFragment.this, 1);
        addServiceDialog.show(getChildFragmentManager(), "Dodaj usługę");
    }

}
