package com.example.jamie.stepcounter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private StorageHandler storageHandler;
    private GraphView graph;
    private Button refreshGraphButton;



    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_history, container, false);
        storageHandler = new StorageHandler(getContext());
        graph = (GraphView) view.findViewById(R.id.graph);

        drawGraph();

        refreshGraphButton = (Button) view.findViewById(R.id.refreshGraphButton);

        refreshGraphButton.setOnClickListener(refreshGraphListener);



        // Inflate the layout for this fragment
        return view;
    }

    private View.OnClickListener refreshGraphListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            drawGraph();
        }
    };

    private void drawGraph(){
        ArrayList<LogDate> logWeights = storageHandler.getWeights();

        //adddata points
        DataPoint[] dp = new DataPoint[logWeights.size()];
        Log.d(MainActivity.DEBUG_TAG, "---------------------len: " + dp.length);

        for (int i = 0; i < dp.length; i++){
            LogDate logDate = logWeights.get(i);
            dp[i] = (new DataPoint(new Date(i), logDate.getWeight()));
            Log.d(MainActivity.DEBUG_TAG, "graphing weight: " + logDate.getWeight());
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);

        graph.addSeries(series);

        Log.d(MainActivity.DEBUG_TAG,"numdatapoints: " + dp.length);

        if(dp.length > 0){

            // set date label formatter
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
            graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space

            // set manual x bounds to have nice steps
            graph.getViewport().setMinX(logWeights.get(0).getDate().getTime());
            graph.getViewport().setMaxX(logWeights.get(logWeights.size()-1).getDate().getTime());
            graph.getViewport().setXAxisBoundsManual(true);

            // as we use dates as labels, the human rounding to nice readable numbers
            // is not necessary
            graph.getGridLabelRenderer().setHumanRounding(false);

        }


//        graph.

    }


}
