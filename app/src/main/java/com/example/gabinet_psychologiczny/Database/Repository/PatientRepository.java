package com.example.gabinet_psychologiczny.Database.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.gabinet_psychologiczny.Database.Dao.PatientDao;
import com.example.gabinet_psychologiczny.Database.Database;
import com.example.gabinet_psychologiczny.Database.Relations.PatientWithVisits;
import com.example.gabinet_psychologiczny.Model.Patient;

import java.util.List;

public class PatientRepository {
    private PatientDao patientDao;
    private LiveData<List<Patient>> allPatients;

    private LiveData<List<PatientWithVisits>> allPatientsWithVisits;

    public PatientRepository(Application application) {
        Database database = Database.getInstance(application);
        patientDao = database.patientDao();
        allPatients = patientDao.getAllPatients();
        allPatientsWithVisits = patientDao.getAllPatientsWithVisits();
    }

    public void insert(Patient patient) {
        new InsertPatientAsyncTask(patientDao).execute(patient);
    }

    public void update(Patient patient) {
        new UpdatePatientAsyncTask(patientDao).execute(patient);
    }

    public void delete(Patient patient) {
        new DeletePatientAsyncTask(patientDao).execute(patient);
    }

    public LiveData<List<Patient>> getAllPatients() {
        return allPatients;
    }

    public LiveData<List<PatientWithVisits>> getAllPatientsWithVisits() {
        return allPatientsWithVisits;
    }

    public LiveData<PatientWithVisits> getPatientWithVisitsById(int id) { return patientDao.getPatientWithVisitsById(id); }

    public LiveData<List<Patient>> searchPatient(String searchQuery) { return patientDao.searchPatient(searchQuery);}

    private static class InsertPatientAsyncTask extends AsyncTask<Patient, Void, Void> {
        private PatientDao patientDao;

        private InsertPatientAsyncTask(PatientDao patientDao) {
            this.patientDao = patientDao;
        }

        @Override
        protected Void doInBackground(Patient... patients) {
            patientDao.insert(patients[0]);
            return null;
        }
    }

    private static class UpdatePatientAsyncTask extends AsyncTask<Patient, Void, Void> {
        private PatientDao patientDao;

        private UpdatePatientAsyncTask(PatientDao patientDao) {
            this.patientDao = patientDao;
        }

        @Override
        protected Void doInBackground(Patient... patients) {
            patientDao.update(patients[0]);
            return null;
        }
    }

    private static class DeletePatientAsyncTask extends AsyncTask<Patient, Void, Void> {
        private PatientDao patientDao;

        private DeletePatientAsyncTask(PatientDao patientDao) {
            this.patientDao = patientDao;
        }

        @Override
        protected Void doInBackground(Patient... patients) {
            patientDao.delete(patients[0]);
            return null;
        }
    }
}
