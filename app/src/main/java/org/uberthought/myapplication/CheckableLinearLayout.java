package org.uberthought.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {

    boolean mIsCheckable;

    public CheckableLinearLayout(Context context) {
        super(context);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isChecked() {
        return getCheckBox().isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        getCheckBox().setChecked(checked);
    }

    @Override
    public void toggle() {
        getCheckBox().toggle();
    }

    CheckBox getCheckBox() {
        return (CheckBox) findViewById(R.id.checkBox);
    }

    public boolean isCheckable() {
        return mIsCheckable;
    }

    public void setCheckable(boolean checkable) {
        this.mIsCheckable = checkable;
        if (checkable)
            getCheckBox().setVisibility(View.VISIBLE);
        else
            getCheckBox().setVisibility(View.INVISIBLE);
    }

}
