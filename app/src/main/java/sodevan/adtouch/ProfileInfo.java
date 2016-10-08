package sodevan.adtouch;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookActivity;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ProfileInfo extends AppCompatActivity {

    String bday ;
    TextView tvbd ;
    URI juri  ;
    URL url ;
    Bitmap image  ;

    CircularImageView circularImageView ;



    FirebaseAuth auth ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        auth = FirebaseAuth.getInstance() ;

        FirebaseUser user = auth.getCurrentUser() ;


        circularImageView = (CircularImageView)findViewById(R.id.imageView) ;




         Uri uri  = user.getPhotoUrl() ;


        try {
            juri = new URI(uri.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        try {
            url  = juri.toURL() ;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.e("Url : ", url.toString()) ;
        new display().execute() ;



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


    // This AsyncTask is To get Image from Image url
    private  class display extends AsyncTask<Void ,Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
            circularImageView.setImageBitmap(Bitmap.createScaledBitmap(image, 350, 350, false));

        }
    }


}