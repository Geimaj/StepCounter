package com.example.jamie.stepcounter;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogDate {

    private int weight;
    private Date date;
    public static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public LogDate(){

    }
    public LogDate(String weight){
        this.weight = Integer.parseInt(weight);
        this.date = new Date();
    }

    public LogDate(int weight){
        this.weight = weight;
        this.date = new Date();
    }

    public LogDate(int weight, Date date){
        this.weight = weight;
        this.date = date;
    }

    public LogDate(int weight, String date){
        this.weight = weight;
        try {
            this.date = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("JAMIE","Invalid date");
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return weight + "," + dateFormat.format(date);
    }
}
