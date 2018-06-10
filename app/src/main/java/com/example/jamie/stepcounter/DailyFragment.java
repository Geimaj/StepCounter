package com.example.jamie.stepcounter;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import static android.content.Context.SENSOR_SERVICE;


/**
 * A simple {@link Fragment} subclass.s
 */
public class DailyFragment extends Fragment
        implements SharedPreferences.OnSharedPreferenceChangeListener, StepListener, SensorEventListener, StorageChanged{

    private Button updateWeightButton;
//    private View.OnClickListener updateWeightHandler;
    private TextView weightValueTextView;
    private TextView stepsTextView;
    public static  StorageHandler storage;
    private StorageChanged storageChangedInterface;
    private UpdateWeightDialog weightDialog;
    public static SharedPreferences preferences;
    private TextView weightGoalTextView;
    private TextView targetWeightTextView;
    private SensorManager sensorManager;
    private Sensor accel;
    private StepDetector simpleStepDetector;
    private int numSteps;

    private ProgressBar weightProgressBar;

    public DailyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_daily, container, false);


        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);

        storage = new StorageHandler(getContext());

        //setup sensors
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);


        //register step listener
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);

        //init weight dialog
        weightDialog = new UpdateWeightDialog(getActivity());
        weightDialog.registerListener(this);

        //init views
        updateWeightButton = (Button) view.findViewById(R.id.updateWeightButton);
        weightValueTextView = (TextView) view.findViewById(R.id.weightValueTextView);
        weightGoalTextView = (TextView) view.findViewById(R.id.weightGoalTextView);
        weightProgressBar = (ProgressBar) view.findViewById(R.id.weightProgressBar);
        targetWeightTextView = (TextView) view.findViewById(R.id.targetWeightTextView);
        stepsTextView = (TextView) view.findViewById(R.id.stepCountTextView);

        //set event listeners
        updateWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialog
                weightDialog.show();
                //update weight view
                displayCurrentWeight();
            }
        });

        //update weight value
        displayCurrentWeight();


        numSteps = 0;
        updateStepsView(numSteps);

        return view;
    }

    private void updateStepsView(int steps){
        stepsTextView.setText(steps + " taken");
    }


    public void displayCurrentWeight(){
        //set weight value to last value in file
        double currentWeight = storage.getCurrentWeight();
        int targetWeight = Integer.parseInt(preferences.getString(SettingsActivity.KEY_WEIGHT_GOAL, "50"));
        boolean isMetric = preferences.getBoolean(SettingsActivity.KEY_UNITS, true);

        Log.d(MainActivity.DEBUG_TAG, "target weight: " + targetWeight);

        //display target weight
        weightGoalTextView.setText(LogDate.getFormattedWeight(targetWeight, isMetric));

        if(currentWeight >= 1){
            //display current weight
            weightValueTextView.setText(LogDate.getFormattedWeight(currentWeight, isMetric));

            double progress;

            //update progress bar
            if(currentWeight > targetWeight){
                //loose weight
                progress = (double)((double)targetWeight / (double)currentWeight) * 100;
            } else {
                //gain weight
                progress = (double)((double)currentWeight/(double)targetWeight ) * 100;
            }

            int color = Color.BLUE;

            if(progress < 25){
                //red
                color = Color.rgb(255,10,10);
            } else if(progress >= 25 && progress < 50) {
                //yorange
                color = Color.rgb(255,100,10);
            } else if(progress >= 50 && progress < 75){
                //yellow
                color = Color.rgb(200,200,10);
            } else if(progress >= 75){
                //green
                color = Color.GREEN;
            }

            Drawable progressDrawable = weightProgressBar.getProgressDrawable().mutate();
            progressDrawable.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
            weightProgressBar.setProgressDrawable(progressDrawable);

            weightProgressBar.setMax(100);
            weightProgressBar.setProgress((int)progress);

            weightGoalTextView.setText((int)progress + "% of the way to achieving your weight goal!");
            targetWeightTextView.setText(LogDate.getFormattedWeight(targetWeight, isMetric));
            
        } else {
            //no weights recorede yet, prompt for weight
            weightDialog.show();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //update weight
        displayCurrentWeight();
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        Log.d(MainActivity.DEBUG_TAG,"step taken! " + numSteps);
        updateStepsView(numSteps);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void weightChanged() {
        displayCurrentWeight();
        Log.d(MainActivity.DEBUG_TAG, "WEIGHT CHANGED");

    }
}
