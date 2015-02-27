package com.hackfsu.hackfsu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Activity for the splash screen displayed after logging in successfully
 * @author Trevor
 *
 */
public class SplashActivity extends Activity {

	private TextView tvWelcome;
	private ImageView ivWink;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		tvWelcome = (TextView) findViewById(R.id.tv_splash_welcome);
		String welcomeMsg = tvWelcome.getText().toString() + getIntent().getExtras().getString("name") ;
		tvWelcome.setText(welcomeMsg);
		
		ivWink = (ImageView) findViewById(R.id.splash_wink);
		ivWink.setImageBitmap(null);
		ivWink.setBackgroundResource(R.drawable.wink_animation);
		
		final AnimationDrawable winkAnimation = (AnimationDrawable) ivWink.getBackground();
		winkAnimation.start();
		
		IntentLauncher launcher = new IntentLauncher();
		launcher.start();
	}

	
	/**
	 * Sets up a Thread to initiate a timer
	 * @author Trevor
	 *
	 */
	private class IntentLauncher extends Thread {
		@Override
		/**
		 * Sleep for some time and than start new activity.
		 */
		public void run() {
			try {
				// Sleeping
				Thread.sleep(2500);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Start main activity
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			SplashActivity.this.startActivity(intent);
			SplashActivity.this.finish();
		}
	}

}
