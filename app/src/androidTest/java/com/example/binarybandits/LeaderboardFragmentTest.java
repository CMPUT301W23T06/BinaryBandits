package com.example.binarybandits;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.ui.auth.LogInActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LeaderboardFragmentTest{
    private Solo solo;
    private PlayerDB db = new PlayerDB(new DBConnector());

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
        solo.enterText((EditText) solo.getView(R.id.editUsername), "robot");
        solo.clickOnView(solo.getView(R.id.loginBtn));
    }

    /**
     * check to make sure in right activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * check to make sure search button functions
     * @throws Exception
     */
    @Test
    public void checkSearch() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_leaderboard)); //Click leaderboard
        solo.clickOnView(solo.getView(R.id.button));//Click search Button
    }

    /**
     * test to make sure top player button functions
     * @throws Exception
     */
    @Test
    public void player1() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_leaderboard)); //Click leaderboard
        solo.waitForText("#4");
        solo.clickOnView(solo.getView(R.id.limage1));//Click player Button
        solo.assertCurrentActivity("Wrong Activity", otherProfileActivity.class);
    }

    /**
     * test to make sure player 2 button functions
     * @throws Exception
     */
    @Test
    public void player2() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_leaderboard)); //Click leaderboard
        solo.waitForText("#4"); // wait until screen is loaded
        solo.clickOnView(solo.getView(R.id.limage2));//Click player Button
        solo.assertCurrentActivity("Wrong Activity", otherProfileActivity.class);
    }

    /**
     * test to make sure player 3 button functions
     * @throws Exception
     */
    @Test
    public void player3() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_leaderboard)); //Click leaderboard
        solo.waitForText("#4"); // wait until screen is loaded
        solo.clickOnView(solo.getView(R.id.limage3));//Click player Button
        solo.assertCurrentActivity("Wrong Activity", otherProfileActivity.class);
    }

    /**
     * test to make sure the player sends to new activity otherProfileActivity
     * @throws Exception
     */
    @Test
    public void playerList() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_leaderboard)); //Click leaderboard
        solo.waitForText("#4"); // wait until screen is loaded
        solo.clickOnView(solo.getView(R.id.player_name_text));//Click player Button
        solo.assertCurrentActivity("Wrong Activity", otherProfileActivity.class);
    }

    /**
     * 
     */
    @Test
    public void tearDown() {
        solo.finishOpenedActivities();
        AuthController.setUserLoggedInStatus(rule.getActivity(), false);
    }

}
