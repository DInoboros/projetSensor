package fr.lenours.sensortracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OSMMapFragment.OnFragmentInteractionListener, StepGraphFragment.OnFragmentInteractionListener, SharedPreferences.OnSharedPreferenceChangeListener, StepTrackerFragment.OnFragmentInteractionListener {

    public static String PACKAGE_NAME;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private StepGraphFragment stepGraphFrag;
    private OSMMapFragment osmFrag;
    private boolean firstUse = true;
    private EditText setupText;
    private SharedPreferences sp;
    private StepTrackerFragment stepTrackerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepGraphFrag = new StepGraphFragment();
        osmFrag = new OSMMapFragment();
        stepTrackerFragment = new StepTrackerFragment();

        PACKAGE_NAME = getApplicationContext().getPackageName();
        FileData.createAppFolder();

        //Preferences listener
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);

        if (!((LocationManager) getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Location disable");
            alert.setMessage("Please enable gps and restart application");
            alert.setCancelable(false);
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            alert.show();
        } else if (firstUse) setup();



    }

    private void setupViewPager(CustomViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(osmFrag, "Map");
        adapter.addFragment(stepTrackerFragment, "Home");
        adapter.addFragment(stepGraphFrag, "Graph");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings)
            startActivity(new Intent(MainActivity.this, Settings.class));
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "interval":
                int interval = Integer.parseInt(sharedPreferences.getString("interval", "30000"));
                stepGraphFrag.setInterval(interval * 1000); //second to microsecond
                System.out.println("Interval Changed ! Interval = " + interval);
                stepGraphFrag.getHandler().removeCallbacks(stepGraphFrag.getUpdater());
                stepGraphFrag.getHandler().postDelayed(stepGraphFrag.getUpdater(), 0);
                break;
            case "removeSteps":
                if (sharedPreferences.getBoolean("removeSteps", true)) {
                    stepGraphFrag.resetGraph();
                }
                System.out.println("Remove steps ! bool = " + sharedPreferences.getBoolean("removeSteps", true));
                break;
            case "removeRoute":
                if (sharedPreferences.getBoolean("removeRoute", true)) {
                    osmFrag.resetRoute();
                }
                break;
            case "stepObjective":
                StepData.objective_step = sharedPreferences.getInt("steObkective", StepData.objective_step);
                stepTrackerFragment.updateViews();
                Log.i("main", "step objective updated");
        }
    }

    public void setup() {


        setupText = new EditText(MainActivity.this);
        setupText.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Welcome to SensorTracker !")
                .setMessage("Please set your daily step objective to start the application")
                .setCancelable(false)
                .setView(setupText)
                .setPositiveButton("ok !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (setupText.getText().toString().equals("")) {
                            dialog.cancel();
                            setup();
                            return;
                        }
                        firstUse = false;
                        StepData.objective_step = Integer.parseInt(setupText.getText().toString());
                        setupUI();
                    }
                }).show();
    }

    public void setupUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
