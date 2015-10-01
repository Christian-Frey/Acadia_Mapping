package cfreyvermont.acadia_mapping;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final String BUTTON_TEXT =
            "com.cfreyvermont.acadia_mapping.BUTTON_TEXT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    //
    //Name: SubmitFeedbackHandler
    //Purpose: A function to pass information to create a new activity
    //in order for the user to submit their feedback.
    //

    public void submitFeedbackHandler(View v) {
        Intent intent = new Intent(this, SubmitFeedback.class);

        //Adding in where the button was submitted in order to ID what feature
        //was rated
        Button b = (Button) findViewById(R.id.submit_feedback_icon);
        String btnText = b.getHint().toString();
        intent.putExtra(BUTTON_TEXT, btnText);

        startActivity(intent);
    }
}
