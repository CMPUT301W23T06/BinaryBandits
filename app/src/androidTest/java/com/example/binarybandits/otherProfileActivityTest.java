package com.example.binarybandits;
import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.content.Context;
import android.widget.EditText;
import android.widget.ListView;

import com.example.binarybandits.qrcode.QRCodeInfoActivity;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class otherProfileActivityTest{
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

        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * check to make sure in right activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void playerList() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_leaderboard)); //Click leaderboard
        solo.waitForText("#4"); // wait until screen is loaded
        solo.clickOnView(solo.getView(R.id.limage1));//Click player Button
        solo.assertCurrentActivity("Wrong Activity", otherProfileActivity.class);
        solo.clickOnView(solo.getView(R.id.QR_name));
    }

    @Test
    public void testBack() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_leaderboard)); //Click leaderboard
        solo.waitForText("#4"); // wait until screen is loaded
        solo.clickOnView(solo.getView(R.id.limage1));//Click player Button
        solo.assertCurrentActivity("Wrong Activity", otherProfileActivity.class);
    }
}



