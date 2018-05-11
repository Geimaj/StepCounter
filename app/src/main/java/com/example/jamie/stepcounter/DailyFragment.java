package com.example.jamie.stepcounter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.s
 */
public class DailyFragment extends Fragment {

    private Button updateWeightButton;
//    private View.OnClickListener updateWeightHandler;
    private TextView weightValueTextView;
    private StorageHandler storage;
    private StorageChanged storageChangedInterface;
    private UpdateWeightDialog weightDialog;


    public DailyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_daily, container, false);

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
        if(currentWeight >= 1){
            weightValueTextView.setText(currentWeight + "kg");
        } else {
            //no weights recorede yet, prompt for weight
            weightDialog.show();
        }
        Log.d("JAMIE", "current weight: " + storage.getCurrentWeight());
    }

}
