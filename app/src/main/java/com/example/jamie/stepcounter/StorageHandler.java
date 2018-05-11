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
import java.util.ArrayList;

public class StorageHandler {

    private Context ctx;
    private String weightFilename = "weights";
    private File path;
    private File weightFile;

    public StorageHandler(Context ctx){
        this.ctx = ctx;
        path = ctx.getFilesDir();
        weightFile = new File(path, weightFilename);
    }

    public void saveWeight(String weight){
        try {
            weightFile.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(weightFile, true);
            //add data
            fos.write(weight.getBytes());
            //add new line
            fos.write("\n".getBytes());
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> getWeights() {
        ArrayList<Integer>weightList = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(weightFile));
            String line;

            while ((line = br.readLine()) != null) {
                Log.d("JAMIE", "LINE: " + line);
                int weight = Integer.parseInt(line);
                weightList.add(weight);
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e){
            Log.d("JAMIE","CANT CONVERT");
            e.printStackTrace();
        }

        return weightList;
    }

    public int getCurrentWeight(){
        ArrayList<Integer> weights = getWeights();
        if(weights.size() >= 1){
            return weights.get(weights.size()-1);
        } else {
            return -1; //no weights yet
        }
    }

}
