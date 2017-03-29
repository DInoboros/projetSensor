package fr.mvalier.sensortracker;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Classe permettant d'afficher un histogramme sur une smartwatch
 * @author mvalier
 */
public class MainActivityWear extends WearableActivity {

    BarChart barChart;
    ArrayList<String> dates;
    Random random;
    ArrayList<BarEntry> barEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wear);

        barChart = (BarChart) findViewById(R.id.bargraph);

        createRandomBarGraph("23/03/2017", "29/03/2017");

    }

    public void createRandomBarGraph(String Date1, String Date2){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date date1 = simpleDateFormat.parse(Date1);
            Date date2 = simpleDateFormat.parse(Date2);

            Calendar mDate1 = Calendar.getInstance();
            Calendar mDate2 = Calendar.getInstance();
            mDate1.clear();
            mDate2.clear();

            mDate1.setTime(date1);
            mDate2.setTime(date2);

            dates = new ArrayList<>();
            dates = getList(mDate1,mDate2);

            barEntries = new ArrayList<>();
            float max = 0f;
            float value = 0f;
            random = new Random();
            for(int j = 0; j< dates.size();j++){
                max = 10000f;
                value = random.nextFloat()*max;
                barEntries.add(new BarEntry(value,j));
            }

        }catch(ParseException e){
            e.printStackTrace();
        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"Pas");
        BarData barData = new BarData(dates,barDataSet);
        barChart.setData(barData);

    }

    public ArrayList<String> getList(Calendar startDate, Calendar endDate){
        ArrayList<String> list = new ArrayList<String>();
        while(startDate.compareTo(endDate)<=0){
            list.add(getDate(startDate));
            startDate.add(Calendar.DAY_OF_MONTH,1);
        }
        return list;
    }

    public String getDate(Calendar cld){
        String curDate = cld.get(Calendar.DAY_OF_MONTH) + "/" + (cld.get(Calendar.MONTH) + 1) + "/" + cld.get(Calendar.YEAR) ;
        try{
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(curDate);
            curDate =  new SimpleDateFormat("dd/MM/yyyy").format(date);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return curDate;
    }

}