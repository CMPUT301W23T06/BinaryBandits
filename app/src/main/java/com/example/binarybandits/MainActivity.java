package com.example.binarybandits;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.binarybandits.ui.home.HomeFragment;
import com.example.binarybandits.ui.leaderboard.LeaderboardFragment;
import com.example.binarybandits.ui.maps.MapFragment;
import com.example.binarybandits.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
    MapFragment mapFragment = new MapFragment();
    HomeFragment homeFragment = new HomeFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.background_black));

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_leaderboard) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, leaderboardFragment).commit();
        } else if (itemId == R.id.navigation_maps) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, mapFragment).commit();
        } else if (itemId == R.id.navigation_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        } else if (itemId == R.id.navigation_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
        } else {
            return false;
        }
        return true;
    }
}