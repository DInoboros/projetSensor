package fr.lenours.sensortracker;

//import android.graphics.Color;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Clemsbrowning on 26/04/2016.
 */
public class Extra {

    public static final String NETWORK_OFFLINE = "Network is unavailable";

    public static Double decimalFormat(double value, int digitsAfterDot) {
        return Double.parseDouble(String.format("%." + Integer.toString(digitsAfterDot) + "f", value).replace(",", "."));
    }


    public static GeoPoint currentLocation(Object systemService, Activity activity) {
        LocationManager locManager = (LocationManager) systemService;
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return null;
        }
        Location location = locManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if (location == null)
            return null;
        return new GeoPoint(location.getLatitude(), location.getLongitude());
    }

    public static String getCityFromPoint(Object systemService, Activity activity, GeoPoint point) {
        LocationManager locManager = (LocationManager) systemService;
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return "CANNOT READ LOCATION TRY AGAIN";
        }
        Geocoder info = new Geocoder(activity, Locale.getDefault());
        List<Address> address = null;
        try {
            address = info.getFromLocation(point.getLatitude(), point.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (address == null) {
            return NETWORK_OFFLINE;
        }
        return address.get(0).getAddressLine(0) + " " + address.get(0).getPostalCode() + " " + address.get(0).getLocality();
    }


    public static ArrayList<String> getValsFromSerie(LineGraphSeries serie) {
        Iterator iterator = serie.getValues(serie.getLowestValueX(), serie.getHighestValueX());
        ArrayList<String> values = new ArrayList<>();
        int cpt = 0;
        while (iterator.hasNext()) {
            DataPoint dp = (DataPoint) iterator.next();
            values.add(dp.getX() + "," + dp.getY());
            cpt++;
        }
        return values;
    }

    public static String arrToString(ArrayList<String> array) {

        String str = new String();

        for (int i = 0; i < array.size(); i++) {
            str += array.get(i).toString() + "\n";
        }
        return str;
    }

    public static DataPoint[] listToDataPoint(List<String[]> list) {
        DataPoint[] values = new DataPoint[list.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = new DataPoint(Double.parseDouble(list.get(i)[0]), Double.parseDouble(list.get(i)[1]));
        }
        return values;
    }

    public static File createFileFromInputStream(InputStream inputStream, String path) {

        try {
            File f = new File(path);
            f.createNewFile();
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        } catch (IOException e) {
            System.out.println("ERROR !!!!!" + e.toString());
        }

        return null;
    }

    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        Intent i = manager.getLaunchIntentForPackage(packageName);
        if (i == null) {
            return false;
            //throw new PackageManager.NameNotFoundException();
        }
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        context.startActivity(i);
        return true;
    }

    public static boolean isPackageInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return false;
        }
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static int stepToMeter(int step) {
        return (int) (step * 0.762);
    }


    /*public static XYPlot lightPlot(XYPlot plot) {
        plot.getGraphWidget().setSize(new SizeMetrics(0, SizeLayoutType.FILL, 0, SizeLayoutType.FILL));
        plot.getGraphWidget().setBackgroundPaint(new Paint());
        plot.getGraphWidget().setGridBackgroundPaint(new Paint());
        plot.getGraphWidget().getBackgroundPaint().setColor(Color.WHITE);
        plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
        plot.getGraphWidget().getDomainLabelPaint().setColor(Color.BLACK);
        plot.getGraphWidget().getRangeLabelPaint().setColor(Color.BLACK);
        plot.getGraphWidget().getDomainOriginLabelPaint().setColor(Color.BLACK);
        plot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
        plot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);
        return plot;
    }

    public static XYPlot darkPlot(XYPlot plot) {
        plot.getGraphWidget().setSize(new SizeMetrics(0, SizeLayoutType.FILL, 0, SizeLayoutType.FILL));
        plot.getGraphWidget().setBackgroundPaint(new Paint());
        plot.getGraphWidget().setGridBackgroundPaint(new Paint());
        plot.getGraphWidget().getBackgroundPaint().setColor(Color.BLACK);
        plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.BLACK);
        plot.getGraphWidget().getDomainLabelPaint().setColor(Color.WHITE);
        plot.getGraphWidget().getRangeLabelPaint().setColor(Color.WHITE);
        plot.getGraphWidget().getDomainOriginLabelPaint().setColor(Color.WHITE);
        plot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.WHITE);
        plot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.WHITE);
        return plot;
    }*/
}