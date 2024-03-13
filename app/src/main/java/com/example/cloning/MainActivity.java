package com.example.cloning;

import static com.example.cloning.R.string.app_name;

import com.example.cloning.Adapter.FragmentAdapter;
import com.example.cloning.R.id;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.cloning.databinding.ActivitySignInBinding;
import com.google.android.material.navigation.NavigationView;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import com.example.cloning.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActivityMainBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    ActivityMainBinding Binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(Binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        Binding.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        Binding.TabLayout.setupWithViewPager(Binding.viewPager);

        setUpToolbar();
        navigationView = (NavigationView) findViewById(id.navigation_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == id.menu_settings) {
                Intent intent5 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent5);

                } else if (itemId == id.menu_About) {


                } else if (itemId == id.menu_log_out) {
                    mAuth.signOut();
                    Intent intent = new Intent(MainActivity.this, SignIn.class);
                    startActivity(intent);
                }

                return false;
            }

        });
    }
    public void setUpToolbar() {
        drawerLayout = findViewById(id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, app_name, app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
}