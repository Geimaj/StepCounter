package com.example.jamie.stepcounter;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class UpdateWeightDialog extends Dialog {

    private Activity activity;
    private Button confirmWeightButton;
    private Button cancelButton;
    private TextInputEditText weightInput;
    private TextView errorMessageTextView;

    public UpdateWeightDialog(Activity a) {
        super(a);
        this.activity = a;
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
                } else {
                    errorMessageTextView.setText("Invalid weight");
                    errorMessageTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        confirmWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weight = weightInput.getText().toString();
                //check valid weight
                if(validWeight(weight)){
                    Log.d("JAMIE",weight);
                    dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

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

}