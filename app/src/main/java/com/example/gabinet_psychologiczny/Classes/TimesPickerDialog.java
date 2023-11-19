package com.example.gabinet_psychologiczny.Classes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.gabinet_psychologiczny.R;

public class TimesPickerDialog extends DialogFragment {

    private TimesPickerDialog.TimesPickerDialogListener listener;
    private NumberPicker hourPicker;
    private NumberPicker minutesPicker;
    private int selectedStartHour = -1, selectedStartMinutes = -1, selectedEndHour = -1, selectedEndMinutes = -1;
    private int minHour, maxHour, minMinutes, maxMinutes;

    public static TimesPickerDialog newInstance(int minStartHour, int maxEndHour, int minStartMinutes, int maxEndMinutes) {
        TimesPickerDialog frag = new TimesPickerDialog();
        Bundle args = new Bundle();
        args.putInt("minHour", minStartHour);
        args.putInt("maxHour", maxEndHour);
        args.putInt("minMinutes", minStartMinutes);
        args.putInt("maxMinutes", maxEndMinutes);
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
            maxMinutes= arguments.getInt("maxMinutes");
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
                int diff = value * 5;
                return "" + diff;
            }
        };
        minutesPicker.setFormatter(minutesFormatter);
        minutesPicker.setMinValue(minMinutes/5);
        minutesPicker.setMaxValue(11);


        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker numberPicker, int previous, int current) {
                if(current == minHour){
                    minutesPicker.setMinValue(minMinutes/5);
                }
                else if(current == maxHour){
                    minutesPicker.setMaxValue(maxMinutes/5);
                }
                else{
                    minutesPicker.setMinValue(0);
                    minutesPicker.setMaxValue(11);
                }
            }
        });

        RadioButton startTimeButton = view.findViewById(R.id.startTimeButton);
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!startTimeButton.isActivated()){
                    selectedEndHour = hourPicker.getValue();
                    selectedEndMinutes = minutesPicker.getValue();
                    if(selectedStartHour != -1 && selectedStartMinutes != -1){
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
                        hourPicker.setValue(selectedEndHour);
                        minutesPicker.setValue(selectedEndMinutes);
                    }
                }
            }
        });

        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        Button nextButton = view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startTimeButton.isChecked())
                    endTimeButton.performClick();
                else {
                    listener.onDialogSuccess(selectedStartHour, selectedStartMinutes*5, selectedEndHour, selectedEndMinutes*5);
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
            listener = (TimesPickerDialog.TimesPickerDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement TimesPickerDialogListener");
        }
    }

    public interface TimesPickerDialogListener{
        void onDialogSuccess(int startHour, int startMinutes, int endHour, int endMinutes);
    }
}
