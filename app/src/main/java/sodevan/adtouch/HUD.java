package sodevan.adtouch;

        import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class HUD extends Service implements OnTouchListener, OnClickListener {
    private View topLeftView;

    private ImageView overlayedButton;
    private RelativeLayout adWindow, fullWindow, blankWindow;
    private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;
    private boolean moving;
    private WindowManager wm;
    private int winHeight = 0;
    private int winWidth = 0;
    private GradientDrawable shape;
    private int[] smallAds = new int[] { R.drawable.facebook, R.drawable.zomato, R.drawable.flipkart };
    private int smallAdsCounter = 0;
    private int[] largeAds = new int[] {R.drawable.ad1, R.drawable.ad2, R.drawable.ad3, R.drawable.ad4,
            R.drawable.ad5};

    private LayoutParams params;
    private Handler handler;
    private SliderLayout slider;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        winHeight = metrics.heightPixels;
        winWidth = metrics.widthPixels;

        // Rounded Corner Shape
        shape = new GradientDrawable();
        shape.setCornerRadius(8);
        shape.setColor(Color.DKGRAY);

        overlayedButton = new ImageView(this);
        overlayedButton.setImageResource(R.drawable.btnfloat);
        overlayedButton.setOnTouchListener(this);

        overlayedButton.setOnClickListener(this);
        overlayedButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);
        overlayedButton.setAdjustViewBounds(true);
        params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.TYPE_SYSTEM_ALERT,
                LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;
        params.width = 200;
        params.height = 200;
        wm.addView(overlayedButton, params);

        topLeftView = new View(this);
        LayoutParams topLeftParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, LayoutParams.TYPE_SYSTEM_ALERT, LayoutParams.FLAG_NOT_FOCUSABLE
                | LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        topLeftParams.gravity = Gravity.LEFT | Gravity.TOP;
        topLeftParams.x = 0;
        topLeftParams.y = 0;
        topLeftParams.width = 0;
        topLeftParams.height = 0;
        wm.addView(topLeftView, topLeftParams);

        // Create a Slider Layout
        slider = new SliderLayout(this);

        // Download Ads
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // Clear Previous Database
                AdDatabase adb1 = new AdDatabase(getApplicationContext());
                adb1.open();
                adb1.cleardatabase();
                adb1.close();

                // Download Ads Here
                new DownloadAds().execute();

            }
        };

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 8);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            timer.schedule(task, today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
        }else{
            Toast.makeText(getApplicationContext(), "Download Ads Schedule Failed. Use Gingerbread and above",
                    Toast.LENGTH_LONG).show();
        }

        // Change Ads in Assistive Touch
        Timer changeAdTimer = new Timer();
        TimerTask changeAdTimerTask = new TimerTask() {

            @Override
            public void run() {

                // Get Sequential Image from Database

                // Display Image on Small Touch
                try {
                    if (smallAdsCounter < smallAds.length) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                overlayedButton.setImageResource(smallAds[smallAdsCounter]);

                                // Update counter
                                smallAdsCounter = smallAdsCounter + 1;
                            }
                        });
                    } else {
                        smallAdsCounter = 0;
                    }
                }catch(Exception ex){
                    Log.e("AdTouchException", ex.getMessage());
                }

            }
        };
        changeAdTimer.schedule(changeAdTimerTask, 0, 20000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (overlayedButton != null) {
            wm.removeView(overlayedButton);
            wm.removeView(topLeftView);
            overlayedButton = null;
            topLeftView = null;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getRawX();
            float y = event.getRawY();

            moving = false;

            int[] location = new int[2];
            overlayedButton.getLocationOnScreen(location);

            originalXPos = location[0];
            originalYPos = location[1];

            offsetX = originalXPos - x;
            offsetY = originalYPos - y;

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int[] topLeftLocationOnScreen = new int[2];
            topLeftView.getLocationOnScreen(topLeftLocationOnScreen);

            System.out.println("topLeftY=" + topLeftLocationOnScreen[1]);
            System.out.println("originalY=" + originalYPos);
            float x = event.getRawX();
            float y = event.getRawY();

            LayoutParams params = (LayoutParams) overlayedButton.getLayoutParams();

            int newX = (int) (offsetX + x);
            int newY = (int) (offsetY + y);

            if (Math.abs(newX - originalXPos) < 1 && Math.abs(newY - originalYPos) < 1 && !moving) {
                return false;
            }

            params.x = newX - (topLeftLocationOnScreen[0]);
            params.y = newY - (topLeftLocationOnScreen[1]);

            wm.updateViewLayout(overlayedButton, params);
            moving = true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (moving) {
                return true;
            }
        }

        return false;
    }

    private WindowManager winm;

    @Override
    public void onClick(View v) {
        //Toast.makeText(this, "Button click event", Toast.LENGTH_SHORT).show();
        winm = (WindowManager)getSystemService(WINDOW_SERVICE);

        adWindow = new RelativeLayout(this);
        //adWindow.setBackgroundColor(Color.BLUE);
        adWindow.setPadding(20, 500, 20, 500);
        //adWindow.setBackgroundDrawable(shape);

        Animation animation =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.animation);
        animation.setDuration(500);
        adWindow.setAnimation(animation);
        //adWindow.animate();
        animation.start();

        LayoutParams adParam = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                LayoutParams.TYPE_SYSTEM_ALERT,
                LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        adParam.x = 0;
        adParam.y = 0;

        //adParam.width = winWidth - 30;
        //adParam.height = winHeight - 250;
        adParam.gravity = Gravity.CENTER_HORIZONTAL;

        // Configure Slider Layout

        // Send AD ID to Database and get HashMap
        HashMap<String, Integer> adImages = new HashMap<String, Integer>();
        adImages.put("AdText1", R.drawable.ad1);
        adImages.put("AdText2", R.drawable.ad2);
        adImages.put("AdText3", R.drawable.ad3);
        adImages.put("AdText4", R.drawable.ad4);
        adImages.put("AdText5", R.drawable.ad5);


        for(String name: adImages.keySet()){
            TextSliderView textSliderView = new TextSliderView(getApplicationContext());
            textSliderView.description(name).image(adImages.get(name)).setScaleType(
                    BaseSliderView.ScaleType.Fit
            );

            if(slider!= null) {
                slider.addSlider(textSliderView);
            }
        }
        slider.setBackgroundColor(Color.GRAY);
        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setDuration(10000);

        adWindow.addView(slider);

        winm.addView(adWindow, adParam);

        wm.removeView(overlayedButton);
        adWindow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                slider.removeAllSliders();
                adWindow.removeView(slider);
                winm.removeView(adWindow);
                wm.addView(overlayedButton, params);
            }
        });

    }

    private void runOnUiThread(Runnable runnable){
        handler.post(runnable);
    }

    private class DownloadAds extends AsyncTask<Void, Void, Boolean>{

        private AdsParser downloaddata;
        private URL url;
        private BufferedReader reader;
        private URL imgurl;
        private HttpURLConnection urlconnection;
        private long insertid;

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean download_result = false;

            // Connect to WebService
            try {
                url = new URL("http://www.adtouch.in/downloadads.php?username=admin");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String datafromurl = "";
            String dataurl;
            try {
                while((dataurl = reader.readLine()) != null){
                    datafromurl = datafromurl + dataurl;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i("DownloadAds", datafromurl);

            Gson gson = new Gson();

            downloaddata = gson.fromJson(datafromurl, AdsParser.class);

            List<AdsParser.ResultEntity> result = downloaddata.getResult();
            for(AdsParser.ResultEntity resdata : result){
                String title = resdata.getTitle();
                String image = resdata.getImage();

                try {
                    imgurl = new URL(image);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {

                    urlconnection = (HttpURLConnection)imgurl.openConnection();
                    InputStream imgstream = urlconnection.getInputStream();
                    Bitmap bmpimage = BitmapFactory.decodeStream(imgstream);
                    byte[] imgblob = getbBytes(bmpimage);

                    AdDatabase adb = new AdDatabase(getApplicationContext());
                    adb.open();
                    insertid = adb.save(title, imgblob);
                    adb.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.i("Title", title);
                Log.i("Image", image);

                List<AdsParser.ResultEntity.DataEntity> data =
                        resdata.getData();

                for(AdsParser.ResultEntity.DataEntity rdata : data){

                    String adtext = rdata.getText();
                    String adimage = rdata.getImage();

                    try {
                        URL adimgurl = new URL(adimage);
                        HttpURLConnection adurlconnection = (HttpURLConnection) adimgurl.openConnection();
                        InputStream adimgstream = adurlconnection.getInputStream();
                        Bitmap adimg = BitmapFactory.decodeStream(adimgstream);
                        byte[] adimgblob = getbBytes(adimg);

                        // Save to Database
                        AdDatabase adb1 = new AdDatabase(getApplicationContext());
                        adb1.open();
                        adb1.saveData(insertid, adtext, adimgblob);
                        adb1.close();

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.i("AdText", adtext);
                    Log.i("AdImage", adimage);

                    // Download Image and Save to Database

                }
                download_result = true;
            }

            // Save to Database
            return download_result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result) {
                Toast.makeText(getApplicationContext(), "Ads Downloaded", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "Ads Download Failed", Toast.LENGTH_LONG).show();
            }
        }

    }

    public static byte[] getbBytes(Bitmap image){
        byte[] result = null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 0, bos);
        return bos.toByteArray();
    }

    public static Bitmap getBitmap(byte[] image){
        Bitmap result = null;
        BitmapFactory.decodeByteArray(image, 0, image.length);
        return result;
    }

}
