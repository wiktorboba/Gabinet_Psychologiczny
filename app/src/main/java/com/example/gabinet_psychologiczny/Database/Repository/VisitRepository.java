package com.example.gabinet_psychologiczny.Database.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.gabinet_psychologiczny.Database.Dao.VisitDao;
import com.example.gabinet_psychologiczny.Database.Database;
import com.example.gabinet_psychologiczny.Database.Relations.PatientWithVisits;
import com.example.gabinet_psychologiczny.Model.Visit;

import java.util.List;

public class VisitRepository {

    private VisitDao visitDao;
    private LiveData<List<Visit>> allVisits;

    public VisitRepository(Application application) {
        Database database = Database.getInstance(application);
        visitDao = database.visitDao();
        allVisits = visitDao.getAllVisits();
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

    public LiveData<PatientWithVisits> getPatientWithVisitsById(int id) { return visitDao.getPatientWithVisitsById(id); }

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
