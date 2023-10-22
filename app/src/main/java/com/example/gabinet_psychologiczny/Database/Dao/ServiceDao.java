package com.example.gabinet_psychologiczny.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.gabinet_psychologiczny.Database.Relations.ServiceWithVisits;
import com.example.gabinet_psychologiczny.Model.Service;

import java.util.List;

@Dao
public interface ServiceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Service service);

    @Update
    void update(Service service);

    @Delete
    void delete(Service service);

    @Query("SELECT * FROM service_table ORDER BY service_id")
    LiveData<List<Service>> getAllServices();

    @Transaction
    @Query("SELECT * FROM service_table")
    LiveData<List<ServiceWithVisits>> getAllServicesWithVisits();

    @Transaction
    @Query("SELECT * FROM service_table WHERE service_id = :id")
    LiveData<ServiceWithVisits> getServiceWithVisitsById(int id);

}
