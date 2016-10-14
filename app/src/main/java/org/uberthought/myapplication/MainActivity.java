package org.uberthought.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private MainDBHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TrackedItemListFragment trackedItemListFragment = TrackedItemListFragment.newInstance();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.FragmentView, trackedItemListFragment)
                .commit();

        trackedItemListFragment.setRowListener(id -> {

            SimpleRecordListFragment simpleRecordListFragment = SimpleRecordListFragment.newInstance();

            simpleRecordListFragment.Bind(id);

            getFragmentManager().beginTransaction()
                    .replace(R.id.FragmentView, simpleRecordListFragment)
                    .addToBackStack(null)
                    .commit();
        });

        getDatabaseHelper().addDBListener(this::onDatabaseChange);

        onDatabaseChange();
    }


    private void onDatabaseChange() {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(String.format(Locale.US, "%d", getDatabaseHelper().getCount(TrackedItem.class)));
    }

    private MainDBHelper getDatabaseHelper() {
        if (databaseHelper == null)
            databaseHelper = OpenHelperManager.getHelper(this, MainDBHelper.class);
        return databaseHelper;
    }
}
