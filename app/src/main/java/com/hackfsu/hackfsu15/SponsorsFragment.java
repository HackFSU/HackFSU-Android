package com.hackfsu.hackfsu15;


import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class SponsorsFragment extends Fragment {
	View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.fragment_sponsors, container, false);

        new ParseSchedule().execute();

        return rootView;
    }

    private class ParseSchedule extends
            AsyncTask<String, String, List<ParseObject>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("Parse", "Trying to get sponsors from parse");
        }

        @Override
        protected List<ParseObject> doInBackground(String... args) {
            List<ParseObject> SponsorItems = null;

            // Gets Parse class from database
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Sponsors");
            query.orderByAscending("rank");

            try {
                SponsorItems = query.find();
                return SponsorItems;
            } catch (ParseException e) {
                e.printStackTrace();
                return SponsorItems;
            }

        }

        @Override
        protected void onPostExecute(List<ParseObject> SponsorItems) {
            super.onPostExecute(SponsorItems);

            if (SponsorItems != null) {
                Log.i("Parse", "Sponsor images where loaded. Found " +
                        SponsorItems.size() + " items");

                //getting the layout where the maps will be placed
                LinearLayout layout = (LinearLayout)rootView.findViewById(R.id.sponsor_layout);

                //getting total number of tiers
                int numTiers = 0;
                numTiers = SponsorItems.get(0).getInt("tier");
                for (int j = 1; j < SponsorItems.size(); j++){
                    if (SponsorItems.get(j).getInt("tier") > numTiers){
                        numTiers = SponsorItems.get(j).getInt("tier");
                    }
                }
                LinearLayout[] tierLayouts = new LinearLayout[numTiers];
                for (int n = 0; n < numTiers; n++){
                    tierLayouts[n] = new LinearLayout(rootView.getContext());
                    tierLayouts[n].setOrientation(LinearLayout.VERTICAL);

                }

                // looping through all SponsorItems
                for (int i = 0; i < SponsorItems.size(); i++) {
                    //getting next image
                    ParseFile imageFile = SponsorItems.get(i).getParseFile("image");

                    //creating params that will be used for text and image view
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);


                    //creating textView
                    ParseImageView image = new ParseImageView(rootView.getContext());
                    image.setLayoutParams(params);
                    image.setPadding(0,0,0,100);
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

                    tierLayouts[SponsorItems.get(i).getInt("tier") - 1].addView(image);
                }

                for (int z = numTiers - 1; z >= 0; z--){
                    layout.addView(tierLayouts[z]);

                    //TextView test = new TextView(rootView.getContext());
                    //test.setText("--------------------------------------");
                    //layout.addView(test);
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }
}
