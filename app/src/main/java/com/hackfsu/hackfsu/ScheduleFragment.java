/**
 * Handles Schedule List. Retrieves list from parse, then displays it.
 * 
 * @author Jared
 */

package com.hackfsu.hackfsu;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONArray;

import android.support.v4.app.Fragment;
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
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;

public class ScheduleFragment extends Fragment {
	public ScheduleFragment() {
	}

	private static final String TAG_TITLE = "title";
    private static final String TAG_SUBTITLE = "subtitle";
	private static final String TAG_EVENT_TIME = "eventTime";
    private static final String TAG_EVENT_END_TIME = "eventEndTime";
    private static final String TAG_EVENT_DATE = "eventDate";
    private static final String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday"};
    private static final String timezone = "America/New_York";

	JSONArray updates = null;
	ArrayList<HashMap<String, String>> scheduleList;

	View rootView;
	ListView lv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_schedule, container,
				false);

		scheduleList = new ArrayList<HashMap<String, String>>();

		new ParseSchedule().execute();

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		PushService.subscribe(getActivity(), "Schedule", MainSplashActivity.class);
		lv = (ListView) getActivity().findViewById(R.id.schedule_list);

	}

	private class ParseSchedule extends
			AsyncTask<String, String, List<ParseObject>> {

		private ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<ParseObject> doInBackground(String... args) {
			List<ParseObject> scheduleItems = null;

			// Gets Parse class from database
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
			query.orderByAscending("startTime");

			try {
				scheduleItems = query.find();
				return scheduleItems;
			} catch (ParseException e) {
				e.printStackTrace();
				return scheduleItems;
			}

		}

		@Override
		protected void onPostExecute(List<ParseObject> scheduleItems) {
			super.onPostExecute(scheduleItems);

			if (scheduleItems != null) {
				// looping through all scheduleItems
				for (int i = 0; i < scheduleItems.size(); i++) {

					// tmp hashmap for single update
					HashMap<String, String> item = new HashMap<String, String>();

					// adding each child node to HashMap key => value

					// FOR TITLE
					item.put(TAG_TITLE, scheduleItems.get(i).getString("title"));

                    // For Subtitle
					item.put(TAG_SUBTITLE, scheduleItems.get(i).getString("subtitle"));

					// FOR EVENT TIME
                    Calendar start = Calendar.getInstance();
                    start.setTime(scheduleItems.get(i).getDate("startTime"));
                    start.setTimeZone(TimeZone.getTimeZone(timezone));
                    String start_AMPM = "PM";
                    if (start.get(Calendar.AM_PM) == 0){
                        start_AMPM = "AM";
                    }
                    String s = String.format("%d:%02d ", start.get(Calendar.HOUR),
                            start.get(Calendar.MINUTE)) + start_AMPM;
					item.put(TAG_EVENT_TIME, s);

                    // FOR EVENT END TIME
                    Calendar end = Calendar.getInstance();
                    end.setTime(scheduleItems.get(i).getDate("endTime"));
                    end.setTimeZone(TimeZone.getTimeZone(timezone));
                    String end_AMPM = "PM";
                    if (end.get(Calendar.AM_PM) == 0){
                        end_AMPM = "AM";
                    }
                    if (start.before(end)) {
                        s = String.format("%d:%02d ", end.get(Calendar.HOUR),
                                end.get(Calendar.MINUTE)) + end_AMPM;
                    } else {
                        s = "";
                    }
                    item.put(TAG_EVENT_END_TIME, s);

                    // FOR EVENT DATE
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(scheduleItems.get(i).getDate("startTime"));
                    calendar.setTimeZone(TimeZone.getTimeZone(timezone));
                    String d = days[calendar.get(Calendar.DAY_OF_WEEK) - 1] + " " +
                            calendar.get(Calendar.MONTH) + "/" +
                            calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                            calendar.get(Calendar.YEAR);
                    item.put(TAG_EVENT_DATE, d);

                    // adding contact to contact list
					scheduleList.add(item);
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new SimpleAdapter(ScheduleFragment.this.getActivity(),
					scheduleList, R.layout.schedule_item,
                    new String[] { TAG_TITLE, TAG_SUBTITLE, TAG_EVENT_TIME,
                            TAG_EVENT_END_TIME, TAG_EVENT_DATE},
                    new int[] { R.id.tvSchedTitle,R.id.tvSchedSubtitle,
                                R.id.tvSchedEventTime, R.id.tvSchedEventEndTime,
                                R.id.tvSchedDate});

			lv.setAdapter(adapter);
		}
	}
}
