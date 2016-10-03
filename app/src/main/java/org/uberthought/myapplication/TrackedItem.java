package org.uberthought.myapplication;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "trackedItemDB")
class TrackedItem {
    @DatabaseField(generatedId = true, columnName = "_id")
    private Long id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String uuid;

    TrackedItem() {
    }


    TrackedItem(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID().toString();
    }

    String getName() {
        return name;
    }

    UUID getUuid() {
        return UUID.fromString(uuid);
    }
}
