package org.uberthought.myapplication;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


@DatabaseTable(tableName = "mainDB")
class SimpleRecord {

    @DatabaseField(generatedId = true, columnName = "_id")
    private Long id;
    @DatabaseField
    private Long dateOffset;
    @DatabaseField
    private String note;

    public SimpleRecord() {

    }

    SimpleRecord(Date dateCreated, String note) {
        this.dateOffset = dateCreated.getTime();
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    Long getDateOffset() {
        return dateOffset;
    }

    Date getDate() {
        return new Date(dateOffset);
    }
}
