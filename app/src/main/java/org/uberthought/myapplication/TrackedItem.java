package org.uberthought.myapplication;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;

@DatabaseTable(tableName = "trackedItemDB")
class TrackedItem {
    @DatabaseField(generatedId = true, columnName = "_id")
    private Long id;

    @DatabaseField
    private String name;

    @ForeignCollectionField
    private Collection<SimpleRecord> simpleRecords = new ArrayList<>();

    TrackedItem() {
    }


    String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Collection<SimpleRecord> getSimpleRecords() {
        return simpleRecords;
    }

    public Long getId() {
        return id;
    }
}
