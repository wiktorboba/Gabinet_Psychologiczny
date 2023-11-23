package com.example.gabinet_psychologiczny.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.gabinet_psychologiczny.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalTime;

public class TimesPickerDialog extends DialogFragment {

    private TimesPickerDialog.TimesPickerDialogListener listener;
    private NumberPicker hourPicker;
    private NumberPicker minutesPicker;
    private int selectedStartHour = -1, selectedStartMinutes = -1, selectedEndHour = -1, selectedEndMinutes = -1;
    private int minHour, maxHour, minMinutes, maxMinutes, dayIndex, minFreeTime;

    public static TimesPickerDialog newInstance(int minStartHour, int maxEndHour, int minStartMinutes, int maxEndMinutes, int day, int minTime) {
        TimesPickerDialog frag = new TimesPickerDialog();
        Bundle args = new Bundle();
        args.putInt("minHour", minStartHour);
        args.putInt("maxHour", maxEndHour);
        args.putInt("minMinutes", minStartMinutes);
        args.putInt("maxMinutes", maxEndMinutes);
        args.putInt("dayIndex", day);
        args.putInt("minFreeTime", minTime);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            minHour = arguments.getInt("minHour");
            maxHour = arguments.getInt("maxHour");
            minMinutes = arguments.getInt("minMinutes");
            maxMinutes = arguments.getInt("maxMinutes");
            dayIndex = arguments.getInt("dayIndex");
            minFreeTime = arguments.getInt("minFreeTime");
        }
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_time_picker, null);
        builder.setView(view);
        hourPicker = view.findViewById(R.id.hourPicker);
        minutesPicker = view.findViewById(R.id.minutesPicker);

        hourPicker.setMinValue(minHour);
        hourPicker.setMaxValue(maxHour);

        NumberPicker.Formatter minutesFormatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                String valueInString = "";
                if(value == 0)
                    valueInString += "00";
                else{
                    int diff = value * 5;
                    valueInString += diff;
                }
                return valueInString;
            }
        };
        minutesPicker.setFormatter(minutesFormatter);
        minutesPicker.setMinValue(minMinutes/5);
        minutesPicker.setMaxValue(11);


        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker numberPicker, int previous, int current) {
                setUpTimes(current);
            }
        });

        Field f = null;
        try {
            f = NumberPicker.class.getDeclaredField("mInputText");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        f.setAccessible(true);
        EditText inputText = null;
        try {
            inputText = (EditText)f.get(minutesPicker);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        inputText.setFilters(new InputFilter[0]);

        RadioButton startTimeButton = view.findViewById(R.id.startTimeButton);
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!startTimeButton.isActivated()){
                    selectedEndHour = hourPicker.getValue();
                    selectedEndMinutes = minutesPicker.getValue();
                    if(selectedStartHour != -1 && selectedStartMinutes != -1){
                        setUpTimes(selectedStartHour);
                        hourPicker.setValue(selectedStartHour);
                        minutesPicker.setValue(selectedStartMinutes);
                    }
                }
            }
        });


        RadioButton endTimeButton = view.findViewById(R.id.endTimeButton);
        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!endTimeButton.isActivated()){
                    selectedStartHour = hourPicker.getValue();
                    selectedStartMinutes = minutesPicker.getValue();
                    if(selectedEndHour != -1 && selectedEndMinutes != -1){
                        setUpTimes(selectedEndHour);
                        hourPicker.setValue(selectedEndHour);
                        minutesPicker.setValue(selectedEndMinutes);
                    }
                }
            }
        });

        Button cancelButton = view.findViewById(R.id.cancelAddVisitButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        Button nextButton = view.findViewById(R.id.nextAddVisitButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startTimeButton.isChecked())
                    endTimeButton.performClick();
                else {
                    startTimeButton.performClick(); // to save endTimes in selectedEndHour and selectedEndMinutes

                    LocalTime start = LocalTime.of(selectedStartHour, selectedStartMinutes*5);
                    LocalTime end = LocalTime.of(selectedEndHour, selectedEndMinutes*5);
                    if(start.isAfter(end))
                        Toast.makeText(getActivity(), "Podano niepoprawny czas!", Toast.LENGTH_SHORT).show();
                    else{
                        long diff = Duration.between(start, end).toMinutes();
                        if(diff < minFreeTime)
                            Toast.makeText(getActivity(), "Podany czas trwania wizyty jest zbyt krótki!", Toast.LENGTH_SHORT).show();
                        else{
                            listener.onDialogSuccess(selectedStartHour, selectedStartMinutes*5, selectedEndHour, selectedEndMinutes*5, dayIndex);
                            dismiss();
                        }
                    }
                }
            }
        });


        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    private void setUpTimes(int hour){
        if(hour == minHour){
            minutesPicker.setMinValue(minMinutes/5);
            minutesPicker.setMaxValue(11);
        }
        else if(hour == maxHour){
            minutesPicker.setMinValue(0);
            minutesPicker.setMaxValue(maxMinutes/5);
        }
        else{
            minutesPicker.setMinValue(0);
            minutesPicker.setMaxValue(11);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (TimesPickerDialog.TimesPickerDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement TimesPickerDialogListener");
        }
    }

    public interface TimesPickerDialogListener{
        void onDialogSuccess(int startHour, int startMinutes, int endHour, int endMinutes, int dayIndex);

    }
}
