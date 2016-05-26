package com.leocai.sensordatagetter;

import android.hardware.SensorManager;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;


/**
 * Created by leocai on 15-12-31.
 */
public class ShakingData implements Serializable, Cloneable {

    private static final long serialVersionUID = -6091530420906090649L;
    private static final String TAG = "ShakingData";

    private int index;
    private double linearAccData[];
    private double gyrData[];
    private long timeStamp;
    private double dt;
    private double[] gravityAccData;
    private double resultantAccData;
    private double[] convertedData;
    private double[] magnetData;
    private double[] accData;
    private float[] rotationMatrix = new float[9];
    private float[] inclimentMatrix = new float[9];
    private double heartData;
    private boolean heartReady;

    public ShakingData(double[] linearAccData, double[] gyrData, int index, double dt) {
        this.linearAccData = linearAccData;
        this.gyrData = gyrData;
        this.index = index;
        this.dt = dt;
    }

    public double[] getAccData() {
        return accData;
    }

    public void setAccData(double[] accData) {
        this.accData = accData;
    }

    public ShakingData(byte[] sdBuffer) {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(sdBuffer);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        this.linearAccData = new double[3];
        this.gravityAccData = new double[3];
        this.gyrData = new double[3];
        try {
            this.index = dataInputStream.readInt();
            for (int i = 0; i < 3; i++) {
                this.linearAccData[i] = dataInputStream.readDouble();
                this.gyrData[i] = dataInputStream.readDouble();
                this.gravityAccData[i] = dataInputStream.readDouble();
            }
            this.resultantAccData = dataInputStream.readDouble();
            this.timeStamp = dataInputStream.readLong();
            this.dt = dataInputStream.readDouble();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ShakingData(ShakingData shakingData) {
        copy(shakingData);
    }

    public byte[] getBytes() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataInputStream = new DataOutputStream(outputStream);
        try {
            dataInputStream.writeInt(this.index);
            for (int i = 0; i < 3; i++) {
                dataInputStream.writeDouble(this.linearAccData[i]);
                dataInputStream.writeDouble(this.gyrData[i]);
                dataInputStream.writeDouble(this.gravityAccData[i]);
            }
            dataInputStream.writeDouble(this.resultantAccData);
            dataInputStream.writeLong(this.timeStamp);
            dataInputStream.writeDouble(this.dt);
            dataInputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    public ShakingData() {
        Random random = new Random(System.currentTimeMillis());
        linearAccData = new double[]{random.nextDouble() * 10, random.nextDouble() * 10, random.nextDouble() * 10};
        gravityAccData = new double[]{random.nextDouble() * 10, random.nextDouble() * 10, random.nextDouble() * 10};
        convertedData = new double[]{random.nextDouble() * 10, random.nextDouble() * 10, random.nextDouble() * 10};
        gyrData = new double[]{random.nextDouble() * 5, random.nextDouble() * 5, random.nextDouble() * 5};
        index = random.nextInt(100);
        timeStamp = random.nextLong();
        dt = random.nextDouble();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public double[] getLinearAccData() {
        return linearAccData;
    }

    public void setLinearAccData(double[] linearAccData) {
        this.linearAccData = linearAccData;
        if (linearAccData == null) return;
        this.resultantAccData = Math.sqrt(Math.pow(linearAccData[0], 2)
                + Math.pow(linearAccData[1], 2)
                + Math.pow(linearAccData[2], 2));
    }

    public double[] getGyrData() {
        return gyrData;
    }

    public void setGyrData(double[] gyrData) {
        this.gyrData = gyrData;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getDt() {
        return dt;
    }

    public void setDt(double dt) {
        this.dt = dt;
    }

    @Override
    public String toString() {
        return "ShakingData{" +
                "linearAccData=" + Arrays.toString(linearAccData) +
                ", gyrData=" + Arrays.toString(gyrData) +
                ", dt=" + dt +
                ", gravityAccData=" + Arrays.toString(gravityAccData) +
                ", resultantAccData=" + resultantAccData +
                ", convertedData=" + Arrays.toString(convertedData) +
                '}';
    }

    public void setGravityAccData(double[] gravityAccData) {
        this.gravityAccData = gravityAccData;
    }

    public double[] getGravityAccData() {
        return gravityAccData;
    }

    public double getResultantAccData() {
        return resultantAccData;
    }

    public void setResultantAccData(double resultantAccData) {
        this.resultantAccData = resultantAccData;
    }

    @Override
    public ShakingData clone() throws CloneNotSupportedException {
        return (ShakingData) super.clone();
    }

    public void copy(ShakingData cuShakingData) {
        this.index = cuShakingData.index;
        this.gyrData = cuShakingData.gyrData;
        this.linearAccData = Arrays.copyOf(cuShakingData.linearAccData, 3);
        this.gravityAccData = Arrays.copyOf(cuShakingData.gravityAccData, 3);
        this.timeStamp = cuShakingData.timeStamp;
        this.dt = cuShakingData.dt;
        this.resultantAccData = cuShakingData.resultantAccData;
    }

    public void setConvertedData(double[] convertedData) {
        this.convertedData = convertedData;
    }

    public double[] getConvertedData() {
        return convertedData;
    }


    public String getCSVHead() {
        StringBuilder info = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            info.append("Acc");
            info.append(i);
            info.append(",");
        }
        for (int i = 0; i < 3; i++) {
            info.append("LinearAcc");
            info.append(i);
            info.append(",");
        }
        for (int i = 0; i < 3; i++) {
            info.append("Gravity");
            info.append(i);
            info.append(",");
        }
        for (int i = 0; i < 3; i++) {
            info.append("Gyro");
            info.append(i);
            info.append(",");
        }
        for (int i = 0; i < 3; i++) {
            info.append("MagnetData");
            info.append(i);
            info.append(",");
        }
        for (int i = 0; i < 3; i++) {
            info.append("ConvertedData");
            info.append(i);
            info.append(",");
        }
        info.append("ResultantAcc");
        info.append(",");
        info.append("HeartRate");
        info.append(",");
        info.append("Timestamp");
        info.append(",");
        info.append("dt");
        info.append("\n");
        return info.toString();
    }

    public String getCSV() {
        StringBuilder info = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            if(accData!=null)
            info.append(accData[i]);
            info.append(",");
        }
        for (int i = 0; i < 3; i++) {
            if(linearAccData!=null)
            info.append(this.linearAccData[i]);
            info.append(",");
        }
        for (int i = 0; i < 3; i++) {
            if(gravityAccData!=null)
            info.append(this.gravityAccData[i]);
            info.append(",");
        }
        for (int i = 0; i < 3; i++) {
            if(gyrData!=null)
            info.append(this.gyrData[i]);
            info.append(",");
        }
        for (int i = 0; i < 3; i++) {
            if(magnetData !=null)
                info.append(this.magnetData[i]);
            info.append(",");
        }
        for (int i = 0; i < 3; i++) {
            if(convertedData!=null)
                info.append(this.convertedData[i]);
            info.append(",");
        }
        info.append(resultantAccData);
        info.append(",");
        if(heartReady){
            heartReady = false;
            info.append(this.heartData);
            info.append(",");
            Log.d(TAG,heartData+"");
        }else{
            info.append("0");
            info.append(",");
        }
        info.append(timeStamp);
        info.append(",");
        info.append(dt);
        info.append("\n");

        return info.toString();
    }

    public void setMagnetData(double[] magnetData) {
        this.magnetData = magnetData;
    }

    public double[] getMagnetData() {
        return magnetData;
    }

    public void transform() {
        if(gravityAccData == null || magnetData == null) return;
        SensorManager.getRotationMatrix(rotationMatrix, inclimentMatrix,
                new float[]{(float) gravityAccData[0], (float) gravityAccData[1], (float) gravityAccData[2]},
                new float[]{(float) magnetData[0], (float) magnetData[1], (float) magnetData[2]});

        double[][] tempData = MatrixUtils.multiply(new double[][]{
                {rotationMatrix[0], rotationMatrix[3], rotationMatrix[6]},
                {rotationMatrix[1], rotationMatrix[4], rotationMatrix[7]},
                {rotationMatrix[2], rotationMatrix[5], rotationMatrix[8]}

        }, MatrixUtils.convertVectorToMatrix(linearAccData));

        convertedData = MatrixUtils.convertMatrixToVector(tempData);
        Log.d(TAG, Arrays.toString(convertedData));

    }

    public void setHeartData(double heartData) {
        this.heartData = heartData;
        heartReady = true;
    }

    public double getHeartData() {
        return heartData;
    }

    public boolean isHeartReady(){
        return heartReady;
    }
}
