package com.example.gabinet_psychologiczny.Classes;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.example.gabinet_psychologiczny.Models.Patient;

@androidx.room.Database(entities = {Patient.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract PatientDao patientDao();

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

        private PopulateDbAsyncTask(Database db) {
            patientDao = db.patientDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            patientDao.insert(new Patient("Jan", "Kowalski", 27, "111222333"));
            patientDao.insert(new Patient("Anna", "Nowak", 31, "444555666"));
            patientDao.insert(new Patient("Adam", "Mickiewicz", 19, "123123123"));


            return null;
        }
    }
}
