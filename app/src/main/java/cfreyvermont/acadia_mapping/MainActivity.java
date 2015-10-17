package cfreyvermont.acadia_mapping;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends FragmentActivity {
    public static final String BUTTON_TEXT =
            "com.cfreyvermont.acadia_mapping.BUTTON_TEXT";
    public static FragmentManager manager;
    public MapsActivity mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (hasPlayService()) {
            mapFragment = new MapsActivity();
            transaction.add(R.id.map_placeholder, mapFragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("MainActivity:", "onSaveInstanceState");
        FragmentManager manager = getFragmentManager();
        manager.putFragment(outState, "mapFragmentSaved", mapFragment);
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        Log.d("MainActivity:", "onRestoreInstanceState");
        Log.d("Bundle is", Boolean.toString(inState.isEmpty()));
        rebuildFragments(inState);
    }

    @Override
    public void onBackPressed() {
        //getBackStackEntryCount seemingly ignores the fact that a fragment
        //has child fragments in the back stack. So we need to check the
        //childBackStack manually.
        if (mapFragment != null &&
                mapFragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
            mapFragment.getChildFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void rebuildFragments(Bundle inState) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        //We saved the fragment (most likely on rotation)
        if (inState != null) {
            mapFragment = (MapsActivity) manager.getFragment(inState,
                    "mapFragmentSaved");

        } else {
            mapFragment = new MapsActivity();
            transaction.add(R.id.map_placeholder, mapFragment);
            transaction.commit();
        }
    }
    private boolean hasPlayService() {
        GoogleApiAvailability google = GoogleApiAvailability.getInstance();
        int result = google.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            Log.e("Play Error:", google.getErrorString(result));
            Log.e("Play Version:",
                    Integer.toString(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE));
            return false;
        }
        return true;
    }
}
