package fr.lenours.sensortracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Classe qui gère les fichieir des l'application
 * Created by Clemsbrowning on 19/05/2016.
 */
public class FileData {

    public static final String folder = Environment.getExternalStorageDirectory() + "/Android/data/" + MainActivity.PACKAGE_NAME + "/";
    public static final File STEP_FILE = newFile("step_count.csv");
    public static final File ROUTE_FILE = newFile("route.csv");
    public static final File MARKERS_FILE = newFile("markers");
    public static final File CONFIG_FILE = newFile("config.json");
    public static final File CURRENT_DAY = newFile("current_day.csv");
    public static  File TOTAL_STEP = newFile("steps.csv");

    public static JSONObject configFile = new JSONObject();

    public FileData() {
        new File(folder + "tracker/").mkdirs();
        try {
            STEP_FILE.createNewFile();
            MARKERS_FILE.createNewFile();
            CONFIG_FILE.createNewFile();
            CURRENT_DAY.createNewFile();
            TOTAL_STEP.createNewFile();
            CsvReader.writeCSV(FileData.TOTAL_STEP,"0,0,0",false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * create new file in the application folder
     */
    public static void createAppFolder() {
        new File(folder).mkdirs();
    }

    public static File newFile(String name) {
        return new File(folder + name);
    }

    /**
     *
     * @param file
     * @return text of the file
     */
    public static String getText(File file) {
        if (!file.exists())
            return null;

        String result = new String();

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                result += line;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * add an element to the configuration file
     * @param key key of the element
     * @param obj value of the element
     */
    public static void addConfigElement(String key, Object obj) {
        try {
            if (configFile.has(key))
                configFile.remove(key);
            configFile.put(key, obj);
            CsvReader.writeCSV(CONFIG_FILE, configFile.toString(), false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * chekc if confguration file exist
     */
    public static void checkJSON() {
        if (FileData.CONFIG_FILE.exists()) {
            try {
                FileData.configFile = new JSONObject(FileData.getText(FileData.CONFIG_FILE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public static void setTotalStep(File f) {
        TOTAL_STEP = f;
    }



}
