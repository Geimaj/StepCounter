package com.example.jamie.stepcounter;


import android.content.SharedPreferences;
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


/**
 * A simple {@link Fragment} subclass.s
 */
public class DailyFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private Button updateWeightButton;
//    private View.OnClickListener updateWeightHandler;
    private TextView weightValueTextView;
    private StorageHandler storage;
    private StorageChanged storageChangedInterface;
    private UpdateWeightDialog weightDialog;
    private SharedPreferences preferences;
    private TextView weightGoalTextView;
    private TextView targetWeightTextView;

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

        storageChangedInterface = new StorageChanged() {
            @Override
            public void weightChanged() {
                displayCurrentWeight();
            }
        };

        weightDialog = new UpdateWeightDialog(getActivity());
        weightDialog.setInterface(storageChangedInterface);

        //init views
        updateWeightButton = (Button) view.findViewById(R.id.updateWeightButton);
        weightValueTextView = (TextView) view.findViewById(R.id.weightValueTextView);
        weightGoalTextView = (TextView) view.findViewById(R.id.weightGoalTextView);
        weightProgressBar = (ProgressBar) view.findViewById(R.id.weightProgressBar);
        targetWeightTextView = (TextView) view.findViewById(R.id.targetWeightTextView);

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

        return view;
    }

    public void displayCurrentWeight(){
        //set weight value to last value in file
        int currentWeight = storage.getCurrentWeight();
        int targetWeight = Integer.parseInt(preferences.getString(SettingsActivity.KEY_WEIGHT_GOAL, "50"));

        //display target weight
        weightGoalTextView.setText("Target Weight: " + getFormattedWeight(targetWeight));

        if(currentWeight >= 1){
            //display current weight
            weightValueTextView.setText(getFormattedWeight(currentWeight));

            double progress;

            //update progress bar
            if(currentWeight > targetWeight){
                //loose weight
                progress = (double)((double)targetWeight / (double)currentWeight) * 100;
            } else {
                //gain weight
                progress = (double)((double)currentWeight/(double)targetWeight ) * 100;
            }


            weightProgressBar.setMax(100);
            weightProgressBar.setProgress((int)progress);

            weightGoalTextView.setText((int)progress + "% of the way to achieving your weight goal!");
            targetWeightTextView.setText("Target weight: " + getFormattedWeight(targetWeight));
            
        } else {
            //no weights recorede yet, prompt for weight
            weightDialog.show();
        }
        Log.d("JAMIE", "current weight: " + storage.getCurrentWeight());

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //update weight
        Log.d("JAMIE","preference changed");
        displayCurrentWeight();
    }

    public String getFormattedWeight(int kgs){
        boolean isMetric = preferences.getBoolean(SettingsActivity.KEY_UNITS, true);
        String units = "kg";

        if(!isMetric){
            units = "lbs";
            double weight = (int) kgs * 2.2046226218488;
            kgs = (int) weight;
        }

        return kgs + " " + units;
    }

}
