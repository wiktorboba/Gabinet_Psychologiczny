package com.example.gabinet_psychologiczny.Database.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.gabinet_psychologiczny.Database.Dao.AnnotationDao;
import com.example.gabinet_psychologiczny.Database.Dao.ServiceDao;
import com.example.gabinet_psychologiczny.Database.Database;
import com.example.gabinet_psychologiczny.Database.Relations.ServiceWithVisits;
import com.example.gabinet_psychologiczny.Model.Annotation;
import com.example.gabinet_psychologiczny.Model.Service;

import java.util.List;

public class AnnotationRepository {
    private AnnotationDao annotationDao;
    private LiveData<List<Annotation>> allAnnotations;


    public AnnotationRepository(Application application) {
        Database database = Database.getInstance(application);
        annotationDao = database.annotationDao();
        allAnnotations = annotationDao.getAllAnnotations();
    }

    public void insert(Annotation annotation) {
        new AnnotationRepository.InsertAnnotationAsyncTask(annotationDao).execute(annotation);
    }

    public void update(Annotation annotation) {
        new AnnotationRepository.UpdateAnnotationAsyncTask(annotationDao).execute(annotation);
    }

    public void delete(Annotation annotation) {
        new AnnotationRepository.DeleteAnnotationAsyncTask(annotationDao).execute(annotation);
    }

    public LiveData<List<Annotation>> getAllAnnotations() {
        return allAnnotations;
    }


    private static class InsertAnnotationAsyncTask extends AsyncTask<Annotation, Void, Void> {
        private AnnotationDao annotationDao;

        private InsertAnnotationAsyncTask(AnnotationDao annotationDao) {
            this.annotationDao = annotationDao;
        }

        @Override
        protected Void doInBackground(Annotation... annotations) {
            annotationDao.insert(annotations[0]);
            return null;
        }
    }

    private static class UpdateAnnotationAsyncTask extends AsyncTask<Annotation, Void, Void> {
        private AnnotationDao annotationDao;

        private UpdateAnnotationAsyncTask(AnnotationDao annotationDao) {
            this.annotationDao = annotationDao;
        }

        @Override
        protected Void doInBackground(Annotation... annotations) {
            annotationDao.update(annotations[0]);
            return null;
        }
    }

    private static class DeleteAnnotationAsyncTask extends AsyncTask<Annotation, Void, Void> {
        private AnnotationDao annotationDao;

        private DeleteAnnotationAsyncTask(AnnotationDao annotationDao) {
            this.annotationDao = annotationDao;
        }

        @Override
        protected Void doInBackground(Annotation... annotations) {
            annotationDao.delete(annotations[0]);
            return null;
        }
    }
}
