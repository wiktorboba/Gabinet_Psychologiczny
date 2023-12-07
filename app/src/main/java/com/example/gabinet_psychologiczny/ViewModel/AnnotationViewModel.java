package com.example.gabinet_psychologiczny.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gabinet_psychologiczny.Database.Relations.ServiceWithVisits;
import com.example.gabinet_psychologiczny.Database.Repository.AnnotationRepository;
import com.example.gabinet_psychologiczny.Database.Repository.ServiceRepository;
import com.example.gabinet_psychologiczny.Model.Annotation;
import com.example.gabinet_psychologiczny.Model.Service;

import java.util.List;

public class AnnotationViewModel extends AndroidViewModel {
    private AnnotationRepository repository;
    private LiveData<List<Annotation>> allAnnotations;

    public AnnotationViewModel(@NonNull Application application) {
        super(application);
        repository = new AnnotationRepository(application);
        allAnnotations = repository.getAllAnnotations();
    }

    public void insert(Annotation annotation){
        repository.insert(annotation);
    }

    public void update(Annotation annotation){
        repository.update(annotation);
    }

    public void delete(Annotation annotation){
        repository.delete(annotation);
    }

    public LiveData<List<Annotation>> getAllAnnotations() {
        return allAnnotations;
    }

}
