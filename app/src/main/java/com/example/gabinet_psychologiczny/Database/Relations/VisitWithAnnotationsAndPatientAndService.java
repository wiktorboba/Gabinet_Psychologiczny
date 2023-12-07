package com.example.gabinet_psychologiczny.Database.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.gabinet_psychologiczny.Model.Annotation;
import com.example.gabinet_psychologiczny.Model.Patient;
import com.example.gabinet_psychologiczny.Model.Service;
import com.example.gabinet_psychologiczny.Model.Visit;

import java.util.List;

public class VisitWithAnnotationsAndPatientAndService {

    @Embedded
    public Visit visit;
    @Relation(
            parentColumn = "service_id",
            entityColumn = "service_id"
    )
    public Service service;
    @Relation(
            parentColumn = "patient_id",
            entityColumn = "patient_id"
    )
    public Patient patient;

    @Relation(
            parentColumn = "visit_id",
            entityColumn = "visit_id"
    )
    public List<Annotation> annotations;

}
