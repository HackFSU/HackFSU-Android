package com.hackfsu.android.hackfsu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hackfsu.android.hackfsu.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class HelpActivity extends AppCompatActivity {

    EditText who;
    EditText where;
    EditText what;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Set up Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close_24dp);
        mToolbar.setTitle("Request help");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolbar.inflateMenu(R.menu.menu_help);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_help_submit:
                        submit();
                        return true;
                }
                return false;
            }
        });

        // ID the Text Fields
        who = (EditText) findViewById(R.id.tv_who);
        where = (EditText) findViewById(R.id.tv_where);
        what = (EditText) findViewById(R.id.tv_what);
        
    }

    public boolean checkInputs() {

        String a = who.getText().toString();
        String b = where.getText().toString();
        String c = what.getText().toString();
        return (!a.isEmpty() && !b.isEmpty() && !c.isEmpty());
    }

    public boolean submit() {

        if(!checkInputs()) {
            showSnackbar("Please provide all fields.");
            return false;
        }

        String name = who.getText().toString();
        String location = where.getText().toString();
        String description = what.getText().toString();

        ParseObject req = new ParseObject(ParseName.HELPREQUEST);
        req.put(ParseName.HELP_NAME, name);
        req.put(ParseName.HELP_LOCATION, location);
        req.put(ParseName.HELP_DESCRIPT, description);

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Submitting your help request...");
        pd.show();

        req.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    finish();
                    Toast.makeText(HelpActivity.this, "Your help request has been submitted.", Toast.LENGTH_SHORT).show();
                } else {
                    showSnackbar("Error submitting request.");
                }

                pd.hide();

            }
        });

        return false;
    }

    public void showSnackbar(String message) {
        Snackbar.make(mToolbar, message, Snackbar.LENGTH_SHORT).show();
    }

}
