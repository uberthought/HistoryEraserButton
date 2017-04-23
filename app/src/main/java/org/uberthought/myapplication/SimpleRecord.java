package org.uberthought.myapplication;

import java.util.Date;

class SimpleRecord {

    private Date date;
    private boolean checked = false;


    SimpleRecord(Date date) {
        this.date = date;
    }

    Date getDate() {
        return date;
    }

    boolean isChecked() {
        return checked;
    }

    void setChecked(boolean checked) {
        this.checked = checked;
    }

}
