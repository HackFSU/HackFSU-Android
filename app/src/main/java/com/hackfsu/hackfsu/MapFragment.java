package com.hackfsu.hackfsu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;

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
    ArrayList<HashMap<String, String>> scheduleList;

    public MapFragment() {
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_map, container, false);

        scheduleList = new ArrayList<HashMap<String, String>>();

        new ParseSchedule().execute();

        //todo remove if not needed
        //initializeUI(savedInstanceState);
        //loadMap();

        return rootView;
    }private class ParseSchedule extends
            AsyncTask<String, String, List<ParseObject>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("Parse", "Trying to get maps from parse");
        }

        @Override
        protected List<ParseObject> doInBackground(String... args) {
            List<ParseObject> mapItems = null;

            // Gets Parse class from database
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Maps");
            query.orderByAscending("floor");

            try {
                mapItems = query.find();
                return mapItems;
            } catch (ParseException e) {
                e.printStackTrace();
                return mapItems;
            }

        }

        @Override
        protected void onPostExecute(List<ParseObject> mapItems) {
            super.onPostExecute(mapItems);

            if (mapItems != null) {
                Log.i("Parse", "Library maps where loaded. Found " + mapItems.size() + " items");

                //getting the layout where the maps will be placed
                LinearLayout layout = (LinearLayout)rootView.findViewById(R.id.map_view);

                Bitmap bm = null;

                // looping through all scheduleItems
                for (int i = 0; i < mapItems.size(); i++) {
                    //getting next image
                    ParseFile imageFile = mapItems.get(i).getParseFile("Image");

                    //creating params that will be used for text and image view
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);

                    //creating imageView
                    TextView text = new TextView(rootView.getContext());
                    text.setLayoutParams(params);
                    text.setGravity(Gravity.CENTER_HORIZONTAL);
                    text.setText(mapItems.get(i).getString("Name"));
                    text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

                    //creating textView
                    ParseImageView image = new ParseImageView(rootView.getContext());
                    image.setLayoutParams(params);
                    image.setParseFile(imageFile);
                    image.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                            if (e == null) {
                                Log.i("Parse", "Image data loaded");
                            } else {
                                Log.d("Parse", "Porblem loading image data");
                            }
                        }
                    });

                    //Adding both to layout
                    layout.addView(text);
                    layout.addView(image);
                    Log.i("Maps", text.getText() + " was added");
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }
}