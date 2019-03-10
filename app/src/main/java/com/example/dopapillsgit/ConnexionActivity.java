package com.example.dopapillsgit;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ConnexionActivity extends AppCompatActivity {
    public EditText loginEmailId, logInpasswd;
    Button btnLogIn;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        firebaseAuth = FirebaseAuth.getInstance();
        loginEmailId = findViewById(R.id.email);
        logInpasswd = findViewById(R.id.mdppatient);
        btnLogIn = findViewById(R.id.boutonseconnecter);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(ConnexionActivity.this, "User logged in ", Toast.LENGTH_SHORT).show();
                    Intent I = new Intent(ConnexionActivity.this, UserActivity.class);
                    startActivity(I);
                } else {
                    Toast.makeText(ConnexionActivity.this, "Login to continue", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = loginEmailId.getText().toString();
                String userPaswd = logInpasswd.getText().toString();
                if (userEmail.isEmpty()) {
                    loginEmailId.setError("Provide your Email first!");
                    loginEmailId.requestFocus();
                } else if (userPaswd.isEmpty()) {
                    logInpasswd.setError("Enter Password!");
                    logInpasswd.requestFocus();
                } else if (userEmail.isEmpty() && userPaswd.isEmpty()) {
                    Toast.makeText(ConnexionActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(userEmail.isEmpty() && userPaswd.isEmpty())) {
                    firebaseAuth.signInWithEmailAndPassword(userEmail, userPaswd).addOnCompleteListener(ConnexionActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(ConnexionActivity.this, "Not sucessfull", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(ConnexionActivity.this, UserActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(ConnexionActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
