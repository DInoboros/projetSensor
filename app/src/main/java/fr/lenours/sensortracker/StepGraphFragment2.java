package fr.lenours.sensortracker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Interface Du graphe
 */
public class StepGraphFragment2 extends Fragment {

    private String[] mMonth = new String[]{"Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static String TAG = "StepGraphFragment2";
    private View view;
    private LinearLayout chartLyt;

    private OnFragmentInteractionListener mListener;

    public StepGraphFragment2() {
        // Required empty public constructor
    }


    public static StepGraphFragment2 newInstance(String param1, String param2) {
        StepGraphFragment2 fragment = new StepGraphFragment2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // stepGraphFragment3.setContentView(R.layout.activity_main);
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

    protected void setupGraph() {

        System.out.println("test ICCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCIIIIIIIIIIIIIII");
        int[] x = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        int[] income = {1000, 1500, 1700, 2500, 2800, 3500, 3000, 3500, 3700, 3800,
                2500, 4000};
        int[] expense = {1200, 1400, 1000, 1800, 2600, 3200, 3300, 3400, 3300, 3900,
                2500, 3500};

        // Creating an XYSeries for Income
        XYSeries incomeSeries = new XYSeries("Income");
        // Creating an XYSeries for Expense
        XYSeries expenseSeries = new XYSeries("Expense");
        // Adding data to Income and Expense Series
        for (int i = 0; i < x.length; i++) {
            incomeSeries.add(i, income[i]);
            expenseSeries.add(i, expense[i]);
        }

        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Income Series to the dataset
        dataset.addSeries(incomeSeries);
        // Adding Expense Series to dataset
        dataset.addSeries(expenseSeries);

        // Creating XYSeriesRenderer to customize incomeSeries
        XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
        incomeRenderer.setColor(Color.RED); // color of the graph set to cyan
        incomeRenderer.setFillPoints(true);
        incomeRenderer.setLineWidth(2);
        incomeRenderer.setDisplayChartValues(true);
        incomeRenderer.setDisplayChartValues(true); // setting chart value
        // distance

        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
        expenseRenderer.setColor(Color.GREEN);
        expenseRenderer.setFillPoints(true);
        expenseRenderer.setLineWidth(2);
        expenseRenderer.setDisplayChartValues(true);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer
                .setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        multiRenderer.setXLabels(0);
        multiRenderer.setChartTitle("Income vs Expense Chart");
        multiRenderer.setXTitle("Year 2014");
        multiRenderer.setYTitle("Amount in Dollars");

/***
 * Customizing graphs
 */
        // setting text size of the title
        multiRenderer.setChartTitleTextSize(28);
        // setting text size of the axis title
        multiRenderer.setAxisTitleTextSize(24);
        // setting text size of the graph lable
        multiRenderer.setLabelsTextSize(24);
        // setting zoom buttons visiblity
        multiRenderer.setZoomButtonsVisible(false);
        // setting pan enablity which uses graph to move on both axis
        multiRenderer.setPanEnabled(false, false);
        // setting click false on graph
        multiRenderer.setClickEnabled(false);
        // setting zoom to false on both axis
        multiRenderer.setZoomEnabled(false, false);
        // setting lines to display on y axis
        multiRenderer.setShowGridY(false);
        // setting lines to display on x axis
        multiRenderer.setShowGridX(false);
        // setting legend to fit the screen size
        multiRenderer.setFitLegend(true);
        // setting displaying line on grid
        multiRenderer.setShowGrid(false);
        // setting zoom to false
        multiRenderer.setZoomEnabled(false);
        // setting external zoom functions to false
        multiRenderer.setExternalZoomEnabled(false);
        // setting displaying lines on graph to be formatted(like using
        // graphics)
        multiRenderer.setAntialiasing(true);
        // setting to in scroll to false
        multiRenderer.setInScroll(false);
        // setting to set legend height of the graph
        multiRenderer.setLegendHeight(30);
        // setting x axis label align
        multiRenderer.setXLabelsAlign(Paint.Align.CENTER);
        // setting y axis label to align
        multiRenderer.setYLabelsAlign(Paint.Align.LEFT);
        // setting text style
        multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
        // setting no of values to display in y axis
        multiRenderer.setYLabels(10);
        // setting y axis max value, Since i'm using static values inside the
        // graph so i'm setting y max value to 4000.
        // if you use dynamic values then get the max y value and set here
        multiRenderer.setYAxisMax(10000);
        // setting used to move the graph on xaxiz to .5 to the right
        multiRenderer.setXAxisMin(-0.5);
        // setting max values to be display in x axis
        multiRenderer.setXAxisMax(11);
        // setting bar size or space between two bars
        multiRenderer.setBarSpacing(0.2);
        // Setting background color of the graph to transparent
        multiRenderer.setBackgroundColor(Color.BLUE);

        multiRenderer.setApplyBackgroundColor(true);

        // setting the margin size for the graph in the order top, left, bottom,
        // right
        multiRenderer.setMargins(new int[]{30, 30, 30, 30});

        for (int i = 0; i < x.length; i++) {
            multiRenderer.addXTextLabel(i, mMonth[i]);
        }

        // Adding incomeRenderer and expenseRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to
        // multipleRenderer
        // should be same
        multiRenderer.addSeriesRenderer(incomeRenderer);
        multiRenderer.addSeriesRenderer(expenseRenderer);

        // this part is used to display graph on the xml
        //LinearLayout chartContainer = (LinearLayout) view.findViewById(R.id.viewpager);
        // remove any views before u paint the chart
        //if (chartContainer != null) {
        //    chartContainer.removeAllViews();
        //}
        // drawing bar chart
        // mChart = ChartFactory.getBarChartView(MainActivity.this, dataset,
        // multiRenderer, Type.DEFAULT);
        String[] types = new String[]{LineChart.TYPE, BarChart.TYPE};

        // Creating a combined chart with the chart types specified in types
        // array
        GraphicalView mChart = ChartFactory.getCombinedXYChartView(getActivity(), dataset,
                multiRenderer, types);
        // adding the view to the linearlayout
        if (chartLyt == null){
            System.out.println("T'a fait de la merde gars");
        }
        else {
            chartLyt.addView(mChart);
        }

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */



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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
