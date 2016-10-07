package sodevan.adtouch;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileInfo extends AppCompatActivity {

    String bday ;
    TextView tvbd ;
    String email="fbf";

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_profile_info);
        GraphRequest request=GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),new GraphRequest.GraphJSONObjectCallback(){

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                final JSONObject jsonObject=response.getJSONObject();

                try {
                    email=jsonObject.getString("email");
                    Log.d("email id is",email);
                    Toast.makeText(ProfileInfo.this, "email is"+email, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
         tvbd = (TextView)findViewById(R.id.bd) ;



        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


    tvbd.setText(email);







    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
         return true ;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext()) ;






        switch (item.getItemId()){
            case R.id.Profile:

                Toast.makeText(ProfileInfo.this,"NOice",Toast.LENGTH_SHORT).show();
                break;
            case  R.id.Toggle_Touch_Assist :
                if(item.isChecked()) {
                    item.setChecked(false);
                    sp.edit().putBoolean("status",false).commit() ;
                    Toast.makeText(getApplicationContext(), "Assistive Touch Deactivated", Toast.LENGTH_SHORT).show();

                }



                else {
                    item.setChecked(true);
                    sp.edit().putBoolean("status", true).commit();
                    Toast.makeText(getApplicationContext(), "Assistive Touch Activated", Toast.LENGTH_SHORT).show();









                    // Build Assitive Touch Functionality Here







                }

                break;


        }
        return super.onOptionsItemSelected(item);
    }



}