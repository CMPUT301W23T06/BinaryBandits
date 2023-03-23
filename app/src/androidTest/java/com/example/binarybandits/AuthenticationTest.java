package com.example.binarybandits;


import android.app.Activity;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.ActivityTestRule$$ExternalSyntheticLambda0;

import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.ui.auth.LogInActivity;
import com.example.binarybandits.ui.auth.SignUpActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for sign up and login
 * Outstanding issues:
 *   - Assumes that no players have the usernames "BostonBoy" or "Josh123". Need to delete
 *   BostonBoy and Josh123 after running test every time
 *   - Assumes that a player with username "toast" is in the database
 */
@RunWith(AndroidJUnit4.class)
public class AuthenticationTest {
    private Solo solo;
    private PlayerDB db = new PlayerDB(new DBConnector());

    @Rule
    public ActivityTestRule<LogInActivity> rule =
            new ActivityTestRule<>(LogInActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        AuthController.setUserLoggedInStatus(solo.getCurrentActivity().getApplicationContext(), false);
        PlayerDB db = new PlayerDB(new DBConnector());
        db.deletePlayer("BostonBoy");
        db.deletePlayer("Josh123");
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
     * Checks that the app changes activity from LoginActivity to MainActivity on successful login.
     * Assumes that a Player with username "toast" is in the database
     */
    @Test
    public void checkValidLogin() {
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editUsername), "toast");
        solo.clickOnView(solo.getView(R.id.loginBtn));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * Checks that the app changes does not change activities when an username not in the database
     * is entered. Assumes that "Toast73" is NOT in the database
     */
    @Test
    public void checkInvalidLogin() {
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editUsername), "Toast73");
        solo.clickOnView(solo.getView(R.id.loginBtn));
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
    }

    /**
     * Checks that the app changes activity from LogInActivity to SignUpActivity when the 'Create
     * an account' button (in LogInActivity) is pressed. When the 'Create an account' button (in SignUpActivity) is
     * pressed with a username not in the database, the app changes from SignUpActivity to MainActivity.
     * Assumes "BostonBoy" is not in the database.
     */
    @Test
    public void signUp() {
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.clickOnView(solo.getView(R.id.createAccountBtn));
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editFullNameSignUp), "Sam Jones");
        solo.enterText((EditText) solo.getView(R.id.editUsernameSignUp), "BostonBoy");
        solo.enterText((EditText) solo.getView(R.id.editPhoneSignUp), "6178230249");
        solo.clickOnView(solo.getView(R.id.createAccountBtnSignUp));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        PlayerDB db = new PlayerDB(new DBConnector());
        db.deletePlayer("BostonBoy");
    }

    /**
     * Checks that a user can sign up without a phone number. Checks that activity changes from
     * SignUpActivity to MainActivity.
     */
    @Test
    public void signUpNoPhoneNumber() {
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.clickOnView(solo.getView(R.id.createAccountBtn));
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editFullNameSignUp), "Josh Smith");
        solo.enterText((EditText) solo.getView(R.id.editUsernameSignUp), "Josh123");
        solo.clickOnView(solo.getView(R.id.createAccountBtnSignUp));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        PlayerDB db = new PlayerDB(new DBConnector());
        db.deletePlayer("Josh123");
    }

    /**
     * Checks that the app does not switch to MainActivity when a user attempts to sign up
     * using a username in the database. Assumes "toast" is in the database.
     */
    @Test
    public void signUpUserInDB() {
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.clickOnView(solo.getView(R.id.createAccountBtn));
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editFullNameSignUp), "Sukhnoor Khehra");
        solo.enterText((EditText) solo.getView(R.id.editUsernameSignUp), "toast");
        solo.enterText((EditText) solo.getView(R.id.editPhoneSignUp), "7802914382");
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
    }

    /**
     * Checks that the app does not switch to MainActivity when a user does not enter a username
     */
    @Test
    public void signUpUsernameEmpty() {
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.clickOnView(solo.getView(R.id.createAccountBtn));
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editFullNameSignUp), "Sukhnoor Khehra");
        solo.enterText((EditText) solo.getView(R.id.editPhoneSignUp), "7802914382");
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
    }

    /**
     * Checks that the app does not switch to MainActivity when a user does not enter their full name
     */
    @Test
    public void signUpFullNameEmpty() {
        solo.assertCurrentActivity("Wrong Activity", LogInActivity.class);
        solo.clickOnView(solo.getView(R.id.createAccountBtn));
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editUsernameSignUp), "toast");
        solo.enterText((EditText) solo.getView(R.id.editPhoneSignUp), "7802914382");
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
    }

    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
