package org.uberthought.myapplication;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


@DatabaseTable(tableName = "simpleRecordDB")
class SimpleRecord {

    @DatabaseField(generatedId = true, columnName = "_id")
    private Long id;
    @DatabaseField
    private Long dateOffset;
    @DatabaseField
    private String note;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private TrackedItem trackedItem = new TrackedItem();

    public SimpleRecord() {
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

    void setDate(Date date) {
        dateOffset = date.getTime();
    }
}
