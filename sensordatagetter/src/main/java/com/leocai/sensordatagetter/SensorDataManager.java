package com.leocai.sensordatagetter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.PowerManager;

import java.io.File;

/**
 * Created by leocai on 16-5-26.
 */
public class SensorDataManager {
    private SensorManager mSensorManager;

    private final Context context;
    private SensorGlobalWriter sensorGlobalWriter;
    private Sensor mSensorAcc;
    private Sensor mSensorGYR;
    private Sensor mSensorMag;

    private int id = 1;

    public SensorDataManager(Context context) {

        this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        mSensorAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorGYR = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorMag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        final PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        mWakeLock.acquire();
    }

    public void start() {
        initFile();
        registerSensor();
    }

    public void stop(){
        unRegisterSensor();
    }

    private void initFile() {
        sensorGlobalWriter = new SensorGlobalWriter();
        sensorGlobalWriter.setFileName("mobile_" + id++ + ".csv");
    }


    private void unRegisterSensor() {
        sensorGlobalWriter.close();
        mSensorManager.unregisterListener(sensorGlobalWriter, mSensorAcc);
        mSensorManager.unregisterListener(sensorGlobalWriter, mSensorGYR);
        mSensorManager.unregisterListener(sensorGlobalWriter, mSensorMag);

    }

    private void registerSensor() {
        sensorGlobalWriter.startDetection();
        mSensorManager.registerListener(sensorGlobalWriter, mSensorAcc, (int) (PublicConstants.SENSOPR_PERIOD * 1000)); // 根据频率调整
        mSensorManager.registerListener(sensorGlobalWriter, mSensorGYR, (int) (PublicConstants.SENSOPR_PERIOD * 1000));
        mSensorManager.registerListener(sensorGlobalWriter, mSensorMag, (int) (PublicConstants.SENSOPR_PERIOD * 1000));
    }




}
