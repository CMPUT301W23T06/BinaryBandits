package com.example.binarybandits;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.Fragment;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.binarybandits.qrcode.QRCodeInfoActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for MainActivity. All the UI tests are written here. Robotium test framework is used
 */
public class MainActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Gets the Activity
     * @throws Exception
     */

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }
//
//    /**
//     * Add a city to the listview and check the city name using assertTrue
//     * Clear all the cities from the listview and check again with assertFalse
//     */
//    @Test
//    public void checkBottomNavBar(){
//        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//
//        Activity current = solo.getCurrentActivity();
//        Fragment mainFragment = current.getFragmentManager().findFragmentById(R.id.main_fragment);
////        Fragment mainFragment = current.getFragmentManager().findFragmentById(R.id.main_fragment);
//        BottomNavigationView navBar = (BottomNavigationView) solo.getView(R.id.nav_view);
//
//
//    }

    /**
     * Checks functionality of scan button
     */
    @Test
    public void testScanner() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertTrue(solo.waitForText("scanning", 1, 2000));
        solo.clickOnView(solo.getView(R.id.scan_button));
        solo.assertCurrentActivity("Should be second activity", ScanQRActivity.class);
    }


    /**
     * Check if profile page is displayed
     */
    @Test
    public void checkProfilePage(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        BottomNavigationView navBar = (BottomNavigationView) solo.getView(R.id.nav_view);
        solo.clickOnView(navBar.findViewById(R.id.navigation_profile));
        assertTrue(solo.waitForText("Profile", 1, 2000));

    }

    /**
     * Check if profile page is displayed
     */
    @Test
    public void checkLeaderboardPage(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        BottomNavigationView navBar = (BottomNavigationView) solo.getView(R.id.nav_view);
        solo.clickOnView(navBar.findViewById(R.id.navigation_leaderboard));
        assertTrue(solo.waitForText("Leaderboard", 1, 2000));

    }

    @Test
    public void checkMapsPage(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        BottomNavigationView navBar = (BottomNavigationView) solo.getView(R.id.nav_view);
        solo.clickOnView(navBar.findViewById(R.id.navigation_maps));
        assertTrue(solo.waitForText("Maps", 1, 2000));

    }


    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
