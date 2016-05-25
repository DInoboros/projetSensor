package fr.lenours.sensortracker;

import android.app.Activity;
import android.graphics.Color;

import org.osmdroid.views.overlay.PathOverlay;

import java.util.ArrayList;

/**
 * Created by Clemsbrowning on 25/05/2016.
 */
public class StepRoutes {

    private Activity activity;
    private ArrayList<PathOverlay> routes;
    public static int noWalkingColor = Color.BLUE;
    public static int walkingColor = Color.GREEN;

    public StepRoutes(Activity activity) {

        routes = new ArrayList<>();
        this.activity = activity;
    }

    public void addPath(PathOverlay path) {

    }


}
