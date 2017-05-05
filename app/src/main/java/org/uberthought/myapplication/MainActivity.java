package org.uberthought.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "MainActivity";
    GoogleApiClient mGoogleApiClient;
    Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signOutButton = (Button) findViewById(R.id.sign_out);
        signOutButton.setOnClickListener(this);

        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user == null)
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.FragmentView, LoginFragment.newInstance(mGoogleApiClient))
                        .commit();

            else
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.FragmentView, TrackedItemFragment.newInstance())
                        .commit();
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_out) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            FirebaseAuth.getInstance().signOut();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
