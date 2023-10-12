package com.example.gabinet_psychologiczny.Classes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gabinet_psychologiczny.Models.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientViewModel extends AndroidViewModel {

    private PatientRepository repository;
    private LiveData<List<Patient>> alLPatients;

    public PatientViewModel(@NonNull Application application) {
        super(application);
        repository = new PatientRepository(application);
        alLPatients = repository.getAllPatients();
    }

    public void insert(Patient patient){
        repository.insert(patient);
    }

    public void update(Patient patient){
        repository.update(patient);
    }

    public void delete(Patient patient){
        repository.delete(patient);
    }

    public LiveData<List<Patient>> getAlLPatients() {
        return alLPatients;
    }
}
