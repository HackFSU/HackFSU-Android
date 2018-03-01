package com.hackfsu.android.api;

import android.app.Activity;
import android.util.Log;


import com.hackfsu.android.api.model.CountdownModel;
import com.hackfsu.android.api.model.UpdateModel;
import com.hackfsu.android.api.model.BaseModel;
import com.hackfsu.android.api.model.MapModel;
import com.hackfsu.android.api.model.SponsorModel;
import com.hackfsu.android.api.model.ScheduleModel;
import com.hackfsu.android.api.util.ISO8601;
import com.hackfsu.android.api.util.ISO8601Updates;
import com.hackfsu.android.api.util.NetworkClient;
import com.hackfsu.android.app.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class API {

    private NetworkClient networkClient = new NetworkClient();

    public final static String API_HOST = BuildConfig.API_HOST;

    private final static String ROUTE_HACKATHON = "https://2017.hackfsu.com" + "/api/hackathon/get";
    private final static String ROUTE_UPDATES = "/updates";
    private final static String ROUTE_SCHEDULE = "/schedule_items";
    private final static String ROUTE_MAPS = "/maps";
    private final static String ROUTE_COUNTDOWN = "/countdowns";
    private final static String ROUTE_SPONSORS = "/sponsors";


    Activity mActivity;

    public API(Activity mActivity) {
        this.mActivity = mActivity;
    }



    public void getUpdates(final APICallback<UpdateModel> callback) {

        /* Sample JSON response:
            {
              "updates": [
                {
                  "content": "Make sure you're registered.",
                  "title": "10 Days Until HackFSU",
                  "submit_time": "2017-02-08T23:58:05.965403+00:00"
                }
              ]
            }
        */

        networkClient.get(ROUTE_HACKATHON + ROUTE_UPDATES,
            new NetworkClient.NetworkCallback() {

                @Override
                public void onComplete(String json) {

                    JSONObject response;
                    JSONArray updateJSON;
                    List<UpdateModel> announcements = new ArrayList<UpdateModel>();

                    // Catch the exception if (for some reason) we don't get JSON
                    try {

                        response = new JSONObject(json);
                        updateJSON = response.getJSONArray("updates");

                        for (int i = 0; i < updateJSON.length(); i++) {

                            try {

                                JSONObject temp = updateJSON.optJSONObject(i);
                                if (temp != null) {

                                    // Use a backup time.
                                    Calendar time = Calendar.getInstance();
                                    try {
                                        time = ISO8601Updates.toCalendar(
                                                temp.getString("submit_time")
                                        );
                                    } catch (Exception e) {
                                        Log.e("getUpdates()", e.getLocalizedMessage());
                                    }

                                    UpdateModel tempAnnouncement = new UpdateModel(
                                            temp.getString("title"),
                                            temp.getString("content"),
                                            time);

                                    announcements.add(tempAnnouncement);

                                }

                            } catch (Exception e) {
                                Log.e("getUpdates(2)", e.getLocalizedMessage());
                            }
                        }

                        //callback.onDataReady(announcements);
                        performCallback(callback, announcements);


                    } catch (JSONException e) {
                        // handle the exception - send error back to the server?
                        Log.e("getUpdates()", e.getLocalizedMessage());
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("getUpdates()", e.getLocalizedMessage());
                }

            });
    }

    public void getMaps(final APICallback<MapModel> callback) {

        /* Example JSON response
            {
              "maps": [
                {
                  "link": "https://www.lib.fsu.edu/sites/default/files/pictures/dirac3.jpg",
                  "title": "3rd Floor",
                  "order": 2
                }
              ]
            }
        */

        networkClient.get(ROUTE_HACKATHON + ROUTE_MAPS,
            new NetworkClient.NetworkCallback() {

                @Override
                public void onComplete(String json) {

                    JSONObject response;
                    JSONArray mapJSON;
                    List<MapModel> maps = new ArrayList<MapModel>();

                    // Catch the exception if (for some reason) we don't get JSON
                    try {

                        response = new JSONObject(json);
                        mapJSON = response.getJSONArray("maps");

                        // Make sure we get the right JSON

                        for (int i = 0; i < mapJSON.length(); i++) {
                            JSONObject temp = mapJSON.optJSONObject(i);
                            try {

                                // Pull data members from JSON into UpdateModel to add to List
                                MapModel tempMap = new MapModel(
                                    temp.getString("title"),
                                    temp.getString("link"),
                                    temp.getInt("order")
                                );
                                maps.add(tempMap);


                            } catch (Exception e) {
                                Log.e("getMaps()", e.getLocalizedMessage());
                            }


                        }


                        Collections.sort(maps, new Comparator<MapModel>() {
                            @Override
                            public int compare(MapModel o1, MapModel o2) {
                                return Integer.compare(o1.getOrdering(), o2.getOrdering());
                            }
                        });

                        performCallback(callback, maps);

                    } catch (JSONException e) {
                        // handle the exception - send error back to the server?
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
    }

    public void getSchedule(final APICallback<ScheduleModel> callback) {

        /* JSON Format Sample
            {
              "schedule_items": [
                {
                  "description": "HCB 101",
                  "name": "Check-In, Dinner",
                  "type": 0,
                  "start": "2017-02-18T02:00:00+00:00",
                  "end": "2017-02-18T04:00:00+00:00"
                }
              ]
            }
        */

        networkClient.get(ROUTE_HACKATHON + ROUTE_SCHEDULE,
            new NetworkClient.NetworkCallback() {

                @Override
                public void onComplete(String json) {

                    JSONObject response;
                    JSONArray scheduleJSON;
                    List<ScheduleModel> schedule = new ArrayList<>();

                    //
                    try {
                        response = new JSONObject(json);
                        scheduleJSON = response.getJSONArray("schedule_items");

                        for(int i = 0; i < scheduleJSON.length(); ++i) {
                            JSONObject obj = scheduleJSON.optJSONObject(i);

                            // Fail each schedule item separately
                            try {

                                // parse calendar times
                                Calendar start = Calendar.getInstance(); //ISO8601.toCalendar(obj.getString("start"));
                                Calendar end = Calendar.getInstance(); //ISO8601.toCalendar(obj.getString("end"));

                                try {
                                    start = ISO8601.toCalendar(obj.getString("start"));
                                } catch (Exception e) {
                                    Log.e("getSchedule(1)", e.getLocalizedMessage());
                                }

                                try {
                                    end = ISO8601.toCalendar(obj.getString("end"));
                                } catch (Exception e) {
                                    Log.e("getSchedule(1)", e.getLocalizedMessage());
                                }

                                // Do the rest
                                ScheduleModel model = new ScheduleModel(
                                        obj.getString("name"),
                                        start,
                                        end,
                                        obj.getString("description"),
                                        obj.getInt("type")
                                );

                                // Add it
                                schedule.add(model);

                            } catch (Exception e) {
                                Log.e(e.getClass().getName(), " "+ e.getLocalizedMessage());
                            }
                        }

                        Log.d("getSchedule(4)", "Returning schedule set of length " + schedule.size());
                        performCallback(callback, schedule);

                    } catch (Exception e) {
                        Log.e("getSchedule(2)", e.getClass().getCanonicalName() + ": "
                                + e.getLocalizedMessage());
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("getSchedule(3)", e.getLocalizedMessage());
                }

            });

    }

    public void getSponsors(final APICallback<SponsorModel> callback) {

        /* Sample JSON response:
        {
            "sponsors": [
            {
              "website_link": "http://www.statefarm.com",
              "logo_link": "https://hackfsu.com/static/img/logos/state-farm.png",
              "tier": 2,
              "name": "State Farm",
              "order": 2
            }
        }
        */

        networkClient.get(ROUTE_HACKATHON + ROUTE_SPONSORS,
            new NetworkClient.NetworkCallback() {

                @Override
                public void onComplete(String json) {

                    JSONObject response;
                    JSONArray sponsorJSON;
                    List<SponsorModel> sponsors = new ArrayList<SponsorModel>();

                    //In case of no JSON (aka check the server if this runs)
                    try {

                        response = new JSONObject(json);
                        sponsorJSON = response.getJSONArray("sponsors");

                        // Ensuring we have the right JSON
                        for (int i = 0; i < sponsorJSON.length(); i++) {
                            JSONObject temp = sponsorJSON.optJSONObject(i);

                            try {
                                // Pull data from JSON into SponsorModel to add to list
                                SponsorModel tempSponsor =
                                        new SponsorModel(
                                            temp.getString("name"),
                                            temp.getString("logo_link"),
                                            temp.getString("website_link"),
                                            temp.getInt("tier"),
                                            temp.getInt("order")
                                        );

                                sponsors.add(tempSponsor);
                            } catch (Exception e) {
                                Log.e("getSponsors()", e.getLocalizedMessage());
                            }
                        }

                        Collections.sort(sponsors, new Comparator<SponsorModel>() {
                            @Override
                            public int compare(SponsorModel o1, SponsorModel o2) {
                                // Initially sort by tier
                                int c = Integer.compare(o1.getTier(), o2.getTier());
                                // If equal, then sort by order.
                                if(c == 0) c = Integer.compare(o1.getOrder(), o2.getOrder());
                                return c;
                            }
                        });

                        performCallback(callback, sponsors);

                    } catch (JSONException e) {
                        // handle the exception - send error back to the server?
                        Log.e("getSponsor()", e.getLocalizedMessage());
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
    }

    public void getCountdowns(final APICallback<CountdownModel> callback) {

        /* Sample JSON Format
           {
              "countdowns": [
                {
                  "title": "Hacking Starts",
                  "end": "2017-02-09T11:00:00+00:00",
                  "start": "2017-02-08T05:00:00+00:00"
                }
              ]
            }
         */

        networkClient.get(ROUTE_HACKATHON + ROUTE_COUNTDOWN, new NetworkClient.NetworkCallback() {

            @Override
            public void onComplete(String json) {
                JSONObject response;
                JSONArray countdownJSON;
                List<CountdownModel> countdowns = new ArrayList<>();

                try {
                    response = new JSONObject(json);
                    countdownJSON = response.getJSONArray("countdowns");

                    for(int i = 0; i < countdownJSON.length(); ++i) {
                        JSONObject obj = countdownJSON.optJSONObject(i);

                        // Fail separately
                        try {

                            // Parse the times to calendars
                            Calendar start = Calendar.getInstance();
                            Calendar end = Calendar.getInstance();

                            try {
                                start = ISO8601.toCalendar(obj.getString("start"));
                            } catch (Exception e) {
                                Log.e("getCountdowns(3)", e.getLocalizedMessage());
                            }

                            try {
                                end = ISO8601.toCalendar(obj.getString("end"));
                            } catch (Exception e) {
                                Log.e("getCountdowns(3)", e.getLocalizedMessage());
                            }

                            // Assemble model
                            CountdownModel model = new CountdownModel(
                                    obj.getString("title"),
                                    start,
                                    end
                            );

                            // Fin!
                            countdowns.add(model);


                        } catch (Exception e) {
                            Log.e("getCountdowns(2)", e.getLocalizedMessage());
                        }
                    }

                    performCallback(callback, countdowns);

                } catch (Exception e) {
                    Log.e("getCountdowns(1)", e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("getCountdowns(4)", e.getLocalizedMessage());
            }

        });

    }


    public interface APICallback<T extends BaseModel> {
        void onDataReady(List<T> dataSet);
    }


    //
    //  These handle running our callback code on the UI thread
    //

    private static class CallbackRunnable implements Runnable {

        APICallback callback;
        List<? extends BaseModel> dataset;

        CallbackRunnable(APICallback callback, List<? extends BaseModel> dataset) {
            this.callback = callback;
            this.dataset = dataset;
        }

        @Override
        public void run() {
            // TODO set the right type dependency
            callback.onDataReady(dataset);
        }
    }

    private void performCallback(APICallback callback, List<? extends BaseModel> dataset) {
        CallbackRunnable runnable = new CallbackRunnable(callback, dataset);
        mActivity.runOnUiThread(runnable);
    }







}

