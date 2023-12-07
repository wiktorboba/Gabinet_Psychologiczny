package com.example.gabinet_psychologiczny.Database.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.gabinet_psychologiczny.Model.Annotation;
import com.example.gabinet_psychologiczny.Model.Patient;
import com.example.gabinet_psychologiczny.Model.Visit;

import java.util.List;

public class VisitWithAnnotations {
    @Embedded
    public Visit visit;
    @Relation(
            entity = Annotation.class,
            parentColumn = "visit_id",
            entityColumn = "visit_id"
    )
    public List<Annotation> annotationList;
}
