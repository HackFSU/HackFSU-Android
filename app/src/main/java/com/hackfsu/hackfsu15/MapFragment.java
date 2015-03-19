package com.hackfsu.hackfsu15;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class MapFragment extends Fragment {

    private View rootView;

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        //new ParseSchedule().execute();

        return rootView;
    }

    private class ParseSchedule extends
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

                // looping through all scheduleItems
                for (int i = 0; i < mapItems.size(); i++) {
                    //getting next image
                    ParseFile imageFile = mapItems.get(i).getParseFile("Image");

                    layout.setPadding(0, 0, 0, 50);

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
                                Log.d("Parse", "Problem loading image data");
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