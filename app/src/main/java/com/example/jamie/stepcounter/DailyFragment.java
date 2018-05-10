package com.example.jamie.stepcounter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.s
 */
public class DailyFragment extends Fragment {

    private Button updateWeightButton;
//    private View.OnClickListener updateWeightHandler;

    public DailyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_daily, container, false);

        //init views
        updateWeightButton = view.findViewById(R.id.updateWeightButton);

        //set event listeners
        updateWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("JAMIE", "CLICK");
                UpdateWeightDialog weightDialog = new UpdateWeightDialog(getActivity());
                weightDialog.show();
            }
        });

        return view;
    }

}
