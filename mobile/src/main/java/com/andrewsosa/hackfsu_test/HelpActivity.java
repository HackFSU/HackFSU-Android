package com.andrewsosa.hackfsu_test;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    EditText who;
    EditText where;
    EditText what;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Set up Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_24dp);
        toolbar.setTitle("Compose message");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //setSupportActionBar(toolbar);

        // ID the Text Fields
        who = (EditText) findViewById(R.id.tv_who);
        where = (EditText) findViewById(R.id.tv_where);
        what = (EditText) findViewById(R.id.tv_what);

        // Init FAB
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(submit()) {
                    finish();
                }

            }
        });
    }

    public boolean checkInputs() {

        String a = who.getText().toString();
        String b = where.getText().toString();
        String c = what.getText().toString();
        return (!a.isEmpty() && !b.isEmpty() && !c.isEmpty());
    }

    // TODO aaron
    public boolean submit() {

        if(!checkInputs()) {
            showSnackbar("Please provide all fields.");
            return false;
        }

        // TODO add submission logic

        showSnackbar("Error submitting request.");
        return false;
    }

    public void showSnackbar(String message) {
        Snackbar.make(fab, message, Snackbar.LENGTH_SHORT).show();
    }

}
