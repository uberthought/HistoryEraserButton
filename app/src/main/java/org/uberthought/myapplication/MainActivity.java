package org.uberthought.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void buttonOnClick(View view) {
        Toast.makeText(MainActivity.this, "You Fool!", Toast.LENGTH_LONG).show();
    }

}
