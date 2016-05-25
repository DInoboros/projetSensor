package fr.lenours.sensortracker;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

import com.dobi.walkingsynth.accelerometer.AccelerometerDetector;
import com.dobi.walkingsynth.accelerometer.OnStepCountChangeListener;

import java.io.File;

/**
 * Created by Clemsbrowning on 24/05/2016.
 */
public class WalkingDetector implements OnStepCountChangeListener {

    public static int accuracy = 650;
    private Handler handler;
    private Runnable runnable;
    private OnUserWalkingChangeListener listener;
    private SensorManager sensorManager;
    private AccelerometerDetector stepDetector;
    private long launchTime;
    private boolean isWalking = false;
    private int walkingCounter = 1;

    public WalkingDetector(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - launchTime > 3000) { //if you're not walking during 3 seconds
                    walkingCounter = 0;
                    isWalking = false;
                    CsvReader.writeCSV(new File(FileData.folder + "step_time.csv"), "Not Walking");
                    listener.OnUserWalkingChange(false);
                    return;
                }
                handler.postDelayed(runnable, 0);
            }
        };
    }

    public void setOnUserWalkingListener(OnUserWalkingChangeListener listener) {
        this.listener = listener;
        startListening();
    }

    private void startListening() {
        listener.OnUserWalkingChange(false); //Not Walking by default
        launchTime = System.currentTimeMillis();
        stepDetector = new AccelerometerDetector(sensorManager);
        stepDetector.setStepCountChangeListener(this);
        stepDetector.startDetector();
    }

    private boolean isWalking(long time) {
        walkingCounter++;
        handler.removeCallbacks(runnable);
        boolean isWalking = true;
        Log.i("WalkingDetector", time - launchTime + "");
        if ((time - launchTime > WalkingDetector.accuracy) && !this.isWalking) { //Not walking
            walkingCounter = 1;
            launchTime = time;
            return !isWalking;
        }
        launchTime = time;
        if (!this.isWalking && walkingCounter >= 5) { //Walking
            CsvReader.writeCSV(new File(FileData.folder + "step_time.csv"), "Walking");
            this.isWalking = true;
            listener.OnUserWalkingChange(true);
        }
        if (this.isWalking) {
            handler.postDelayed(runnable, 0);
            return isWalking;
        }
        return !isWalking;
    }

    @Override
    public void onStepCountChange(SensorEvent event) {
        if (isWalking(System.currentTimeMillis())) {
            CsvReader.writeCSV(new File(FileData.folder + "step_time.csv"), System.currentTimeMillis() + "");
        }
    }
}