package com.hackfsu.hackfsu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DefaultItem extends Fragment {
	
	private TextView tvTest;
	private View rootView;
	
	public DefaultItem() {
	}
	
	private void init() {
		this.tvTest = (TextView) this.rootView.findViewById(R.id.tvTest);
		this.tvTest.setText("SUCCESS");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_home, container, false);
		
		init();
		
		return rootView;
	}
}