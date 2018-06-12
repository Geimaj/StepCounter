package com.example.jamie.stepcounter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import static com.example.jamie.stepcounter.DailyFragment.preferences;

public class PromptDialog extends Dialog implements SharedPreferences.OnSharedPreferenceChangeListener{

    protected Button confirmButton;
    protected Button cancelButton;
    protected TextInputEditText input;
    protected TextView errorMessageTextView;
    protected TextView promptTextView;
    protected StorageHandler sh;
    protected StorageChanged storageChangedInterface;
    protected String weightUnit;
    protected String distanceUnit;
    protected boolean isMetric;


    public PromptDialog(@NonNull Context context) {
        super(context);
        weightUnit = "kg";
        distanceUnit = "m";
        isMetric = true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_prompt_dialog);

        confirmButton = (Button) findViewById(R.id.btnConfirm);
        cancelButton = (Button) findViewById(R.id.cancelWeightButton);
        input = (TextInputEditText)findViewById(R.id.inputEditText);
        errorMessageTextView = (TextView) findViewById(R.id.errorTextView);
        promptTextView = (TextView) findViewById(R.id.promptTextView);
        sh = new StorageHandler(getContext());

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        preferences.registerOnSharedPreferenceChangeListener(this);

        errorMessageTextView.setVisibility(View.GONE);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear input
                input.setText("");
                dismiss();
            }
        });

    }

    public void registerListener(StorageChanged sc){
        this.storageChangedInterface = sc;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        isMetric = preferences.getBoolean(SettingsActivity.KEY_UNITS, true);
    }
}
