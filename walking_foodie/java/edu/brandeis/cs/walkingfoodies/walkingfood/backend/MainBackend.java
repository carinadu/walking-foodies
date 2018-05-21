package edu.brandeis.cs.walkingfoodies.walkingfood.backend;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.brandeis.cs.walkingfoodies.walkingfood.R;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.DatabaseOperations;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.ProfileData;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.StepData;
import edu.brandeis.cs.walkingfoodies.walkingfood.frontend.MainActivity;

public class MainBackend implements SensorEventListener{

    private Context mainContext;

    //data fields
    private static int steps_walked = 0;
    private static int steps_to_walk = 0;
    private static int total_steps = 0;
    private static double calories_burnt = 0;
    private static double calories_total = 0;

    //information needed to calculate walking rate
    private static long lastTime = System.currentTimeMillis();
    private static int lastStep = 0;
    private static long currentTime = lastTime;
    private static double walking_rate = 0;


    private int fakeSteps = 0; // for test mode
    private static int fakeRate = 500; //for test mode
    private Handler handler; // for test mode
    private DatabaseOperations dbo;
    private Date lastDate = new Date();
    private StepInsert lastInsert = new StepInsert(0, 0);
    private int todaysStartStep = 3000;
    private int totalSensorSteps = 0;
    private double weight;
    private static double burn_rate = 0.049;

    public MainBackend(Context mainContext) {
        this.mainContext = mainContext;
    }

    public void setup() {
        dbo = new DatabaseOperations(mainContext);
        SensorManager sensorManager = (SensorManager) mainContext.getSystemService(Context.SENSOR_SERVICE);
        Sensor stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        //if the device has an api of 4.4 above and supports step_counter sensor
        //we use this sensor to record steps
        if (stepCounter != null) {
            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_UI);
        }
        else {
            new AlertDialog.Builder(mainContext)
                    .setTitle("SENSOR NOT FOUND")
                    .setMessage("Your device doesn't support STEP_COUNTER Sensor! \n\n" +
                            "Click 'OK' to enter test-mode, which generates one step per half second for test purpose.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startTestMode();
                        }
                    })
                    .setIcon(R.drawable.my_ic_alert)
                    .show();
        }

        startInsertEveryTenM();
        Cursor profile = dbo.getAllData(ProfileData.TABLE_NAME, ProfileData.getColArray());
        if (profile.moveToFirst()){
            weight = profile.getDouble(profile.getColumnIndex(ProfileData.WEIGHT));
            burn_rate = 0.001 * weight;
        }
        Log.d("Current Date: ", lastDate.toString());
    }

    private void startInsertEveryTenM() {
        handler = new Handler();

        Runnable insertEvery10 = new Runnable() {

            @Override
            public void run() {
                Date date = new Date();
                StepInsert si = new StepInsert(steps_walked, (int) calories_burnt);
                Log.d("Current Date: ", date.toString());
                if(date.getDate() != lastDate.getDate()) {
                    newDay();
                }
                else {
                    dbo.insertToSteps(new SimpleDateFormat("HH:mm").format(date), si.count - lastInsert.count, si.calories - lastInsert.calories);
                    MainActivity.myPagerAdapter.notifyDataSetChanged();
                }
                lastDate = date;
                lastInsert = si;
                handler.postDelayed(this, 600000);

            }
        };
        handler.postDelayed(insertEvery10, 600000);
    }

    private void newDay() {
        dbo.insertToHistory(new SimpleDateFormat("yyyy-MM-dd").format(lastDate),
                steps_walked, (int)calories_burnt, (int)(calories_total - calories_burnt));
        dbo.delete(StepData.TABLE_NAME);
        calories_burnt = 0;
        calories_total = 0;
        //to fix
        steps_walked = 0;
        todaysStartStep = totalSensorSteps;
    }

    //in test mode, we simulate walking steps by generating one step per half second
    public void startTestMode() {
        handler = new Handler();

        Runnable test = new Runnable() {

            @Override
            public void run() {
                fakeSteps++;
                updateSteps(fakeSteps);
                handler.postDelayed(this, fakeRate);

            }
        };
        handler.postDelayed(test, 500);
    }

    private static void updateSteps(int steps) {
        //update data fields for current time point.
        steps_walked = steps;
        calories_burnt = steps_walked * burn_rate;
        steps_to_walk = calories_total > calories_burnt? (int) ((calories_total - calories_burnt) / burn_rate) : 0;
        total_steps = steps_walked + steps_to_walk;

        //calculate walking rate
        currentTime = System.currentTimeMillis();
        long timeDelta = currentTime - lastTime;
        int stepDelta = steps_walked - lastStep;
        walking_rate = 1.0 * stepDelta / timeDelta;

        //update history data fields for calculating rates
        lastTime = currentTime;
        lastStep = steps_walked;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
            updateSteps((int) sensorEvent.values[0] - todaysStartStep);
            totalSensorSteps = (int) sensorEvent.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Not used
    }

    public int getTotalSteps() {
        return total_steps;
    }

    public int getStepsWalked() {
        return steps_walked;
    }

    public double getRate() {
        return walking_rate;
    }

    public static double getTotalCalories() {
        return calories_total;
    }

    public static void setTotalCalories(double totalCalories) {
        calories_total = totalCalories;
        updateSteps(steps_walked);
    }

    //for demo and test purpose
    public static void setFakeRate(int fakeRate) {
        MainBackend.fakeRate = fakeRate;
    }

    private class StepInsert {
        int count = 0;
        int calories = 0;
        StepInsert(int ct, int c){
            count = ct;
            calories = c;
        }
    }
}
