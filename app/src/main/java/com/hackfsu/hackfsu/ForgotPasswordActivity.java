/**
 * Handles password recovery. Prompts for email (must be valid), then sends an email from Parse with how to 
 * change password.
 * 
 * @author Jared
 */

package com.hackfsu.hackfsu;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends Activity implements OnClickListener {
	
	//constant settings
	private static final int MIN_PASSWORD_LENGTH = 8;
	private static final int CODE_LENGTH = 6;
	
	//view objects
	private EditText etEmail;
	private Button bSendEmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_forgot_password);
		
		init();
	}
	
	/**
	 * Initialize view objects
	 * @author Jared
	 */
	private void init() {
		this.etEmail = (EditText) this.findViewById(R.id.etEmailToRecover);

		this.bSendEmail = (Button) this.findViewById(R.id.bSendRequestCode);
		
		this.bSendEmail.setOnClickListener(this);
	}
	
	/**
	 * Button click events
	 * @author Jared
	 */
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.bSendRequestCode:
			//sendRequestCode();
			sendPasswordResetRequest();
			break;
		}
	}
	
	/**
	 * Uses parse api to send an email that has a link to reset password.
	 * @author Jared
	 */
	private void sendPasswordResetRequest() {
		String email = this.etEmail.getText().toString();
		
		ParseUser.requestPasswordResetInBackground(email,
				  new RequestPasswordResetCallback() {
			  public void done(ParseException e) {
				  if (e == null) {
					  // An email was successfully sent with reset instructions.
					  Toast.makeText(ForgotPasswordActivity.this, "Email Sent!", Toast.LENGTH_SHORT).show();
					  Intent a = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
					  ForgotPasswordActivity.this.startActivity(a);
				  } else {
					  Log.e("ParseError", e.toString());
					  Toast.makeText(ForgotPasswordActivity.this, "Error: Invalid Email", Toast.LENGTH_SHORT).show();
				  }
			  }
		  });	
	}
	
}
