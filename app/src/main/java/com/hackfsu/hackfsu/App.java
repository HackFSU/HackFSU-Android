/**
 * 
 */
package com.hackfsu.hackfsu;

import android.app.Application;

import com.parse.Parse;
import com.parse.PushService;

/**
 * @author Trevor Things to be initialized for the entire application
 */
public class App extends Application {

	private static final String PARSE_APP_ID = "jeoeVa2Nz3VLmrnWpAknbWKZADXHbmQltPSlU8mX";
	private static final String PARSE_CLIENT_KEY = "nMdA9eBEQU5l2KNWvvWJ3gIDN70M4yi5hBkdyxs2";
	
	public static boolean develMode = false;

	/**
	 * Initializes things when the app is started
	 */
	@Override
	public void onCreate() {
		super.onCreate();

        // Initialize Parse
        Parse.initialize(this, PARSE_APP_ID, PARSE_CLIENT_KEY); 
        
		
		// Initialize push notification service
		PushService.setDefaultPushCallback(this, MainSplashActivity.class);
		
		
	}
}
