package com.example.gabinet_psychologiczny.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gabinet_psychologiczny.Database.Relations.PatientWithVisits;
import com.example.gabinet_psychologiczny.Database.Repository.PatientRepository;
import com.example.gabinet_psychologiczny.Model.Patient;

import java.util.List;

public class PatientViewModel extends AndroidViewModel {

    private PatientRepository repository;
    private LiveData<List<Patient>> allPatients;
    private LiveData<List<PatientWithVisits>> allPatientsWithVisits;

    public PatientViewModel(@NonNull Application application) {
        super(application);
        repository = new PatientRepository(application);
        allPatients = repository.getAllPatients();
        allPatientsWithVisits = repository.getAllPatientsWithVisits();
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

    public LiveData<List<Patient>> getAllPatients() {
        return allPatients;
    }

    public LiveData<List<PatientWithVisits>> getAllPatientsWithVisits() {
        return allPatientsWithVisits;
    }

    public LiveData<PatientWithVisits> getPatientWithVisitsById(int id) { return repository.getPatientWithVisitsById(id); }

    public LiveData<List<Patient>> searchPatient(String searchQuery) { return repository.searchPatient(searchQuery); }
}
