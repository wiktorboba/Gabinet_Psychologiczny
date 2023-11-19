package com.example.gabinet_psychologiczny.Classes;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gabinet_psychologiczny.Fragments.PatientSearchFragment;
import com.example.gabinet_psychologiczny.Model.Service;
import com.example.gabinet_psychologiczny.Model.Visit;
import com.example.gabinet_psychologiczny.R;
import com.example.gabinet_psychologiczny.ViewModel.ServiceViewModel;
import com.example.gabinet_psychologiczny.ViewModel.VisitViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddVisitDialog extends AppCompatDialogFragment implements TimesPickerDialog.TimesPickerDialogListener {

    AutoCompleteTextView serviceTextView;
    AutoCompleteTextView patientTextView;
    AutoCompleteTextView dateTextView;
    TextView descriptionTextView;

    ArrayList<String> serviceItems = new ArrayList<>();
    List<Service> serviceList = new ArrayList<>();

    ArrayAdapter<String> arrayAdapter;


    Integer patientId = null;
    String patientFirstName;
    String patientLastName;

    int selectedServiceInArrayAdapter;
    LocalDate visitDay = null;
    LocalTime visitStartTime = null;
    LocalTime visitEndTime = null;


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

                        if(patientId == null || visitDay == null || visitStartTime == null || visitEndTime == null) {
                            Toast.makeText(getActivity(), "Podano niepoprawne dane", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Visit visit = new Visit(serviceList.get(selectedServiceInArrayAdapter).getId(), patientId, descriptionTextView.getText().toString(),
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
        descriptionTextView = view.findViewById(R.id.visitDescription);

        FloatingActionButton buttonAddService = view.findViewById(R.id.button_add_service);
        buttonAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAddServiceDialog();
                //openTimesPickerDialog();
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


        dateTextView.setOnClickListener(new View.OnClickListener() {

            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View view) {
                DatePickerDialog dayDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        c.set(year, month, day);
                        visitDay = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                        TimePickerDialog startTimeDialog = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {

                                visitStartTime = LocalTime.of(hours, minutes);

                                TimePickerDialog endTimeDialog = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {

                                        visitEndTime = LocalTime.of(hours, minutes);
                                        dateTextView.setText(CalendarUtils.formattedDate(visitDay) + " " +
                                                CalendarUtils.formattedTime(visitStartTime) + "-" + CalendarUtils.formattedTime(visitEndTime));
                                    }
                                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);

                                endTimeDialog.setTitle("Godzina zakończenia wizyty");
                                endTimeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                endTimeDialog.show();
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);

                        startTimeDialog.setTitle("Godzina rozpoczęcia wizyty");
                        startTimeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        startTimeDialog.show();
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

                dayDialog.setTitle("Dzień wizyty");
                dayDialog.show();


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

        Dialog dialog = builder.create();
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
    public void onDialogSuccess(int startHour, int startMinutes, int endHour, int endMinutes) {
        visitStartTime = LocalTime.of(startHour, startMinutes);
        visitEndTime = LocalTime.of(endHour, endMinutes);
    }
}
