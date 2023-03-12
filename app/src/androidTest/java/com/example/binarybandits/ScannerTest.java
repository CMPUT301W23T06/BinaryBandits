package com.example.binarybandits;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.ui.auth.LogInActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ScannerTest {
    private Solo solo;
    private QRCode mockQRCode = new QRCode("cdf07521489cd151da290b3315207a61935357af5fe5614df7668b30a1d6f672", "SuperAmazingFerret", 47);

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
     *
     */
    @Test
    public void testAddQRCode() {
        solo.assertCurrentActivity("Wrong activity", LogInActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editUsername), "PieceOfPi");
        solo.clickOnView(solo.getView(R.id.loginBtn));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

    }
}
