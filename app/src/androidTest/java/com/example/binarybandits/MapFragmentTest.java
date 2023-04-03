package com.example.binarybandits;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.appcompat.widget.SearchView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.controllers.PermissionsController;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.qrcode.QRCodeInfoActivity;
import com.example.binarybandits.ui.QRpage.QRpage;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for MapFragment functionality
 */
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(("QRCodes")).document("testQR").set(new QRCode("589054", "testQR",12));
        db.collection("Players").document("test").set(new Player("test"));
        db.collection("Players").document("test").update("qrCode", "testQR");
        db.collection("QRCode").document("testQR").update("coordinates", "53.5232183", "coordinates", "-113.5263137");

    }

    /**
     * check to make sure in right activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        //PermissionsController.askLocationPermission(rule.getActivity());
        Activity activity = rule.getActivity();
    }

    /***
     * Test to see if the map fragment is displayed
     */
    @Test
    public void checkMap() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_maps)); //Click maps
        solo.waitForView(solo.getView(R.id.map_search_view));
        assert(solo.waitForView(solo.getView(R.id.map)));
    }

    /***
     * Test to see if the search bar is displayed
     */
    @Test
    public void checkSearchBar() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_maps)); //Click maps
        solo.waitForView(solo.getView(R.id.map_search_view));
        solo.waitForView(solo.getView(R.id.map));
        assert(solo.waitForView(solo.getView(R.id.map_search_view)));
    }


    /***
     * Test to see if search works
     */
    @Test
    public void checkSearch() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_maps)); //Click maps
        solo.waitForView(solo.getView(R.id.map_search_view));
        SearchView searchView = (SearchView)solo.getView(R.id.map_search_view);
        searchView.setQuery("University of Alberta", true);
        solo.clickOnView(solo.getView(R.id.map_search_view));
        solo.sendKey(Solo.ENTER);
        solo.clickOnView(solo.getView(R.id.map)); //Click middle of map view
        solo.assertCurrentActivity("Wrong Activity", QRCodeInfoActivity.class);

    }

    /***
     * Test to see if marker is displayed
     */
    @Test
    public void checkMarker() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_maps)); //Click maps
        solo.waitForView(solo.getView(R.id.map_search_view));
        SearchView searchView = (SearchView)solo.getView(R.id.map_search_view);
        searchView.setQuery("University of Alberta", true);
    }


    /**
     * Close activity after each test
     */
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
        AuthController.setUserLoggedInStatus(rule.getActivity(), false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("QRCodes").document("testQR").delete();
        db.collection("Players").document("test").delete();
    }

}
