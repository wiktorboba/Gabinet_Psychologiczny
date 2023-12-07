package com.example.gabinet_psychologiczny.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gabinet_psychologiczny.Database.Relations.PatientWithVisits;
import com.example.gabinet_psychologiczny.Database.Relations.VisitAndPatient;
import com.example.gabinet_psychologiczny.Database.Relations.VisitWithAnnotationsAndPatientAndService;
import com.example.gabinet_psychologiczny.Database.Repository.VisitRepository;
import com.example.gabinet_psychologiczny.Model.Visit;

import java.time.LocalDate;
import java.util.List;

public class VisitViewModel extends AndroidViewModel {

    private VisitRepository repository;
    private LiveData<List<Visit>> allVisits;

    private LiveData<List<VisitWithAnnotationsAndPatientAndService>> allVisitsAndServices;

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

    public LiveData<List<VisitWithAnnotationsAndPatientAndService>> getAllVisitsAndServices() { return allVisitsAndServices; }

    public LiveData<PatientWithVisits> getPatientWithVisitsById(int id) { return repository.getPatientWithVisitsById(id); }

    public LiveData<VisitWithAnnotationsAndPatientAndService> getVisitAndServiceById(int id) { return repository.getVisitAndServiceById(id); }
    public LiveData<VisitAndPatient> getVisitAndPatientById(int id) { return repository.getVisitAndPatientById(id); }
    public LiveData<List<VisitWithAnnotationsAndPatientAndService>> getVisitAndServiceFromDayToDay(LocalDate fromDay, LocalDate toDay) { return repository.getVisitAndServiceFromDayToDay(fromDay, toDay); }

}
