package fr.lenours.sensortracker;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dobi.walkingsynth.accelerometer.AccelerometerDetector;
import com.dobi.walkingsynth.accelerometer.OnStepCountChangeListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepGraphFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepGraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepGraphFragment extends Fragment implements OnStepCountChangeListener, OnDataPointTapListener, OnStepAlarmRingListener {


    private OnFragmentInteractionListener mListener;
    private View view;
    private GraphView stepGraph;
    private LineGraphSeries<DataPoint> stepSerie;
    private Runnable updater;
    private Handler handler;
    private SensorManager sensorManager;
    private AccelerometerDetector accelDetector;
    private TextView textView;
    private int xCurrentVal = 1;
    private boolean firstLaucnh = true;
    private int interval = 36000000;
    private long launchTime = 0;
    private boolean isWalking = false;
    private Handler handler2;
    private Runnable runnable;
    private WalkingDetector walkingDetector;


    public StepGraphFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static StepGraphFragment newInstance(String param1, String param2) {
        StepGraphFragment fragment = new StepGraphFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LineGraphSeries<DataPoint> getStepSerie() {
        return stepSerie;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Runnable getUpdater() {
        return updater;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_step_graph, container, false);


        stepSerie = new LineGraphSeries<>();
        stepSerie.setOnDataPointTapListener(this);
        stepGraph = (GraphView) view.findViewById(R.id.graph);
        textView = (TextView) view.findViewById(R.id.textView6);
        if (firstLaucnh) {
            sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            accelDetector = new AccelerometerDetector(sensorManager);
            accelDetector.setStepCountChangeListener(this);
            accelDetector.startDetector();

            /*StepService service = new StepService();
            StepAlarm alarm = new StepAlarm();
            alarm.setOnStepAlarmRingListener(this);
            service.setAlarm(alarm);
            getActivity().startService(new Intent(getActivity(), service.getClass()));*/
        }

        stepGraph.addSeries(stepSerie);
        stepGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        stepGraph.getGridLabelRenderer().setNumHorizontalLabels(3);
        stepGraph.getViewport().setScalable(true);
        stepGraph.getViewport().setScrollable(true);
        stepGraph.getViewport().scrollToEnd();
        stepGraph.getViewport().setMinX(1);
        stepGraph.getViewport().setMaxX(10);
        stepGraph.getViewport().setMaxY(1000);
        stepGraph.getViewport().setYAxisBoundsManual(true);

        stepGraph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);

        handler = new Handler();
        updater = new Runnable() {
            @Override
            public void run() {
                stepSerie.appendData(new DataPoint(xCurrentVal, StepData.day_step), true, (int) Double.POSITIVE_INFINITY);
                handler.postDelayed(updater, interval);
                CsvReader.writeCSV(FileData.STEP_FILE, xCurrentVal + "," + StepData.day_step);
                xCurrentVal++;
                StepData.day_step = 0;
            }
        };

        handler.postDelayed(updater, 0);
        firstLaucnh = false;

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStepCountChange(SensorEvent event) {
        StepData.day_step++;
        textView.setText("Steps : " + StepData.day_step);
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i("on", "onPause");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("on", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (FileData.STEP_FILE.exists()) {
            Log.i("on", "onResume");
            List<String[]> vals = CsvReader.readCSV(FileData.STEP_FILE, ",");
            xCurrentVal = vals.size();
            getStepSerie().resetData(Extra.listToDataPoint(vals));
        }
    }

    public Handler getHandler() {
        return handler;
    }

    public void refresh() {
        xCurrentVal = (int) stepSerie.getHighestValueX() + 1;
    }

    public void resetGraph() {
        DataPoint[] dp = {new DataPoint(0, 0)};
        getStepSerie().resetData(dp);
        xCurrentVal = 0;
    }

    @Override
    public void onTap(Series series, DataPointInterface dataPoint) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Compteur de pas")
                .setMessage("Vous avez fait " + (int) dataPoint.getY() + " pas, le " + (int) dataPoint.getX())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    @Override
    public void onStepAlarmRing() {
        Toast.makeText(getActivity(), "This is an alarm test", Toast.LENGTH_SHORT).show();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
