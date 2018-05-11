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

public class StorageHandler {

    private Context ctx;
    private String weightFilename = "weights";
    private File path;
    private File weights;

    public StorageHandler(Context ctx){
        this.ctx = ctx;
        path = ctx.getFilesDir();
        weights = new File(path, weightFilename);
    }

    public void saveWeight(String weight){
        try {
            weights.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(weights, true);
            //add new line
            fos.write("\n".getBytes());
            //add data
            fos.write(weight.getBytes());
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> getWeights() {
        ArrayList<Integer>weightList = new ArrayList<>();

        try {

            BufferedReader reader = new BufferedReader(new FileReader(weights));

            String line = "";
            while ((line = reader.readLine()) != null) {
                Log.d("JAMIE", line);
                weightList.add(Integer.parseInt(line));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return weightList;
    }

    public int getCurrentWeight(){
        ArrayList<Integer> weights = getWeights();
        return weights.get(weights.size());
    }

}
