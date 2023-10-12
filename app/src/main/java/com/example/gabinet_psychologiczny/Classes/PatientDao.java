package com.example.gabinet_psychologiczny.Classes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gabinet_psychologiczny.Models.Patient;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface PatientDao {

    @Insert
    void insert(Patient patient);

    @Update
    void update(Patient patient);

    @Delete
    void delete(Patient patient);

    @Query("SELECT * FROM patient_table ORDER BY firstName")
    LiveData<List<Patient>> getAllPatients();
}
