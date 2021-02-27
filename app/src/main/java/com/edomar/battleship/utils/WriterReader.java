package com.edomar.battleship.utils;

import android.util.Log;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WriterReader {

    private static final String TAG = "WriterReader";

    private static WriterReader sInstance;
    private static String directoryPath;

    private WriterReader(){
        directoryPath = "/data/data/com.edomar.battleship/";
    }

    public static WriterReader getInstance(){
        if(sInstance == null){
            sInstance = new WriterReader();
        }
        return sInstance;
    }

    public boolean write(List<String[]> gridRows, String directoryName,String levelName){

        File levelDir = new File(directoryPath+directoryName);
        levelDir.mkdirs();

        String filePath = directoryPath+directoryName+"/"+levelName+".csv";
        Log.d(TAG, "write: filePath "+ filePath);



        File file = new File(filePath);

        try{

            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile, CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

            writer.writeAll(gridRows);
            writer.close();

            Log.d(TAG, "write: complete");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "write: problem");
            return false;
        }

    }



    public List<String[]> readFleet(String directoryName,String levelName) {
        List<String[]> gridRows = new ArrayList<>();

        String filePath = directoryPath+directoryName+"/"+levelName+".csv";
        Log.d(TAG, "read: filePath "+ filePath);

        File file = new File(filePath);

        try{
            CSVReader csvReader = new CSVReader(new BufferedReader(
                    new FileReader(file)));

            gridRows = csvReader.readAll();

            csvReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gridRows;
    }
}
