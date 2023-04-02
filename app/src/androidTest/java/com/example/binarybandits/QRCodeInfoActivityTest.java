package com.example.binarybandits;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

/**
 * Assumptions: There is a player with the username "test" in the database that has two QR
 * codes scanned, one "UltraUniqueGiraffe" worth 58 points and one "SuperHilariousLeopard"
 * worth 68 points. (Where SuperHilariousLeopard is the QR code for the wikipedia page of QR codes)
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
        solo.clickOnView(solo.getView(R.id.createAccountBtn));
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editUsernameSignUp), "test");
        solo.enterText((EditText) solo.getView(R.id.editFullNameSignUp), "Test User");
        solo.clickOnView(solo.getView(R.id.createAccountBtnSignUp));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Players").document("test").update("qrCodesScanned", FieldValue.arrayUnion("SuperHilariousLeopard"));

        // go to profile page
        solo.clickOnView(solo.getView(R.id.navigation_profile));
        //assert users QR codes are showing, click on testQR
        solo.waitForText("SuperHilariousLeopard", 1, 2000);
        solo.clickOnText("SuperHilariousLeopard");
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
    public void checkBackButton() {
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

        solo.clickOnView(solo.getView(R.id.back_button));

        //make sure we are on main activity and profile page
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertTrue(solo.waitForText("test", 1, 2000));

    }


    /**
     * Check if delete button works as intended (deletes QR code from users profile
     *  and takes you back to an updated profile page)
     */
    //@Test
    /*
    public void checkDelete(){
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

        solo.clickOnView(solo.getView(R.id.back_button));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.waitForText("SuperHilariousLeopard", 1, 2000);
        solo.clickOnText("SuperHilariousLeopard");

        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

        solo.clickOnView(solo.getView(R.id.delete_button));

        solo.waitForText("Yes", 1, 2000);
        solo.clickOnText("Yes");


        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_home));
        solo.clickOnView(solo.getView(R.id.navigation_profile));



        PlayerDB db_p = new PlayerDB(new DBConnector());
        QRCodeDB db_q = new QRCodeDB(new DBConnector());
        db_q.getQRCode("SuperHilariousLeopard", new QRCodeCallback() {
            @Override
            public void onQRCodeCallback(QRCode qrCode) {
                db_p.getPlayer("test", new PlayerCallback() {
                    @Override
                    public void onPlayerCallback(Player player) {
                        assertFalse(player.findQRCodeScanned(qrCode.getName()));
                    }
                });
            }


        });

    }*/




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
