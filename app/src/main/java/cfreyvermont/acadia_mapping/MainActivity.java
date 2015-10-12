package cfreyvermont.acadia_mapping;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class MainActivity extends AppCompatActivity {
    public static final String BUTTON_TEXT =
            "com.cfreyvermont.acadia_mapping.BUTTON_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openMap(findViewById(R.id.openMap));
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

    public void openMap(View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        boolean havePlay = havePlayService();

        if (havePlay) {
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Error with Play Services",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean havePlayService() {
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
