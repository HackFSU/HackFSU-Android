package com.hackfsu.hackfsu15;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;

public class UpdatesFragment extends Fragment {
	public UpdatesFragment() {
	}

	private static final String TAG_TITLE = "title";
    private static final String TAG_SUBTITLE = "subtitle";
	private static final String TAG_TIME = "time";
    private static final String timezone = "America/New_York";

	ArrayList<HashMap<String, String>> updatesList;

	View rootView;
	ListView lv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.fragment_updates, container, false);

		updatesList = new ArrayList<HashMap<String, String>>();

		new ParseUpdates().execute();

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		PushService.subscribe(getActivity(), "updates", MainSplashActivity.class);
		lv = (ListView) getActivity().findViewById(R.id.updates_list);

	}

	private class ParseUpdates extends
			AsyncTask<String, String, List<ParseObject>> {

        //Shows Loading Dialog.
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<ParseObject> doInBackground(String... args) {
			List<ParseObject> updates = null;

			ParseQuery<ParseObject> query = ParseQuery.getQuery("Updates");
			query.orderByDescending("createdAt");
			
			try {
				updates = query.find();
				return updates;
			} catch (ParseException e) {
				e.printStackTrace();
				return updates;
			}

		}

		@Override
		protected void onPostExecute(List<ParseObject> updates) {
			super.onPostExecute(updates);

			if (updates != null) {
				// looping through all updates
				for (int i = 0; i < updates.size(); i++) {

					// tmp hashmap for single update
					HashMap<String, String> update = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					update.put(TAG_TITLE, updates.get(i).getString("title"));
                    update.put(TAG_SUBTITLE, updates.get(i).getString("subtitle"));
					//update.put(TAG_MSG, updates.get(i).getString("msg"));

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(updates.get(i).getCreatedAt());
                    calendar.setTimeZone(TimeZone.getTimeZone(timezone));


                    String AMPM = "PM";
                    if (calendar.get(Calendar.AM_PM) == 0){
                        AMPM = "AM";
                    }
                    String d = String.format("%d/%d/%d  %d:%02d ",calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.HOUR),
                            calendar.get(Calendar.MINUTE)) + AMPM;

					update.put(TAG_TIME, d);

					// adding contact to contact list
					updatesList.add(update);
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}


			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new SimpleAdapter(getActivity(), updatesList,
					R.layout.updates_item,
                    new String[] { TAG_TITLE, TAG_SUBTITLE, TAG_TIME },
                    new int[] { R.id.tvUpdateTitle, R.id.tvUpdateSubtitle, R.id.tvUpdatesTime });

			lv.setAdapter(adapter);
		}
	}
}
