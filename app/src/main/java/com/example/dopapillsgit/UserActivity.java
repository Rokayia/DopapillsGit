package com.example.dopapillsgit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;

import com.example.dopapillsgitFragment.*;
import com.example.dopapillsgitFragment.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity {

    /********************************** Attributs de la classe*************************************/


    /**********************************Variables****************************************/
        Button btnSignOut;
        FirebaseAuth firebaseAuth;
        FirebaseUser user;



        private FirebaseAuth.AuthStateListener authStateListener;
        BottomNavigationView bottomNav;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_page_acceuil);

            /**********************************  de la classe*************************************/


            /**********************************Variables et Firebase****************************************/
            firebaseAuth = FirebaseAuth.getInstance();
            user = firebaseAuth.getCurrentUser();


            bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.setOnNavigationItemSelectedListener(navListener);

            //I added this if statement to keep the selected fragment when rotating the device
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AccueilFragment()).commit();
            }
        }

        private BottomNavigationView.OnNavigationItemSelectedListener navListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        switch (item.getItemId()) {
                            case R.id.acceuil:
                                selectedFragment = new AccueilFragment();
                                break;
                            case R.id.profil:
                                selectedFragment = new ProfilFragment();
                                break;
                            case R.id.evolution:
                                selectedFragment = new HomeFragment();
                                break;
                            case R.id.evaluation:
                                selectedFragment = new HomeFragment();
                                break;
                            case R.id.reseau:
                                selectedFragment = new HomeFragment();
                                break;

                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();

                        return true;
                    }
                };
    }
