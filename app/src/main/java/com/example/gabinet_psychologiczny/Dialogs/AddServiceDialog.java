package com.example.gabinet_psychologiczny.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gabinet_psychologiczny.Model.Service;
import com.example.gabinet_psychologiczny.R;
import com.example.gabinet_psychologiczny.ViewModel.ServiceViewModel;

public class AddServiceDialog extends AppCompatDialogFragment {

    private EditText editTextServiceName;
    private EditText editTextServicePrice;

    private ServiceViewModel serviceViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_service_dialog, null);

        serviceViewModel = new ViewModelProvider(getActivity()).get(ServiceViewModel.class);

        builder.setView(view)
                .setTitle("Dodaj usługę")
                .setNegativeButton("anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String serviceName = editTextServiceName.getText().toString();
                        double servicePrice = Double.valueOf(editTextServicePrice.getText().toString());

                        if(serviceName.trim().isEmpty() || servicePrice < 0) {
                            Toast.makeText(getActivity(), "Podano niepoprawne dane", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Service service = new Service(serviceName, servicePrice);
                            serviceViewModel.insert(service);
                            return;
                        }

                    }
                });

        editTextServiceName = view.findViewById(R.id.edit_text_service_name);
        editTextServicePrice = view.findViewById(R.id.edit_text_service_price);

        return builder.create();
    }



}
