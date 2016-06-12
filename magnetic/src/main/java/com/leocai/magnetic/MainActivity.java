package com.leocai.magnetic;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.leocai.sensordatagetter.PublicConstants;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensorAcc;
    private Sensor mSensorGYR;
    private Sensor mSensorMag;

    private TextView tvLog;

    private float[] rtm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        tvLog = (TextView) findViewById(R.id.tv_log);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mSensorAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorGYR = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorMag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this, mSensorAcc, (int) (20 * 1000)); // 根据频率调整
        mSensorManager.registerListener(this, mSensorGYR, (int) (20 * 1000));
        mSensorManager.registerListener(this, mSensorMag, (int) (20 * 1000));
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        mWakeLock.acquire();
    }

    float[] mGravity;
    float[] mGeomagnetic;

    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;

        if (mGravity != null && mGeomagnetic != null) {
            rtm = new float[9];
            float I[] = new float[9];
            SensorManager.getRotationMatrix(rtm, I, mGravity, mGeomagnetic);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    double Mx = rtm[3], My = rtm[4], Mz = rtm[5];
                    double cosTheta = Mx/(Math.sqrt(Math.pow(Mx,2)+Math.pow(My,2)+Math.pow(Mz,2)));
                    double azimut = Math.acos(cosTheta);

                    tvLog.setText(""+azimut/Math.PI*180);
                }
            });
//            if (SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic)) {
//
//                // orientation contains azimut, pitch and roll
//                float orientation[] = new float[3];
//                SensorManager.getOrientation(R, orientation);
//
//                final float azimut = orientation[0];
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        tvLog.setText(""+azimut/Math.PI*180);
//                    }
//                });
//            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
}
