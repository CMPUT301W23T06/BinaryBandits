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
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Assumptions: There is a player with the username "test" in the database that has two QR
 * codes scanned, one "UltraUniqueGiraffe" worth 58 points and one "SuperHilariousLeopard"
 * worth 68 points. (Where SuperHilariousLeopard is the QR code for the wikipedia page of QR codes)
 * QRCodeInfoActivityTest tests functionalities in QRCodeInfoActivity
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

        Intent myIntent = new Intent(rule.getActivity(), QRCodeInfoActivity.class);
        Bundle extras = new Bundle();
        extras.putString("name", "SuperHilariousLeopard");
        extras.putString("username", "robot");
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
     * Assert we switch to QRCodeInfoActivity after we click on testQR
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
    public void checkBackButton(){
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

        solo.clickOnView(solo.getView(R.id.back_button));

        //make sure we are on main activity and profile page
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertTrue(solo.waitForText("Profile", 1, 2000));

    }


    /**
     * Check if delete button works as intended (deletes QR code from users profile
     *  and takes you back to an updated profile page)
     */
    @Test
    public void checkNoDelete(){
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

        solo.clickOnView(solo.getView(R.id.delete_button));
        solo.waitForDialogToOpen();
        assertTrue(solo.waitForText("delete", 1, 2000));

    }

    @Test
    public void checkComments(){
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);


        solo.enterText((EditText) solo.getView(R.id.user_comment), "cool!");
        solo.clickOnView(solo.getView(R.id.addCommentBtn));
        assertTrue(solo.waitForText("cool!", 1, 2000));

    }

    @Test
    public void checkOtherPlayers(){
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);


        solo.clickOnView(solo.getView(R.id.other_players_button));
        assertTrue(solo.waitForText("Players Scanned By", 1, 2000));

        // check back button
        solo.clickOnView(solo.getView(R.id.back_to_qrcode));
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

    }

    @Test
    public void checkNoLocationImg(){
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

        solo.clickOnView(solo.getView(R.id.location_img_button));
        assertTrue(solo.waitForText("No location image", 1, 2000));

    }

    @Test
    public void checkNoGeolocation(){
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

        QRCodeDB db_q = new QRCodeDB(new DBConnector());
        db_q.getQRCode("SuperHilariousLeopard", new QRCodeCallback() {
            @Override
            public void onQRCodeCallback(QRCode qrCode) {
                qrCode.removeCoordinates();
            }
        });

        solo.clickOnView(solo.getView(R.id.map_button));
        assertTrue(solo.waitForText("No geolocation", 1, 2000));

    }


    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
