package fr.lenours.sensortracker;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dobi.walkingsynth.accelerometer.AccelerometerDetector;
import com.dobi.walkingsynth.accelerometer.OnStepCountChangeListener;


public class StepTrackerFragment extends Fragment implements OnStepCountChangeListener {

    private View view;
    private TextView dailyStepView;
    private AccelerometerDetector accelDetector;
    private ProgressBar pBar;
    private Vibrator vibrator;

    private OnFragmentInteractionListener mListener;

    public StepTrackerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static StepTrackerFragment newInstance(String param1, String param2) {
        StepTrackerFragment fragment = new StepTrackerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_step_tracker, container, false);

        dailyStepView = (TextView) view.findViewById(R.id.dailyStepView);
        pBar = (ProgressBar) view.findViewById(R.id.progressBar);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        pBar.setMax(StepData.objective_step);

        //Setup Listener
        accelDetector = new AccelerometerDetector((SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE));
        accelDetector.setStepCountChangeListener(this);
        accelDetector.startDetector();

        updateViews();
        return view;
    }

    public void updateViews() {
        dailyStepView.setText("Daily objective : " + StepData.day_step + "/" + StepData.objective_step);
        if (StepData.day_step == StepData.objective_step) {
            vibrator.vibrate(500);
            new AlertDialog.Builder(getActivity()).setMessage("You reach " + StepData.objective_step + " steps for this fay, congratulations !").show();
            reset();
        }
    }

    private void reset() {
        pBar.setProgress(0);
        StepData.day_step = 0;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        updateViews();
        pBar.setProgress(StepData.day_step);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
