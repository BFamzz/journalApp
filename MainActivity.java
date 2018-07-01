package com.example.android.journalapp.activities;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.journalapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

     private static DatabaseReference mDatabase;
     private EditText firstName;
     private EditText lastName;
     private SignInButton signInButton;
     private GoogleSignInClient mGoogleSignInClient;
     private static final int RC_SIGN_IN = 9001;
     private static final String TAG = "signInActivity";
     private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstName = findViewById(R.id.register_first_name);
        lastName = findViewById(R.id.register_last_name);
        signInButton = findViewById(R.id.sign_in_button);

        // Google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, EntryListActivity.class));
        } else {
            signInButton.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        Log.d(TAG, "firebaseAuthWithGoogle " + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(TAG, "signInWithCredentialSuccess");

                            String userId;
                            // TODO ----> Get some details about the user here
                            startActivity(new Intent(getApplicationContext(), EntryListActivity.class));
                            final String fName = firstName.getText().toString().trim();
                            String lName = lastName.getText().toString().trim();
                            String email = mAuth.getCurrentUser().getEmail();

                            if (mAuth.getCurrentUser() == null) {
                                userId = mAuth.getCurrentUser().getUid();
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                            } else {
                                userId = mAuth.getCurrentUser().getUid();
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                            }

                            HashMap<String, String> userObject = new HashMap();
                            userObject.put("first_name", fName);
                            userObject.put("last_name", lName);
                            userObject.put("email", email);
                            mDatabase.setValue(userObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent entryListIntent = new Intent(getApplicationContext(), EntryyActivity.class);
                                        entryListIntent.putExtra("firstname", fName);
                                        startActivity(entryListIntent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(),"User not created", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {
                            // Sign in fails
                            Log.d(TAG, "signInWithCredentials:failed");
                            Toast.makeText(getApplicationContext(), "Sign in failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
