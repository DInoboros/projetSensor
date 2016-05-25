package fr.lenours.sensortracker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import java.util.ArrayList;

public class OSMMapFragment extends Fragment implements OnUserWalkingChangeListener, LocationListener {

    private MapView osmMap;
    private PathOverlay pathOverlay;
    private View view;
    private LocationManager locManager;
    private ArrayList<IGeoPoint> points;
    private GeoPoint current_loc;
    private GeoPoint last_loc;
    private OnFragmentInteractionListener mListener;
    private ImageButton bCenter;
    private ArrayList<OverlayItem> itemList;
    private Button bMarker;
    private Button bRouteRecording;
    private Marker startMarker;
    private Marker marker;
    private String markerInfo;
    private Object locService;
    private SensorManager sensorManager;
    private WalkingDetector walkingDetector;
    private boolean isWalking = false;
    private boolean routeRecording = false;
    private boolean markerWasSet = false;
    private int color = Color.BLUE;




    public OSMMapFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OSMMapFragment newInstance(String param1, String param2) {
        OSMMapFragment fragment = new OSMMapFragment();
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
        view = inflater.inflate(R.layout.fragment_osmmap, container, false);

        locService = getActivity().getSystemService(Context.LOCATION_SERVICE);
        last_loc = Extra.currentLocation(locService, getActivity());

        //Views init
        bCenter = (ImageButton) view.findViewById(R.id.bCenter);
        bMarker = (Button) view.findViewById(R.id.bMark);
        bRouteRecording = (Button) view.findViewById(R.id.routeRecording);
        locManager = (LocationManager) locService;
        points = new ArrayList<>();
        itemList = new ArrayList<>();
        markerInfo = new String();

        initMap();

        //init StepWalkingListener
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        walkingDetector = new WalkingDetector(sensorManager);
        walkingDetector.setOnUserWalkingListener(this);


        //When current location is updated
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return view;
        }
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        //Buttons listener init
        bMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!markerWasSet)
                    updateMarkers(last_loc, false);
                markerWasSet = true;
            }
        });

        points.add(last_loc);


        bCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OSMMapFragment", "okay in onClick");
                osmMap.getController().setCenter(Extra.currentLocation(locService, getActivity()));
                osmMap.getController().zoomTo(17);
            }
        });

        bRouteRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bRouteRecording.setText(routeRecording ? "start route recording" : "stop route recording");
                routeRecording = !routeRecording;
            }
        });

        return view;
    }

    //Improve accuracy of the route
    private boolean hasMoved() {
        return (Math.abs(current_loc.getLongitude() - last_loc.getLongitude()) > 0.00001) && (Math.abs(current_loc.getLatitude() - last_loc.getLatitude()) > 0.00001);
    }

    public void initMap() {
        osmMap = (MapView) view.findViewById(R.id.osmMap);
        osmMap.setClickable(true);
        osmMap.setTileSource(TileSourceFactory.MAPQUESTOSM);
        osmMap.getController().setZoom(17);
        pathOverlay = new PathOverlay(Color.BLUE, getActivity());
        osmMap.setUseDataConnection(true);
        osmMap.setMultiTouchControls(true);
        updateCurrentLocation(last_loc);
        Log.i("OSMMapFragment", "Map successfully initialized!");
    }


    public void updateCurrentLocation(GeoPoint point) {
        markerWasSet = false;
        osmMap.getOverlays().remove(startMarker);
        osmMap.getController().setCenter(point);
        startMarker = new Marker(osmMap);
        startMarker.setPosition(point);
        markerInfo = "Vous avez fait " + StepData.day_step + " pas jusqu'ici, " + Extra.stepToMeter(StepData.day_step) + "m";
        startMarker.setSubDescription(markerInfo);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setIcon(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.loc48));
        osmMap.getOverlays().add(startMarker);
    }

    public void resetRoute() {
        points = new ArrayList<>();
        osmMap.getOverlays().remove(pathOverlay);
        osmMap.getOverlays().remove(itemList);
        osmMap.invalidate();
        Log.i("OSMMapFragment", "Resetting the route.. Success!");
    }


    public void updatePath(ArrayList<IGeoPoint> points, int color) {
        pathOverlay = new PathOverlay(color, getActivity());
        Paint paint = pathOverlay.getPaint();
        paint.setStrokeWidth(8);
        pathOverlay.addPoints(points);
        osmMap.getOverlays().add(pathOverlay);
        Log.i("OSMMapFragment", "Updating path.. Success !");
    }

    public void updateRoute(GeoPoint point) {
        points.add(point);
        updatePath(this.points, color);
        Log.i("OSMMapFragment", "Updating the route with point : (" + point.getLatitude() + "," + point.getLongitude() + ") .. Success!");
        CsvReader.writeCSV(FileData.ROUTE_FILE, Extra.decimalFormat(point.getLatitude(), 6) + "," + Extra.decimalFormat(point.getLongitude(), 6) + "," + System.currentTimeMillis());
        osmMap.invalidate();
    }

    public void updateMarkers(GeoPoint markerPoint, boolean isRestoring) {

        marker = new Marker(osmMap);
        marker.setPosition(markerPoint);
        marker.setSubDescription(markerInfo);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.loc48marker));
        osmMap.getOverlays().add(marker);
        Log.i("OSMMapFragment", "Updating the markers with point : (" + markerPoint.getLatitude() + "," + markerPoint.getLongitude() + ") .. Success!");
        osmMap.invalidate();

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
    public void onLocationChanged(Location location) {
        current_loc = new GeoPoint(location.getLatitude(), location.getLongitude());
        if (current_loc == null || !hasMoved() || location.getSpeed() <= 0.75) return;
        Log.d("OSMMapFragment", "x = " + location.getLatitude() + " y = " + location.getLongitude());
        if (routeRecording) updateRoute(current_loc); //Update the map
        updateCurrentLocation(current_loc); //Set item marker
        last_loc = new GeoPoint(current_loc.getLatitude(), current_loc.getLongitude());
        osmMap.invalidate(); //Refresh the map
        Log.i("OSMMapFragment", "speed : " + location.getSpeed() + "");
        CsvReader.writeCSV(FileData.newFile("speed.csv"), String.valueOf(location.getSpeed()));
    }

    @Override
    public void OnUserWalkingChange(boolean isWalking) {
        if (isWalking && !this.isWalking) {
            color = Color.GREEN;
        } else if (!isWalking && this.isWalking) {
            color = Color.BLUE;
        }
        Log.i("OSMMapfragment", isWalking?"Walking !":"Not Walking");
        points = new ArrayList<>();
        this.isWalking = isWalking;
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
