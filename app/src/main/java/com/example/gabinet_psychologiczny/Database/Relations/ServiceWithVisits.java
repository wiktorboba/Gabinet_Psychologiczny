package com.example.gabinet_psychologiczny.Database.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.gabinet_psychologiczny.Model.Patient;
import com.example.gabinet_psychologiczny.Model.Service;
import com.example.gabinet_psychologiczny.Model.Visit;

import java.util.List;

public class ServiceWithVisits {
    @Embedded
    public Service service;
    @Relation(
            parentColumn = "service_id",
            entityColumn = "service_id"
    )
    public List<Visit> visitList;
}
