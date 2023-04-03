package com.example.binarybandits;

import android.app.Activity;

import android.widget.EditText;


import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;


import com.example.binarybandits.models.Player;
import com.example.binarybandits.player.PlayerDB;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;
import org.junit.Rule;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;




/**
 * Tests the LeaderboardSearchFragment fragment
 */
public class LeaderboardSearchFragmentTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activityTestRule.getActivity());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //db.collection("Players").add(new Player("test"));
        db.collection("Players").document("test").set(new Player("test"));
    }

    /*public Player setUpPlayer() {
        //solo = new Solo(InstrumentationRegistry.getInstrumentation(), activityTestRule.getActivity());
        Player player = new Player("test");
        return player;
    }*/


    /**
     * Gets the activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = activityTestRule.getActivity();
    }


    /**
     * Tests that the onTextChanged method searches for the correct player when the search text is not empty and adds the player to the players list
     */
    @Test
    public void testOnTextChanged() {
        // Check if the current activity is the LeaderboardSearchFragment activity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.navigation_leaderboard));

        solo.clickOnView(solo.getView(R.id.button));

        solo.enterText((EditText) solo.getView(R.id.search_bar), "test");

        solo.waitForText("test", 1, 2000);
        solo.clickOnMenuItem("test");
        solo.clickOnView(solo.getView(R.id.search_results));
        solo.clickOnView(solo.getView(R.id.profileCardView2));
        solo.assertCurrentActivity("Wrong Activity", otherProfileActivity.class);
    }

    /**
     * Tests that the onItemClicked method launches the otherProfileActivity when a player is clicked, and that it is the correct player
     */
    @Test
    public void testOnItemClicked() {
        // Check if the current activity is the LeaderboardSearchFragment activity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.navigation_leaderboard));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.button));

        solo.enterText((EditText) solo.getView(R.id.search_bar), "test");


        solo.waitForText("test", 1, 2000);
        solo.clickOnMenuItem("test");
        solo.clickOnView(solo.getView(R.id.search_results));
        solo.clickOnView(solo.getView(R.id.profileCardView2));
        solo.assertCurrentActivity("Wrong Activity", otherProfileActivity.class);

        assertTrue(solo.waitForText("test", 2, 2000));

    }

    /**
     * Tests that the onTextChanged method does not add a player to the player list when the search text is empty and clears the players list
     */
    @Test
    public void testOnTextChangedEmpty() {
        // Check if the current activity is the LeaderboardSearchFragment activity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.navigation_leaderboard));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.button));


        solo.enterText((EditText) solo.getView(R.id.search_bar), "test");
        solo.waitForText("test", 1, 2000);

        solo.clearEditText((EditText) solo.getView(R.id.search_bar));
        solo.sleep(3000);
        solo.enterText((EditText) solo.getView(R.id.search_bar), " ");

        assertFalse(solo.waitForText("test",2,3000)==true);

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

    }


    /**
     * Tests that when the back to leaderboard button is clicked, the leaderboard fragment is switched to it's view
     */
    @Test
    public void testBackToLeaderboard() {
        // Check if the current activity is the LeaderboardSearchFragment activity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.navigation_leaderboard));
        solo.clickOnView(solo.getView(R.id.button));
        solo.enterText((EditText) solo.getView(R.id.search_bar), "test");
        solo.clickOnView(solo.getView(R.id.search_bar));
        solo.waitForText("test", 1, 2000);

        solo.clickOnView(solo.getView(R.id.back_to_leaderboard));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

    }


    /**
     * Closes the activity after each test
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Players").document("test").delete();
    }

}
