package sodevan.adtouch;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {
    TextView tvname,heading ;
    FirebaseAuth auth ;
    FirebaseUser user ;
    String name ;
    Typeface cav,robo;
    private Boolean exit = false;
    private long BackRepeatTime = 3000 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        FirebaseAuth auth = FirebaseAuth.getInstance() ;
        user = auth.getCurrentUser() ;
        name = user.getDisplayName() ;
        heading=(TextView)findViewById(R.id.heading);
        cav = Typeface.createFromAsset(getAssets() ,"caviar.ttf") ;
        robo = Typeface.createFromAsset(getAssets() ,"Roboto-Light.ttf");
        heading.setTypeface(cav);




        //tvname = (TextView)findViewById(R.id.name) ;

       // tvname.setText(name);




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

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
            case R.id.Profile:

                Toast.makeText(Home.this,"NOice",Toast.LENGTH_SHORT).show();
                Intent changeprofile = new Intent(getApplicationContext() ,  ProfileInfo.class) ;


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle() ;
                    this.startActivity(changeprofile,bundle);
                }

                else{
                    startActivity(changeprofile);
                }

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




    @Override
    public void onBackPressed() {

        if (exit){   // If user press Back button Again Within 3 seconds.

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);

        }

        else { // when user press Back button FirstTime or After 3 seconds of First Press.

            Toast.makeText(this, "Press Back Again To Exit", Toast.LENGTH_SHORT).show();
            exit=true ;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    exit=false ;
                }
            },BackRepeatTime) ;



        }
    }




}


