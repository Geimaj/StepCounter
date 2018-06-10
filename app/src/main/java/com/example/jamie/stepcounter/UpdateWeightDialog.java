package com.example.jamie.stepcounter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;


public class UpdateWeightDialog extends Dialog implements SharedPreferences.OnSharedPreferenceChangeListener{

    private Button confirmWeightButton;
    private Button cancelButton;
    private TextInputEditText weightInput;
    private TextView errorMessageTextView;
    private TextView weightPromptTextView;
    private Activity actvity;
    private StorageChanged storageChangedInterface;
    private StorageHandler sh;
    private SharedPreferences preferences;
    private String units;
    private String prompt;
//    OnDia

    public UpdateWeightDialog(Activity a) {
        super(a);
        this.actvity = a;
    }

    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_update_weight);

        confirmWeightButton = (Button) findViewById(R.id.confirmWeightButton);
        cancelButton = (Button) findViewById(R.id.cancelWeightButton);
        weightInput = (TextInputEditText)findViewById(R.id.weightInput);
        errorMessageTextView = (TextView) findViewById(R.id.errorMessageTextView);
        weightPromptTextView = (TextView) findViewById(R.id.weightPromptTextView);
        sh = new StorageHandler(getContext());
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        preferences.registerOnSharedPreferenceChangeListener(this);

        prompt = "Enter your weight in";

        //set unit label
        updateUnitLabel();

        //hide error message by default
        errorMessageTextView.setVisibility(View.GONE);

        //set text changed listener
        weightInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //validate input
                String age = s.toString();
                if(validWeight(age)){
                    errorMessageTextView.setVisibility(View.GONE);
                    confirmWeightButton.setEnabled(true);

                } else {
                    errorMessageTextView.setText("Invalid weight");
                    errorMessageTextView.setVisibility(View.VISIBLE);
                    confirmWeightButton.setEnabled(false);
                }
            }
        });

        confirmWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weight = weightInput.getText().toString();
                //check valid weight
                if(validWeight(weight)){
                    boolean isMetric = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(SettingsActivity.KEY_UNITS, true);

                    //apped weight to file
                    sh.logWeight(weight, isMetric);

                    //callback
                    storageChangedInterface.weightChanged();
                    //clear input
                    weightInput.setText("");
                    dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear input
                weightInput.setText("");
                dismiss();
            }
        });

    }

    public void registerListener(StorageChanged sc){
        this.storageChangedInterface = sc;
    }

    private boolean validWeight(String weight){
        boolean result = false;
        if(weight.length() > 1){ //check atleast double digit
            //check valid number
            try{
                int w = Integer.parseInt(weight);
                if(w > 10){ //weight must be greater than 10
                    result = true;
                }
            }catch (Exception e){
                result = false;
            }
        }
        return result;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateUnitLabel();
        Log.d(MainActivity.DEBUG_TAG, "update units");
    }

    private void updateUnitLabel(){
        String unitPrompt;

        if(preferences.getBoolean(SettingsActivity.KEY_UNITS, true)){
            units = "kg";
        } else {
            units = "lbs";
        }

        unitPrompt = prompt + " " + units + ":";
        weightPromptTextView.setText(unitPrompt);
    }

}
