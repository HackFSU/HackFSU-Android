package com.hackfsu.mobile.android.app.fragment;

import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.transition.TransitionManager;
import android.transition.PathMotion;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hackfsu.mobile.android.api.API;
import com.hackfsu.mobile.android.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment .OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JudgingFragment extends BaseFragment {

    Toolbar mToolbar;
    AppBarLayout mAppBar;
    API mAPI;


    final JSONArray Hack = new JSONArray();
    final JSONObject childorder = new JSONObject();
    final JSONObject parentorder = new JSONObject();
    final JSONObject parentsuperlative = new JSONObject();
    final JSONArray childsuperlative1 = new JSONArray();
    final JSONArray childsuperlative2 = new JSONArray();
    final JSONArray childsuperlative3 = new JSONArray();
    final JSONObject teensuperlative = new JSONObject();


    BaseFragment.OnFragmentInteractionListener mListener;

    public JudgingFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {

            childorder.put("1", 0);
            childorder.put("2", 0);
            childorder.put("3", 0);
            parentorder.put("Order",childorder);

            childsuperlative1.put("Best cactus");
            childsuperlative2.put("Worst Hack");
            childsuperlative3.put("Best hack");

            teensuperlative.put("6",childsuperlative1);
            teensuperlative.put("7", childsuperlative2);
            teensuperlative.put("8",childsuperlative3);

            parentsuperlative.put("superlatives",teensuperlative);

            Hack.put(parentorder);
            Hack.put(parentsuperlative);

        } catch(JSONException e)

        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_judging, container, false);
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mAppBar = (AppBarLayout) v.findViewById(R.id.app_bar);
        ImageButton imagebutton1 = (ImageButton) v.findViewById(R.id.imageButton1);
        ImageButton imagebutton2 = (ImageButton) v.findViewById(R.id.imageButton2);
        ImageButton imagebutton3 = (ImageButton) v.findViewById(R.id.imageButton3);
        Button button1 = (Button) v.findViewById(R.id.button1);



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_anchor, profileFragment, profileFragment.getTag()).commit();
            }
        });


        final ArrayDeque<Integer> Q = new ArrayDeque();

        Q.add(1);
        Q.add(2);
        Q.add(3);


        imagebutton1.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {


                    ImageButton im= (ImageButton) v.findViewById(R.id.imageButton1);
                    int location[] = new int[2];
                    im.getLocationOnScreen(location);
                    int x = location[0];
                    int y = location[1];

                    TextView textView = (TextView) getActivity().findViewById((R.id.textView3));
                    Log.d("POPPPPP", "value: " + Q.peek());
                    switch(Q.pollFirst()) {
                        case 1:{textView = (TextView) getActivity().findViewById((R.id.textView3));
                             try{childorder.getJSONObject("Order").put("1","1");}
                             catch(JSONException e){
                                 e.printStackTrace();
                             }
                        }
                        break;
                        case 2:{textView = (TextView) getActivity().findViewById((R.id.textView4));
                            try{childorder.getJSONObject("Order").put("2","1");}
                            catch(JSONException e){
                                e.printStackTrace();
                            }}
                        break;
                        case 3:{textView = (TextView) getActivity().findViewById((R.id.textView5));
                            try{childorder.getJSONObject("Order").put("3","1");}
                            catch(JSONException e){
                                e.printStackTrace();
                            }}
                        break;
                    }





                    int location1[] = new int[2];
                    textView.getLocationOnScreen(location1);
                    int xt = location1[0];
                    int yt= location1[1];

                    Log.d("MYINTXXXX", "value: " + xt);
                    Log.d("MYINTYYYY", "value: " + yt);
                    int xx = xt - x;
                    int yy = yt -y;

                    //set position TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta
                    final Animation animation = new TranslateAnimation(0,xx,0,yy);

                    im.setVisibility(View.VISIBLE);
                    // set Animation for 5 sec
                    animation.setDuration(1000);
                    //for button stops in the new position.
                    animation.setFillAfter(true);
                    im.startAnimation(animation);



                }

        });


        imagebutton2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                ImageButton im= (ImageButton) v.findViewById(R.id.imageButton2);
                int location[] = new int[2];
                im.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];

                TextView textView = (TextView) getActivity().findViewById((R.id.textView3));
                Log.d("POPPPPP222", "value: " + Q.peek());
                switch(Q.pollFirst()) {
                    case 1:{textView = (TextView) getActivity().findViewById((R.id.textView3));
                    try{childorder.getJSONObject("Order").put("1","2");}
                    catch(JSONException e){
                        e.printStackTrace();
                    }}
                    break;
                    case 2:{textView = (TextView) getActivity().findViewById((R.id.textView4));
                        try{childorder.getJSONObject("Order").put("2","2");}
                        catch(JSONException e){
                            e.printStackTrace();
                        }}
                    break;
                    case 3:{textView = (TextView) getActivity().findViewById((R.id.textView5));
                        try{childorder.getJSONObject("Order").put("3","2");}
                        catch(JSONException e){
                            e.printStackTrace();
                        }}
                    break;
                }

                int location2[] = new int[2];
                textView.getLocationOnScreen(location2);
                int xt = location2[0];
                int yt= location2[1];

                int xx = xt - x;
                int yy = yt -y;

                //set position TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta
                final Animation animation = new TranslateAnimation(0,xx,0,yy);

                im.setVisibility(View.VISIBLE);
                // set Animation for 5 sec
                animation.setDuration(1000);
                //for button stops in the new position.
                animation.setFillAfter(true);
                im.startAnimation(animation);

            }

        });

        imagebutton3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                ImageButton im= (ImageButton) v.findViewById(R.id.imageButton3);
                int location[] = new int[2];
                im.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];

                TextView textView = (TextView) getActivity().findViewById((R.id.textView3));
                Log.d("POPPPPP333", "value: " + Q.peek());
                switch(Q.pollFirst()) {
                    case 1:{textView = (TextView) getActivity().findViewById((R.id.textView3));
                        try{childorder.getJSONObject("Order").put("1","3");}
                        catch(JSONException e){
                            e.printStackTrace();
                        }}
                        break;
                    case 2:{textView = (TextView) getActivity().findViewById((R.id.textView4));
                        try{childorder.getJSONObject("Order").put("2","3");}
                        catch(JSONException e){
                            e.printStackTrace();
                        }}
                        break;
                    case 3:{textView = (TextView) getActivity().findViewById((R.id.textView5));
                        try{childorder.getJSONObject("Order").put("3","3");}
                        catch(JSONException e){
                            e.printStackTrace();
                        }}
                        break;
                }

                int location3[] = new int[2];
                textView.getLocationOnScreen(location3);
                int xt = location3[0];
                int yt= location3[1];

                int xx = xt - x;
                int yy = yt -y;

                //set position TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta
                final Animation animation = new TranslateAnimation(0,xx,0,yy);

                im.setVisibility(View.VISIBLE);
                // set Animation for 5 sec
                animation.setDuration(1000);
                //for button stops in the new position.
                animation.setFillAfter(true);
                im.startAnimation(animation);

            }

        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mToolbar.setTitle("Judging");
    }



}
