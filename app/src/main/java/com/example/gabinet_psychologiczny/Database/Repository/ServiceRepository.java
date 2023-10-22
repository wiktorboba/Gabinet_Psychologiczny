package com.example.gabinet_psychologiczny.Database.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.gabinet_psychologiczny.Database.Dao.PatientDao;
import com.example.gabinet_psychologiczny.Database.Dao.ServiceDao;
import com.example.gabinet_psychologiczny.Database.Database;
import com.example.gabinet_psychologiczny.Database.Relations.PatientWithVisits;
import com.example.gabinet_psychologiczny.Database.Relations.ServiceWithVisits;
import com.example.gabinet_psychologiczny.Model.Patient;
import com.example.gabinet_psychologiczny.Model.Service;

import java.util.List;

public class ServiceRepository {

    private ServiceDao serviceDao;
    private LiveData<List<Service>> allServices;

    private LiveData<List<ServiceWithVisits>> allServicesWithVisits;

    public ServiceRepository(Application application) {
        Database database = Database.getInstance(application);
        serviceDao = database.serviceDao();
        allServices = serviceDao.getAllServices();
        allServicesWithVisits = serviceDao.getAllServicesWithVisits();
    }

    public void insert(Service service) {
        new ServiceRepository.InsertServiceAsyncTask(serviceDao).execute(service);
    }

    public void update(Service service) {
        new ServiceRepository.UpdateServiceAsyncTask(serviceDao).execute(service);
    }

    public void delete(Service service) {
        new ServiceRepository.DeleteServiceAsyncTask(serviceDao).execute(service);
    }

    public LiveData<List<Service>> getAllServices() {
        return allServices;
    }

    public LiveData<List<ServiceWithVisits>> getAllServicesWithVisits() {
        return allServicesWithVisits;
    }

    public LiveData<ServiceWithVisits> getServiceWithVisitsById(int id) { return serviceDao.getServiceWithVisitsById(id); }


    private static class InsertServiceAsyncTask extends AsyncTask<Service, Void, Void> {
        private ServiceDao serviceDao;

        private InsertServiceAsyncTask(ServiceDao serviceDao) {
            this.serviceDao = serviceDao;
        }

        @Override
        protected Void doInBackground(Service... services) {
            serviceDao.insert(services[0]);
            return null;
        }
    }

    private static class UpdateServiceAsyncTask extends AsyncTask<Service, Void, Void> {
        private ServiceDao serviceDao;

        private UpdateServiceAsyncTask(ServiceDao serviceDao) {
            this.serviceDao = serviceDao;
        }

        @Override
        protected Void doInBackground(Service... services) {
            serviceDao.update(services[0]);
            return null;
        }
    }

    private static class DeleteServiceAsyncTask extends AsyncTask<Service, Void, Void> {
        private ServiceDao serviceDao;

        private DeleteServiceAsyncTask(ServiceDao serviceDao) {
            this.serviceDao = serviceDao;
        }

        @Override
        protected Void doInBackground(Service... services) {
            serviceDao.delete(services[0]);
            return null;
        }
    }
}
