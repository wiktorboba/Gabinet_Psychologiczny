package com.example.gabinet_psychologiczny.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gabinet_psychologiczny.Database.Relations.PatientWithVisits;
import com.example.gabinet_psychologiczny.Database.Relations.VisitAndService;
import com.example.gabinet_psychologiczny.Database.Repository.VisitRepository;
import com.example.gabinet_psychologiczny.Model.Visit;

import java.util.List;

public class VisitViewModel extends AndroidViewModel {

    private VisitRepository repository;
    private LiveData<List<Visit>> allVisits;

    private LiveData<List<VisitAndService>> allVisitsAndServices;

    public VisitViewModel(@NonNull Application application) {
        super(application);
        repository = new VisitRepository(application);
        allVisits = repository.getAllVisits();
        allVisitsAndServices = repository.getAllVisitsAndServices();
    }

    public void insert(Visit visit){
        repository.insert(visit);
    }

    public void update(Visit visit){
        repository.update(visit);
    }

    public void delete(Visit visit){
        repository.delete(visit);
    }

    public LiveData<List<Visit>> getAlLVisits() {
        return allVisits;
    }

    public LiveData<List<VisitAndService>> getAllVisitsAndServices() { return allVisitsAndServices; }

    public LiveData<PatientWithVisits> getPatientWithVisitsById(int id) { return repository.getPatientWithVisitsById(id); }

    public LiveData<VisitAndService> getVisitAndServiceById(int id) { return repository.getVisitAndServiceById(id); }

}
