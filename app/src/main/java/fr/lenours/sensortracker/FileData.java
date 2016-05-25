package fr.lenours.sensortracker;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Clemsbrowning on 19/05/2016.
 */
public class FileData {

    public static final String folder = Environment.getExternalStorageDirectory() + "/android/data/" + MainActivity.PACKAGE_NAME + "/";
    public static final File STEP_FILE = new File(folder + "step_count.csv");
    public static final File ROUTE_FILE = new File(folder + "route.csv");
    public static final File MARKERS_FILE = new File(folder + "markers.csv");

    public static void checkFile(File file) {
        if (!file.exists()) {
            try {
                Log.d("path", file.getPath());
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createAppFolder() {
        new File(folder).mkdirs();
    }

    public static File newFile(String name) {
        return new File(folder + name);
    }
}
