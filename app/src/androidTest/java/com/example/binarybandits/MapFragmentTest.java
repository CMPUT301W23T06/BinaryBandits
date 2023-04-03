package com.example.binarybandits;

import android.app.Activity;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.appcompat.widget.SearchView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.binarybandits.controllers.PermissionsController;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MapFragmentTest {
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
        PermissionsController.askLocationPermission(rule.getActivity());
        Activity activity = rule.getActivity();
    }


    /**
     *
     *
     */
    @Test
    public void checkSearch() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_maps)); //Click maps
        solo.waitForView(solo.getView(R.id.map_search_view));
        solo.clickOnView(solo.getView(R.id.map_search_view)); //Click search view
        SearchView searchView = (SearchView)solo.getView(R.id.map_search_view);
        searchView.setQuery("University of Alberta", true);
    }

}
