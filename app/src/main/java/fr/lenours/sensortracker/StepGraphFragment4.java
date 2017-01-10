package fr.lenours.sensortracker;


import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by potte on 10/01/2017.
 */

public class StepGraphFragment4 extends Fragment {
    public static String TAG = "StepGraphFragment4";
    private View view;
    private LinearLayout chartLyt;

    private OnFragmentInteractionListener mListener;

    public StepGraphFragment4() {
        // Required empty public constructor
    }


    public static StepGraphFragment4 newInstance(String param1, String param2) {
        StepGraphFragment4 fragment = new StepGraphFragment4();
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
        view = inflater.inflate(R.layout.fragment_step_graph_fragment2, container, false);

        chartLyt = (LinearLayout) view.findViewById(R.id.chart);

        setupGraph();
        chartLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "X = " + v.getX() + " Y = " + v.getY());
            }
        });
        return view;
    }

    private void setupGraph() {

        FileData.addConfigElement("isGraphSet",true);
        TimeSeries series = new TimeSeries("Nombre de pas");

        List<String[]> stepsDataSet = CsvReader.readCSV(FileImportation.selected, ",");

        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        Date d;
        if (stepsDataSet != null)
            for (String[] dayStep : stepsDataSet) {
                try {
                    d = f.parse(dayStep[0]);
                    series.add(d, Integer.parseInt(dayStep[1]));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);

        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(7);
        renderer.setColor(Color.rgb(66, 145, 241));
        renderer.setPointStyle(PointStyle.SQUARE);

        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setLabelsTextSize(20);
        mRenderer.addSeriesRenderer(renderer);
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
        mRenderer.setPanEnabled(false, false);
        mRenderer.setYAxisMax(10000);
        mRenderer.setYAxisMin(0);

        GraphicalView chartView = ChartFactory.getTimeChartView(getActivity(), dataset, mRenderer, "dd/MMM/yyyy");
        chartLyt.addView(chartView);

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
        if (context instanceof StepGraphFragment4.OnFragmentInteractionListener) {
            mListener = (StepGraphFragment4.OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
