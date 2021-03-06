package sodevan.adtouch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String hometown ;
    String email ;
    String dob ;
    int year ;
    FirebaseDatabase database ;
    DatabaseReference reference ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());


        mAuth = FirebaseAuth.getInstance(); // Initializing Firebase Auth or Getting Instance of Loogenin User



        // mAuthListener for AuthState Change

        //As soon as State of Auth is Changed , mAuthListener

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Statusi", "onAuthStateChanged:signed_in:" + user.getUid());

                    Intent change = new Intent(getApplicationContext(), Home.class);
                    startActivity(change);
                    finish();



                } else {
                    // User is signed out
                    Log.d("Statusi", "onAuthStateChanged:signed_out");
                }

            }
        };

        mAuth.addAuthStateListener(mAuthListener);


        setContentView(R.layout.activity_main);
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create(); // Facebook CallBack Manager


        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login); // Facebook Login Button

        loginButton.setReadPermissions("email","public_profile","user_friends","user_hometown","user_likes","user_birthday"); //Permision



        // This Function is called through OnActivityResult When FaceBook User Either Succesfully Logged In or is Not able to Log in.

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("status ", "facebook:onSuccess:" + loginResult);
                //If The User Successfully Signed In Then we Call handleFacebookAcessToken Function To Store The User In Firebase Authentication System
                handleFacebookAccessToken(loginResult.getAccessToken());
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "me?fields=birthday,email,hometown",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
            /* handle the result */

                                    Log.d("Response::" , String.valueOf(response.getJSONObject()));
                                JSONObject jso =     response.getJSONObject() ;
                                 hometown =  jso.optJSONObject("hometown").optString("name") ;
                                 email = jso.optString("email") ;
                                 dob = jso.optString("birthday") ;
                                year = Integer.parseInt(dob.substring(6)) ;
                               Log.d("year" , year+"") ;
                                Log.d("hometown" ,hometown) ;
                                Log.d("email" , email) ;
                                Log.d("dob",dob) ;







                            }
                        }
                ).executeAsync();
            }


            @Override
            public void onCancel() {

                Log.i("s", "error");

            }

            @Override
            public void onError(FacebookException error) {

                Log.i("s", "error");

            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }




    //Listening for Facebook User to Log In successfully or Failure . It Then Calls RegisterCallBack
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }


    // After User Is Successfully Loged in  , Then This method is called to Register The User at FireBase
    private void handleFacebookAccessToken(AccessToken token) {

         String s = token.getToken() ;
        Log.e("Access" , s ) ;


        //Storing Credential

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        // setting up firebase User

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Log.d("StoreState ", "signInWithCredential:onComplete:" + task.isSuccessful());



                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser() ;
                    String uid =user.getUid() ;
                    database = FirebaseDatabase.getInstance() ;
                    reference = database.getReference("Users").child(uid);
                    reference.child("hometown").setValue(hometown) ;
                    reference.child("dob").setValue(dob) ;
                    reference.child("birthyear").setValue(year) ;
                    reference.child("email").setValue(email) ;
                    reference.child("name").setValue(user.getDisplayName()) ;



                }
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.


                else if (!task.isSuccessful()) {
                    Log.w("StoreStatus", "signInWithCredential", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    private void FacebookSignOut() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        } else {
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                    .Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {

                    LoginManager.getInstance().logOut();


                }
            }).executeAsync();
        }
    }

}
