package com.example.gabinet_psychologiczny.Database.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.gabinet_psychologiczny.Model.Patient;
import com.example.gabinet_psychologiczny.Model.Visit;

import java.util.List;

public class PatientWithVisits {
    @Embedded
    public Patient patient;
    @Relation(
            parentColumn = "patient_id",
            entityColumn = "patient_id"
    )
    public List<Visit> visitList;

}
