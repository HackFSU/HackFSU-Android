package com.hackfsu.hackfsu;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.AttributedCharacterIterator;

/**
 * Loads up a Google Map into the fragment and displays locations relevant to
 * the hackathon.
 *
 * @author Trevor TODO: Once venue figured out, change coordinates to match
 */
public class MapFragment extends Fragment {

    // Set up for map
    private static final double[] coordsVenue = { 30.445, -84.2999 };
    private static final double[] coordsMainParking = { 30.444661, -84.29885 };
    private static final double[] coordsMapCenter = { 30.445208, -84.299856 };
    private static final int mapZoom = 17;

    private View rootView;
    private MapView mv;
    private GoogleMap map;

    public MapFragment() {
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_map, container, false);

        //todo remove if not needed
        //initializeUI(savedInstanceState);
        //loadMap();

        return rootView;
    }
    /*

    @Override
    public void onResume() {
        super.onResume();
        mv.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mv.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mv.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mv.onLowMemory();
    }

    private void initializeUI(Bundle savedInstanceState) {
        mv = (MapView) rootView.findViewById(R.id.mapView);
        mv.onCreate(savedInstanceState);
        map = mv.getMap();
    }

    private void loadMap() {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a new element here for each marker to set up
        LatLng markerVenue = new LatLng(coordsVenue[0], coordsVenue[1]);
        LatLng markerMainParking = new LatLng(coordsMainParking[0],
                coordsMainParking[1]);

        map.addMarker(new MarkerOptions().position(markerVenue)
                .title("Dirac Science Library")
                .snippet("Hackathon will take place here"));
        map.addMarker(new MarkerOptions()
                .position(markerMainParking)
                .title("Woodward Parking Garage")
                .snippet(
                        "Main parking for the hackathon. Free to park for anyone on weekends."));

        MapsInitializer.initialize(this.getActivity());

        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(
                coordsMapCenter[0], coordsMapCenter[1]), mapZoom);
        map.animateCamera(cu);
    }*/

}