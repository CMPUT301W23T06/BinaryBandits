package com.example.binarybandits;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.qrcode.QRCodeInfoActivity;
import com.example.binarybandits.qrcode.QRCodeScannedByActivity;
import com.example.binarybandits.ui.auth.LogInActivity;
import com.example.binarybandits.ui.auth.SignUpActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for the QRCodeScannedActivity view class.
 */
public class QRCodeScannedByActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<LogInActivity> rule =
            new ActivityTestRule<>(LogInActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        AuthController.setUserLoggedInStatus(rule.getActivity(), false);
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        // log in with test account "test"
        solo.enterText((EditText) solo.getView(R.id.editUsername), "robot");
        solo.clickOnView(solo.getView(R.id.loginBtn));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * Check that a QRCode in a profile takes users to QRCodeScannedByActivity when players click
     * the "other players" button on the QRCodeInfoActivity page.
     */
    @Test
    public void checkQRCodeInProfile() {
        BottomNavigationView navBar = (BottomNavigationView) solo.getView(R.id.nav_view);
        solo.clickOnView(navBar.findViewById(R.id.navigation_profile));
        assertTrue(solo.waitForText("Profile", 1, 2000));
        solo.clickOnView(solo.getView(R.id.list_view_player_qr_codes));
        solo.clickOnView(solo.getView(R.id.profileCardView3));
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);
        solo.clickOnView(solo.getView(R.id.other_players_button));
        solo.assertCurrentActivity("Wrong Activity", QRCodeScannedByActivity.class);
    }

    /**
     * Close activity after each test
     */
    @After
    public void tearDown() {
        AuthController.setUserLoggedInStatus(rule.getActivity(), false);
        solo.finishOpenedActivities();
    }
}
