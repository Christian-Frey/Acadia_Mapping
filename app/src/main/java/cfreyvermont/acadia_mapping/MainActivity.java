/* 
 * Title      : Acadia Mapping Application
 * Names      : Redacted
 * Student ID : Redacted
 * Description: This application will provide users with the 
 *        	    ability to view the Acadia Campus with outlines
 *		        for each building, search for buildings, and
 *		        get information on each building by tapping on it.
 */


package cfreyvermont.acadia_mapping;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends FragmentActivity {
    public static final String BUTTON_TEXT =
            "com.cfreyvermont.acadia_mapping.BUTTON_TEXT";
    private MapsActivity mapFragment;

    /*
     * On page 19 of the book, we used figure 1.7 and section 1.5.1 to guide the
     * design of the application. We wanted our app to be easy to use, effective in the
     * information provided, safe in that it will not edit the phone beyond repair or track your
     * location. We wanted our app to have good utility, providing the user with what
     * they expect a map to do. One example of utility is that users expect to close the
     * buildingInfo window by tapping on an area outside of the building outline.
     * Our app is easy to learn due to its limited feature set and similar interface
     * to Google Maps, which most users are familiar with. Our app is easy to remember
     * since it does not have many features to remember how to use. These 6 goals helped
     * to guide us in our design of the application.
     */


    /**
     * Is called when the activity is first created, before the view is inflated.
     *
     * @param savedInstanceState the saved state of the app, if it was saved.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (hasPlayService()) {
            mapFragment = new MapsActivity();
            transaction.add(R.id.map_placeholder, mapFragment);
            transaction.commit();
        } else {
            /*
             * If the user does not have Google Play Services, or it is
             * not up to date, a message appears reminding the user to
             * update their device. This is impersonal, but should get the
             * point across without any confusion.
             */
            Toast.makeText(getApplicationContext(),
                    "Please update or install Google Play Services",
                    Toast.LENGTH_SHORT).show();

        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    */

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_feedback_icon:
                Intent intent = new Intent(this, SubmitFeedback.class);
                //Adding the button submission location
                String btnLocation = (String) item.getTitle();
                intent.putExtra(BUTTON_TEXT, btnLocation);

                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    /**
     * Intercepts the back button being pressed to correctly remove the
     * BuildingInfo fragment when the user presses the back button.
     */
    @Override
    public void onBackPressed() {
        /*
         * getBackStackEntryCount seemingly ignores the fact that a fragment
         * has child fragments in the back stack. So we need to check the
         * childBackStack manually.
         */
        if (mapFragment != null &&
                mapFragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
            mapFragment.getChildFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Checks to make sure the user has a working and updated version of Google
     * Play Services.
     *
     * @return If the user has access to Google Play Services, which is required for
     * Google maps to work.
     */
    private boolean hasPlayService() {
        GoogleApiAvailability google = GoogleApiAvailability.getInstance();
        int result = google.isGooglePlayServicesAvailable(this);
        return result == ConnectionResult.SUCCESS;
    }
}
