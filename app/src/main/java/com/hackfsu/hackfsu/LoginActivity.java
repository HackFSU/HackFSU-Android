package com.hackfsu.hackfsu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.PushService;

/**
 * Main login screen.Validates through Parse.com
 *
 * @author Trevor
 *
 * Not used as of 02/13/15.
 * Kept for future reference/integrations.
 *
 */
public class LoginActivity extends Activity implements View.OnClickListener {

	private Button bLogin;
	private EditText etUser, etPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        boolean loggedIn = false;
		FileInputStream fis = null;

		try {
			fis = this.openFileInput("loggedIn");
			loggedIn = true;
			Log.d("Works", "Works");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loggedIn = false;
		}

		if (App.develMode || loggedIn) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}

		// When users indicate they are no longer Giants fans, we unsubscribe
		// them.
		PushService.unsubscribe(this, "Updates");

		setContentView(R.layout.activity_login);
		initUI();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Set up on click actions for login TODO: Implement OAuth to actually login
	 * people
	 */
	@Override
	public void onClick(View v) {
		
		final ProgressDialog pDialog = new ProgressDialog(this);
		pDialog.setMessage("Signing in...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
		
		final Bundle loginExtras = new Bundle();
		final Intent intent = new Intent(this, SplashActivity.class);

		ParseUser.logInInBackground(etUser.getText().toString(), etPass
				.getText().toString(), new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					String file = "loggedIn";
					String username = user.getUsername();

					FileOutputStream fos = null;
					try {
						fos = openFileOutput(file, Context.MODE_PRIVATE);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						fos.write(username.getBytes());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						fos.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					loginExtras.putString("name", user.getString("name"));
					intent.putExtras(loginExtras);
					startActivity(intent);
				} else {
					// Signup failed. Look at the ParseException to see
					// what happened.
					
					//print out dialog box error msg
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
					        switch (which){
					        case DialogInterface.BUTTON_POSITIVE:
					            //Yes button clicked
					        	pDialog.cancel();
					            break;
					        case DialogInterface.BUTTON_NEGATIVE:
					        	//reset pw button clicked
					        	Intent a = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
					        	LoginActivity.this.startActivity(a);
					        	
					        	break;
					        }
					    }
					};
					
					AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
					builder.setMessage("Invalid username or password").setNegativeButton("Reset Password", dialogClickListener)
								.setPositiveButton("Try Again", dialogClickListener).show();
					
					Log.d("Failed Login", "DAMNIT");
				}
			}
		});

	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}

	private void initUI() {
		bLogin = (Button) findViewById(R.id.bLogin);
		etUser = (EditText) findViewById(R.id.etUser);
		etPass = (EditText) findViewById(R.id.etPassword);

		bLogin.setOnClickListener(this);
	}

}
