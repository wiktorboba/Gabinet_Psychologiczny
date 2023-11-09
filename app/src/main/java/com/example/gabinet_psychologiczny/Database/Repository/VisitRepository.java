package com.example.gabinet_psychologiczny.Database.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.gabinet_psychologiczny.Database.Dao.VisitDao;
import com.example.gabinet_psychologiczny.Database.Database;
import com.example.gabinet_psychologiczny.Database.Relations.PatientWithVisits;
import com.example.gabinet_psychologiczny.Database.Relations.VisitAndPatient;
import com.example.gabinet_psychologiczny.Database.Relations.VisitWithPatientAndService;
import com.example.gabinet_psychologiczny.Model.Visit;

import java.time.LocalDate;
import java.util.List;

public class VisitRepository {

    private VisitDao visitDao;
    private LiveData<List<Visit>> allVisits;

    private LiveData<List<VisitWithPatientAndService>> allVisitsAndServices;

    public VisitRepository(Application application) {
        Database database = Database.getInstance(application);
        visitDao = database.visitDao();
        allVisits = visitDao.getAllVisits();
        allVisitsAndServices = visitDao.getAllVisitsAndServices();
    }

    public void insert(Visit visit) {
        new VisitRepository.InsertVisitAsyncTask(visitDao).execute(visit);
    }

    public void update(Visit visit) {
        new VisitRepository.UpdateVisitAsyncTask(visitDao).execute(visit);
    }

    public void delete(Visit visit) {
        new VisitRepository.DeleteVisitAsyncTask(visitDao).execute(visit);
    }

    public LiveData<List<Visit>> getAllVisits() {
        return allVisits;
    }

    public LiveData<List<VisitWithPatientAndService>> getAllVisitsAndServices() { return allVisitsAndServices; }

    public LiveData<PatientWithVisits> getPatientWithVisitsById(int id) { return visitDao.getPatientWithVisitsById(id); }

    public LiveData<VisitWithPatientAndService> getVisitAndServiceById(int id) { return visitDao.getVisitAndServiceById(id); }
    public LiveData<VisitAndPatient> getVisitAndPatientById(int id) { return visitDao.getVisitAndPatientById(id); }

    public LiveData<List<VisitWithPatientAndService>>getVisitAndServiceFromDayToDay(LocalDate fromDay, LocalDate toDay) { return visitDao.getVisitAndServiceFromDayToDay(fromDay, toDay); }

    private static class InsertVisitAsyncTask extends AsyncTask<Visit, Void, Void> {
        private VisitDao visitDao;

        private InsertVisitAsyncTask(VisitDao visitDao) {
            this.visitDao = visitDao;
        }

        @Override
        protected Void doInBackground(Visit... visits) {
            visitDao.insert(visits[0]);
            return null;
        }
    }

    private static class UpdateVisitAsyncTask extends AsyncTask<Visit, Void, Void> {
        private VisitDao visitDao;

        private UpdateVisitAsyncTask(VisitDao visitDao) {
            this.visitDao = visitDao;
        }

        @Override
        protected Void doInBackground(Visit... visits) {
            visitDao.update(visits[0]);
            return null;
        }
    }

    private static class DeleteVisitAsyncTask extends AsyncTask<Visit, Void, Void> {
        private VisitDao visitDao;

        private DeleteVisitAsyncTask(VisitDao visitDao) {
            this.visitDao = visitDao;
        }

        @Override
        protected Void doInBackground(Visit... visits) {
            visitDao.delete(visits[0]);
            return null;
        }
    }
}
