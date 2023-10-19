package com.example.gabinet_psychologiczny.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.gabinet_psychologiczny.Database.Relations.PatientWithVisits;
import com.example.gabinet_psychologiczny.Model.Patient;

import java.util.List;

@Dao
public interface PatientDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Patient patient);

    @Update
    void update(Patient patient);

    @Delete
    void delete(Patient patient);

    @Query("SELECT * FROM patient_table ORDER BY firstName")
    LiveData<List<Patient>> getAllPatients();

    @Transaction
    @Query("SELECT * FROM patient_table")
    LiveData<List<PatientWithVisits>> getAllPatientsWithVisits();

    @Transaction
    @Query("SELECT * FROM patient_table WHERE patient_id = :id")
    LiveData<PatientWithVisits> getPatientWithVisitsById(int id);

    @Query("SELECT * FROM patient_table WHERE firstName LIKE :searchQuery OR lastName LIKE :searchQuery")
    LiveData<List<Patient>> searchPatient(String searchQuery);
}
