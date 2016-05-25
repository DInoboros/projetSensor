package fr.lenours.sensortracker;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clemsbrowning on 09/05/2016.
 */
public class CsvReader {

    public static List<String[]> readCSV(File file, String separator) {

        if (!file.exists())
            return null;

        List<String[]> result = new ArrayList<>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] sLine = line.split(separator);
                result.add(sLine);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void writeCSV(File file, String text) {

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
            fos.write((text + "\n").getBytes());
       //     Log.i("csvWriting", "Succesfully write \"" + text + "\" to " + file.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("csvWriting", "File " + file.getPath() + " not found");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("csvWriting", "Can't write bytes to " + file.getPath());
        }
    }

}
