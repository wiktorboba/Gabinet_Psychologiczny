package com.example.gabinet_psychologiczny.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gabinet_psychologiczny.Other.CalendarUtils;
import com.example.gabinet_psychologiczny.Fragments.CalendarFragment;
import com.example.gabinet_psychologiczny.Fragments.PatientDetailsFragment;
import com.example.gabinet_psychologiczny.Model.Service;
import com.example.gabinet_psychologiczny.Model.Visit;
import com.example.gabinet_psychologiczny.R;
import com.example.gabinet_psychologiczny.ViewModel.ServiceViewModel;
import com.example.gabinet_psychologiczny.ViewModel.VisitViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AddVisitDialog extends DialogFragment implements TimesPickerDialog.TimesPickerDialogListener, SelectPatientDialog.SelectPatientDialogListener {

    private AddVisitDialog.AddVisitDialogListener listener;
    AutoCompleteTextView serviceTextView;
    AutoCompleteTextView patientTextView;
    AutoCompleteTextView dateTextView;

    TextView descriptionTextView;

    ArrayList<String> serviceItems = new ArrayList<>();
    List<Service> serviceList = new ArrayList<>();

    ArrayAdapter<String> arrayAdapter;


    int patientId;
    String patientFirstName;
    String patientLastName;

    int selectedServiceInArrayAdapter;
    int year, month, day;
    LocalTime visitStartTime = null;
    LocalTime visitEndTime = null;
    LocalDate visitDay = null;

    int startHour;
    int startMinute;
    int endHour;
    int endMinute;


    private VisitViewModel visitViewModel;
    private ServiceViewModel serviceViewModel;

    public static AddVisitDialog newInstance(int patientId, String patientFirstName, String patientLastName,
                                             int year, int month, int day, int startHour, int startMinute, int endHour, int endMinute) {
        AddVisitDialog frag = new AddVisitDialog();
        Bundle args = new Bundle();
        args.putInt("patientId", patientId);
        args.putString("patientFirstName", patientFirstName);
        args.putString("patientLastName", patientLastName);

        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);

        args.putInt("startHour", startHour);
        args.putInt("startMinute", startMinute);
        args.putInt("endHour", endHour);
        args.putInt("endMinute", endMinute);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            patientId = args.getInt("patientId", -1);
            patientFirstName = args.getString("patientFirstName", "");
            patientLastName = args.getString("patientLastName", "");

            year = args.getInt("year", -1);
            month = args.getInt("month", -1);
            day = args.getInt("day", -1);
            if(year != -1 && month != -1 && day != -1)
                visitDay = LocalDate.of(year, month, day);

            startHour = args.getInt("startHour", -1);
            startMinute = args.getInt("startMinute", -1);
            endHour = args.getInt("endHour", -1);
            endMinute = args.getInt("endMinute", -1);
        }
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_visit_dialog, null);
        builder.setView(view);

        visitViewModel = new ViewModelProvider(getActivity()).get(VisitViewModel.class);
        serviceViewModel = new ViewModelProvider(getActivity()).get(ServiceViewModel.class);

        serviceTextView = view.findViewById(R.id.autoCompleteTextViewServiceSelection);
        patientTextView = view.findViewById(R.id.autoCompleteTextViewPatientSelection);
        dateTextView = view.findViewById(R.id.autoCompleteTextViewDateSelection);
        descriptionTextView = view.findViewById(R.id.visitDescription);

        FloatingActionButton buttonAddService = view.findViewById(R.id.button_add_service);
        buttonAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAddServiceDialog();
                //openTimesPickerDialog();
            }
        });

        Button cancelButton = view.findViewById(R.id.cancelAddVisitButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button nextButton = view.findViewById(R.id.nextAddVisitButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(patientId == -1 || visitDay == null || visitStartTime == null || visitEndTime == null) {
                    Toast.makeText(getActivity(), "Podano niepoprawne dane", Toast.LENGTH_SHORT).show();
                }
                else{
                    Visit visit = new Visit(serviceList.get(selectedServiceInArrayAdapter).getId(), patientId, descriptionTextView.getText().toString(),
                            visitDay, visitStartTime, visitEndTime, false, false);
                    visitViewModel.insert(visit);
                    Toast.makeText(getActivity(), "Dodano nową wizytę", Toast.LENGTH_SHORT).show();
                    listener.onDialogSuccess();
                    dismiss();
                }
            }
        });


        serviceViewModel.getAllServices().observe(getActivity(), new Observer<List<Service>>() {
            @Override
            public void onChanged(List<Service> services) {
                serviceList = services;
                setServiceItems();
            }
        });



        serviceTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedServiceInArrayAdapter = i;
            }
        });


        if(getTargetFragment().getClass().equals(PatientDetailsFragment.class)){
            dateTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    openCalendar();

                }

            });
        }

        if(startHour != -1 && startMinute != -1 && endHour != -1 && endMinute != -1){
            visitStartTime = LocalTime.of(startHour, startMinute);
            visitEndTime = LocalTime.of(endHour, endMinute);
            dateTextView.setText(CalendarUtils.formattedDate(visitDay) + " " + CalendarUtils.formattedTime(visitStartTime) + " - " + CalendarUtils.formattedTime(visitEndTime));
        }

        if(patientId != -1) {
            patientTextView.setText(patientFirstName + " " + patientLastName);
        }
        else {
            patientTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openSelectPatientDialog();
                }
            });
        }

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
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

    private void openCalendar(){
        //Fragment calendar = new CalendarFragment();
        //replaceFragment(calendar);
        CalendarFragment calendarFragment = new CalendarFragment();
        //
        //
        //
        // TODO
        //
        //      1. Otwórz fragment CalendarFragment
        //      2. W kalendarzu po wyborze czasu nowej wizyty, zwróć wybrane czasy z powrotem do AddVisitDialog zamiast tworzyć go od nowa.
        //      3. Uzupełnij odpowiednie TextView otrzymanymi wartosciami
        //
        //
        //
        //
        //
    }

    private void openSelectPatientDialog(){
        SelectPatientDialog selectPatientDialog = new SelectPatientDialog();

        selectPatientDialog.setTargetFragment(AddVisitDialog.this, 1);
        selectPatientDialog.show(getFragmentManager(), "Wybierz pacjenta");
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout2, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openTimesPickerDialog(){
        TimesPickerDialog timesPickerDialog = new TimesPickerDialog();
        Bundle args = new Bundle(); // min max times
        args.putInt("minHour", 8);
        args.putInt("maxHour", 13);
        args.putInt("minMinutes", 25);
        args.putInt("maxMinutes", 50);
        timesPickerDialog.setArguments(args);

        timesPickerDialog.setTargetFragment(AddVisitDialog.this, 1);
        timesPickerDialog.show(getFragmentManager(), "Czas trwania uslugi");
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (AddVisitDialog.AddVisitDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddVisitDialogListener");
        }
    }

    @Override // TimesPickerDialog
    public void onDialogSuccess(int startHour, int startMinutes, int endHour, int endMinutes, int dayIndex) {
        visitStartTime = LocalTime.of(startHour, startMinutes);
        visitEndTime = LocalTime.of(endHour, endMinutes);
    }

    @Override // SelectPatientDialog
    public void onDialogSuccess(int id, String name, String lName) {
        patientId = id;
        patientTextView.setText(name + " " + lName);
    }


    public interface AddVisitDialogListener{
        void onDialogSuccess();
    }
}
