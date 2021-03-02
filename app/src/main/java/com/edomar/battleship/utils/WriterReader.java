package com.edomar.battleship.utils;

import android.os.Build;
import android.os.FileUtils;
import android.util.Log;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    public void deleteMatchFleets() {
        String dirPath1 = directoryPath+"/match1";
        String dirPath2 = directoryPath+"/match2";

        Log.d(TAG, "deleteMatchFleets: ");

        try {
            delete(new File(dirPath1));
            delete(new File(dirPath2));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "deleteMatchFleets: eliminazione non riuscita");
        }

    }

    public static void delete(File file)
            throws IOException {

        if (file.isDirectory()) {

            //directory is empty, then delete it
            if (file.list().length == 0) {

                file.delete();
                Log.d(TAG, "Directory is deleted : "+ file.getAbsolutePath());

            } else {

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                    Log.d(TAG,"Directory is deleted : " + file.getAbsolutePath());
                }
            }

        } else {
            //if file, then delete it
            file.delete();
            Log.d(TAG, "File is deleted : " + file.getAbsolutePath());
        }
    }

    public boolean write(List<String[]> gridRows, String directoryName,String levelName){

        File levelDir = new File(directoryPath+directoryName);
        levelDir.mkdirs();

        levelName = Utils.translateScenario(levelName);

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
