package org.uberthought.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;
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

        Button speechInputButton = (Button)findViewById(R.id.speech_input);
        speechInputButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
            startActivityForResult(intent, 1234);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Toast.makeText(this, matches.get(0), Toast.LENGTH_LONG).show();

            TrackedItem.createOrIncrement(matches.get(0), getDatabaseHelper());
            getDatabaseHelper().dbChanged();
        }
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
