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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.LineChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Interface Du graphe
 */
public class StepGraphFragment2 extends Fragment {

    public static String TAG = "StepGraphFragment2";
    private View view;
    private LinearLayout chartLyt;
    private int valeurMax = 1000 ;
    private String[] modifications = {"Couleur", "Donnée"};
    private String[] couleurs = {"Rouge", "Bleu","Noir","Vert","Gris","Jaune"};
    private Integer[] intCouleurs = {Color.RED,Color.BLUE,Color.BLACK,Color.GREEN,Color.GRAY,Color.YELLOW};
    private Integer[] tabCouleurs = {Color.RED,Color.BLUE,Color.BLACK,Color.GREEN,Color.GRAY,Color.YELLOW};
    private String[] donnees = {"Pas", "Distance","Calories"};
    private String[] donneesList = {"Pas","Distance","Calories","Pas","Pas","Pas"};
    private String[] courbes = {"1","2","3","4","5","6"};
    private ArrayList courbesBis = new ArrayList<>();
    private ArrayList<String> types = new ArrayList<>();
    private Spinner nbCourbes;
    private Spinner numCourbe;
    private Spinner modification;
    private Spinner couleurSpinner;
    private Spinner donneeSpinner;
    private Button valider ;
    private Integer couleurChoisie ;
    private String donneeChoisie ;
    private Integer nombreCourbes ;
    private Integer numeroCourbes ;
    XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();



    private OnFragmentInteractionListener mListener;

    public StepGraphFragment2() {
        // Required empty public constructor
    }


