package cfreyvermont.acadia_mapping;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


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
        Intent intent = getIntent();
        String btnText = intent.getStringExtra(MainActivity.BUTTON_TEXT);

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
        String feedback = eText.getText().toString();

        //Now we have all the data we want to submit.
        //Lets check the network connection.
        if (hasNetwork()) {
            new UploadFeedbackTask().execute(btnText, feedback, featureLiked);
        }
        else {
            Toast.makeText(getApplicationContext(), "Network Error",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasNetwork() {
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

    private class UploadFeedbackTask extends AsyncTask<String, Integer, String> {
        private final String FEEDBACK_SERVER = "http://10.0.2.2/android/insert.php";

        @Override
        protected void onPreExecute() {
            //Toast.makeText(getApplicationContext(), "Made it!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... vars) {

            try {
                //Sends to a function designed to upload the data.
                return uploadData(vars);
            } catch (Exception e) {
                return "Error Uploading Feedback";
            }
        }

        @Override
        //Lets the user know the feedback has been submitted.
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            SubmitFeedback.this.finish();
        }

        //uploads the data to the server specified at the top of the private class.
        String uploadData(String[] vars) {
            try {
                vars[0] = URLEncoder.encode(vars[0], "UTF-8");
                String dataToEncode = "?btnText=" + vars[0] + "&isLiked=" + vars[2] +
                        "&feedback=" + vars[1];

                URL url = new URL(FEEDBACK_SERVER + dataToEncode);
                Log.d("URL connection:", url.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                int responseCode = conn.getResponseCode();
                Log.d("HTTP Response:", Integer.toString(responseCode));

                //Making sure the server got our package correctly.
                if (responseCode != 200) {
                    Toast.makeText(getApplicationContext(), "Connection Error",
                            Toast.LENGTH_SHORT).show();
                    return "Sending failed";
                }
            }
            catch (IOException e) {
                Log.e("I/O Exception: ", e.getMessage());
            }
            return "Feedback Submitted Successfully!";
        }
    }
}
