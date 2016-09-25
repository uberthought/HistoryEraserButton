package org.uberthought.myapplication;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


@DatabaseTable(tableName = "mainDB")
class SimpleRecord {

    @DatabaseField(generatedId = true)
    private Long id;

    public Long getId() {
        return id;
    }

    Date getDateCreated() {
        return dateCreated;
    }

    public String getNote() {
        return note;
    }

    @DatabaseField
    private Date dateCreated;

    @DatabaseField
    private String note;

    public SimpleRecord() {

    }

    SimpleRecord(Date dateCreated, String note) {
        this.dateCreated = dateCreated;
        this.note = note;
    }
}
