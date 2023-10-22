package com.example.gabinet_psychologiczny.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gabinet_psychologiczny.Database.Relations.PatientWithVisits;
import com.example.gabinet_psychologiczny.Database.Relations.ServiceWithVisits;
import com.example.gabinet_psychologiczny.Database.Repository.PatientRepository;
import com.example.gabinet_psychologiczny.Database.Repository.ServiceRepository;
import com.example.gabinet_psychologiczny.Model.Patient;
import com.example.gabinet_psychologiczny.Model.Service;

import java.util.List;

public class ServiceViewModel extends AndroidViewModel {

    private ServiceRepository repository;
    private LiveData<List<Service>> allServices;
    private LiveData<List<ServiceWithVisits>> allServicesWithVisits;

    public ServiceViewModel(@NonNull Application application) {
        super(application);
        repository = new ServiceRepository(application);
        allServices = repository.getAllServices();
        allServicesWithVisits = repository.getAllServicesWithVisits();
    }

    public void insert(Service service){
        repository.insert(service);
    }

    public void update(Service service){
        repository.update(service);
    }

    public void delete(Service service){
        repository.delete(service);
    }

    public LiveData<List<Service>> getAllServices() {
        return allServices;
    }

    public LiveData<List<ServiceWithVisits>> getAllServicesWithVisits() {
        return allServicesWithVisits;
    }

    public LiveData<ServiceWithVisits> getServiceWithVisitsById(int id) { return repository.getServiceWithVisitsById(id); }

}
