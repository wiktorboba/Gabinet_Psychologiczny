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
import com.example.gabinet_psychologiczny.Model.Visit;

import java.util.List;

@Dao
public interface VisitDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Visit visit);

    @Update
    void update(Visit visit);

    @Delete
    void delete(Visit visit);

    @Query("SELECT * FROM visit_table ORDER BY visit_id")
    LiveData<List<Visit>> getAllVisits();

    @Transaction
    @Query("SELECT * FROM patient_table WHERE patient_id = :id")
    LiveData<PatientWithVisits> getPatientWithVisitsById(int id);

}
