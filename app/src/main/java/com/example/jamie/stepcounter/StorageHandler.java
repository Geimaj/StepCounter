package com.example.jamie.stepcounter;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StorageHandler {

    private Context ctx;
    private String weightFilename = "weights";
    private File path;
    private File weightFile;

    public StorageHandler(Context ctx){
        this.ctx = ctx;
        path = ctx.getFilesDir();
        weightFile = new File(path, weightFilename);

//        weightFile.getParentFile().mkdirs();

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
