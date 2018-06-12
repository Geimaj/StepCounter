package com.example.jamie.stepcounter;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class StorageHandler {

    private Context ctx;
    private String weightFilename = "weights";
    private String stepFilename = "steps";
    private File path;
    private File weightFile;
    private File stepFile;

    public StorageHandler(Context ctx){
        this.ctx = ctx;
        path = ctx.getFilesDir();
        weightFile = new File(path, weightFilename);
        stepFile = new File(path,stepFilename);
//        weightFile.getParentFile().mkdirs();
        try {
            weightFile.createNewFile();
            stepFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logStep(){
        ArrayList<LogStep> allSteps = getLogSteps();
        LogStep currentSteps = null;
        Date today = new Date();

        //make sure theres entries and the last one is from today
        if(allSteps.size() > 0 && allSteps.get(allSteps.size()-1).getDate().getDate() == today.getDate()){
            Log.d(MainActivity.DEBUG_TAG, "Entries for today! INC");
            allSteps.get(allSteps.size()-1).step();
        } else { //no entries for today, add one
            allSteps.add(new LogStep(0,today));
            Log.d(MainActivity.DEBUG_TAG,"NO STEPS TODAY YET");
        }

        //save current steps
        try {
            FileOutputStream fos = new FileOutputStream(stepFile, false);
            for(LogStep ls : allSteps){
                String line = ls.toString() + "\n";
                fos.write(line.getBytes());
            }
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public LogStep getStepsForDate(Date date){
        for(LogStep step : getLogSteps()){
            if(step.getDate().getDate() == (date.getDate())){
                return  step;
            }
        }

        return null;
    }

    public int getStepsToday(){
        Date today = new Date();
        return getStepsForDate(today) == null ? 0 : getStepsForDate(today).getSteps();
    }

    public LogStep getLastSteps(){
        //return last element from file
        return getLogSteps().size() >= 1 ? getLogSteps().get(getLogSteps().size()-1) : null;
    }

    public ArrayList<LogStep> getLogSteps(){
        ArrayList<LogStep> steps = new ArrayList<>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(stepFile));
            String line;

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                int step = Integer.parseInt(tokens[0]);
                Date date = new Date(tokens[1]);
                LogStep ls = new LogStep(step,date);
                steps.add(ls);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return steps;
    }

    public void logWeight(String weight, boolean isMetric){
        try {
            weightFile.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(weightFile, true);

            LogDate ld = new LogDate(weight, isMetric);

            String line = ld.toString();
            //add data
            fos.write(line.getBytes());
            //add new line
            fos.write("\n".getBytes());
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LogDate> getWeights() {
        ArrayList<LogDate>weightList = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(weightFile));
            String line;

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                double weight = Double.parseDouble(tokens[0]);
                String date = (tokens[1]);
                LogDate ld = new LogDate(weight,date);
                weightList.add(ld);

                Log.d(MainActivity.DEBUG_TAG, ld.toString());
            }
            br.close();
            Log.d(MainActivity.DEBUG_TAG, "file over");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e){
            Log.d(MainActivity.DEBUG_TAG,"CANT CONVERT DATA FROM WEIGHT FILE TO DOUBLE");
            e.printStackTrace();
        } catch(ArrayIndexOutOfBoundsException e){
            Log.d(MainActivity.DEBUG_TAG,"No dates in file...");
            e.printStackTrace();
        }

        return weightList;
    }

    public double getCurrentWeight(){
        ArrayList<LogDate> weights = getWeights();
        if(weights.size() >= 1){
            return weights.get(weights.size()-1).getWeight();
        } else {
            return -1; //no weights yet
        }
    }

}
