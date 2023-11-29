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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gabinet_psychologiczny.Fragments.PatientDetailsFragment;
import com.example.gabinet_psychologiczny.Model.Service;
import com.example.gabinet_psychologiczny.Model.Visit;
import com.example.gabinet_psychologiczny.Other.CalendarUtils;
import com.example.gabinet_psychologiczny.R;
import com.example.gabinet_psychologiczny.ViewModel.ServiceViewModel;
import com.example.gabinet_psychologiczny.ViewModel.VisitViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EditVisitDialog extends DialogFragment {

    private EditVisitDialog.EditVisitDialogListener listener;
    private int visitStatus;
    private int paymentStatus;
    AutoCompleteTextView visitStatusTextView;
    AutoCompleteTextView paymentStatusTextView;
    String[] visitStatusItems;
    String[] paymentStatusItems;
    ArrayAdapter<String> visitStatusArrayAdapter;
    ArrayAdapter<String> paymentStatusArrayAdapter;



    public static EditVisitDialog newInstance(int visitStatus, int paymentStatus) {
        EditVisitDialog frag = new EditVisitDialog();
        Bundle args = new Bundle();
        args.putInt("visitStatus", visitStatus);
        args.putInt("paymentStatus", paymentStatus);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            visitStatus = args.getInt("visitStatus", -1);
            paymentStatus = args.getInt("paymentStatus", -1);
        }
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_visit_fragment, null);
        builder.setView(view);



        visitStatusTextView = view.findViewById(R.id.autoCompleteTextViewTakePlaceSelection);
        paymentStatusTextView = view.findViewById(R.id.autoCompleteTextViewPaymentSelection);

        setPaymentStatusItems();
        setVisitStatusItems();

        visitStatusTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                visitStatus = i;
            }
        });

        paymentStatusTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                paymentStatus = i;
            }
        });

        Button cancelButton = view.findViewById(R.id.cancelEditVisitButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button nextButton = view.findViewById(R.id.saveEditVisitButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDialogSuccess(visitStatus, paymentStatus);
                Toast.makeText(getActivity(), "Zmieniono status pomyślnie.", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        visitStatusTextView.setText(visitStatusTextView.getAdapter().getItem(visitStatus).toString(), false);
        paymentStatusTextView.setText(paymentStatusTextView.getAdapter().getItem(paymentStatus).toString(), false);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }


    private void setVisitStatusItems(){
        visitStatusItems = getResources().getStringArray(R.array.visit_status_items);

        visitStatusArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, visitStatusItems);
        visitStatusTextView.setAdapter(visitStatusArrayAdapter);
    }

    private void setPaymentStatusItems(){
        paymentStatusItems = getResources().getStringArray(R.array.payment_status_items);

        paymentStatusArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, paymentStatusItems);
        paymentStatusTextView.setAdapter(paymentStatusArrayAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (EditVisitDialog.EditVisitDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement EditVisitDialogListener");
        }
    }
    public interface EditVisitDialogListener{
        void onDialogSuccess(int newVisitStatus, int newPaymentStatus);
    }
}
