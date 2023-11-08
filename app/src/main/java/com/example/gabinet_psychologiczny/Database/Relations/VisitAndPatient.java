package com.example.gabinet_psychologiczny.Database.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;


import com.example.gabinet_psychologiczny.Model.Patient;
import com.example.gabinet_psychologiczny.Model.Visit;

public class VisitAndPatient {

    @Embedded
    public Visit visit;
    @Relation(
            parentColumn = "patient_id",
            entityColumn = "patient_id"
    )
    public Patient patient;
}
