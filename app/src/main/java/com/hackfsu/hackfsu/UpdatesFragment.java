package com.hackfsu.hackfsu;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;

import android.app.Fragment;
import android.app.ProgressDialog;
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
	private static final String TAG_MSG = "message";
	private static final String TAG_TIME = "time";

	JSONArray updates = null;
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

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Getting updates...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected List<ParseObject> doInBackground(String... args) {
			List<ParseObject> updates = null;

			ParseQuery<ParseObject> query = ParseQuery.getQuery("updates");
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
			pDialog.dismiss();

			if (updates != null) {
				// looping through all updates
				for (int i = 0; i < updates.size(); i++) {

					// tmp hashmap for single update
					HashMap<String, String> update = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					update.put(TAG_TITLE, updates.get(i).getString("title"));
					update.put(TAG_MSG, updates.get(i).getString("msg"));

					Format formatter = new SimpleDateFormat(
							"M/d/yy h:mm a", Locale.US);
					String s = formatter.format(updates.get(i).getCreatedAt());
					update.put(TAG_TIME, s);

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
					R.layout.updates_item, new String[] { TAG_TITLE, TAG_TIME,
							TAG_MSG }, new int[] { R.id.tvUpdateTitle, R.id.tvUpdatesTime,
							R.id.tvUpdateMsg });

			lv.setAdapter(adapter);
		}
	}
}
