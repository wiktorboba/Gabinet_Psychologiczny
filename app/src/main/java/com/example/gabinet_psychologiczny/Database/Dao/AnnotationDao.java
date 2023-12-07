package com.example.gabinet_psychologiczny.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gabinet_psychologiczny.Model.Annotation;

import java.util.List;

@Dao
public interface AnnotationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Annotation annotation);

    @Update
    void update(Annotation annotation);

    @Delete
    void delete(Annotation annotation);

    @Query("SELECT * FROM annotation_table ORDER BY annotation_id")
    LiveData<List<Annotation>> getAllAnnotations();


}
