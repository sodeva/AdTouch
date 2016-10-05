package sodevan.adtouch;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {
    TextView tvname ;
    FirebaseAuth auth ;
    FirebaseUser user ;
    String name ;
    private Boolean exit = false;
    private long BackRepeatTime = 3000 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseAuth auth = FirebaseAuth.getInstance() ;
        user = auth.getCurrentUser() ;
        name = user.getDisplayName() ;


        tvname = (TextView)findViewById(R.id.name) ;

        tvname.setText(name+"Test");


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:

                Toast.makeText(Home.this,"NOice",Toast.LENGTH_SHORT).show();
                break;
            case R.id.item2:
                Toast.makeText(Home.this,"GLHF",Toast.LENGTH_SHORT).show();
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


