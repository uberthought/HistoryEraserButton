package org.uberthought.myapplication;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DatabaseTable(tableName = "trackedItemDB")
class TrackedItem {
    @DatabaseField(generatedId = true, columnName = "_id")
    private Long id;

    @DatabaseField
    private String name;

    @SuppressWarnings("CanBeFinal")
    @ForeignCollectionField
    private Collection<SimpleRecord> simpleRecords = new ArrayList<>();

    TrackedItem() {
    }

    static TrackedItem createOrIncrement(String name, MainDBHelper databaseHelper) {
        TrackedItem trackedItem = find(name, databaseHelper);

        if (trackedItem == null)
            trackedItem = create(name, databaseHelper);

        SimpleRecord.create(trackedItem, databaseHelper);

        return trackedItem;
    }

    private static TrackedItem find(String name, MainDBHelper databaseHelper) {
        try {
            Dao<TrackedItem, Long> trackedItemDao = databaseHelper.getDao(TrackedItem.class);
            List<TrackedItem> trackedItemList = trackedItemDao.queryForEq("name", name);
            if (!trackedItemList.isEmpty())
                return trackedItemList.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static TrackedItem create(String name, MainDBHelper databaseHelper) {
        TrackedItem trackedItem = databaseHelper.create(TrackedItem.class);
        trackedItem.setName(name);
        databaseHelper.update(trackedItem);
        return find(name, databaseHelper);
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
