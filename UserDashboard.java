package com.example.fypapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.fypapplication.Post.activity.PostListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UserDashboard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        return true;
                    case R.id.nav_guard:
                        startActivity(new Intent(getApplicationContext(), GuardActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_useraccount:
                        startActivity(new Intent(getApplicationContext(), UserAccountActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_post:
                        startActivity(new Intent(getApplicationContext(), PostListActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}