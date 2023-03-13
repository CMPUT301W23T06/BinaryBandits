package com.example.binarybandits;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.qrcode.QRCodeInfoActivity;
import com.example.binarybandits.ui.auth.LogInActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Assumptions: There is a player with the username "test" in the database that has two QR
 * codes scanned, one "testQR" worth 100 points and one "toDelete" worth 50 points.
 * QRCodeInfoActivityTest tests functionalities in QRCodeInfoActivity
 */
public class QRCodeInfoActivityTest {

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
        // log in with test account "test"
        solo.enterText((EditText) solo.getView(R.id.editUsername), "test");
        solo.clickOnView(solo.getView(R.id.loginBtn));

        // go to profile page
        solo.clickOnView(solo.getView(R.id.navigation_profile));

        //assert users QR codes are showing, click on testQR
        solo.waitForText("testQR", 1, 2000);
        solo.clickOnText("testQR");
    }




    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();


    }

    /**
     * Assert we switch to QRCodeInfoActivity after we click on testQR
     */
    @Test
    public void checkActivity(){
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

    }

    /**
     * Check if QR code name displayed is "testQR"
     */
    @Test
    public void checkQRName(){
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

        assertTrue(solo.waitForText("testQR", 1, 2000));

    }

    /**
     * Check if QR score displayed is "100"
     */
    @Test
    public void checkQRScore(){
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);
        assertTrue(solo.waitForText("100", 1, 2000));

    }


    /**
     * Check if back button works as intended (takes you back to profile page)
     */
    @Test
    public void checkBackButton(){
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

        solo.clickOnView(solo.getView(R.id.back_button));

        //make sure we are on main activity and profile page
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertTrue(solo.waitForText("test", 1, 2000));

    }


//    /**
//     * Check if delete button works as intended (deletes QR code from users profile
//     *  and takes you back to an updated profile page)
//     */
//    @Test
//    public void checkDelete(){
//        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);
//
//        solo.clickOnView(solo.getView(R.id.back_button));
//
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//
//        solo.waitForText("toDelete", 1, 2000);
//        solo.clickOnText("toDelete");
//
//        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);
//
//        solo.clickOnView(solo.getView(R.id.delete_button));
//        solo.waitForText("Yes", 1, 2000);
//        solo.clickOnText("Yes");
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//
//        assertFalse(solo.waitForText("toDelete", 1, 2000));
//
//
//    }




    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
