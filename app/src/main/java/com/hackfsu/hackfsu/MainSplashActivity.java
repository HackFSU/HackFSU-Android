package com.hackfsu.hackfsu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainSplashActivity extends Activity {
	
	private final int WAIT_TIME = 1500;		// in miliseconds
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_start_splash);
		
		Thread waitTimer = new Thread() {
			@Override
		    public void run() {
		        try {
		            super.run();
		            Thread.sleep(WAIT_TIME);
		            
		        } catch (Exception e) {
		        	e.printStackTrace();
		        } finally {
		        	Intent intent = new Intent(MainSplashActivity.this, MainActivity.class);
		    		startActivity(intent);
		            finish();
		        }
		    }
		};
		
		waitTimer.start();
		
	}
	


}
