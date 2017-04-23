package org.uberthought.myapplication;

class TrackedItem {
    private String name;
    private long count;
    private boolean checked = false;

    TrackedItem() {
    }

    TrackedItem(String name, long count) {
        this.name = name;
        this.count = count;
    }

    String getName() {
        return name;
    }

    long getCount() {
        return count;
    }

    boolean isChecked() {
        return checked;
    }

    void setChecked(boolean checked) {
        this.checked = checked;
    }
}
