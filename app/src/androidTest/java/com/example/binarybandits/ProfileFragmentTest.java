package com.example.binarybandits;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.Fragment;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.binarybandits.qrcode.QRCodeInfoActivity;
import com.example.binarybandits.ui.auth.LogInActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * ProfileFragmentTest tests that while on main activity, when a user clicks their profile icon on
 * the bottom navigation, profile page fragment is displayed.
 */
public class ProfileFragmentTest {
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
        BottomNavigationView navBar = (BottomNavigationView) solo.getView(R.id.nav_view);
        solo.clickOnView(navBar.findViewById(R.id.navigation_profile));
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Check that we are looking at the profile page by confirming page title
     */
    @Test
    public void checkProfileTitle(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertTrue(solo.waitForText("Profile", 1, 2000));
    }

    /**
     * Check we are looking at profile page by confirming the "QR Codes Scanned" title
     */
    @Test
    public void checkQRTitle(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertTrue(solo.waitForText("QR Codes Scanned", 1, 2000));
    }

}
