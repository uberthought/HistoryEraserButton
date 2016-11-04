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

    static SimpleRecord create(TrackedItem trackedItem, MainDBHelper databaseHelper) {
        Date currDateTime = new Date(System.currentTimeMillis());

        // create the new record
        SimpleRecord simpleRecord = databaseHelper.create(SimpleRecord.class);
        simpleRecord.setDate(currDateTime);
        databaseHelper.update(simpleRecord);

        // add the record to the tracked item
        trackedItem.getSimpleRecords().add(simpleRecord);
        databaseHelper.update(trackedItem);

        return simpleRecord;
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
