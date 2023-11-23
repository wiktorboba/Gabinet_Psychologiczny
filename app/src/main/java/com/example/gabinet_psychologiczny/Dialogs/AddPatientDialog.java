package com.example.gabinet_psychologiczny.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.gabinet_psychologiczny.R;

public class AddPatientDialog extends AppCompatDialogFragment {

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextAge;
    private EditText editTextPhoneNumber;
    private AddPatientDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_patient_dialog, null);

        builder.setView(view)
                .setTitle("Dodaj pacjenta")
                .setNegativeButton("anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String firstname = editTextFirstName.getText().toString();
                        String lastname = editTextLastName.getText().toString();
                        String age = editTextAge.getText().toString();
                        String phonenumber = editTextPhoneNumber.getText().toString();

                        if(firstname.trim().isEmpty() || lastname.trim().isEmpty() || age.isEmpty() || phonenumber.isEmpty()) {
                            Toast.makeText(getActivity(), "Podano niepoprawne dane", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else listener.onDialogSuccess(firstname, lastname, age, phonenumber);

                    }
                });

        editTextFirstName = view.findViewById(R.id.edit_text_firstname);
        editTextLastName = view.findViewById(R.id.edit_text_lastname);
        editTextAge = view.findViewById(R.id.edit_text_age);
        editTextPhoneNumber = view.findViewById(R.id.edit_text_phonenumber);



        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (AddPatientDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddPatientDialogListener");
        }
    }

    public interface AddPatientDialogListener{
        void onDialogSuccess(String firstname, String lastname, String age, String phonenumber);
    }
}
