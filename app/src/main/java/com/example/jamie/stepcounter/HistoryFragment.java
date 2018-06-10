package com.example.jamie.stepcounter;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private StorageHandler storageHandler;
    private LineChart graph;
    private Button refreshGraphButton;



    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_history, container, false);

        storageHandler = DailyFragment.storage;//new StorageHandler(getContext());

        graph = (LineChart) view.findViewById(R.id.graph);

//        drawGraph();

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
        graph.clear();
        final ArrayList<LogDate> logWeights = storageHandler.getWeights();
        List<Entry> entries = new ArrayList<Entry>();
        List<Entry> heightEntries = new ArrayList<Entry>();

        //create entries (actual data)
        int i = 0;
        for(LogDate d : logWeights){
            Log.d(MainActivity.DEBUG_TAG, "I: " + i);
            entries.add(new Entry((float) i, (float) d.getKgs()));
            i++;
        }

        //add height entries
        

        //add entries to dataset
        LineDataSet dataSet = new LineDataSet(entries, "Weight");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLUE);
        dataSet.setValueTextSize(12f);
        dataSet.setLineWidth(4f);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        //add target weight line
        int targetWeight = Integer.parseInt(DailyFragment.preferences.getString(SettingsActivity.KEY_WEIGHT_GOAL, "50"));
        LimitLine weightGoalLine = new LimitLine((float) targetWeight, "Target Weight");
        weightGoalLine.setLineColor(Color.RED);
        weightGoalLine.setLineWidth(2f);
        weightGoalLine.setTextColor(Color.BLACK);
        weightGoalLine.setTextSize(12f);

        //add datasets to LineData
        LineData data = new LineData(dataSet);
        //set data
        graph.setData(data);

        //format data

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM");
                int index = (int) value;
                Date target;

                Log.d(MainActivity.DEBUG_TAG, "INDEX: " + index);

                if(index < 0){
                    //return day before logweights[0]
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(logWeights.get(0).getDate());
                    cal.add(Calendar.DATE, -1);
                    target = cal.getTime();
                } else if(index >= logWeights.size()-1){
                    //return day after logweights[size]
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(logWeights.get(logWeights.size()-1).getDate());
                    cal.add(Calendar.DATE, 1);
                    target = cal.getTime();
                } else {
                    //return formatted date
                    target = logWeights.get(index).getDate();
                }
                String result = formatter.format(target);
                Log.d(MainActivity.DEBUG_TAG, "RESULT: " + result);
                return formatter.format(target);
//                return new DateFormatSymbols().getMonths()[(int)value];
            }

        };

        //format x axis labels
        XAxis xAxis = graph.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        xAxis.setTextSize(14f);

        YAxis y = graph.getAxisLeft();
        y.setTextSize(14f);
        y.setAxisMinimum(20f);
        y.setAxisMaximum(100f);

        //add target weight
        y.addLimitLine(weightGoalLine);

        YAxis right = graph.getAxisRight();
        right.setDrawAxisLine(false);
        right.setDrawAxisLine(false);
        right.setDrawGridLines(false);
        right.setEnabled(false);

        //refresh
        graph.invalidate();
    }


}

