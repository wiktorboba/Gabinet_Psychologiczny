package com.example.gabinet_psychologiczny.Database.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.gabinet_psychologiczny.Model.Patient;
import com.example.gabinet_psychologiczny.Model.Service;
import com.example.gabinet_psychologiczny.Model.Visit;

public class VisitWithPatientAndService {

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
}
