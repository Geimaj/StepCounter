package com.example.jamie.stepcounter;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

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
    private Button drawWeightButton;
    private Button drawStepsButton;
    private TextView dateTextView;
    private TextView unitTextView;


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_history, container, false);

        storageHandler = new StorageHandler(getContext());

        graph = (LineChart) view.findViewById(R.id.graph);
        dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        unitTextView = (TextView) view.findViewById(R.id.unitTextView);
        drawWeightButton = (Button) view.findViewById(R.id.refreshGraphButton);
        drawWeightButton.setOnClickListener(refreshGraphListener);
        drawStepsButton = (Button) view.findViewById(R.id.drawStepsButton);
        drawStepsButton.setOnClickListener(drawStepsListener);

        //hide axes labels by default
        dateTextView.setVisibility(View.GONE);
        unitTextView.setVisibility(View.GONE);


        // Inflate the layout for this fragment
        return view;
    }


    private View.OnClickListener drawStepsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            drawStepsGraph();
        }
    };

    private View.OnClickListener refreshGraphListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            drawWeightGraph();
        }
    };

    private void drawStepsGraph(){
        dateTextView.setVisibility(View.VISIBLE);
        unitTextView.setVisibility(View.VISIBLE);
        unitTextView.setText("Steps");

        graph.clear();

        final ArrayList<LogStep> logSteps = storageHandler.getLogSteps();
        ArrayList<Entry> entries = new ArrayList<>();

        int i = 0;
        for(LogStep ls : logSteps){
            entries.add(new Entry(i,ls.getSteps()));
            i++;
        }
        int currentSteps = storageHandler.getStepsToday();
        int targetSteps = Integer.parseInt(DailyFragment.preferences.getString("key_steps","0"));
        float min = Math.min(targetSteps, currentSteps) - 100;
        float max = Math.max(targetSteps, currentSteps) + 100;



        //add entries to dataset
        LineDataSet dataSet = new LineDataSet(entries, "Steps");
        dataSet.setColor(Color.MAGENTA);
        dataSet.setValueTextColor(Color.BLUE);
        dataSet.setValueTextSize(12f);
        dataSet.setLineWidth(4f);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        //add target weight line
        LimitLine stepGoalLine = new LimitLine((float) targetSteps, "Target Steps");
        stepGoalLine.setLineColor(Color.RED);
        stepGoalLine.setLineWidth(2f);
        stepGoalLine.setTextColor(Color.BLACK);
        stepGoalLine.setTextSize(12f);

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
                    cal.setTime(logSteps.get(0).getDate());
                    cal.add(Calendar.DATE, -1);
                    target = cal.getTime();
                } else if(index >= logSteps.size()-1){
                    //return day after logweights[size]
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(logSteps.get(logSteps.size()-1).getDate());
                    cal.add(Calendar.DATE, 1);
                    target = cal.getTime();
                } else {
                    //return formatted date
                    target = logSteps.get(index).getDate();
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
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis y = graph.getAxisLeft();
        y.setTextSize(14f);
        //set range
        y.setAxisMinimum(min);
        y.setAxisMaximum(max);
        //add target weight
        y.removeAllLimitLines();
        y.addLimitLine(stepGoalLine);

        YAxis right = graph.getAxisRight();
        right.setDrawAxisLine(false);
        right.setDrawAxisLine(false);
        right.setDrawGridLines(false);
        right.setEnabled(false);
        Description desc = new Description();
        desc.setText("Steps activity");

//        graph.setDes
        graph.setDescription(desc);
        graph.setExtraOffsets(10,10,15,10);
        graph.invalidate();

    }

    private void drawWeightGraph(){
        dateTextView.setVisibility(View.VISIBLE);
        unitTextView.setVisibility(View.VISIBLE);

        graph.clear();
        final ArrayList<LogDate> logWeights = storageHandler.getWeights();
        List<Entry> entries = new ArrayList<Entry>();
        List<Entry> heightEntries = new ArrayList<Entry>();
        double minWeight = Double.MAX_VALUE;
        double maxWeight = Double.MIN_VALUE;
        int targetWeight = Integer.parseInt(DailyFragment.preferences.getString(SettingsActivity.KEY_WEIGHT_GOAL, "50"));

        //create entries (actual data)
        int i = 0;
        for(LogDate d : logWeights){
            if(DailyFragment.isMetric) {
                entries.add(new Entry((float) i, (float) d.getKgs()));
            } else {
                entries.add(new Entry((float) i, (float) d.getLbs()));
            }
            if(d.getKgs() > maxWeight){
                maxWeight = d.getKgs();
            }
            if(d.getKgs() < minWeight){
                minWeight = d.getKgs();
            }
            i++;
        }
        //find min between goal and min weight
        float min = (float) Math.min(minWeight,targetWeight) - 10;
        float max = (float) Math.max(targetWeight, maxWeight) + 10;

        if(!DailyFragment.isMetric){
            min *= 2.2;
            max *= 2.2;
            targetWeight *= 2.2;
            unitTextView.setText("LBS");
        } else {
            unitTextView.setText("KG");

        }

        //add entries to dataset
        LineDataSet dataSet = new LineDataSet(entries, "Weight");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLUE);
        dataSet.setValueTextSize(12f);
        dataSet.setLineWidth(4f);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        //add target weight line
        LimitLine weightGoalLine = new LimitLine((float) targetWeight, "Target Weight");
        weightGoalLine.setLineColor(Color.RED);
        weightGoalLine.setLineWidth(2f);
        weightGoalLine.setTextColor(Color.BLACK);
        weightGoalLine.setTextSize(12f);

//        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

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
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis y = graph.getAxisLeft();
        y.setTextSize(14f);
        //set range
        y.setAxisMinimum(min);
        y.setAxisMaximum(max);
        //add target weight
        y.removeAllLimitLines();
        y.addLimitLine(weightGoalLine);

        YAxis right = graph.getAxisRight();
        right.setDrawAxisLine(false);
        right.setDrawAxisLine(false);
        right.setDrawGridLines(false);
        right.setEnabled(false);
        Description desc = new Description();
        desc.setText("Weight activity");


//        graph.setDes
        graph.setDescription(desc);
        //refresh
        graph.invalidate();
    }


}

