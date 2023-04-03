package com.example.binarybandits;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.binarybandits.ui.home.HomeFragment;
import com.example.binarybandits.ui.leaderboard.LeaderboardFragment;
import com.example.binarybandits.ui.maps.MapFragment;
import com.example.binarybandits.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

/**
 * MainActivity contains the 4 main fragments of the program: Leaderboard, Map, Home, and Profile. Allows for
 * navigation between fragments using a bottom navigation bar
 */
public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
    MapFragment mapFragment = new MapFragment();
    HomeFragment homeFragment = new HomeFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    /**
     * Create MainActivity
     * @param savedInstanceState the saved instance state that is restored after the app crashes
     */
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

        // If a player has just deleted a QR code, they come back to the Profile Page Fragment of the
        // MainActivity with their QRCode list updated
        boolean deleted_qr;
        try{
            Bundle extras_qr = getIntent().getExtras();
            deleted_qr = extras_qr.getBoolean("Deleted QR code");
        } catch(Exception ex){
            deleted_qr = false;
        }

        if (deleted_qr){
            Log.d("MainActivity", "QRCode deleted");
            bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
        }

        boolean go_to_map;
        try{
            Bundle extras_map = getIntent().getExtras();
            go_to_map = extras_map.getBoolean("Map Page");
        } catch(Exception ex){
            go_to_map = false;
        }

        if (go_to_map){
            Log.d("MainActivity", "Change to map page");
            bottomNavigationView.setSelectedItemId(R.id.navigation_maps);
            //getSupportFragmentManager().beginTransaction().replace(R.id.container, mapFragment).commit();
            Bundle extras_map = getIntent().getExtras();
            String qr_code_string = extras_map.getString("QRCode");

//            Bundle args = new Bundle();
//            args.putString("QRCode", qr_code_string);
//            mapFragment newFragment = new mapFragment();
//            newFragment.setArguments(args);

            Bundle bundle = new Bundle();
            bundle.putString("QRCode", qr_code_string);
            //mapFragment fragment = new mapFragment();
            mapFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, mapFragment).commit();
        }
    }

    /**
     * Change the current fragment to the fragment selected
     * @param item item selected from BottomNavigationView
     * @return Return True if the item selected was a valid fragment, false otherwise
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_leaderboard) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, leaderboardFragment).commit();
        } else if (itemId == R.id.navigation_maps) {
            Bundle bundle = new Bundle();
            bundle.putString("QRCode", "none");
            mapFragment.setArguments(bundle);
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