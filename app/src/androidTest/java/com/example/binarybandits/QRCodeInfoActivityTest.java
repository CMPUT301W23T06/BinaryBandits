package com.example.binarybandits;


import static android.app.PendingIntent.getActivity;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.qrcode.QRCodeCallback;
import com.example.binarybandits.qrcode.QRCodeDB;
import com.example.binarybandits.qrcode.QRCodeInfoActivity;
import com.example.binarybandits.ui.auth.LogInActivity;
import com.example.binarybandits.ui.auth.SignUpActivity;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Assumptions: QR code SuperHilariousLeopard exists in the Firebase Database, worth 68 points.
 * Link: https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/QR_code_for_mobile_English_Wikipedia
 *          .svg/330px-QR_code_for_mobile_English_Wikipedia.svg.png
 * (QR code from QR code Wikipedia page)
 * Also assume person who is running the test has previously logged in on an account
 */
public class QRCodeInfoActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);




    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(),rule.getActivity());

        //send bundle to QRCodeInfoActivity with QRcode name "SuperHilariousLeopard"
        Intent myIntent = new Intent(rule.getActivity(), QRCodeInfoActivity.class);
        Bundle extras = new Bundle();
        extras.putString("name", "SuperHilariousLeopard");
        extras.putString("username", "robot");  //NPC player to send through just to not cause errors
        extras.putBoolean("current_user", true);
        myIntent.putExtras(extras);
        // go to QRCodeInfoActivity to display the QR code
        rule.getActivity().startActivity(myIntent);
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
     * Assert we switch to QRCodeInfoActivity
     */
    @Test
    public void checkActivity(){
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

    }

    /**
     * Check if QR code name displayed is "SuperHilariousLeopard"
     */
    @Test
    public void checkQRName(){
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

        assertTrue(solo.waitForText("SuperHilariousLeopard", 1, 2000));

    }

    /**
     * Check if QR score displayed is "68"
     */
    @Test
    public void checkQRScore(){
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);
        assertTrue(solo.waitForText("68", 1, 2000));

    }


    /**
     * Check if back button works as intended (takes you back to profile page)
     */
    @Test
    public void checkBackButton() {
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

        solo.clickOnView(solo.getView(R.id.back_button));

        //make sure we are on main activity and profile page
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertTrue(solo.waitForText("Profile", 1, 2000));

    }


    /**
     * Check if delete button alert dialogue works. Does not check delete functionality from
     * firestore
     */

    @Test
    public void checkNoDelete(){

        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

        // Click on the delete button
        solo.clickOnView(solo.getView(R.id.delete_button));

        // Wait for the dialog to open and check that it is showing the correct message
        assertTrue(solo.waitForText("Are you sure you want to delete this QR code from your profile?", 1, 5000));
    }



    /**
     * Check if user can comment on their QR code and display comments
     */
    @Test
    public void checkComments(){
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);


        solo.enterText((EditText) solo.getView(R.id.user_comment), "cool!");
        solo.clickOnView(solo.getView(R.id.addCommentBtn));
        assertTrue(solo.waitForText("cool!", 1, 2000));

    }

    /**
     * Check if see other players button takes you to the intended screen
     */
    @Test
    public void checkOtherPlayers(){
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);


        solo.clickOnView(solo.getView(R.id.other_players_button));
        assertTrue(solo.waitForText("Players Scanned By", 1, 2000));

        // check back button
        solo.clickOnView(solo.getView(R.id.back_to_qrcode));
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

    }

    /**
     * check if location image button displays a "no location image" alert dialogue if no location
     * picture taken. In this case: user robot never takes a location imagine so alert dialogue should
     * come up
     */
    @Test
    public void checkNoLocationImg(){
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

        solo.clickOnView(solo.getView(R.id.location_img_button));
        assertTrue(solo.waitForText("No location image", 1, 2000));

    }


    /**
     * Check that a click on the geolocation button gives an alert dialogue that no geolocation was
     * found. Save any instance of coordinates, delete them to see if no geolocation message works,
     * reapply the coordinates to the QR code
     */
    @Test
    public void checkNoGeolocation() {
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

//        QRCodeDB db_q = new QRCodeDB(new DBConnector());
//        db_q.getQRCode("SuperHilariousLeopard", new QRCodeCallback() {
//            @Override
//            public void onQRCodeCallback(QRCode qrCode) {
//                qrCode.removeCoordinates();
//            }
//        });

        solo.clickOnView(solo.getView(R.id.map_button));
        solo.waitForDialogToOpen();
        assertTrue(solo.waitForText("No geolocation", 1, 2000));


    }


    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        PlayerDB playerDB = new PlayerDB(new DBConnector());
        playerDB.deletePlayer("test");
        solo.finishOpenedActivities();
    }

}
