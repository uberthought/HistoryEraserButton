package org.uberthought.myapplication;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@DatabaseTable(tableName = "trackedItemDB")
class TrackedItem {
    @DatabaseField(generatedId = true, columnName = "_id")
    private Long id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String uuid;

    @ForeignCollectionField
    private Collection<SimpleRecord> simpleRecords = new ArrayList<SimpleRecord>();

    TrackedItem() {
    }


    TrackedItem(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID().toString();
    }

    String getName() {
        return name;
    }

    UUID getUuidObject() {
        return UUID.fromString(uuid);
    }

    String getUuid() {
        return uuid;
    }

    public Collection<SimpleRecord> getSimpleRecords() {
        return simpleRecords;
    }

}
