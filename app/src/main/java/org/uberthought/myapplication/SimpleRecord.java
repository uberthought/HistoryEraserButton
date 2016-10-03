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
    @DatabaseField
    private String trackedItemUuid;

    public SimpleRecord() {
    }

    SimpleRecord(Date dateCreated, String note, String trackedItemUuid) {
        this.dateOffset = dateCreated.getTime();
        this.note = note;
        this.trackedItemUuid = trackedItemUuid;
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
