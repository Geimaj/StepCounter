package com.example.jamie.stepcounter;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogDate {

    private double kgs;
    private Date date;
    public static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public LogDate(){

    }
    public LogDate(String weight){
        this.kgs = Double.parseDouble(weight);
        this.date = new Date();
    }

    public LogDate(String weight, boolean isMetric){
        this.kgs = Double.parseDouble(weight);
        this.date = new Date();

        if(!isMetric){this.kgs = getKgs(this.kgs);}

    }

    public LogDate(double weight, boolean isMetric){
        this.kgs = weight;
        this.date = new Date();

        if(!isMetric){this.kgs = getKgs(weight);}

    }

    public LogDate(double weight, Date date){
        this.kgs = weight;
        this.date = date;

    }

    public LogDate(double weight, String date){
        this.kgs = weight;
        try {
            this.date = dateFormat.parse(date);
        } catch (ParseException e) {
            Log.d("JAMIE","Invalid date");
            e.printStackTrace();
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getWeight() {
        return kgs;
    }

    public void setWeight(int weight) {
        this.kgs = weight;
    }

    @Override
    public String toString() {
        return kgs + "," + dateFormat.format(date);
    }

    public double getKgs() {
        return kgs;
    }

    public double getKgs(double lbs) {
        return lbs / 2.2046226218488;
    }

    public double getLbs() {
        return kgs * 2.2046226218488;
    }

    public static double getLbs(double pKgs) {
        return pKgs * 2.2046226218488;
    }

    public static String getFormattedWeight(double kgs, boolean isMetric){
        String units = "kg";
        double weight = kgs;

        if(!isMetric){
            units = "lbs";
            weight = getLbs(kgs);
        }

        return Math.round(weight) + " " + units;
    }


}
