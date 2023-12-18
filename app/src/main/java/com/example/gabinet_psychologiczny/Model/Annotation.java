package com.example.gabinet_psychologiczny.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "annotation_table", foreignKeys = {

        @ForeignKey(
                entity = Visit.class,
                parentColumns = "visit_id",
                childColumns = "visit_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
})
public class Annotation {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "annotation_id")
    private int id;

    @ColumnInfo(name = "visit_id", index = true)
    private int visitId;
    private String name;
    private int type;
    private String uri;
    private LocalDateTime time;

    public Annotation(int visitId, String name, int type, String uri, LocalDateTime time) {
        this.visitId = visitId;
        this.name = name;
        this.type = type;
        this.uri = uri;
        this.time = time;
    }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setVisitId(int id) { this.visitId = visitId; }
    public void setName(String name) { this.name = name; }
    public void setType(int type) { this.type = type; }
    public void setUri(String uri) { this.uri = uri; }
    public void setTime(LocalDateTime time) { this.time = time; }


    // Getters
    public int getId() { return id; }
    public int getVisitId() { return visitId; }
    public String getName() { return name; }
    public int getType() { return type; }
    public String getUri() { return uri; }
    public LocalDateTime getTime() { return time; }

}
