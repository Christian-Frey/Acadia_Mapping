package cfreyvermont.acadia_mapping;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


public class SubmitFeedback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_feedback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_submit_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void submitData(View view) {
        //Grabbing the sent data
        //Intent intent = getIntent();
        //String btnText = intent.getStringExtra(MainActivity.BUTTON_TEXT);


        String feedback = "";
        String featureLiked = "";
        //Lets get the values of the selected radioButton
        RadioGroup rg = (RadioGroup) findViewById(R.id.feedback_radioGroup);
        int btn = rg.getCheckedRadioButtonId();
        switch(btn) {
            case (R.id.positive_feedback):
                featureLiked="true";
                break;
            case (R.id.negative_feedback):
                featureLiked= "false";
                break;
        }

        //Grabbing the value of any typed feedback.
        EditText eText = (EditText) findViewById(R.id.feedbackText);
        feedback = eText.getText().toString();

        //Now we have all the data we want to submit.
        //Lets check the network connection.
        if (hasNetwork()) {
            //send data
        }
        else {
            Toast.makeText(getApplicationContext(), "Network Error",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public boolean hasNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        //checks for the current state of the active network
        //null if no network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        //No network connection, Stopping.
        return false;
    }
}
