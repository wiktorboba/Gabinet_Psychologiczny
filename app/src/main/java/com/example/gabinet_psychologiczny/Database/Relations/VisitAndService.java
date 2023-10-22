package com.example.gabinet_psychologiczny.Database.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.gabinet_psychologiczny.Model.Service;
import com.example.gabinet_psychologiczny.Model.Visit;

import java.util.List;

public class VisitAndService {

    @Embedded
    public Visit visit;
    @Relation(
            parentColumn = "service_id",
            entityColumn = "service_id"
    )
    public Service service;
}
