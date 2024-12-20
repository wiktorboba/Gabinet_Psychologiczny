package com.example.gabinet_psychologiczny.Database;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;
import android.os.AsyncTask;

import com.example.gabinet_psychologiczny.Database.Converters.DateConverter;
import com.example.gabinet_psychologiczny.Database.Converters.DateTimeConverter;
import com.example.gabinet_psychologiczny.Database.Converters.TimeConverter;
import com.example.gabinet_psychologiczny.Database.Dao.AnnotationDao;
import com.example.gabinet_psychologiczny.Database.Dao.PatientDao;
import com.example.gabinet_psychologiczny.Database.Dao.ServiceDao;
import com.example.gabinet_psychologiczny.Database.Dao.VisitDao;
import com.example.gabinet_psychologiczny.Model.Annotation;
import com.example.gabinet_psychologiczny.Model.Patient;
import com.example.gabinet_psychologiczny.Model.Service;
import com.example.gabinet_psychologiczny.Model.Visit;

@androidx.room.Database(entities = {Patient.class, Visit.class, Service.class, Annotation.class}, version = 7)
@androidx.room.TypeConverters({DateConverter.class, TimeConverter.class, DateTimeConverter.class})
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract PatientDao patientDao();
    public abstract VisitDao visitDao();
    public abstract ServiceDao serviceDao();
    public abstract AnnotationDao annotationDao();

    public static synchronized Database getInstance(Context context) {

        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "Database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private PatientDao patientDao;
        private VisitDao visitDao;
        private ServiceDao serviceDao;
        private AnnotationDao annotationDao;

        private PopulateDbAsyncTask(Database db) {
            patientDao = db.patientDao();
            visitDao = db.visitDao();
            serviceDao = db.serviceDao();
            annotationDao = db.annotationDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Patient p = new Patient("Jan", "Kowalski", 27, "111222333");

            patientDao.insert(p);
            patientDao.insert(new Patient("Anna", "Nowak", 31, "444555666"));
            patientDao.insert(new Patient("Adam", "Mickiewicz", 19, "123123123"));

            Service service = new Service("Terapia", 50);
            serviceDao.insert(service);


            return null;
        }
    }
}
