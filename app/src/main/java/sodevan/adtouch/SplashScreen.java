package sodevan.adtouch;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tv1;

        final long  SplashScreenTime = 2000 ;   // Time gap for Splash Screen



        setContentView(R.layout.activity_splash_screen);



        Typeface mycustomfont=Typeface.createFromAsset(getAssets(),"Construthinvism.ttf");


        //Splash Screen Text
        tv1=(TextView)findViewById(R.id.textView);

        tv1.setTypeface(mycustomfont);




        // Delaying intent-Change By SplashScreenTime mentioned Above

        Thread thread=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(SplashScreenTime);
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }



}
