package sodevan.adtouch;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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
import android.widget.ImageView;
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

    TextView bday, agerange , eduaction , email , hometown , location , relation , religion  ;

    TextView tvbd ;
    URI juri  ;
    URL url ;
    Typeface cav , robo;
    Bitmap image  ;


    public CircularImageView circularImageView ;


    FirebaseAuth auth ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);


        cav = Typeface.createFromAsset(getAssets() ,"caviar.ttf") ;
        robo = Typeface.createFromAsset(getAssets() ,"Roboto-Light.ttf");

        bday =(TextView)findViewById(R.id.birhday) ;
        eduaction =(TextView)findViewById(R.id.education) ;
        agerange =(TextView)findViewById(R.id.Agerange) ;
        email = (TextView)findViewById(R.id.email);
        hometown=(TextView)findViewById(R.id.hometown) ;
        location=(TextView)findViewById(R.id.location);
        relation=(TextView)findViewById(R.id.rstatus) ;
        religion=(TextView)findViewById(R.id.religion) ;

        bday.setTypeface(cav);
        eduaction.setTypeface(cav);
        agerange.setTypeface(cav);
        email.setTypeface(cav);
        hometown.setTypeface(cav);
        location.setTypeface(cav);
        relation.setTypeface(cav);
        religion.setTypeface(cav);


        tvbd = (TextView)findViewById(R.id.Username) ;
        tvbd.setTypeface(robo);





        auth = FirebaseAuth.getInstance() ;



        FirebaseUser user = auth.getCurrentUser() ;
        String name = user.getDisplayName() ;

        tvbd.setText(name);



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

        new display().execute(url) ;
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu2,menu);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext()) ;
        boolean b = sp.getBoolean("status" , false) ;
        MenuItem item =  menu.findItem(R.id.Toggle_Touch_Assist) ;
        item.setChecked(b)  ;




        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext()) ;






        switch (item.getItemId()){


            case  R.id.Toggle_Touch_Assist :
                if(item.isChecked()) {
                    item.setChecked(false);
                    sp.edit().putBoolean("status",false).commit() ;
                    Toast.makeText(getApplicationContext(), "Assistive Touch Deactivated", Toast.LENGTH_SHORT).show();
                    stopService(new Intent(ProfileInfo.this, HUD.class));

                }



                else {
                    item.setChecked(true);
                    sp.edit().putBoolean("status", true).commit();
                    Toast.makeText(getApplicationContext(), "Assistive Touch Activated", Toast.LENGTH_SHORT).show();
                    startService(new Intent(ProfileInfo.this, HUD.class));








                    // Build Assitive Touch Functionality Here







                }

                break;


        }
        return super.onOptionsItemSelected(item);
    }


    // This AsyncTask is To get Image from Image url
    private  class display extends AsyncTask<URL ,Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(URL... param) {

            Bitmap image1 = null;
            try {
                url = param[0];
                Log.e("ThredUrl", url.toString());
                image1 = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return image1;
        }


        @Override
        protected void onPostExecute(Bitmap aVoid) {

            super.onPostExecute(aVoid);
            //circularImageView.setImageBitmap(Bitmap.createScaledBitmap(aVoid, 350, 350, false));
            if(circularImageView != null) {
                circularImageView.setImageBitmap(aVoid);
            }else{
                Log.e("BitmapNull", "Bitmap Null");
            }
            }
    }


}