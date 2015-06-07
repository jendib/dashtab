package com.sismics.dashtab;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sismics.dashtab.datahelper.CompassDataHelper;
import com.sismics.dashtab.datahelper.PlayLocationDataHelper;
import com.sismics.dashtab.view.SpeedView;

public class HomeActivity extends Activity {
    // Data helpers
    private PlayLocationDataHelper playLocationDataHelper;
    private CompassDataHelper compassDataHelper;

    private Handler handler = new Handler();

    // User interface
    private TextView txtSpeed;
    private ImageView imgCompass;
    private TextView txtCompass;
    private SpeedView speedView;

    private int debugSpeed = 0;
    private boolean debugRaising = true;

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            // Speedometer GPS
            Location location = playLocationDataHelper.getLocation();
            if (location != null) {
                // txtSpeed.setText("" + (int) location.getSpeed());

                if (debugRaising && debugSpeed < 160) {
                    debugSpeed += 5;
                } else if (debugRaising && debugSpeed == 160) {
                    debugSpeed -= 5;
                    debugRaising = false;
                } else if (!debugRaising && debugSpeed > 0) {
                    debugSpeed -= 5;
                } else if (!debugRaising && debugSpeed == 0) {
                    debugSpeed += 5;
                    debugRaising = true;
                }
                txtSpeed.setText("" + debugSpeed);
                speedView.setValue(debugSpeed);
                speedView.invalidate();
            }

            // Compass
            double azimuth = compassDataHelper.getAzimuth() + 90f;
            imgCompass.setRotation((float) azimuth);
            if (azimuth > -45 && azimuth <= 45) {
                txtCompass.setText("N");
            } else if (azimuth > 45 && azimuth <= 135) {
                txtCompass.setText("E");
            } else if (azimuth > 135 && azimuth <= 225) {
                txtCompass.setText("S");
            } else if (azimuth > 225 && azimuth <= 270 || azimuth >= -90 && azimuth <= -45) {
                txtCompass.setText("W");
            }

            handler.postDelayed(updateRunnable, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // Cache user interface
        txtSpeed = (TextView) findViewById(R.id.speed);
        imgCompass = (ImageView) findViewById(R.id.compass);
        txtCompass = (TextView) findViewById(R.id.compassDirection);
        speedView = (SpeedView) findViewById(R.id.speedView);

        // Initialize data helper
        playLocationDataHelper = new PlayLocationDataHelper(this);
        playLocationDataHelper.onCreate();

        compassDataHelper = new CompassDataHelper(this);
        compassDataHelper.onCreate();

        // Start refreshing the user interface
        handler.post(updateRunnable);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(updateRunnable);

        playLocationDataHelper.onDestroy();
        compassDataHelper.onDestroy();

        super.onDestroy();
    }
}