    public static StepGraphFragment2 newInstance() {
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

        nombreCourbes=2;
        couleurChoisie=Color.RED;
        donneeChoisie="Pas";
        view = inflater.inflate(R.layout.fragment_step_graph_fragment2, container, false);
        valider =(Button) view.findViewById(R.id.valider);
        chartLyt = (LinearLayout) view.findViewById(R.id.chart);
        setupGraph();
        chartLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "X = " + v.getX() + " Y = " + v.getY());
            }
        });
        nbCourbes = (Spinner) view.findViewById(R.id.nbCourbe);
        numCourbe = (Spinner) view.findViewById(R.id.numCourbe);
        modification = (Spinner) view.findViewById(R.id.modification);
        couleurSpinner = (Spinner) view.findViewById(R.id.couleur);
        donneeSpinner = (Spinner) view.findViewById(R.id.donnee);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,courbes);
        nbCourbes.setAdapter(adapter);
        nbCourbes.setSelection(1);

        // Iterator<String> iterator = courbesBis.iterator();
        for(String courbe : courbes) {
            courbesBis.add(courbe);
        }
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,courbes);
        numCourbe.setAdapter(adapter);
        numCourbe.setSelection(0);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,couleurs);
        couleurSpinner.setAdapter(adapter);
        couleurSpinner.setSelection(0);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,donnees);
        donneeSpinner.setAdapter(adapter);
        donneeSpinner.setSelection(0);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,modifications);
        modification.setAdapter(adapter);
        modification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        couleurSpinner.setVisibility(view.VISIBLE);
                        donneeSpinner.setVisibility(view.INVISIBLE);
                        break;
                    case 1:
                        couleurSpinner.setVisibility(view.INVISIBLE);
                        donneeSpinner.setVisibility(view.VISIBLE);
                        break;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        nbCourbes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                courbesBis.clear();
                for (int j = 0 ; j < position+1 ; j++){
                    courbesBis.add(j+1);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,courbesBis);
                numCourbe.setAdapter(adapter);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombreCourbes = Integer.parseInt(nbCourbes.getSelectedItem().toString());
                numeroCourbes =numCourbe.getSelectedItemPosition();
                String choixModification = modification.getSelectedItem().toString();

                if (choixModification == "Couleur") {
                    couleurChoisie = new Integer(intCouleurs[couleurSpinner.getSelectedItemPosition()]);
                    tabCouleurs[numeroCourbes] = couleurChoisie;
                }
                else if (choixModification == "Donnée"){
                    donneeChoisie = donnees[donneeSpinner.getSelectedItemPosition()];

                    donneesList[numeroCourbes]=donneeChoisie;
                }
                chartLyt.removeAllViews();
                setupGraph();
            }

        });


        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, courbes);
        nbCourbes.setAdapter(adapter);
        nbCourbes.setSelection(1);

       // Iterator<String> iterator = courbesBis.iterator();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,courbes);
        numCourbe.setAdapter(adapter);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,modifications);
        modification.setAdapter(adapter);


        return view;
    }

    protected void setupGraph() {

        multiRenderer = new XYMultipleSeriesRenderer();
        dataset = new XYMultipleSeriesDataset();
        types.clear();
        // Creating an XYSeries for Income
        /*Color color ;
        try {
            Field field = Class.forName("java.awt.Color").getField(couleurChoisie);
            color = (Color)field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            color = null;
        }*/

        // Creating a XYMultipleSeriesRenderer to customize the whole chart

        // multiRenderer
        //   .setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        //multiRenderer.setXLabels(0);
        multiRenderer.setChartTitle("Nombre de pas et distance parcourue quotidienne");
        multiRenderer.setLabelsColor(Color.BLACK);
        multiRenderer.setXTitle("Jour");
        multiRenderer.setYTitle("Nombre de pas");
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setYLabelsColor(0, Color.BLACK);


/***
 * Customizing graphs
 */
        // setting text size of the title
        multiRenderer.setChartTitleTextSize(28);
        // setting text size of the axis title
        multiRenderer.setAxisTitleTextSize(30);
        // setting text size of the graph lable
        multiRenderer.setLabelsTextSize(30);
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
        multiRenderer.setLegendTextSize(30);
        // setting x axis label align
        multiRenderer.setXLabelsAlign(Paint.Align.CENTER);
        // setting y axis label to align
        multiRenderer.setYLabelsAlign(Paint.Align.LEFT);
        // setting text style
        multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
        // setting no of values to display in y axis
        //multiRenderer.setYLabels(10);
        // setting y axis max value, Since i'm using static values inside the
        // graph so i'm setting y max value to 4000.
        // if you use dynamic values then get the max y value and set here
        multiRenderer.setYAxisMax(valeurMax + 1000);
        multiRenderer.setYAxisMin(0);
        // setting used to move the graph on xaxiz to .5 to the right
        //multiRenderer.setXAxisMin(-0.5);
        // setting max values to be display in x axis
        //multiRenderer.setXAxisMax(10);
        // setting bar size or space between two bars
        multiRenderer.setBarSpacing(0.5);
        // Setting background color of the graph to transparent
        multiRenderer.setBackgroundColor(Color.WHITE);

        multiRenderer.setApplyBackgroundColor(true);

        // setting the margin size for the graph in the order top, left, bottom,
        // right
        multiRenderer.setMargins(new int[]{30, 30, 30, 30});
        multiRenderer.setMarginsColor(Color.rgb(66, 145, 241));


        for (int i = 0 ;i<nombreCourbes;i++) {
            TimeSeries courbeSeries = new TimeSeries(donneeChoisie.toString());
            List<String[]> stepsDataSet = CsvReader.readCSV(FileData.TOTAL_STEP, ",");
            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
            Date d;
            Calendar cal = Calendar.getInstance();

            if (stepsDataSet != null)
                for (String[] dayStep : stepsDataSet) {
                    try {

                        d = f.parse(dayStep[0]);
                        cal.setTime(d);
                        if (Integer.parseInt(dayStep[1]) > valeurMax) {
                            valeurMax = Integer.parseInt(dayStep[1]);
                        }
                        if (donneesList[i]== donnees[0]){
                            courbeSeries.add(cal.get(Calendar.DAY_OF_MONTH), Integer.parseInt(dayStep[1]));
                        }
                        else if (donneesList[i]==donnees[1]){
                            courbeSeries.add(cal.get(Calendar.DAY_OF_MONTH), StepTrackerFragment2.stepToMeter(Integer.parseInt(dayStep[1])));
                        }
                        else if (donneesList[i]==donnees[2]){
                            courbeSeries.add(cal.get(Calendar.DAY_OF_MONTH), StepTrackerFragment2.stepToCalories(Integer.parseInt(dayStep[1])));
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            // Adding data to Income and Expense Series

            // Creating a dataset to hold each series

            // Adding Income Series to the dataset
            dataset.addSeries(courbeSeries);

            // Creating XYSeriesRenderer to customize courbeSeries
            XYSeriesRenderer courbeRenderer = new XYSeriesRenderer();
            courbeRenderer.setColor(tabCouleurs[i]); // color of the graph set to cyan
            courbeRenderer.setFillPoints(true);
            courbeRenderer.setLineWidth(7);
            courbeRenderer.setChartValuesTextSize(50);
            courbeRenderer.setDisplayChartValues(true); // setting chart value
            // distance


            /** for (int i = 0; i < x.length; i++) {
             multiRenderer.addXTextLabel(i, mMonth[i]);
             }**/

            // Adding courbeRenderer and expenseRenderer to multipleRenderer
            // Note: The order of adding dataseries to dataset and renderers to
            // multipleRenderer
            // should be same

            multiRenderer.addSeriesRenderer(courbeRenderer);
            types.add(LineChart.TYPE);
        }

        String[] typesList = new String[nombreCourbes];
        typesList = types.toArray(typesList);
        // this part is used to display graph on the xml
        //LinearLayout chartContainer = (LinearLayout) view.findViewById(R.id.viewpager);
        // remove any views before u paint the chart
        //if (chartContainer != null) {
        //    chartContainer.removeAllViews();
        //}
        // drawing bar chart
        // mChart = ChartFactory.getBarChartView(MainActivity.this, dataset,
        // multiRenderer, Type.DEFAULT);

        // Creating a combined chart with the chart types specified in types
        // array
        GraphicalView mChart = ChartFactory.getCombinedXYChartView(getActivity(), dataset,
                multiRenderer, typesList);

        // adding the view to the linearlayout

        //chartLyt.postInvalidate();
        chartLyt.addView(mChart);


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
